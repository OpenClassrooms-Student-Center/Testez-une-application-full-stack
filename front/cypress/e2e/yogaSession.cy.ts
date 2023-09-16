/// <reference types="cypress" />

describe('Session Form Component', () => {

  // login before each test as admin
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/teacher', { 
      body: [
        { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }
      ] 
    });

    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: 'Hard Workout',
        description: 'New session for expert!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: 'Hard Workout',
        description: 'New session for expert!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      }]
    });

    // fill the login form
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
    cy.url().should('include', '/sessions');
  });

  // check the possibility to create a new yoga session
  it('should create a new yoga session', () => {
    cy.url().should('include', '/sessions');
    // click on the create button
    cy.get('.create-button').click();
    // fill the form to create a new session
    cy.get('input[formControlName="name"]').type('Nouvelle session');
    cy.get('input[formControlName="date"]').type('2023-12-11');
    cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').type('Ceci est une nouvelle session de test!');
    cy.get('button[type="submit"]').click();
  });

  // check if the button 'save' (submit) is disabled when the name is empty in the creation form of a new yoga session
  it('should be not possible to create a new yoga session when the name is empty ', () => {
    cy.url().should('include', '/sessions');
    // click on the create button
    cy.get('.create-button').click();
    // fill the form to create a new session
    cy.get('input[formControlName="name"]').clear();
    cy.get('input[formControlName="date"]').type('2023-12-11');
    cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').type('Ceci est une nouvelle session de test!');
    // check if the button 'save' is disabled
    cy.get('button[type="submit"]').should('be.disabled');
  });

  // check if the list of sessions created is displayed 
  it('should display list of sessions', () => {
    cy.url().should('include', '/sessions');
    // check if the session informations are displayed correctly
    cy.get('.mat-card-title').should('contain', 'Hard Workout');
    cy.get('.mat-card-content').should('contain', 'New session for expert!');
    // check if the buttons create and edit are displayed
    cy.get('.create-button').should('exist');
    cy.get('.mat-card-actions > .ng-star-inserted').should('exist');
  });

  // check if the session details are displayed correctly
  it('should display session details', () => {
    cy.url().should('include', '/sessions')

    cy.intercept('GET', '/api/teacher/2', { 
      body: [
        {id: 2,firstName: 'Hélène',lastName: 'THIERCELIN'}
      ]
    });
    
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'Pilates',
          description: 'Intermediate level',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 2,
          users: [],
        }
      });
      
      // click on the session details button
      cy.get('.mat-card-actions > :nth-child(1)').click();
      cy.url().should('include', '/sessions/detail');
      // check the session details elements
      cy.get('h1').invoke('text').should('contains', 'Pilates');
      cy.get('.mat-card-content').should('contain', 'Intermediate level');
      // check if the button 'delete' is displayed
      cy.get('button.mat-raised-button:contains("Delete")').should('be.visible');
  });

  // check the possibility to edit a session
   it('should update a session', () => {
    cy.url().should('include', '/sessions');

      cy.intercept('GET', '/api/teacher', {
        body: [{
            id: 1,
            firstName: 'Margot',
            lastName: 'DELAHAYE',
        }]
      });
    
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'Stretching',
          description: 'for beginners',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      });
    
      cy.intercept('POST', '/api/session/1', {
        body: {
          id: 1,
          name: 'Pilates',
          description: 'Intermediate level',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1
        },
     });

      // click on the edit button
      cy.get('.mat-card-actions > .ng-star-inserted').click();
      // check if the edit form is displayed
      cy.get('h1').invoke('text').should('contains', 'Update session');
      // fill the form to update the session
      cy.get('input[formControlName=name]').clear().type("Pilates");
      cy.get('input[formControlName=date]').type("2024-05-15");
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
      cy.get('textarea[formControlName=description]').clear().type(`Intermediate level{enter}{enter}`);
      cy.url().should('include', '/sessions');
    });

    // check if the button 'save' (submit) is disabled when the name is empty in the edit form of a new yoga session
    it('should be not possible to update a session when the name is empty ', () => {
      cy.url().should('include', '/sessions');

      cy.intercept('GET', '/api/teacher', {
        body: [{
            id: 1,
            firstName: 'Margot',
            lastName: 'DELAHAYE',
        }]
      });
    
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'Stretching',
          description: 'for beginners',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      });
    
      cy.intercept('POST', '/api/session/1', {
        body: {
          id: 1,
          name: 'Pilates',
          description: 'Intermediate level',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1
        },
     });

      // click on the edit button
      cy.get('.mat-card-actions > .ng-star-inserted').click();
      // check if the edit form is displayed
      cy.get('h1').invoke('text').should('contains', 'Update session');
      // fill the form to update the session with an empty name
      cy.get('input[formControlName=name]').clear();
      cy.get('input[formControlName=date]').type("2024-05-15");
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
      // check if the button 'save' is disabled
      cy.get('button[type="submit"]').should('be.disabled');
      cy.url().should('include', '/sessions');

    });
    
    // check the possibility to delete a session
    it('should delete a session', () => {
      cy.intercept('GET', '/api/teacher/1', { 
        body: [
          { id: 1, firstName: 'Margot', lastName: 'DELAHAYE'}
        ]
      });
    
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'Pilates',
          description: 'Intermediate level',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      });
    
      cy.intercept('DELETE', '/api/session/1', {
        statusCode: 200
      });
    
      cy.url().should('include', '/sessions');
      // click on the session details button
      cy.get('.mat-card-actions > :nth-child(1)').click();
      cy.url().should('include', '/sessions/detail');
      // check the session to delete is displayed
      cy.get('h1').invoke('text').should('contains', 'Pilates');
      // click on the delete button
      cy.get('button.mat-raised-button:contains("Delete")').click();
      cy.url().should('include', '/sessions');
    });
});
