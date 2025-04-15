/// <reference types="cypress" />

describe('User Registration Tests', () => {

    // Mock user data for registration
    const newUser = {
      firstName: 'Alice',
      lastName: 'Williams',
      email: 'alice.williams@example.com',
      password: 'SecurePass789',
    };
  
    // Navigate to the registration page before each test
    beforeEach(() => {
      cy.visit('/register');
    });
  
    describe('Successful Registration', () => {
      it('Should register a new user successfully and redirect to login page', () => {
        cy.intercept('POST', '/api/auth/register', {
          statusCode: 200,
          body: newUser,
        }).as('registerSuccess');
  
        cy.get('input[formControlName=firstName]').type(newUser.firstName);
        cy.get('input[formControlName=lastName]').type(newUser.lastName);
        cy.get('input[formControlName=email]').type(newUser.email);
        cy.get('input[formControlName=password]').type(newUser.password);
  
        cy.get('button[type="submit"]').should('not.be.disabled');
        cy.get('.error').should('not.exist');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@registerSuccess');
  
        cy.url().should('eq', Cypress.config().baseUrl + 'login');
      });
    });
  
    describe('Validation Errors', () => {
      it('Should disable the submit button when fields are empty', () => {
        cy.get('input[formControlName=firstName]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName=lastName]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
        cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
  
        cy.get('button[type="submit"]').should('be.disabled');
      });
  
      it('Should display error for invalid email format', () => {
        cy.get('input[formControlName=firstName]').type(newUser.firstName);
        cy.get('input[formControlName=lastName]').type(newUser.lastName);
        cy.get('input[formControlName=email]').type('invalid-email');
        cy.get('input[formControlName=password]').type(newUser.password);
  
        cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
        cy.get('button[type="submit"]').should('be.disabled');
      });
  
      it('Should display an error for too short first name', () => {
        cy.get('input[formControlName=firstName]').type('Al');
        cy.get('input[formControlName=lastName]').type(newUser.lastName);
        cy.get('input[formControlName=email]').type(newUser.email);
        cy.get('input[formControlName=password]').type(newUser.password);
  
        cy.get('button[type="submit"]').click();
        cy.get('.error').should('be.visible').and('contain', 'An error occurred');
      });
    });
  
    describe('Server Errors', () => {
      it('Should display an error message when server returns an error', () => {
        cy.intercept('POST', '/api/auth/register', {
          statusCode: 500,
          body: {},
        }).as('registerError');
  
        cy.get('input[formControlName=firstName]').type(newUser.firstName);
        cy.get('input[formControlName=lastName]').type(newUser.lastName);
        cy.get('input[formControlName=email]').type(newUser.email);
        cy.get('input[formControlName=password]').type(newUser.password);
  
        cy.get('button[type="submit"]').should('not.be.disabled');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@registerError');
        cy.get('.error').should('be.visible').and('contain', 'An error occurred');
      });
    });
  
  });
  