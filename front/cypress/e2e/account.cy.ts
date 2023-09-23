/// <reference types="Cypress" />

describe('Account component', () => {
    // login before each test as user
    beforeEach(() => {
      cy.visit('/login')
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 2,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      });

      cy.intercept('GET', '/api/user/2', {
        body: {
          id: 2,
          email: 'alex@gmail.com',
          lastName: 'ALEX',
          firstName: 'Alex',
          admin: false,
          createdAt: '2023-05-05',
          updatedAt: '2023-05-05',
        }
      });
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session');
      // fill the login form
      cy.get('input[formControlName=email]').type("alex@gmail.com");
      cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
      cy.url().should('include', '/sessions');
    });

    it('should display the user informations', () => {
        // click on the account word
        cy.contains('span.link', 'Account').click();
        cy.url().should('include', '/me');
        // check if the user informations is displayed
        cy.get('p:contains("Name:")').should('contain', 'Alex ALEX');
        cy.get('p:contains("Email:")').should('contain', 'alex@gmail.com');
        // check if the delete button is displayed
        cy.get('.my2 > .mat-focus-indicator').should('exist');
    });

    it('should delete the account', () => {
        cy.intercept('GET', '/api/session', {});

        cy.intercept('DELETE', '/api/user/2', {
            statusCode: 200
        }).as('delete user');

        // click on the account word
        cy.contains('span.link', 'Account').click();
        cy.url().should('include', '/me');
        // click on the delete button
        cy.get('.my2 > .mat-focus-indicator').click();
        // check if the message is displayed
        cy.get('.mat-simple-snack-bar-content').should('exist');

        // check if the "account" link is not displayed
        cy.contains('span.link', 'Account').should('not.exist');
    });
});