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
          "name": "Yoga dynamique",
          "date": "2020-12-12T12:00:00.000+00:00",
          "teacher_id": 2,
          "description": "Yoga dynamique pour confirmé",
          "users": [],
          "createdAt": "2023-04-28T11:12:11",
          "updatedAt": "2023-04-28T11:12:11"
        },
        {
          "id": 2,
          "name": "Yoga doux",
          "date": "2020-12-12T10:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga doux pour débutant",
          "users": [],
          "createdAt": "2023-04-28T11:12:11",
          "updatedAt": "2023-04-28T11:12:11"
        },
        {
          "id": 3,
          "name": "Yoga intermédiaire",
          "date": "2020-12-12T14:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga intermédiaire pour intermédiaire",
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
    cy.intercept('GET', '/api/session', {
      body: [
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
          "name": "Yoga intermédiaire",
          "date": "2020-12-12T14:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga intermédiaire pour intermédiaire",
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
    }).as('sessionWithoutId1');
    cy.intercept('GET', '/api/session/2', {
      body: {
        "id": 2,
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
    cy.intercept('DELETE', '/api/session/2', []).as('deleteOk');
  });

  it('Session should show Edit and Delete in Detail page if you are the owner', () => {
    //Given
    cy.visit('/sessions');
    cy.get('@login');
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('@session');
    cy.contains('Detail').click();
    cy.get('@teacher1');
    cy.contains('Delete').click();
    //Then
    cy.get('@sessionWithoutId1');
    cy.get('@deleteOk');
    cy.contains('Yoga doux').should('not.exist');
  });
});
