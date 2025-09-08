// cypress/e2e/login.cy.ts
/// <reference types="cypress" />

describe('Login', () => {
  it('succès', () => {
    let loggedIn = false;
    cy.intercept('GET', '**/api/session', (req) => {
      if (loggedIn) {
        req.reply({ id: 1, admin: true });
      } else {
        req.reply([]);
      }
    }).as('session');

    cy.intercept('POST', '**/api/auth/login', (req) => {
      loggedIn = true;
      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true,
        },
      });
    }).as('loginOk');

    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('test!1234');

    cy.get('button[type="submit"]').click();

    cy.wait('@loginOk');
    cy.url().should('include', '/sessions');
  });

  it('erreur 401', () => {
    cy.intercept('GET', '**/api/session', []).as('session');

    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' },
    }).as('login401');

    cy.visit('/login');

    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('wrong');

    cy.get('button[type="submit"]').click();

    cy.wait('@login401');
    cy.url().should('include', '/login');
  });
});
