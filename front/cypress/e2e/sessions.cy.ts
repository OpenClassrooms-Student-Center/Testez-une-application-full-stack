/// <reference types="Cypress" />

const ADMIN_DETAILS = {
  token: 'jwt',
  type: 'Bearer',
  id: 1,
  username: 'yoga@studio.com',
  firstName: 'Admin',
  lastName: 'Admin',
  admin: true,
};

const USER_DETAILS = {
  token: 'jwt',
  type: 'Bearer',
  id: 2,
  username: 'user@user.com',
  firstName: 'User',
  lastName: 'User',
  admin: false,
};

const TEST_SESSION = {
  id: 1,
  name: 'TEST session',
  date: '2024-01-13T13:27:22.000+00:00',
  teacher_id: 1,
  description: 'TEST description for the session',
  users: [],
  createdAt: '2024-01-13T14:24:33',
  updatedAt: '2024-01-26T09:20:22',
};

const SESSIONS_LIST = [TEST_SESSION];

const EDITED_TEST_SESSION = {
  ...TEST_SESSION,
  name: 'EDITED TEST session',
};

describe('Sessions page', () => {
  describe('As an admin', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);

      // Log in with valid credentials
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.intercept('GET', '/api/session', SESSIONS_LIST);
      cy.intercept('GET', `/api/session/${TEST_SESSION.id}`, TEST_SESSION);

      cy.intercept('DELETE', `/api/session/${TEST_SESSION.id}`, {
        body: null,
      });

      cy.intercept(
        'PUT',
        `/api/session/${TEST_SESSION.id}`,
        EDITED_TEST_SESSION
      );

      cy.url().should('include', '/sessions');
    });

    it('Performs the following actions:', () => {
      // ? Visits the list of sessions and sees all active sessions
      // TODO: Check for presence of rows representing active sessions
      // ? Creates a new session and adds it to the list
      // TODO: Open the create session dialog
      // TODO: Fill in the form fields
      // TODO: Submit the form
      // TODO: Check for the new session in the list
      // ? Navigates to the details page and ensures data matches
      // TODO: Navigate to the details page
      // TODO: Extract session data from the page
      // TODO: Make API call to retrieve the session from the backend
      // TODO: Compare extracted data with received data
      // ? Deletes a session
      // TODO: Delete a session
      // TODO: Refresh the list page
      // TODO: Check that the session no longer appears in the list
      // ? Edits a session and saves the changes
      // TODO: Edit a session
      // TODO: Save the changes
      // TODO: Compare the saved data with the updated data in the backend
    });
  });

  describe('As a regular user', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', USER_DETAILS);

      // Log in with valid credentials
      cy.get('input[formControlName=email]').type('user@user.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.intercept('GET', '/api/session', SESSIONS_LIST);
      cy.intercept('GET', `/api/session/${TEST_SESSION.id}`, TEST_SESSION);

      cy.intercept('DELETE', `/api/session/${TEST_SESSION.id}`, {
        body: null,
      });

      cy.intercept(
        'PUT',
        `/api/session/${TEST_SESSION.id}`,
        EDITED_TEST_SESSION
      );

      cy.url().should('include', '/sessions');
    });

    it('Performs the following actions:', () => {
      // ? Views session details and does not see the delete button
      // TODO: Navigate to the details page
      // TODO: Check for absence of delete button
      //
      // ? Enables and disables session participation
      // TODO: Navigate to the details page
      // TODO: Enable participation
      // TODO: Disable participation
      // TODO: Check that participation state changes appropriately
      // TODO: Check that we do not have the button to delete a session
      // TODO: Navigate back to the sessions page clicking the button
    });
  });
});
