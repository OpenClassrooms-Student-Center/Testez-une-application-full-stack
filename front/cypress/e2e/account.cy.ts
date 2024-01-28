/// <reference types="Cypress" />

describe('Account page', () => {
  const ADMIN_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
    createdAt: '2024-01-12T15:33:42',
    updatedAt: '2024-01-12T15:33:42',
  };

  const USER_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 2,
    username: 'user@user.com',
    firstName: 'User',
    lastName: 'User',
    admin: false,
    createdAt: '2024-01-12T15:33:42',
    updatedAt: '2024-01-12T15:33:42',
  };

  beforeEach(() => {
    cy.intercept('GET', '/api/session', (req) => {
      req.reply([]);
    });
  });

  describe('As an admin', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);

      cy.intercept('GET', `/api/user/${ADMIN_DETAILS.id}`, (req) => {
        req.reply(ADMIN_DETAILS);
      });

      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('should show their info and NOT include a delete button', () => {
      cy.get('span.link').contains('Account').click();

      cy.url().should('include', '/me');
    });
  });
  describe('As a regular user', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', USER_DETAILS);

      cy.intercept('GET', `/api/user/${USER_DETAILS.id}`, (req) => {
        req.reply(USER_DETAILS);
      });

      cy.get('input[formControlName=email]').type('user@user.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('should show their info and include a delete button', () => {
      cy.get('span.link').contains('Account').click();

      cy.url().should('include', '/me');
    });
  });
});
