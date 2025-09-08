/// <reference types="cypress" />

describe('Account (/me)', () => {
  it('affiche les informations de l’utilisateur connecté', () => {
    // (debug facultatif) loggue toutes les requêtes /api dans la console du runner
    cy.intercept('**/api/**', (req) => {
      console.log('[API]', req.method, req.url);
      req.continue();
    });

    // Intercept EXACT d'après ton UserService: GET api/user/:id (singulier)
    cy.intercept('GET', '**/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
      },
    }).as('getMe');

    // 1) Login
    cy.loginAs({
      id: 1,
      email: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
    });

    // 2) Navigation SPA → pas de cy.visit('/me') qui rechargerait l'app
    cy.contains('span.link', /^Account$/)
      .should('be.visible')
      .click();

    // 3) Attendre l'appel du composant puis asserter
    cy.wait('@getMe', { timeout: 10000 });
    cy.contains(/john/i).should('be.visible');
    cy.contains(/doe/i).should('be.visible');
    cy.contains(/john\.doe@example\.com/i).should('be.visible');
  });
});
