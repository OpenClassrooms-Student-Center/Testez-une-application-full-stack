/// <reference types="cypress" />
import CypressConfig from "../../../cypress.config";

describe('End-to-End Testing - App', () => {

  // Test user
  const user = {
    id: 2,
    firstName: 'Alice',
    lastName: 'Smith',
    email: 'alice.smith@example.com',
    password: 'securePass123',
    admin: false
  };

  // Test session
  const session = {
    id: 2,
    name: 'Cypress Test Session',
    description: 'Session for Cypress testing',
    date: '2025-04-10',
    teacher_id: 3,
    users: [2, 5],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  /**
   * Logs in as a specified user.
   * @param user - User object containing email and password
   */
  function loginAs(user: any) {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: user }).as('login');
    
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button[type="submit"]').click();
  }

  beforeEach(() => {
    cy.visit('/');
  });

  describe('Navigation display when user is not logged in', () => {
    it('Should show Login and Register but not Sessions, Logout, or Account', () => {
      cy.visit('/');

      cy.get('mat-toolbar').contains('Login').should('exist');
      cy.get('mat-toolbar').contains('Register').should('exist');

      cy.get('mat-toolbar').contains('Sessions').should('not.exist');
      cy.get('mat-toolbar').contains('Logout').should('not.exist');
      cy.get('mat-toolbar').contains('Account').should('not.exist');
    });
  });

  describe('Navigation display when user is logged in', () => {
    it('Should display Logout, Account, and Sessions after login', () => {
      loginAs(user);
      cy.intercept('GET', '/api/session', { body: session }).as('sessions');

      cy.get('mat-toolbar').contains('Logout').should('exist');
      cy.get('mat-toolbar').contains('Account').should('exist');
      cy.get('mat-toolbar').contains('Sessions').should('exist');

      cy.get('mat-toolbar').contains('Login').should('not.exist');
      cy.get('mat-toolbar').contains('Register').should('not.exist');
    });
  });

});
