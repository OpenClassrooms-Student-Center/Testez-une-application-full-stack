/// <reference types="cypress" />

describe('Account (/me)', () => {
  it('affiche les informations de l’utilisateur', () => {
    cy.intercept('GET', '**/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
      },
    }).as('getMe');

    cy.loginAs({
      id: 1,
      email: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
    });

    cy.contains('span.link', /^Account$/)
      .should('be.visible')
      .click();

    cy.wait('@getMe', { timeout: 10000 });
    cy.contains(/john/i).should('be.visible');
    cy.contains(/doe/i).should('be.visible');
    cy.contains(/john\.doe@example\.com/i).should('be.visible');
  });
});
