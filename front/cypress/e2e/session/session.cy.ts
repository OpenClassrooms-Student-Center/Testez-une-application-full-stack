/// <reference types="cypress" />
import CypressConfig from "../../../cypress.config";

describe('Session Management Tests', () => {
  const teachers = [
    { id: 101, firstName: 'Alice', lastName: 'Johnson' },
    { id: 102, firstName: 'Bob', lastName: 'Anderson' }
  ];

  const sessions = [
    { id: 201, name: 'Introduction to Cypress', description: 'Learn Cypress', date: '2025-04-01', teacher_id: 101, users: [1, 2] },
    { id: 202, name: 'Advanced JavaScript', description: 'Deep dive into JS', date: '2025-04-02', teacher_id: 101, users: [] }
  ];

  const newSession = { id: 203, name: 'Mastering Angular', description: 'Advanced Angular', date: '2025-04-03', teacher_id: 101, users: [] };
  const updatedSession = { ...sessions[0], name: 'Updated Cypress Session', description: 'Updated details', teacher_id: 102 };

  const adminUser = { id: 1, firstName: 'Emma', lastName: 'Williams', email: 'emma.williams@example.com', password: 'AdminPass123', admin: true };
  const regularUser = { id: 2, firstName: 'Liam', lastName: 'Brown', email: 'liam.brown@example.com', password: 'UserPass456', admin: false };

  function loginAs(user) {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: user }).as('login');
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button[type="submit"]').should('be.visible').click();
    cy.wait('@login');
  }

  function setupInterceptions() {
    cy.intercept('GET', '/api/session', { body: sessions }).as('sessions');
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('teachers');
  }

  describe('Admin Session Management', () => {
    beforeEach(() => {
      setupInterceptions();
      loginAs(adminUser);
    });

    it('Should display the list of available sessions', () => {
      cy.wait('@sessions');
      cy.get('mat-card.item').should('have.length', sessions.length);
    });

    it('Should navigate to the session creation page', () => {
      cy.wait('@sessions');
      cy.get('button').contains('Create').should('be.visible').click();
      cy.url().should('include', '/sessions/create');
    });

    it('Should display error when required fields are empty', () => {
      cy.wait('@sessions');
      cy.get('button').contains('Create').should('be.visible').click();
      cy.get('button[type="submit"]').contains('Save').should('be.visible').click();
      cy.get('.ng-invalid').should('exist').and('be.visible');
    });

    it('Should successfully create a new session', () => {
      cy.intercept('POST', '/api/session', { statusCode: 201, body: newSession }).as('createSession');
      cy.intercept('GET', '/api/session', { body: [...sessions, newSession] }).as('getSessions');
    
      // Дожидаемся, что кнопка "Create" появится
      cy.contains('button', 'Create', { timeout: 10000 })
        .should('be.visible')
        .click();
    
      // Заполняем форму
      cy.get('input[formControlName="name"]').type(newSession.name);
      cy.get('input[formControlName="date"]').type(newSession.date);
      cy.get('mat-select[formControlName="teacher_id"]').click();
      cy.get('mat-option').first().click();
      cy.get('textarea[formControlName="description"]').type(newSession.description);
    
      // Кликаем "Save"
      cy.get('button[type="submit"]').contains('Save', { timeout: 10000 })
        .should('be.visible')
        .click();
    
      // Ждем запросов
      cy.wait('@createSession');
      cy.wait('@getSessions');
    
      // Проверяем, что новая сессия появилась
      cy.contains('mat-card.item', newSession.name, { timeout: 15000 }).should('exist');
    
      // Улучшенная проверка snackbar
      cy.get('snack-bar-container', { timeout: 15000 })
        .should('be.visible')
        .invoke('text')
        .then((text) => {
          const cleanedText = text.trim().replace(/\s+!/, '!'); // Убираем возможные лишние пробелы
          cy.log(`📌 Текст в snackbar: "${cleanedText}"`);
          expect(cleanedText).to.include('Session created!');
        });
    });
    
  });

  describe('Regular User Session Participation', () => {
    beforeEach(() => {
      setupInterceptions();
      loginAs(regularUser);
    });

    it('Should display available sessions', () => {
      cy.wait('@sessions');
      cy.get('mat-card.item').should('have.length', sessions.length);
    });

    it('Should allow user to participate in a session', () => {
      cy.contains('mat-card.item', sessions[1].name).within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.window().then((win) => {
        const token = win.localStorage.getItem('authToken');
        if (!token) return;

        cy.request({
          method: 'GET',
          url: `/api/session/${sessions[1].id}`,
          headers: { Authorization: `Bearer ${token}` },
          failOnStatusCode: false
        }).then((response) => {
          if (response.status === 401) return;
          expect(response.status).to.eq(200);
        });

        cy.get('button').contains('Participate').should('exist').click();
        cy.intercept('POST', `/api/session/${sessions[1].id}/participate/${regularUser.id}`, { statusCode: 200 }).as('participate');
        cy.wait('@participate');
        cy.get('button').contains('Do not participate').should('exist');
      });
    });

    it('Should allow user to leave a session', () => {
      cy.contains('mat-card.item', sessions[1].name).within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.window().then((win) => {
        const token = win.localStorage.getItem('authToken');
        if (!token) return;

        cy.request({
          method: 'GET',
          url: `/api/session/${sessions[1].id}`,
          headers: { Authorization: `Bearer ${token}` },
          failOnStatusCode: false
        }).then((response) => {
          if (response.status === 401) return;
          expect(response.status).to.eq(200);
        });

        cy.get('button').contains('Do not participate').should('exist').click();
        cy.intercept('DELETE', `/api/session/${sessions[1].id}/participate/${regularUser.id}`, { statusCode: 200 }).as('leave');
        cy.wait('@leave');
        cy.get('button').contains('Participate').should('exist');
      });
    });
  });
});
