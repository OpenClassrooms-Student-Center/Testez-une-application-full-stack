/// <reference types="Cypress" />

describe('Session with user credential', () => {
    it('should login as user', () => {
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'alex@gmail.fr',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      });
  
  
      cy.intercept('GET', '/api/session', {
        body: [{
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: []
        }]
      });
  
  
      cy.get('input[formControlName=email]').type("alex@gmail.fr");
      cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
  
    })
  
    it('should access to session and particpate and unparticipate', () => {
  
      cy.intercept('GET', '/api/teacher/1', { body: [{id: 2,firstName: 'Hélène',lastName: 'DELAHAYE',}]});
  
      cy.intercept('GET', '/api/session/1', {
        body: [{
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: []
        }]
      });
  
      cy.intercept('POST', '/api/session/1/participate/1', {})
  
      cy.url().should('include', '/sessions');
      cy.get('.mat-card-actions > :nth-child(1)').click();
      cy.url().should('include', '/sessions/detail')
      cy.get('.register_to_session').should('exist')
      cy.get('.unregister_to_session').should('not.exist')
  
      cy.intercept('GET', '/api/session/1', {
        body: [{
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: [1]
        }]
      });

      cy.get('.register_to_session').click()
      cy.get('.register_to_session').should('not.exist')
      cy.get('.unregister_to_session').should('exist')
  
      cy.intercept('DELETE', '/api/session/1/participate/1', {})
  
       cy.intercept('GET', '/api/session/1', {
        body: [{
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: []
        }]
      });

      cy.get('.unregister_to_session').click()
      cy.get('.unregister_to_session').should('not.exist')
      cy.get('.register_to_session').should('exist')
    })
  
  })


  /// <reference types="Cypress" />

// describe('DetailComponent', () => {
//     let sessionId: number;
//     let userId: number;
  
//     before(() => {

//         cy.visit('/login');
  
//         cy.intercept('POST', '/api/auth/login', {
//           body: {
//             id: 1,
//             username: 'yoga@studio.com',
//             firstName: 'firstName',
//             lastName: 'lastName',
//             admin: true
//           },
//         });
  
//         cy.intercept('GET', '/api/session', []).as('session');
  
//         cy.get('input[formControlName=email]').type("yoga@studio.com");
//         cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  
//         cy.url().should('include', '/sessions');
//       });

//       cy.request('POST', '/api/session', {
       
//         name: 'Nom de la session',
//         description: 'Description de la session',
//         date: '2023-09-20',
//         teacher_id: 1,
//         user: [],
//         created_at: '2021-09-20',
//         updated_at: '2021-09-20'

        
//       }).then((response) => {
//         sessionId = response.body.id;
  
//     });
  
//     beforeEach(() => {
//       cy.visit(`/sessions/detail/${sessionId}`);
//     });
  
//     it('should display session details', () => {
//       cy.get('mat-card-title h1').should('contain', 'Nom de la session');
//       cy.get('.description').should('contain', 'Description de la session');
//       cy.get('mat-card-subtitle span').should('contain', 'Nom de l\'enseignant');
//     });
  
//     it('should allow participation for users', () => {
//       cy.get('.participate-button').should('be.visible').click();
//       cy.request('POST', `/api/session/${sessionId}/participate/${userId}`).should((response) => {
//         expect(response.status).to.equal(200);
//       });
  
//       cy.get('#success-message').should('contain', 'Participation réussie !');
//       cy.get('.unparticipate-button').should('be.visible');
//     });
  
//     it('should allow unparticipation for users', () => {
//       cy.get('.unparticipate-button').should('be.visible').click();
//       cy.request('DELETE', `/api/session/${sessionId}/participate/${userId}`).should((response) => {
//         expect(response.status).to.equal(200);
//       });
  
//       cy.get('#success-message').should('contain', 'Suppression de la participation réussie !');
  
//       cy.get('.participate-button').should('be.visible');
//     });
  
//     it('should delete session for admin users', () => {
//       cy.get('.delete-button').should('be.visible').click();
//       cy.get('.mat-simple-snackbar').should('contain', 'Session deleted');
//       cy.url().should('include', '/sessions');
//     });
// });