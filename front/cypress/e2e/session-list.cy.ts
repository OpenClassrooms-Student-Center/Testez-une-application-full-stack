/// <reference types="cypress" />

describe('Session list (ListComponent)', () => {
  const mockSession = {
    id: 1,
    name: 'Toto',
    date: '1987-06-20T00:00:00.000+00:00',
    teacher_id: 1,
    description: 'Je suis une description',
    users: [],
    createdAt: '2025-09-08T12:37:30',
    updatedAt: '2025-09-08T12:37:30',
  };

  beforeEach(() => {
    cy.intercept('GET', '**/api/session', [mockSession]).as('getSessions');
  });

  const loginAndGoToSessions = (admin: boolean) => {
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        id: admin ? 1 : 2,
        username: admin ? 'admin' : 'user',
        firstName: admin ? 'Admin' : 'User',
        lastName: admin ? 'User' : 'Basic',
        admin,
        token: 'fake-jwt-token',
      },
    }).as('loginOk');

    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginOk');
    cy.url().should('include', '/sessions');
    cy.wait('@getSessions');
  };

  it('affiche la liste des sessions avec leurs données', () => {
    loginAndGoToSessions(true);

    cy.contains('mat-card-title', mockSession.name).should('be.visible');
    cy.contains('mat-card-subtitle', /June 20, 1987/i).should('be.visible');
    cy.contains('mat-card-content p', mockSession.description).should(
      'be.visible'
    );

    cy.get('button span.ml1')
      .contains(/detail/i)
      .should('be.visible');
    cy.get('button span.ml1').contains(/edit/i).should('be.visible');
  });

  it('n’affiche pas le bouton Edit si non-admin', () => {
    loginAndGoToSessions(false);

    cy.get('button span.ml1')
      .contains(/detail/i)
      .should('be.visible');
    cy.get('button span.ml1').contains(/edit/i).should('not.exist');
  });
});
