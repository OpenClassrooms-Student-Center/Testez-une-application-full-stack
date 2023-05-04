import * as cypress from "cypress";
describe('Register e2e Tests Suites', () => {
  it('Register successful', () => {
    //Given
    cy.visit('/register')
    cy.intercept('POST', '/api/auth/register', {
      body: {
        "message": "User registered successfully!"
      },
    })
    cy.intercept(
      {
        method: 'GET',
        url: '/api/auth/login',
      },
      []).as('login')
    //When
    cy.get('input[formControlName=lastName]').type("toto")
    cy.get('input[formControlName=firstName]').type("toto")
    cy.get('input[formControlName=email]').type("toto3@toto.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    //Then
    cy.url().should('include', '/login')
  });

  it('Register failed because of wrong email & password', () => {
    //Given
    cy.visit('/register')
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        message: 'Unauthorized',
      },
    })
    cy.intercept(
      {
        method: 'GET',
        url: '/api/auth/register',
      },
      []).as('register')
    //When
    cy.get('input[formControlName=lastName]').type("toto")
    cy.get('input[formControlName=firstName]').type("toto")
    cy.get('input[formControlName=email]').type("toto3@toto.com")
    cy.get('input[formControlName=password]').type(`com{enter}{enter}`)
    //Then
    cy.url().should('include', '/register')
    cy.get('span[class="error ml2 ng-star-inserted"]').should('contain', 'An error occurred')
  });
});
