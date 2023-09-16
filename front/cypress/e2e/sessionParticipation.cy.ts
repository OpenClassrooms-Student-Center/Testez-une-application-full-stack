/// <reference types="Cypress" />

describe('Session with user credential', () => {
    beforeEach(() => {
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

      cy.intercept('GET', '/api/teacher/2', { 
        body: [
            {id: 2,firstName: 'Hélène',lastName: 'THIERCELIN',}
        ]
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

      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: []
        }
      });
  
      cy.get('input[formControlName=email]').type("alex@gmail.fr");
      cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
  
    })
  
    it('should participate at a yoga session', () => {
      cy.intercept('POST', '/api/session/1/participate/1', {});
  
      cy.url().should('include', '/sessions');
      cy.get('.mat-card-actions > :nth-child(1)').click();
      cy.url().should('include', '/sessions/detail');
      cy.get('button:contains("Participate")').should('exist');
      cy.get('button:contains("Do not participate")').should('not.exist');
  
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: 'yoga',
          description: 'relax....!',
          teacher_id: 2,
          users: [1]
        }
      });

      cy.get('button:contains("Participate")').click();
      cy.get('button:contains("Do not participate")').should('exist');
    });

    it('should not participate at a yoga session', () => {
        cy.intercept('POST', '/api/session/1/participate/1', {});
  
        cy.url().should('include', '/sessions');
        cy.get('.mat-card-actions > :nth-child(1)').click();
        cy.url().should('include', '/sessions/detail');
        cy.get('button:contains("Participate")').should('exist');
        cy.get('button:contains("Do not participate")').should('not.exist');
    
        cy.intercept('GET', '/api/session/1', {
          body: {
            id: 1,
            name: 'yoga',
            description: 'relax....!',
            teacher_id: 2,
            users: [1]
          }
        });
  
        cy.get('button:contains("Participate")').click();
        cy.get('button:contains("Do not participate")').should('exist');

        cy.intercept('DELETE', '/api/session/1/participate/1', {});
            
        cy.intercept('GET', '/api/session/1', {
            body: {
            id: 1,
            name: 'yoga',
            description: 'relax....!',
            teacher_id: 2,
            users: []
            }
        });

        cy.get('button:contains("Do not participate")').click();
        cy.get('button:contains("Participate")').should('exist');
    });
     
});