describe('Admin buttons visibility', ()=>{

  it('should show session list', ()=>{

    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {});

    const sessionsList = [
      {
      id: 1,
      name: "test",
      date: "2023-07-27T00:00:00",
      teacher_id: 1,
      description: "test description",
      users: [],
      createdAt: "2023-07-28T00:00:00",
      updatedAt: "2023-07-29T00:00:00"
    },
    {
      id: 2,
      name: "Frida",
      date: "2023-08-21T08:49:00.000+00:00",
      teacher_id: 2,
      description: "Description for Frida Kalo",
      users: [],
      createdAt: "2023-08-21T10:49:00",
      updatedAt: "2023-08-25T15:22:27",
    }
  ];
    cy.intercept('GET', '/api/sessions', sessionsList);

    cy.get('input[formControlName="email"]').type("yoga@studio.com");
    cy.get('input[formControlName="password"]').type("test!1234");
    cy.get('button[type="submit"]').click();

    cy.url().should('include', 'sessions');
  });
    
  it('should show create and detail button for admin users', ()=>{

        //Visiter la page login
        cy.visit('/login');

        // Intercepter l'url et envoyer le body
        cy.intercept('POST', '/api/auth/login', {
 
         body: {
           id: 1,
           username: 'yoga@studio.com',
           firstName: 'Admin',
           lastName: 'Admin',
           admin: true
         }
        });
 
        //Declarer les sorties attendues
        const sessionInformation = [{
         token: 'test',
         type: 'type',
         id: 1,
         username: 'yoga@studio.com',
         firstName: 'Admin',
         lastName: 'Admin',
         admin: true,
     }];
 
     // Intercepter la session
        cy.intercept(
         {
           method: 'GET',
           url: '/api/session',
         },
         sessionInformation).as('session')
 
     //Remplir les formulaires
        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        //Check if "Create" button is visible for admin
        const create = cy.get('button[mat-raised-button]');
        create.get('mat-icon').should('contain', 'add');
        create.get('.ml1').should('contain', 'Create');
        create.contains('Create').should('be.visible');
   
        //Check if "Detail" button is visible for admin
        const detail = cy.get('button[mat-raised-button]');
        detail.get('mat-icon').should('contain', 'search');
        detail.get('.ml1').should('contain', 'Detail');
        detail.contains('Detail').should('be.visible');
    });
    it('should not show buttons for non admin users', ()=>{

        cy.visit('login');

        cy.intercept('POST', '/api/auth/login', {
          body: {
            id: 1,
            username: 'username',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
          }
        });

        const sessionInformationForNonAdmin = [{
          token: 'token',
          type: 'Bearer',
          id: 1,
          username: 'username',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        }];

        cy.intercept({method: 'GET', url: '/api/sessions'}, sessionInformationForNonAdmin) as 'session';

        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        // Check if the "Create" button is not visible for non-admin users
        cy.contains('Create').should('not.exist');

        // Check if the "Detail" button is not visible for non-admin users
        cy.contains('Detail').should('not.exist');
    }),

    it('should create a session', ()=>{
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Admin',
            lastName: 'admin',
            admin: true
      });

      cy.intercept('GET', '/api/session', [
        {
          id: 1,
          name: "test",
          date: "2023-07-27T00:00:00",
          teacher_id: 1,
          description: "test description",
          users: [],
          createdAt: "2023-07-28T00:00:00",
          updatedAt: "2023-07-29T00:00:00"
        }
      ]);
      cy.intercept('GET', '/api/teacher', [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
      },
      {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
        }
    ]);

      cy.get('input[formControlName="email"]').type('yoga@studio.com');
      cy.get('input[formControlName="password"]').type('test!1234');
      cy.get('button[type="submit"]').click();

      cy.contains('Create').click();

      //Create session
      cy.get('input[formControlName="name"]').type('Altina');
      cy.get('input[formControlName="date"]').type('2023-08-29');

      //teacher        
      const teacher_id = cy.get('mat-select[formControlName="teacher_id"]');
      teacher_id.click();
      teacher_id.get('mat-option');
      teacher_id.contains('Margot DELAHAYE').click();      

      cy.get('textarea[formControlName="description"]').type('This is Altina schinasi');
      
      cy.intercept('POST', '/api/session', {});
      cy.get('button[type="submit"]').click();
    }),
    it('Delete session', ()=>{
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Admin',
            lastName: 'admin',
            admin: true
      });

      cy.intercept('GET', '/api/session', [
        {
          id: 1,
          name: "test",
          date: "2023-07-27T00:00:00",
          teacher_id: 1,
          description: "test description",
          users: [],
          createdAt: "2023-07-28T00:00:00",
          updatedAt: "2023-07-29T00:00:00"
        }
      ]);
      cy.intercept('GET', '/api/teacher', [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
      },
      {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
        }
    ]);

      cy.get('input[formControlName="email"]').type('yoga@studio.com');
      cy.get('input[formControlName="password"]').type('test!1234');
      cy.get('button[type="submit"]').click();

      cy.intercept('GET', '/api/session/1',{
          id: 1,
          name: "test",
          date: "2023-07-27T00:00:00",
          teacher_id: 1,
          description: "test description",
          users: [],
          createdAt: "2023-07-28T00:00:00",
          updatedAt: "2023-07-29T00:00:00"
      });

      cy.intercept('GET', '/api/teacher/1', {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2023-07-03T15:55:49",
        updatedAt: "2023-07-03T15:55:49"
    });
      cy.contains('Detail').click();

      //Delete
      cy.intercept('DELETE', '/api/session/1', {});
      
      const deleteSession = cy.get('button[mat-raised-button]').should('be.visible');
      deleteSession.get('mat-icon').should('contain', 'delete');
      deleteSession.get('.ml1').should('contain', 'Delete');

      deleteSession.contains('Delete').click()

      cy.contains('Session deleted !');
      
    }),

    it('should modify a session', ()=>{
      
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Admin',
            lastName: 'admin',
            admin: true
      });

      cy.intercept('GET', '/api/session', [
        {
          id: 1,
          name: "test",
          date: "2023-07-27T00:00:00",
          teacher_id: 1,
          description: "test description",
          users: [],
          createdAt: "2023-07-28T00:00:00",
          updatedAt: "2023-07-29T00:00:00"
        }
      ]);
      cy.intercept('GET', '/api/teacher', [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
      },
      {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2023-07-03T15:55:49",
          updatedAt: "2023-07-03T15:55:49"
        }
    ]);

      cy.get('input[formControlName="email"]').type('yoga@studio.com');
      cy.get('input[formControlName="password"]').type('test!1234');
      cy.get('button[type="submit"]').click();

      cy.intercept('GET', '/api/session/1',{
          id: 1,
          name: "test",
          date: "2023-07-27T00:00:00",
          teacher_id: 1,
          description: "test description",
          users: [],
          createdAt: "2023-07-28T00:00:00",
          updatedAt: "2023-07-29T00:00:00"
      });

      cy.intercept('GET', '/api/teacher/1', {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2023-07-03T15:55:49",
        updatedAt: "2023-07-03T15:55:49"
    });
      cy.contains('Edit').click();

      cy.get('input[formControlName="name"]').clear();

      cy.get('input[formControlName="name"]').type('Teste uno');

      cy.get('textarea[formControlName="description"]').clear();

      cy.get('textarea[formControlName="description"]').type('Description de teste uno');

      cy.intercept('PUT', '/api/session/1',{});
      cy.get('button[type="submit"]').click();

      cy.contains('Session updated !').should('be.visible');
    });
});
