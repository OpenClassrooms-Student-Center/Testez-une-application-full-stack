///<reference path="../../node_modules/cypress/types/cypress.d.ts"/>
describe('Login spec', () => {
  it('Session should show Create and detail if user is admin', () => {
      //Given
      cy.visit('/sessions')
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      })
      cy.intercept('GET', '/api/session', {
        body: [
          {
            "id": 1,
            "name": "Yoga doux",
            "date": "2020-12-12T10:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Yoga doux pour débutant",
            "users": [],
            "createdAt": "2023-04-28T11:12:11",
            "updatedAt": "2023-04-28T11:12:11"
          },
          {
            "id": 2,
            "name": "Yoga dynamic",
            "date": "2020-12-12T12:00:00.000+00:00",
            "teacher_id": 2,
            "description": "Yoga dynamique pour confirmé",
            "users": [],
            "createdAt": "2023-04-28T11:12:11",
            "updatedAt": "2023-04-28T11:12:11"
          },
          {
            "id": 3,
            "name": "Yoga doux",
            "date": "2020-12-12T14:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Yoga doux pour débutant",
            "users": [],
            "createdAt": "2023-04-28T11:12:11",
            "updatedAt": "2023-04-28T11:12:11"
          },
          {
            "id": 4,
            "name": "Yoga dynamique",
            "date": "2020-12-12T16:00:00.000+00:00",
            "teacher_id": 2,
            "description": "Yoga dynamique pour confirmé",
            "users": [],
            "createdAt": "2023-04-28T11:12:11",
            "updatedAt": "2023-04-28T11:12:11"
          }
        ]
      });

      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
      //When
      cy.url().should('include', '/sessions')
      //Then
      cy.get('button[routerLink="create"]').should('contain', 'Create')
      cy.get('div[class="ng-star-inserted"]').should('contain', 'Logout')
      cy.get('span[class="mat-button-wrapper"]').should('contain', 'Detail')
      cy.get('span[class="mat-button-wrapper"]').should('contain', 'Edit')
      cy.get('span[routerLink="sessions"]').should('contain', 'Sessions')
      cy.get('span[routerLink="me"]').should('contain', 'Account')
    }
  );
});
