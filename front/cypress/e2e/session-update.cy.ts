/// <reference types="cypress" />

describe('Session update (FormComponent) - Option B', () => {
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

  const mockTeachers = [
    { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
    { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' },
  ];

  beforeEach(() => {
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'Admin',
        lastName: 'User',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('login');

    cy.intercept('GET', '**/api/session', [mockSession]).as('getSessions');
    cy.intercept('GET', '**/api/session/1', mockSession).as('getSession');
    cy.intercept('GET', '**/api/teacher', mockTeachers).as('getTeachers');
  });

  it('pré-remplit et met à jour la session', () => {
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@example.com');
    cy.get('input[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@login');
    cy.wait('@getSessions');

    // On clique sur le bouton Edit de la session
    cy.get('button span.ml1').contains(/edit/i).click();

    cy.wait('@getSession');
    cy.wait('@getTeachers');

    cy.get('input[formControlName="name"]').should(
      'have.value',
      mockSession.name
    );
    cy.get('textarea[formControlName="description"]').should(
      'have.value',
      mockSession.description
    );

    cy.intercept('PUT', '**/api/session/1', (req) => {
      req.reply({
        statusCode: 200,
        body: {
          ...mockSession,
          name: 'Toto updated',
          description: 'Nouvelle description',
        },
      });
    }).as('updateSession');

    cy.get('input[formControlName="name"]').clear().type('Toto updated');
    cy.get('textarea[formControlName="description"]')
      .clear()
      .type('Nouvelle description');

    cy.get('button[type="submit"]').click();
    cy.wait('@updateSession');

    cy.contains(/session updated !/i).should('be.visible');
    cy.url().should('include', '/sessions');
  });
});
