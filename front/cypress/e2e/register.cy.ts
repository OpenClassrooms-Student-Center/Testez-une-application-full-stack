describe('submit', ()=>{
    it('Should create account with success', ()=>{
        cy.visit('/register');

        cy.intercept('POST', '/api/auth/register',{
                body:{
                    firstName: "Guerda",
                    lastName: "Taro",
                    email: "guerda@taro.com",
                    password: "test!123"
                }
        });

        cy.get('input[formControlName="firstName"]').type("Guerda");
        cy.get('input[formControlName="lastName"]').type('Taro');
        cy.get('input[formControlName="email"]').type("guerda@taro.com");
        cy.get('input[formControlName="password"').type("test!123");
        cy.get('button[type="submit"]').click();

        cy.url().should('include', '/login');
    })
})