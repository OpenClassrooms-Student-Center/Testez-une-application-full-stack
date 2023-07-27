describe('Admin buttons visibility', ()=>{
    
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
        cy.contains('Create').should('be.visible');
   
        //Check if "Detail" button is visible for admin
        cy.contains('Detail').should('be.visible');
    });
    it('should not show buttons to non admin users', ()=>{

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
    });
});
