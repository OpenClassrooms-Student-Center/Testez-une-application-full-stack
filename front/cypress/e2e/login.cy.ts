/// <reference types="cypress" />

describe('Login Page', () => {
  it('successfully loads', () => {
    cy.visit('/login')
    cy.contains('Yoga app')
    cy.contains('Login')
    cy.contains('Register')
    cy.get('input')
    .first()
    .should('have.attr', 'ng-reflect-placeholder', 'Email');
    cy.get('input')
    .eq(1)
    .should('have.attr', 'ng-reflect-placeholder', 'Password');
   // cy.get('input[matInput][placeholder="Password"]').should('be.visible')
    cy.get('button[type=submit]')   
  })

  it('should navigate to the login page', () => {
    cy.visit('/');

    cy.contains('Login').click();

    cy.url().should('include', '/login');
  });
  
  it('should navigate to the register page', () => {
    cy.visit('/');

    cy.contains('Register').click();

    cy.url().should('include', '/register');
  });

  it('should login with valid credentials', () => {
    cy.visit('/login')  
    cy.get('input[ng-reflect-placeholder="Email"]').type('yoga@studio.com')
    cy.get('input[ng-reflect-placeholder="Password"]').type('test!1234')
    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    })
    


  it('login should add BEARER', () => {
    cy.request('POST', 'http://localhost:4200/api/auth/login', { 
    email: 'yoga@studio.com',
    password: 'test!1234'
    }).then((response) => {
      const token = response.body.token;
    // Place le token dans le localStorage AVANT de visiter la page
    cy.window().then((win) => {
      win.localStorage.setItem('token', token); // adapte la clé selon ton app
    })
    // Vérifie la présence d'un élément spécifique à la page sessions
    cy.contains('Rentals available'); // adapte selon ton template
    })
    
  });

}); 


    
  