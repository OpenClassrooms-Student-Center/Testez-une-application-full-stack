/// <reference types="Cypress" />

describe('Account page', () => {
  const ADMIN_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 1,
    email: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'ADMIN',
    admin: true,
    createdAt: '2024-01-12T15:33:42',
    updatedAt: '2024-01-12T15:33:42',
  };

  const USER_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 2,
    email: 'user@user.com',
    firstName: 'User',
    lastName: 'USER',
    admin: false,
    createdAt: '2024-01-12T15:33:42',
    updatedAt: '2024-01-12T15:33:42',
  };

  beforeEach(() => {
    cy.intercept('GET', '/api/session', (req) => {
      req.reply([]);
    });

    cy.intercept('DELETE', '/api/user');
  });

  describe('As an admin', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);

      cy.intercept('GET', `/api/user/${ADMIN_DETAILS.id}`, (req) => {
        req.reply(ADMIN_DETAILS);
      });

      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('should show their info and NOT include a delete button', () => {
      cy.get('span.link').contains('Account').click();

      cy.url().should('include', '/me');

      // * We check that all the info matches
      cy.get('mat-card-title h1').should('contain', 'User information');
      cy.get('p')
        .eq(0)
        .should(
          'contain',
          `Name: ${ADMIN_DETAILS.firstName} ${ADMIN_DETAILS.lastName}`
        );
      cy.get('p').eq(1).should('contain', `Email: ${ADMIN_DETAILS.email}`);
      cy.get('p').eq(2).should('contain', `You are admin`);
      cy.get('p').eq(3).should('contain', `Create at:  January 12, 2024`);
      cy.get('p').eq(4).should('contain', `Last update:  January 12, 2024`);

      cy.get('button[mat-raised-button]').should('not.exist');
    });
  });
  describe('As a regular user', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', USER_DETAILS);

      cy.intercept('GET', `/api/user/${USER_DETAILS.id}`, (req) => {
        req.reply(USER_DETAILS);
      });

      cy.get('input[formControlName=email]').type('user@user.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('should show their info and include a delete button', () => {
      cy.get('span.link').contains('Account').click();

      cy.url().should('include', '/me');

      cy.get('mat-card-title h1').should('contain', 'User information');
      cy.get('p')
        .eq(0)
        .should(
          'contain',
          `Name: ${USER_DETAILS.firstName} ${USER_DETAILS.lastName}`
        );
      cy.get('p').eq(1).should('contain', `Email: ${USER_DETAILS.email}`);
      cy.get('p').eq(2).should('contain', `Delete my account:`);
      cy.get('p').eq(3).should('contain', `Create at:  January 12, 2024`);
      cy.get('p').eq(4).should('contain', `Last update:  January 12, 2024`);

      cy.get('button[mat-raised-button]').should('exist');
    });
  });
});
