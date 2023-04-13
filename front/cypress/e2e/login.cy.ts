/// <reference types="cypress" />

describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

  })

  it('Logout successfull', () => {
    cy.url().should('include', '/sessions');
    cy.get('span[data-test-id=logout-btn]').click();
    cy.url().should('not.contain', '/sessions');
  });

  it('should login failed if wrong credential', () => {

    cy.visit('/login');
    cy.get('input[formControlName=email]').type("unknow@user.com");
    cy.get('input[formControlName=password]').type(`${"wrongpass"}{enter}{enter}`);
    cy.url().should('not.contain', '/sessions');
    cy.get('p[data-test-id=error-p]').should('be.visible');
  });

  it('should disabled submit button if empty email', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').clear;
    cy.get('input[formControlName=password]').type(`${"wrongpass"}{enter}{enter}`);
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('p[data-test-id=error-p]').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disabled submit button if empty password', () => {
    cy.visit('/login');
    cy.get('input[formControlName=password]').clear;
    cy.get('input[formControlName=email]').type(`${"yoga@studio.com"}{enter}{enter}`);
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
    cy.get('p[data-test-id=error-p]').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

});
