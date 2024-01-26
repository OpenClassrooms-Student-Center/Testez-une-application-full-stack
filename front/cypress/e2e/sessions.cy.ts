/// <reference types="Cypress" />

describe('Sessions page', () => {
  describe('As an admin', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true,
        },
      }).as('login');

      // * Admin account
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

      cy.wait('@login').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
    });

    it('Performs the following actions:', () => {
      // ? Visits the list of sessions and sees all active sessions
      cy.visit('/sessions');
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

      // * User account
      cy.get('input[formControlName=email]').type('user@user.com');
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);
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

      // Navigates to the /sessions page
      cy.visit('/sessions');

      // Clicks the first session's Details button
      cy.get('button[mat-icon-button][aria-label="Details"]').first().click();

      // Checks for the absence of the delete button
      cy.get('button[aria-label="Delete"]').should('not.exist');

      // Toggles the participation state and confirms the change
      cy.get('button[mat-menu-item]:contains("Join")').should('exist').click();
      cy.get('button[mat-menu-item]:contains("Leave")').should('exist');

      // Returns to the /sessions page
      cy.get('a[href="/sessions"]').click();
    });
  });
});
