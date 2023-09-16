/// <reference types="Cypress" />

describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
    
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    


    cy.url().should('include', '/session')
  })

  it('should return error if email/password are not valid', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga2@studio.com")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)
 
    cy.get('.error').should('be.visible');

  })

  it('Logout successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
    
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    

    cy.url().should('include', '/session')

    cy.get('.link').contains('Logout').click();
  })

});