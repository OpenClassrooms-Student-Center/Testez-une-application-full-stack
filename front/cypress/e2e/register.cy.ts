/// <reference types="Cypress" />

const FIRST_NAME_INPUT = 'input[formcontrolname="firstName"]';
const LAST_NAME_INPUT = 'input[formcontrolname="lastName"]';
const EMAIL_INPUT = 'input[formcontrolname="email"]';
const PASSWORD_INPUT = 'input[formcontrolname="password"]';
const REGISTER_BUTTON = 'button[type="submit"]';
const ERROR_MESSAGE = '.error';

describe('Register page', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('Should create account with success', () => {
    cy.intercept('POST', '/api/auth/register', {}).as('register');

    cy.get('mat-card-title').should('be.visible');
    cy.contains('First name').should('be.visible');
    cy.contains('Last name').should('be.visible');
    cy.contains('Email').should('be.visible');
    cy.contains('Password').should('be.visible');
    cy.contains('Submit').should('be.visible');

    cy.get(FIRST_NAME_INPUT).type('Younes');
    cy.get(LAST_NAME_INPUT).type('LAHOUITI');
    cy.get(EMAIL_INPUT).type('e2e@test.com');
    cy.get(PASSWORD_INPUT).type('test!1234');

    cy.get(REGISTER_BUTTON).click();

    cy.url().should('include', '/login');
  });

  it('should show error for a required field not properly filled', () => {
    cy.get(FIRST_NAME_INPUT).type('Test');
    cy.get(LAST_NAME_INPUT).type('TEST');
    cy.get(EMAIL_INPUT).type('invalid');

    cy.get(PASSWORD_INPUT).type('test!1234');

    cy.get(REGISTER_BUTTON).then(($buttons) => {
      const disabledButtonsCount = $buttons.filter(':disabled').length;
      expect(disabledButtonsCount).to.equal(1);
    });
  });
});
