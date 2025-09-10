/// <reference types="cypress" />

describe('Session delete (DetailComponent) - Option B', () => {
  const mockSession = {
    id: 1,
    name: 'Toto',
    date: '1987-06-20T00:00:00.000+00:00',
    teacher_id: 1,
    description: 'Je suis une description',
    users: [],
    createdAt: '2025-09-08T12:37:30',
    updatedAt: '2025-09-08T12:37:30',
  };

  const mockTeacher = { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' };

  beforeEach(() => {
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        admin: true,
        firstName: 'Admin',
        lastName: 'User',
        token: 'fake-jwt-token',
      },
    }).as('login');

    cy.intercept('GET', '**/api/session', [mockSession]).as('getSessions');
    cy.intercept('GET', '**/api/session/1', mockSession).as('getSession');
    cy.intercept('GET', '**/api/teacher/1', mockTeacher).as('getTeacher');
  });

  it('supprime une session et met à jour la liste', () => {
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@example.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    cy.wait('@getSessions');

    // Navigue via le bouton Detail
    cy.get('button span.ml1')
      .contains(/detail/i)
      .click();

    cy.wait('@getSession');
    cy.wait('@getTeacher');

    cy.contains(mockSession.name).should('be.visible');

    cy.intercept('DELETE', '**/api/session/1', { statusCode: 200 }).as(
      'deleteSession'
    );
    cy.intercept('GET', '**/api/session', []).as('getSessionsAfterDelete');

    cy.get('button span.ml1')
      .contains(/delete/i)
      .click();

    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');

    cy.contains(/session deleted !/i).should('be.visible');
    cy.url().should('include', '/sessions');
    cy.contains(mockSession.name).should('not.exist');
  });
});
