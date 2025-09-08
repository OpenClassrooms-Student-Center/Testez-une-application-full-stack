/// <reference types="cypress" />

describe('Logout', () => {
  it('login puis logout → navbar non connectée + redirection home', () => {
    let loggedIn = false;

    cy.intercept('GET', '**/api/session', (req) => {
      req.reply(loggedIn ? { id: 1, admin: true } : []);
    }).as('session');

    cy.intercept('POST', '**/api/auth/login', (req) => {
      loggedIn = true;
      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          username: 'user',
          firstName: 'John',
          lastName: 'Doe',
          admin: true,
        },
      });
    }).as('loginOk');

    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginOk');

    cy.contains('span.link', /^Sessions$/).should('be.visible');
    cy.contains('span.link', /^Account$/).should('be.visible');
    cy.contains('span.link', /^Logout$/).should('be.visible');

    cy.wrap(null).then(() => {
      loggedIn = false;
    });

    cy.contains('span.link', /^Logout$/).click();

    cy.contains('span.link', /^Login$/).should('be.visible');
    cy.contains('span.link', /^Register$/).should('be.visible');
    cy.contains('span.link', /^Sessions$/).should('not.exist');
    cy.contains('span.link', /^Account$/).should('not.exist');
    cy.contains('span.link', /^Logout$/).should('not.exist');

    cy.url().should('not.include', '/sessions');
  });
});
