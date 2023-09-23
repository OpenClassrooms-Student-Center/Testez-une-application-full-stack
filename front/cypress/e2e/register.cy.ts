/// <reference types="Cypress" />

describe('Register spec', () => {
    beforeEach(() => {
        cy.visit('/register');
    });
  
    it('Register successfull', () => {
        cy.intercept('POST', '/api/auth/register', { 
            statusCode: 200 
        });

        cy.get('input[formControlName=firstName]').type('John');
        cy.get('input[formControlName=lastName]').type('Doe');
        cy.get('input[formControlName=email]').type('test@example.com');
        cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
   
    });
  
    it('should return error if email is already used', () => {
        cy.intercept('POST', '/api/auth/register', { 
            statusCode: 400 
        });

        cy.get('input[formControlName=firstName]').type('John');
        cy.get('input[formControlName=lastName]').type('Doe');
        cy.get('input[formControlName=email]').type('test@example.com');
        cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
        
        cy.get('.error').should('be.visible');
    });

    it('should disable submit button if email is empty', () => {
        cy.get('input[formControlName=firstName]').type('John');
        cy.get('input[formControlName=lastName]').type('Doe');
        cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
        
        cy.get('input[formControlName=email]').clear();
        cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
        
        cy.get('button[type=submit]').should('be.disabled');
    });
})
  