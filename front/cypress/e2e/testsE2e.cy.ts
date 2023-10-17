/// <reference types="Cypress" />

describe('Testing the Register Component', () => {
  beforeEach(() => {
      cy.visit('/register');
  });
  
  it('Register successfull', () => {
    cy.intercept('POST', '/api/auth/register', { 
        statusCode: 200 
    });
    // fill the register form
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('test@example.com');
    cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
    // check if the user is redirected to the login page
    cy.url().should('include', '/login');
  
  });

  it('should return error if email is already used', () => {
    cy.intercept('POST', '/api/auth/register', { 
        statusCode: 400 
    });
    // fill the register form with an email already used
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('test@example.com');
    cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
    // check if the error message is displayed
    cy.get('.error').should('be.visible');
  });

  it('should disable submit button if email is empty', () => {
    // fill the register form without email
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=password]').type(`${'password123'}{enter}{enter}`);
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    // check if the submit button is disabled
    cy.get('button[type=submit]').should('be.disabled');
  });
  
  it('should disable submit button if password is too short', () => {
    // fill the register form with a too short password
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('test@example.com');
    cy.get('input[formControlName=password]').type('123');
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
    // check if the submit button is disabled
    cy.get('button[type=submit]').should('be.disabled');
  });
});
    
describe('Testing the login Component', () => {
  it('Login successfull', () => {
    cy.visit('/login')

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
    // fill the login form
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    // check if the user is redirected to the sessions page
    cy.url().should('include', '/session')
  })

  it('should return error if email/password are not valid', () => {
    cy.visit('/login')
    // fill the login form with an invalid email and password
    cy.get('input[formControlName=email]').type("yoga2@studio.com")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)
    // check if the error message is displayed
    cy.get('.error').should('be.visible');
  })

  it('Logout successfull', () => {
    cy.visit('/login')
    // fill the login form
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
    
    // fill the login form
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    // check if the user is redirected to the sessions page
    cy.url().should('include', '/session')
    // click on the logout button
    cy.get('.link').contains('Logout').click();
  })
});

describe('Testing the account Component', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    });

    cy.intercept('GET', '/api/user/2', {
      body: {
        id: 2,
        email: 'alex@gmail.com',
        lastName: 'ALEX',
        firstName: 'Alex',
        admin: false,
        createdAt: '2023-05-05',
        updatedAt: '2023-05-05',
      }
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');
    // fill the login form
    cy.get('input[formControlName=email]').type("alex@gmail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.url().should('include', '/sessions');
  });

  it('should display the user informations', () => {
    // click on the account word
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    // check if the user informations is displayed
    cy.get('p:contains("Name:")').should('contain', 'Alex ALEX');
    cy.get('p:contains("Email:")').should('contain', 'alex@gmail.com');
    // check if the delete button is displayed
    cy.get('.my2 > .mat-focus-indicator').should('exist');
  });

  it('should allow the user to go back', () => {
    cy.intercept('GET', '/api/session', {});
      // click on the account word
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    // check if the arrow button allow the user to go back
    cy.get('button.mat-icon-button').click();
    cy.url().should('not.include', '/me');
  });

  it('should delete the account', () => {
    cy.intercept('GET', '/api/session', {});

    cy.intercept('DELETE', '/api/user/2', {
        statusCode: 200
    }).as('delete user');

    // click on the account word
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    // click on the delete button
    cy.get('.my2 > .mat-focus-indicator').click();
    // check if the message is displayed
    cy.get('.mat-simple-snack-bar-content').should('exist');

    // check if the "account" link is not displayed
    cy.contains('span.link', 'Account').should('not.exist');
    cy.url().should('not.include', '/me');
  });
});

describe('Testing the Not-found Component', () => {
  // login before each test as user
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    });

    cy.intercept('GET', '/api/user/2', {
      body: {
        id: 2,
        email: 'alex@gmail.com',
        lastName: 'ALEX',
        firstName: 'Alex',
        admin: false,
        createdAt: '2023-05-05',
        updatedAt: '2023-05-05',
      }
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');
    // fill the login form
    cy.get('input[formControlName=email]').type("alex@gmail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.url().should('include', '/sessions');
  });

  // check if the not found page is displayed
  it('should display the not found page', () => {
      cy.visit('/not-found');
      cy.url().should('include', '/404');
      cy.get('h1').should('contain', 'Page not found');
  });
});   

describe('Testing the participation in yoga session with user credential (detail component)', () => {
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
    // fill the login form
    cy.get('input[formControlName=email]').type("alex@gmail.fr");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)
  });
  
  it('should participate at a yoga session', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {});
    cy.url().should('include', '/sessions');
    cy.get('.mat-card-actions > :nth-child(1)').click();
    cy.url().should('include', '/sessions/detail');
    // check if the participate button is displayed
    cy.get('button:contains("Participate")').should('exist');
    // check if the do not participate button is not displayed
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
    // click on the participate button
    cy.get('button:contains("Participate")').click();
    // check if the do not participate button is displayed
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
    // click on the participate button
    cy.get('button:contains("Participate")').click();
    // check if the do not participate button is displayed
    cy.get('button:contains("Do not participate")').should('exist');
    // click on the do not participate button
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
    // click on the do not participate button
    cy.get('button:contains("Do not participate")').click();
    // check if the participate button is displayed
    cy.get('button:contains("Participate")').should('exist');
  });
});

describe('Testing the management of yoga session with admin credential (Form Component) ', () => {
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

  it('should display list of sessions', () => {
    cy.url().should('include', '/sessions');
    // check if the session informations are displayed correctly
    cy.get('.mat-card-title').should('contain', 'Hard Workout');
    cy.get('.mat-card-content').should('contain', 'New session for expert!');
    // check if the buttons create and edit are displayed
    cy.get('.create-button').should('exist');
    cy.get('.mat-card-actions > .ng-star-inserted').should('exist');
  });

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
