/// <reference types="cypress" />
import CypressConfig from "../../../cypress.config";

describe('Login Tests', () => {

  const userAdmin = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    password: 'password12345',
    admin: true
  };

  const user = {
    id: 2,
    firstName: 'Jane',
    lastName: 'Doe',
    email: 'jane.doe@example.com',
    password: 'password12345',
    admin: false
  };

  /**
   * Logs in as a specified user
   * @param user User object with email and password
   */
  function loginAs(user: any) {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: user }).as('login');
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button[type="submit"]').click();
    cy.wait('@login');
  };

  beforeEach(() => {
    cy.visit('/login');
  });

  it('Should log in successfully and redirect to /sessions',  () => {
    cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: user
      }).as('loginSuccess');
    
      cy.intercept('GET', '/api/session', (req) => {
        req.reply((res) => {
          console.log('Intercepted session response:', res.body);
        });
      }).as('session');
    
      cy.get('input[formControlName=email]').type(user.email);
      cy.get('input[formControlName=password]').type(user.password);
    
      cy.contains('button', 'Submit').should('not.be.disabled');
      cy.contains('button', 'Submit').click();
    
      cy.wait('@loginSuccess');
      cy.wait('@session');
    
      cy.url().should('include', '/sessions');
      cy.get('.error').should('not.exist');
    });
  

  it('Should display an error when logging in with incorrect password', () => {
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type('wrongpassword');

    cy.contains('button', 'Submit').click();
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });

  it('Should display an error when logging in with incorrect email', () => {
    cy.get('input[formControlName=email]').type("wrongMail@studio.com");
    cy.get('input[formControlName=password]').type(user.password);

    cy.contains('button', 'Submit').click();
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });

  it('Should disable login button when fields are empty', () => {
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Should toggle password visibility when clicking the visibility icon', () => {
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);

    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
   
    cy.get('button[mat-icon-button]').click();

    cy.get('input[formControlName=password]').should('have.attr', 'type', 'text');

    cy.get('button[mat-icon-button]').click();
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
  });

 
  it('Should log in as admin and verify account details', () => {
    loginAs(userAdmin);

    cy.intercept('GET', `/api/user/${userAdmin.id}`, {
      statusCode: 200,
      body: userAdmin
    }).as('getMe');

    cy.get('span[routerLink=me]').click();
    cy.wait('@getMe');

    cy.url().should('eq', Cypress.config().baseUrl + 'me');

    cy.contains(`${userAdmin.firstName} ${userAdmin.lastName.toUpperCase()}`).should('be.visible');
    cy.contains(userAdmin.email).should('be.visible');
    cy.contains('You are admin').should('be.visible');
    cy.get('button span mat-icon').contains('delete').should('not.exist');
  });

  it('Should log in as a regular user, verify details, and delete the account', () => {
    loginAs(user);

    cy.intercept('GET', `/api/user/${user.id}`, {
      statusCode: 200,
      body: user
    }).as('getMe');

    cy.get('span[routerLink=me]').should('be.visible').click();
    cy.wait('@getMe');

    cy.url().should('include', Cypress.config().baseUrl);

    cy.contains(`${user.firstName} ${user.lastName.toUpperCase()}`).should('be.visible');
    cy.contains(user.email).should('be.visible');
    cy.contains('You are admin').should('not.exist');

    cy.intercept('DELETE', `/api/user/${user.id}`, {
        statusCode: 200,
        body: user
      }).as('deleteSuccess')
  
      cy.get('button[mat-raised-button][color="warn"]').contains('delete').should('be.visible');
      cy.get('button[mat-raised-button][color="warn"]').click();
      cy.wait('@deleteSuccess');
      cy.location('pathname').should('eq', '/');
      cy.get('snack-bar-container').contains('Your account has been deleted !').should('exist');
});


  it('Should redirect to /login if user is not logged in', () => {
    cy.intercept('GET', '/api/session', { body: { isLogged: false } }).as('sessionCheck'); 
    cy.visit('/sessions');

    cy.url().should('eq', Cypress.config().baseUrl + 'login');
  });

});