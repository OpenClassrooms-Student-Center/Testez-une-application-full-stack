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
        },
        {
          "id": 5,
          "name": "Yoga intermédiaire",
          "date": "2020-12-12T16:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga doux pour intermédiaire",
          "users": [],
          "createdAt": "2023-04-28T11:12:11",
          "updatedAt": "2023-04-28T11:12:11"
        }
      ]
    }).as('sessionCreated');

    cy.intercept('GET', '/api/teacher', {
      body:
        [
          {
            id: 1,
            lastName: "DELAHAYE",
            firstName: "Margot",
            createdAt: "2023-04-28T11:08:31",
            updatedAt: "2023-04-28T11:08:31"
          },
          {
            id: 2,
            lastName: "THIERCELIN",
            firstName: "Hélène",
            createdAt: "2023-04-28T11:08:31",
            updatedAt: "2023-04-28T11:08:31"
          }
        ]
    }).as('teacher');
    cy.intercept('POST', '/api/session', {
      body:
          {
            "id": 5,
            "name": "Yoga intermédiaire",
            "date": "2020-12-12T16:00:00.000+00:00",
            "teacher_id": 1,
            "description": "Yoga doux pour intermédiaire",
            "users": [],
            "createdAt": "2023-04-28T11:12:11",
            "updatedAt": "2023-04-28T11:12:11"
          }
    }).as('createSession');
  });

  it('Should show Creation page and create the yoga session. Session appears on the Session List page', () => {
    //Given
    cy.visit('/sessions/create');
    cy.get('@login');
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('@session');
    //When
    cy.contains('Create').click();
    cy.get('@teacher');
    cy.url().should('include', '/sessions/create');
    cy.get('input[formControlName=name]').type("Yoga intermédiaire")
    cy.get('input[formControlName=date]').type("2020-12-12")
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type("Yoga doux pour intermédiaire")
    cy.get('button[type=submit]').click();
    //Then
    cy.get('@sessionCreated');
    cy.get('button[routerLink="create"]').should('contain', 'Create')
    cy.url().should('include', '/sessions')
  });

  it('Should show Creation page and create the yoga session. Session appears on the Session List page', () => {
    //Given
    cy.visit('/sessions/create');
    cy.get('@login');
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('@session');
    //When
    cy.contains('Create').click();
    cy.get('@teacher');

    cy.url().should('include', '/sessions/create');
    cy.get('input[formControlName=name]').type("Yoga intermédiaire")
    cy.get('input[formControlName=date]').clear();
    cy.get('textarea[formControlName=description]').type("Yoga doux pour intermédiaire")
    //Then
    cy.get('button[type=submit]').should('be.disabled');
    // cy.get('input["aria-invalid"]').should('contain', 'true');
    // cy.get('input["aria-required"]').should('contain',  'true');

  });

});
