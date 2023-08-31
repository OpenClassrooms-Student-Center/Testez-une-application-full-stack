describe('session information', ()=>{
    it('should show session informations', ()=>{

        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            admin: true
        });

        const session = {
            id: 1,
            name: "test",
            date: "2023-07-27T00:00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-29T00:00:00"
        };

        cy.intercept('GET', '/api/session', [session]);

        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();

        cy.url().should('include', 'sessions');

        //Verify session informations
        
        cy.contains('Rentals available').should('be.visible');

        const sessions = cy.get('div.items');
        sessions.get('mat-card.item');
        sessions.get('mat-card-header');
        sessions.get('mat-card-title');
        sessions.contains(session.name ? 'test' : 'Test').should('be.visible');

        sessions.get('mat-card-subtitle');
        sessions.contains(session.date ? 'Session on July 27, 2023' : 0);

        const image = cy.get('div[mat-card-image]');
        image.get('img.picture').should('have.attr', 'src', 'assets/sessions.png');
        image.get('img.picture').should('have.attr', 'alt', 'Yoga session');
        image.should('be.visible');

        const description = sessions.get('mat-card-content');
        description.get('p');
        description.contains(session.description ? 'test description' : 'Test description');

    }),

    it('should show delete button for admin users', ()=>{

        
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 2,
                username: 'yoga@studio.com',
                firstName: 'admin',
                lastName: 'Admin',
                admin: true
                }
        });

        cy.intercept('GET', '/api/session', {
            body: [{
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

        cy.url().should('include', 'sessions');

        const session = {
            id: 1,
            name: "test",
            date: "2023-07-27T00:00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-29T00:00:00"
        };
        const teacher = {
            id: 1,
            lastName: "DELAHAYE",
            firstName: "Margot",
            createdAt: "2023-07-03T15:55:49",
            updatedAt: "2023-07-03T15:55:49"
            };

        cy.intercept('GET', '/api/session/1',session );
        cy.intercept('GET', '/api/teacher/1', teacher);

        const detail = cy.get('button[mat-raised-button]');
        detail.get('mat-icon').should('contain', 'search');
        detail.get('span.ml1').should('contain', 'Detail');
        
        detail.contains('Detail').click();

        //Verify delete button
        const delete_button = cy.get('button[mat-raised-button]');
        delete_button.get('mat-icon').should('contain', 'delete');
        delete_button.get('span.ml1').should('contain', 'Delete');
        delete_button.contains('Delete').should('be.visible');
    });
});