describe('Login', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    //Verify login fields
    cy.get('mat-card-title').contains('Login').should('be.visible');
    cy.get('input[formControlName="email"]').should('be.visible');
    cy.get('input[formControlName="password"').should('be.visible');
    cy.get('button[type="submit"]').should('be.disabled');

    cy.intercept('POST', '/api/auth/login', {})

    cy.intercept('GET', '/api/sessions', [{}]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");

    //Verify hide password input

    let visibleToggle = cy.get('button[matSuffix]');

    visibleToggle.should('contain', 'visibility_off');  

    visibleToggle.click();
    
    visibleToggle.should('contain', 'visibility');

    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/sessions')

  }),
  it('Error bad credentials', ()=>{

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: "An error occurred",
      status: 500
    })


    cy.get('input[formControlName="email"]').type('fake@email.com');
    cy.get('input[formControlName="password"]').type('fakePassword');

    //Verify hide password input

    let visibleToggle = cy.get('button[matSuffix]');

    visibleToggle.should('contain', 'visibility_off');  

    visibleToggle.click();
    
    visibleToggle.should('contain', 'visibility');

    cy.get('button[type="submit"]').click();

    const onError = cy.get('.error');
    onError.contains('An error occurred').should('be.visible');
  }),

  it('should disable submit button for empty required fields ', ()=>{

    cy.visit('/login');

    cy.intercept('GET', '/api/auth/login', {});

    cy.get('input[formControlName="email"]').type('yoga@studio.com');
    
    cy.get('button[type="submit"]').should('be.disabled');
  });
});