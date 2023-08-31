describe('list', ()=>{
    it('List of sessions for admin', ()=>{
        cy.visit('/login');
        cy.intercept('POST','/api/auth/login', {
            body:{
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Admin',
                lastName: 'admin',
                admin: true
            }
        });
        
        cy.intercept('GET', '/api/session', {
        body:[{
            id: 1,
            name: "test",
            date: "2023-07-27T00:00:00",
            teacher_id: 1,
            description: "test description",
            users: [],
            createdAt: "2023-07-28T00:00:00",
            updatedAt: "2023-07-29T00:00:00"
        }]
    });

        cy.get('input[formControlName="email"]').type('yoga@studio.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', 'sessions');

        //Sessions informations for admins
        cy.contains('Rentals available').should('be.visible');
        cy.contains('Create').should('be.visible');
        cy.contains('test').should('be.visible');
        cy.contains('Session on July 27, 2023').should('be.visible');

        //image
        const image = cy.get('img.picture');
        image.should('have.attr', 'src', 'assets/sessions.png');
        image.should('have.attr', 'alt', 'Yoga session');
        image.should('be.visible');

        cy.contains('test description').should('be.visible');
        cy.contains('Detail').should('be.visible');
        cy.contains('Edit').should('be.visible');
    })
});