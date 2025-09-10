/// <reference types="cypress" />

describe('Session create (FormComponent)', () => {
  const teachers = [
    { id: 7, firstName: 'Sara', lastName: 'Lee' },
    { id: 8, firstName: 'Marc', lastName: 'Zed' },
  ];

  const goToCreateAsAdmin = () => {
    cy.intercept('GET', '**/api/teacher*', teachers).as('teachers');
    cy.loginAs({
      id: 1,
      admin: true,
      email: 'admin@example.com',
      firstName: 'Admin',
      lastName: 'User',
    });
    cy.location('pathname', { timeout: 10000 }).then((p) => {
      if (p !== '/sessions') cy.contains('span.link', /^Sessions$/).click();
    });
    cy.get('button[routerLink="create"]').should('be.visible').click();
    cy.wait('@teachers', { timeout: 10000 });
  };

  it('validations: bouton Save désactivé tant que le formulaire est invalide (required + date)', () => {
    goToCreateAsAdmin();

    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName="name"]').type('Morning Flow');
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName="date"]').type('2025-11-05', { force: true });
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option')
      .contains(/sara lee/i)
      .click();
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('textarea[formControlName="description"]').type(
      'Gentle morning flow'
    );
    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('succès: crée la session puis redirige vers /sessions avec snackbar', () => {
    cy.intercept('GET', '**/api/teacher*', [
      { id: 7, firstName: 'Sara', lastName: 'Lee' },
      { id: 8, firstName: 'Marc', lastName: 'Zed' },
    ]).as('teachers');

    cy.intercept('POST', '**/api/session', {
      statusCode: 201,
      body: { id: 123 },
    }).as('create');

    cy.loginAs({
      id: 1,
      admin: true,
      email: 'admin@example.com',
      firstName: 'Admin',
      lastName: 'User',
    });

    cy.location('pathname', { timeout: 10000 }).then((p) => {
      if (p !== '/sessions') cy.contains('span.link', /^Sessions$/).click();
    });

    cy.get('button[routerLink="create"]').click();
    cy.wait('@teachers', { timeout: 10000 });

    cy.get('input[formControlName="name"]').type('Evening Stretch');
    cy.get('input[formControlName="date"]').type('2025-12-01', { force: true });
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option')
      .contains(/sara lee/i)
      .click();
    cy.get('textarea[formControlName="description"]').type(
      'Unwind at the end of the day'
    );

    cy.get('button[type="submit"]').click();

    cy.wait('@create').then((i) => {
      const s = i && i.response && i.response.statusCode;
      expect([200, 201]).to.include(s as number);
    });

    cy.contains(/session created !/i).should('be.visible');
    cy.url().should('include', '/sessions');
  });

  it('non-admin: pas de bouton Create sur la liste', () => {
    cy.loginAs({
      id: 2,
      admin: false,
      email: 'user@example.com',
      firstName: 'User',
      lastName: 'Basic',
    });
    cy.location('pathname', { timeout: 10000 }).then((p) => {
      if (p !== '/sessions') cy.contains('span.link', /^Sessions$/).click();
    });
    cy.get('button[routerLink="create"]').should('not.exist');
  });
});
