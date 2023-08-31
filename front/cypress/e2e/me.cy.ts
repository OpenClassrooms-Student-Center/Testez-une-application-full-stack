describe('Me', () => {
    it('User information for admin', () => {
        cy.visit('/login');

        //Interceptions
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Admin',
                lastName: 'ADMIN',
                admin: true
            }
        });

        const session = [{
            id: 1,
            name: "test",
            date: "2023-07-27T00:00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-29T00:00:00"
        }];

        cy.intercept('GET', '/api/session', session);

        //Login
        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        cy.url().should('include', 'session');

        const user = {
            firstName: "Admin",
            lastName: "ADMIN",
            email: "yoga@studio.com",
            admin: true,
            createdAt: "2023-07-04T15:55:49",
            updatedAt: "2023-07-03T15:55:49"
        };
        cy.intercept('GET', '/api/user/1', user);
        cy.contains('Account').click();

        cy.get('h1').should('be.visible');
        cy.contains(user.firstName + user.lastName ? 'Admin ADMIN' : 'something went wrong').should('be.visible');
        cy.contains(user.email ? 'yoga@studio.com' : 'invalid email').should('be.visible');
        cy.contains(user.admin ? 'You are admin' : 'You are not admin').should('be.visible');
        cy.contains(user.createdAt ? 'July 4, 2023' : '2023-07-04T15:55:49').should('be.visible');
        cy.contains(user.updatedAt ? 'July 3, 2023' : '2023-07-03T15:55:49').should('be.visible');
    });

    it('User information for not admin', ()=>{
        cy.visit('/login');

        //Interceptions
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'toto3@toto.com',
                firstName: 'toto',
                lastName: 'TOTO',
                admin: false
            }
        });

        const session = [{
            id: 1,
            name: "test",
            date: "2023-07-27T00:00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-29T00:00:00"
        }];

        cy.intercept('GET', '/api/session', session);

        //Login
        cy.get('input[formControlName="email"]').type('toto3@toto.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        cy.url().should('include', 'session');

        const user = {
            firstName: "toto",
            lastName: "TOTO",
            email: "toto3@toto.com",
            admin: false,
            createdAt: "2023-07-04T15:55:49",
            updatedAt: "2023-07-03T15:55:49"
        };
        cy.intercept('GET', '/api/user/1', user);
        cy.contains('Account').click();

        cy.get('h1').should('be.visible');
        cy.contains(user.firstName + user.lastName ? 'toto TOTO' : 'something went wrong').should('be.visible');
        cy.contains(user.email ? 'toto3@toto.com' : 'invalid email').should('be.visible');
        
        //Delete account
        cy.contains('Delete my account').should('be.visible');
        const deleteButton = cy.get('button.mat-raised-button');
        deleteButton.find('mat-icon').should('contain', 'delete');
        deleteButton.get('span .ml1').should('contain', 'Detail');


        cy.contains(user.createdAt ? 'July 4, 2023' : '2023-07-04T15:55:49').should('be.visible');
        cy.contains(user.updatedAt ? 'July 3, 2023' : '2023-07-03T15:55:49').should('be.visible');
    });
});