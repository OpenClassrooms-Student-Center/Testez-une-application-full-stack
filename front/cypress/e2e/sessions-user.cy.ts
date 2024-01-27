/// <reference types="Cypress" />

describe('As a regular user', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', USER_DETAILS);

    // Log in with valid credentials
    cy.get('input[formControlName=email]').type('user@user.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.intercept('GET', '/api/session', (req) => {
      req.reply(SESSIONS_LIST);
    });

    cy.intercept('GET', `/api/session/${TEST_SESSION.id}`, TEST_SESSION);

    cy.intercept('GET', `/api/teacher`, TEACHERS_LIST);

    cy.url().should('include', '/sessions');

    cy.url().should('include', '/sessions');
  });

  it('Performs the following actions:', () => {
    // * Navigate to the details page
    cy.get('button[mat-raised-button] span')
      .contains('Edit')
      .should('not.exist');

    cy.get('button').contains('Detail').click();

    // ? Check for absence of delete button
    cy.get('button').contains('Participate').click();

    // ? Enable participation
    cy.get('button[mat-raised-button] span')
      .contains('Enable Participation')
      .click();
    cy.get('snack-bar-container')
      .contains('Successfully enabled participation.')
      .should('exist');
    cy.get('snack-bar-container button span').contains('Close').click();

    // ? Check that participation state changed
    cy.get('button[mat-raised-button] span')
      .contains('Disable Participation')
      .should('exist');

    // ? Disable participation
    cy.get('button[mat-raised-button] span')
      .contains('Disable Participation')
      .click();
    cy.get('snack-bar-container')
      .contains('Successfully disabled participation.')
      .should('exist');
    cy.get('snack-bar-container button span').contains('Close').click();

    // ? Check that participation state changed
    cy.get('button[mat-raised-button] span')
      .contains('Enable Participation')
      .should('exist');

    // ? Navigate back to the sessions page clicking the button
    cy.get('button[mat-icon-button] span').contains('Back').click();
  });
});
