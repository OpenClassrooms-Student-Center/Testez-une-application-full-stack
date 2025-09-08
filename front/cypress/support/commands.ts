// ***********************************************
// Cette déclaration de namespace permet
// l'autocomplétion dans VS Code ou autres IDE.
// ***********************************************

/// <reference types="cypress" />

declare global {
  namespace Cypress {
    interface Chainable {
      /**
       * Simule une authentification utilisateur (admin ou non)
       * et intercepte les appels HTTP nécessaires à l'application.
       *
       * @example
       * cy.loginAs({ admin: true, email: 'admin@example.com' });
       */
      loginAs(
        user?: Partial<{
          id: number;
          admin: boolean;
          email: string;
          firstName: string;
          lastName: string;
          username: string;
        }>
      ): Chainable<void>;
    }
  }
}

// ***********************************************
// Définition de la commande personnalisée : cy.loginAs()
// ***********************************************

Cypress.Commands.add('loginAs', (user = {}) => {
  const u = {
    id: 1,
    admin: true,
    email: 'john.doe@example.com',
    firstName: 'John',
    lastName: 'Doe',
    username: 'user',
    ...user,
  };

  // Interception de la requête GET /api/session (utilisée par les guards)
  cy.intercept('GET', '**/api/session', {
    id: u.id,
    admin: u.admin,
  }).as('sessions');

  // Interception de la requête POST /api/auth/login (lors de la soumission du formulaire)
  cy.intercept('POST', '**/api/auth/login', {
    statusCode: 200,
    body: {
      id: u.id,
      admin: u.admin,
      username: u.username,
      firstName: u.firstName,
      lastName: u.lastName,
    },
  }).as('login');

  // Simulation de l'action utilisateur : login
  cy.visit('/login');
  cy.get('input[formControlName="email"]').type(u.email);
  cy.get('input[formControlName="password"]').type('test!1234');
  cy.get('button[type="submit"]').click();

  // Attente des deux requêtes HTTP interceptées
  return cy
    .wait('@login')
    .then(() => cy.wait('@sessions'))
    .then(() => {}) as unknown as Cypress.Chainable<void>;
});

export {};
