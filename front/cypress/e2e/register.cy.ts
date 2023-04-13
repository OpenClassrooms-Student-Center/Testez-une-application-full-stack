/// <reference types="cypress" />

describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {statusCode: 200})

    cy.get('input[formControlName=firstName]').type("firstname")
    cy.get('input[formControlName=lastName]').type("lastname")
    cy.get('input[formControlName=email]').type("new@user.com")
    cy.get('input[formControlName=password]').type(`${"newpass"}{enter}{enter}`)

    cy.url().should('include', '/login')

  })

  it('should disabled submit button if empty email', () => {
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').type("firstname")
    cy.get('input[formControlName=lastName]').type("lastname")
    cy.get('input[formControlName=email]').clear
    cy.get('input[formControlName=password]').type(`${"wrongpass"}{enter}{enter}`)
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
    cy.get('button[type=submit]').should('be.disabled')

  })


  it('should return error if email already used', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {statusCode: 400})

    cy.get('input[formControlName=firstName]').type("firstname")
    cy.get('input[formControlName=lastName]').type("lastname")
    cy.get('input[formControlName=email]').type("new@user.com")
    cy.get('input[formControlName=password]').type(`${"wrongpass"}{enter}{enter}`)
    cy.get('span[data-test-id=error-register]').should('be.visible');
  })


})
