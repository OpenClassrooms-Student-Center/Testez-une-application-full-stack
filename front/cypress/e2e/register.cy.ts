describe('Register spec', () => {
  it('Register successful', () => {
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

    cy.get('input[formControlName=lastName]').type("toto")
    cy.get('input[formControlName=firstName]').type("toto")
    cy.get('input[formControlName=email]').type("toto3@toto.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/login')
  });

  it('Register failed because of wrong email & password', () => {
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

    cy.get('input[formControlName=lastName]').type("toto")
    cy.get('input[formControlName=firstName]').type("toto")
    cy.get('input[formControlName=email]').type("toto3@toto.com")
    cy.get('input[formControlName=password]').type(`com{enter}{enter}`)

    cy.url().should('include', '/register')
    cy.get('span[class="error ml2 ng-star-inserted"]').should('contain', 'An error occurred')

  });

});
