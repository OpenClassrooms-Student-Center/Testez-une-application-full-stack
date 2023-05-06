import * as cypress from "cypress";
describe('Login spec', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('login');
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
          "name": "Yoga dynamique",
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
    }).as('session');
    cy.intercept('GET', '/api/session/1', {
      body: {
        "id": 1,
        "name": "Yoga doux",
        "date": "2020-12-12T10:00:00.000+00:00",
        "teacher_id": 1,
        "description": "Yoga doux pour débutant",
        "users": [],
        "createdAt": "2023-04-28T11:12:11",
        "updatedAt": "2023-04-28T11:12:11"
      }
    }).as('sessionWithId1');
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2023-04-28T11:08:31",
        updatedAt: "2023-04-28T11:08:31"
      }
    }).as('teacher1');
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: "yoga@studio.com",
        lastName: "Admin",
        firstName: "Admin",
        admin: true,
        createdAt: "2023-04-28T11:08:31",
        updatedAt: "2023-04-28T11:08:31"
      }
    }).as('user1');
  });

  it('Session should Logout user and return to login Page', () => {
    //Given
    cy.visit('/me');
    cy.get('@login');
    //When
    cy.get('input[formControlName=email]').type(" ")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)
    //When
    cy.contains('Logout').click();
    //Then
    cy.url().should('include', '/');
    cy.get('span[routerLink="login"]').contains('Login');
    cy.get('span[routerLink="register"]').contains('Register');
  });
});
