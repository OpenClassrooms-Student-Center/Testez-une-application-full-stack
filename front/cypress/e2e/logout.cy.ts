describe('Logout', ()=>{
    it('should logout of session', ()=>{
        cy.visit('/login');

        cy.intercept('POST','/api/auth/login', {});

        cy.intercept('GET','/api/sessions', [{}]);

        cy.get('input[formControlName="email"]').type('toto3@email.com');
        cy.get('input[formControlName="password"]').type('test123');
        cy.get('button[type="submit"]').click();

        cy.contains('Logout').click();

        cy.url().should('include', '/');
    });
});