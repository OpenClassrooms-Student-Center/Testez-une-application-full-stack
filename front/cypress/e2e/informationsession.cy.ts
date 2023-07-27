describe('Session information', ()=>{

    it('Session information should be correctly displayed', ()=>{
        // Intercepter l'url login
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body:{
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Admin',
                lastName: 'admin',
                admin: true
            }
        });
        //Declarer le data à charger
        const verifySessionInformation = [{
            id: 1,
            name: "test",
            date: "2023-07-27T22:00:00.000+00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-28T00:00:00"
        }];

        //Intercepter la session avec le data
        cy.intercept('GET', '/api/session', verifySessionInformation) as 'session';
    
        //Remplir les champs obligatoires
        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"').type('test!1234');
        cy.get('button[type="submit"]').click();

        // Intercepter les les informations de la session
        cy.intercept('GET','/api/user/1', {
            body:{
                admin: true,
                createdAt: "2023-07-03T15:55:49",
                email: "yoga@studio.com",
                firstName: "Admin",
                id: 1,
                lastName: "Admin",
                updatedAt: "2023-07-03T15:55:49"
            }
        });

        //Visible informations
        const sessionInformation = {  
            id: 1,
            admin: 'You are admin',
            createdAt: 'July 3, 2023',
            email: "yoga@studio.com",
            firstName: "Admin",
            lastName: "ADMIN",
            updatedAt: 'July 3, 2023'
        };

        //Page Account
        cy.contains('Account').should('be.visible').click();
        
        //Session information verified
        cy.contains(sessionInformation.admin ? 'You are admin' : 'User').should('be.visible');
        cy.contains(sessionInformation.createdAt).should('be.visible');
        cy.contains(sessionInformation.email).should('be.visible');
        cy.contains(sessionInformation.firstName).should('be.visible');
        cy.contains(sessionInformation.lastName).should('be.visible');
        cy.contains(sessionInformation.updatedAt).should('be.visible');
    
    });
});