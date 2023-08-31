describe('submit', ()=>{
    it('Update session', ()=>{
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Admin',
                lastName: 'admin',
                admin: true
            }
        });

        cy.intercept('GET', '/api/session',{
            body:  [{
                id: 1,
                name: "test",
                date: "2023-07-27T22:00:00.000+00:00",
                teacher_id: 1,
                description: "test description",
                users: [],
                createdAt: "2023-07-28T00:00:00",
                updatedAt: "2023-07-28T00:00:00"
            }]
        });

        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/session');

          //session object
          const session = {
            id: 1,
            name: "test",
            date: "2023-07-27T22:00:00.000+00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-28T00:00:00"
        };

        const teacher = [{
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
        }];

        cy.intercept('GET', '/sessions/update/1');
        cy.intercept('GET', '/api/session');
        cy.intercept('GET', '/api/session/1', session);
        cy.intercept('GET', '/api/teacher', teacher);

        cy.contains('Edit').should('be.visible').click();

        cy.intercept('PUT', '/api/session/1', session);

        //Verify editions fields
        cy.get('input[formControlName="name"]').should('be.visible');
        cy.get('input[formControlName="date"]').should('be.visible');
        
        //--Verify text options--
        cy.get('mat-select').click();
        cy.get('mat-option').contains('Margot DELAHAYE').should('be.visible');
        cy.get('mat-option').contains('Hélène THIERCELIN').should('be.visible');
        cy.get('mat-option').contains('Margot DELAHAYE').click();

        cy.get('textarea[formControlName="description"]').should('be.visible');

        cy.get('button[type="submit"]').should('be.visible');

        cy.contains('Save').click();

        cy.url().should('include', 'sessions');
    });
});