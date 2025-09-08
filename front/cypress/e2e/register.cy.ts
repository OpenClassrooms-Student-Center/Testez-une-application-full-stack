/// <reference types="cypress" />

describe('Register', () => {
  beforeEach(() => {
    // Pour laisser passer UnauthGuard
    cy.intercept('GET', '**/api/session', []).as('session');
  });

  it('désactive le bouton tant que le formulaire est invalide', () => {
    cy.visit('/register');

    cy.contains('button', /submit/i).should('be.disabled');

    cy.get('input[formControlName="firstName"]').type('Ana');
    cy.get('input[formControlName="lastName"]').type('D');
    cy.get('input[formControlName="email"]').type('not-an-email');
    cy.contains('button', /submit/i).should('be.disabled');

    cy.get('input[formControlName="email"]').clear().type('ana@example.com');
    cy.get('input[formControlName="lastName"]').clear().type('Doe');
    cy.get('input[formControlName="password"]').type('secret123');

    cy.contains('button', /submit/i).should('not.be.disabled');
  });

  it('inscription réussie → redirection /login', () => {
    cy.intercept('POST', '**/api/auth/register', {
      statusCode: 200,
      body: {},
    }).as('register');

    cy.visit('/register');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('StrongP@ss1');

    cy.contains('button', /submit/i).click();
    cy.wait('@register').its('response.statusCode').should('eq', 200);

    cy.url().should('include', '/login');
  });

  it('erreur serveur → message affiché, pas de redirection', () => {
    cy.intercept('POST', '**/api/auth/register', {
      statusCode: 400,
      body: { message: 'Bad Request' },
    }).as('registerFail');

    cy.visit('/register');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('Weak');

    cy.contains('button', /submit/i).click();
    cy.wait('@registerFail');

    cy.get('.error')
      .should('be.visible')
      .and('contain.text', 'An error occurred');
    cy.url().should('include', '/register');
  });
});
