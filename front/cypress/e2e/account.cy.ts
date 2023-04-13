/// <reference types="cypress" />

describe('User account', () => {
  it('should login', () => {
    cy.visit('/login')

    cy.intercept('GET','/api/session',[]).as('session')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('auth information')


    cy.get('input[formControlName=email]').type("user@studio.com")
    cy.get('input[formControlName=password]').type(`${"user"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('should show account information', () => {

    cy.intercept('GET', '/api/user/1', {
      body: [{
              id: 1,
              email: 'user@studio.com',
              lastName: 'lastName',
              firstName: 'firstName',
              admin: false,
              createdAt: '2023-04-10T00:00:00',
              updatedAt: '2023-04-10T00:00:00',
      }]
    }).as('user information')


    cy.get('span[data-test-id=account-btn]').click()
    cy.url().should('include', '/me')
    cy.get('button[data-test-id=delete-btn]').should('exist')
  })


  it('should delete the account', () => {

    cy.intercept('GET', '/api/user/1', {
      body: [{
              id: 1,
              email: 'user@studio.com',
              lastName: 'lastName',
              firstName: 'firstName',
              admin: false,
              createdAt: '2023-04-10T00:00:00',
              updatedAt: '2023-04-10T00:00:00',
      }]
    }).as('user information')

    cy.intercept('GET', '/api/session', {});

    cy.intercept('DELETE', '/api/user/1', {statusCode: 200})

    cy.url().should('include', '/me');
    cy.get('button[data-test-id=delete-btn]').click()
    cy.get('span[data-test-id=login-btn]').should('exist')
    cy.get('span[data-test-id=register-btn]').should('exist')

  })

})
