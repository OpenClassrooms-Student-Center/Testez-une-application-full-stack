describe('submit', ()=>{
    it('Should create account with success', ()=>{
        cy.visit('/register');

        cy.intercept('POST', '/api/auth/register',{});
        //Testing title and inputs presence
        cy.get('mat-card-title').should('be.visible');
        cy.contains('First name').should('be.visible');
        cy.contains('Last name').should('be.visible');
        cy.contains('Email').should('be.visible');
        cy.contains('Password').should('be.visible');
        cy.contains('Submit').should('be.visible');
        
        //Inserting fields values and validating the form
        cy.get('input[formControlName="firstName"]').type("Guerda");
        cy.get('input[formControlName="lastName"]').type('Taro');
        cy.get('input[formControlName="email"]').type("guerda@taro.com");
        cy.get('input[formControlName="password"').type("test!123");
        
        cy.get('button[type="submit"]').click();

        cy.url().should('include', '/login');
    });

    it('should show error for empty required field', ()=>{
        
        cy.visit('/register');
        cy.intercept('POST', '/api/auth/register', {
            body: 'An error occured',
            status: 500
        });

        cy.get('input[formControlName="firstName"]').type('Yoko');
        cy.get('input[formControlName="lastName"]').type('Cat');

        cy.get('input[formControlName="password"]').type('test!1234');        

        cy.get('button[type="submit"]').should('be.disabled');

    });
})