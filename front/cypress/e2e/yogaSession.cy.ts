/// <reference types="Cypress" />

describe('Session Form Component', () => {
 beforeEach(() => {
  // it('should login as admin', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
    
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  
  })


  it('should create a new yoga session', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/teacher', {body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE'}]});

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


    cy.url().should('include', '/sessions');

    cy.get('button.create-button').click();
    
    cy.get('input[formControlName="name"]').type('Nouvelle session');
    cy.get('input[formControlName="date"]').type('2023-12-11');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').clear().type('Ceci est une nouvelle session de test.');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions');


    // it('should update a session', () => {
    
      // cy.url().should('include', '/sessions');
    
      cy.intercept('GET', '/api/teacher', {
        body: [{
            id: 1,
            firstName: 'Margot',
            lastName: 'DELAHAYE',
        }]
      });
    
      cy.intercept('POST', '/api/session', {
        body: {
          id: 1,
          name: 'Stretching',
          description: 'for beginners',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1,
        },
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
    
      cy.intercept('PUT', '/api/session/1', {
        body: {
          id: 1,
          name: 'Pilates',
          description: 'Intermediate level',
          date: '2024-05-15T00:00:00.000+00:00',
          teacher_id: 1
        },
     });
    
     cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: 'Pilates',
        description: 'Intermediate level',
        date: '2024-05-15T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      }]
     });
    
      cy.get('.mat-card-actions > .ng-star-inserted').click();
      cy.get('h1').invoke('text').should('contains', 'Update session')
      cy.get('input[formControlName=name]').clear().type("Pilates");
      cy.get('input[formControlName=date]').type("2024-05-15");
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
      cy.get('textarea[formControlName=description]').clear().type("Intermediate level");
      cy.get('button[type=submit]').click()
      cy.url().should('include', '/sessions');
    // })
    
    // it('should delete a session', () => {
    
      cy.intercept('GET', '/api/teacher/1', { body: [{id: 1,firstName: 'Margot',lastName: 'DELAHAYE',}]});
    
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
    
      cy.intercept('GET', '/api/session', {});
    
      cy.intercept('DELETE', '/api/session/1', {statusCode: 200})
    
      cy.url().should('include', '/sessions');
      cy.get('.mat-card-actions > :nth-child(1)').click();
      cy.url().should('include', '/sessions/detail')
      cy.get('h1').invoke('text').should('contains', 'Pilates')
     
      cy.get('button.mat-raised-button:contains("Delete")').click();

      cy.url().should('include', '/sessions');
    
    })
});
