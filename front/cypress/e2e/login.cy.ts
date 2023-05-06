import * as cypress from "cypress";

describe('Login spec', () => {
  it('Login successful', () => {
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

    cy.url().should('include', '/sessions')
  });

  it('Login failed because of wrong email & password', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {
        message: 'Unauthorized',
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("robert@studio.com")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)

    cy.url().should('include', '/login')
    cy.get('p[class="error ng-star-inserted"]').should('contain', 'An error occurred')

  });

  it('Login failed because of less than 3 character password', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {
        message: 'Unauthorized',
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("robert@studio.com")
    cy.get('input[formControlName=password]').type(`${" "}{enter}{enter}`)

    cy.url().should('include', '/login')
    cy.get('p[class="error ng-star-inserted"]').should('contain', 'An error occurred')
  });

  it('Login failed because of lack of email', () => {
    //Given
    cy.visit('/login')
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {
        message: 'Unauthorized',
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
    //When
    cy.get('input[formControlName=email]').type(" ")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)
    //Then
    cy.url().should('include', '/login')
    cy.get('p[class="error ng-star-inserted"]').should('contain', 'An error occurred')
  });
});
