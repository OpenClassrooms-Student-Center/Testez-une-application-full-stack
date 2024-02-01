/// <reference types="Cypress" />

describe('Login page', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should sucessfully let the user log in', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    }).as('login');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');

    cy.wait('@login').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });

    cy.wait('@session').then(({ response }) => {
      expect(response!.statusCode).to.equal(200);
    });
  });

  it('should return error if one of the inputs is not valid', () => {
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`'invalid' {enter}{enter}`);

    cy.get('.error').should('be.visible');
  });

  it('should be able to log out the user after logging in', () => {
    // fill the login form
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/session');

    cy.get('.link').contains('Logout').click();

    cy.url().should('include', '/');
  });
});
