/// <reference types="cypress" />

describe('The Home Page', () => {
  it('create and use a new registered visitor', () => {

  // Vérifie la home page
  cy.visit('/') 
  cy.contains('Yoga app')
  cy.get('span.link[routerlink="login"]').should('be.visible').and('contain.text', 'Login')
  cy.get('span.link[routerlink="register"]').should('be.visible').and('contain.text', 'Register')

  // Vérifie la page register
  cy.get('span.link[routerlink="register"]').click();
  cy.url().should('include', '/register')
  cy.contains('Yoga app')
  cy.get('span.link[routerlink="login"]').should('be.visible').and('contain.text', 'Login')
  cy.get('span.link[routerlink="register"]').should('be.visible').and('contain.text', 'Register')
  cy.get('input[formcontrolname="firstName"]')
      .should('exist')
      .should('be.visible')
  cy.get('input[formcontrolname="lastName"]')
      .should('exist')
      .should('be.visible')
  cy.get('input[formcontrolname="email"]')
      .should('exist')
      .should('be.visible')
  cy.get('input[formcontrolname="password"]')
      .should('exist')
      .should('be.visible')
  cy.get('button[type=submit]')
      .should('exist')

  // Vérifie la page de création de compte
  cy.get('input[formcontrolname="firstName"]').type('Test')
  cy.get('input[formcontrolname="lastName"]').type('Test')
  cy.get('input[formcontrolname="email"]').type('email@test.com')
  cy.get('input[formcontrolname="password"]').type('test!1234')
  cy.get('button[type=submit]').click()

  // Vérifie la page de login
  cy.url().should('include', '/login')
  cy.get('input[formcontrolname="email"]').type('email@test.com')
  cy.get('input[formcontrolname="password"]').should('have.attr', 'type', 'password').type('test!1234')
  cy.get('button[aria-label="Hide password"]').click()
  cy.get('input[formcontrolname="password"]').should('have.attr', 'type', 'text')
  cy.get('button[aria-label="Hide password"]').click()
  cy.get('input[formcontrolname="password"]').should('have.attr', 'type', 'password')
  cy.get('input[formcontrolname="password"]').clear
  cy.get('input[formcontrolname="password"]').type('test!1234');

  // Vérifie le logout
  cy.get('span.link').contains('Logout').click();
  cy.url().should('include', '/'); // adapte selon ta route de login
  
  
  // Vérifie une erreur sur la page de register
  cy.get('span.link[routerlink="register"]').click();
  cy.get('input[formcontrolname="firstName"]').type('Test')
  cy.get('input[formcontrolname="lastName"]').type('Test')
  cy.get('input[formcontrolname="email"]').type('email@test.com')
  cy.get('input[formcontrolname="password"]').type('test!1234')
  cy.get('button[type=submit]').click()
  cy.get('span.error').should('be.visible').and('contain.text', 'An error occurred');
  cy.get('span.link[routerlink="login"]').click();

  // puis relog avec erreur 
  cy.url().should('include', '/login')
  cy.get('input[formcontrolname="email"]').type('email@fakeTest.com')
  cy.get('input[formcontrolname="password"]').should('have.attr', 'type', 'password').type('test!1234')
  cy.get('button[type=submit]').click()
  cy.get('p.error').should('contain.text', 'An error occurred')

  // relog avec les bons identifiants
  cy.get('input[formcontrolname="email"]').clear()
  cy.get('input[formcontrolname="email"]').type('email@test.com')
  cy.get('input[formcontrolname="password"]').clear()
  cy.get('input[formcontrolname="password"]').type('test!1234')
  cy.get('button[type=submit]').click()

  //tente a une page interdite
  cy.url().should('include', '/sessions') 
  cy.visit('/sessions/create');
  cy.url().should('include', '/login'); 
  

  

  // puis relog 
  cy.get('input[formcontrolname="email"]').type('email@test.com')
  cy.get('input[formcontrolname="password"]').should('have.attr', 'type', 'password').type('test!1234')
  cy.get('button[type=submit]').click()
  


  // vérifie la page sessions
  
  cy.url().should('include', '/sessions')
  cy.contains('Yoga app')
  cy.contains('Rentals available')
  cy.get('span.link[routerlink="sessions"]').should('be.visible').and('contain.text', 'Sessions')
  cy.get('span.link[routerlink="me"]').should('be.visible').and('contain.text', 'Account')
  cy.get('span.link').should('be.visible').and('contain.text', 'Logout')

  // Vérifie la session déjà créée
  cy.get('mat-card-title.mat-card-title').should('contain.text','Test Main Session').should('exist');
  cy.get('mat-card-subtitle.mat-card-subtitle').should('contain.text','Session on May 12, 2025').should('exist');
  cy.get('img.picture[alt="Yoga session"][src="assets/sessions.png"]').should('be.visible')
  cy.get('button[mat-raised-button][color="primary"]').contains('Detail').should('be.visible');
  cy.get('mat-card-content.mat-card-content').find('p').should('contain.text', 'Test Main Session');
  cy.contains('mat-card', 'Test Main Session').within(() => {
  cy.contains('button', 'Detail').click(); // click sur le lien pour detail de la session
  })

  // Vérifie la page de détail de la session
  cy.url().should('include', '/detail')
  cy.get('H1').should('contain.text','Test Main Session');
  cy.contains('Margot DELAHAYE').should('be.visible');
  cy.get('button[mat-icon-button]').find('mat-icon').should('contain.text', 'arrow_back');
  cy.get('img.picture[alt="Yoga session"][src="assets/sessions.png"]').should('be.visible')
  cy.get('button[mat-raised-button][color="primary"]').contains('Participate').should('be.visible');
  cy.get('button[mat-raised-button][color="primary"]').find('mat-icon').should('contain.text', 'person_add');
  cy.get('mat-icon').contains('group').should('exist');
  cy.get('span.ml1').should('contains.text','attendees');
  cy.get('mat-icon').contains('arrow_back').should('be.visible').click()   // test du bouton de retour
  cy.url().should('include', '/sessions')
  cy.contains('mat-card', 'Test Main Session').within(() => {
  cy.contains('button', 'Detail').click(); // click sur le lien pour detail de la session
  })
  cy.url().should('include', '/detail')
  cy.get('button[mat-raised-button][color="primary"]').contains('Participate').click() // participe à la session

  // vérifie l'ajout du participant
  var attendees;
 /* cy.get('span.ml1').contains('attendees').invoke('text').then(text => {
  const match = text.match(/(\d+)\s+attendees/);
  expect(match, 'attendees text format').to.not.be.null;
  if (match) {
    attendees = parseInt(match[1], 10);
    expect(attendees).to.be.greaterThan(0);
  }
})*/
  cy.get('button[mat-raised-button][color="warn"]').contains('Do not participate').should('be.visible');
  cy.get('button[mat-raised-button][color="warn"]').find('mat-icon').should('contain.text', 'person_remove');


  // vérification de la desinscription
  cy.get('button[mat-raised-button][color="warn"]').contains('Do not participate').click() 
  cy.url().should('include', '/detail')
  cy.get('button[mat-raised-button][color="primary"]').contains('Participate').should('be.visible');
  cy.get('button[mat-raised-button][color="primary"]').find('mat-icon').should('contain.text', 'person_add');
  
  // Vérifie la décrementation du nombre d'attendees
 /* cy.get('span.ml1').contains('attendees').invoke('text').then(text => {
  const match = text.match(/(\d+)\s+attendees/);
  expect(match, 'attendees text format').to.not.be.null;
  if (match) {
    const attendeesDesc = parseInt(match[1], 10);
    expect(attendeesDesc).to.be.equals(attendees -1);
  }
})*/


  // Vérifie la Maj de la date de l'update
  const todayDate = new Date()
  const today = todayDate.toISOString().split('T')[0];
  const options = { year: 'numeric', month: 'long', day: 'numeric' } as const;
  const formattedDate = todayDate.toLocaleDateString('en-US', options);

  cy.get('i').contains('Last update:').parent().should('contain.text', 'May 24, 2025');

// Vérifie la page account
    cy.get('span.link[routerlink="me"]').click()
    cy.url().should('include', '/me')
    cy.contains('Yoga app')
    cy.get('span.link[routerlink="sessions"]').should('be.visible').and('contain.text', 'Sessions')
    cy.get('span.link[routerlink="me"]').should('be.visible').and('contain.text', 'Account')
    cy.get('span.link').should('be.visible').and('contain.text', 'Logout')
    cy.contains('User information')
    cy.contains('Name: Test TEST')
    cy.contains('Email: email@test.com')
    cy.get('i').contains('Create at:').parent().should('contain.text', formattedDate);
    cy.get('i').contains('Last update:').parent().should('contain.text', formattedDate);
    cy.get('mat-icon').contains('arrow_back').should('be.visible').click()
    cy.url().should('include', '/sessions')
    cy.get('span.link[routerlink="me"]').click()

    // Vérifie la supression du compte
    cy.get('button[mat-raised-button][color="warn"]').contains('Detail').should('be.visible');
    cy.get('button[mat-raised-button][color="warn"]').contains('Detail').click();

    cy.url().should('include', '/')
    // Vérifie la page de login
    cy.get('span.link[routerlink="login"]').click();
    cy.url().should('include', '/login')
    cy.get('input[formcontrolname="email"]').type('email@test.com')
    cy.get('input[formcontrolname="password"]').type('test!1234')
    cy.get('button[type=submit]').click()
    cy.get('p.error').should('contain.text', 'An error occurred')
  })

  it('Should verify 404 and guard auth', () => {
    cy.visit('/login/fakeCompnent') // Visite une page fictive pour éviter les erreurs de route
    cy.url().should('include', '404') // Vérifie que la page n'existe pas
    cy.contains('Yoga app')
    cy.get('span.link[routerlink="login"]').should('be.visible').and('contain.text', 'Login')
    cy.get('span.link[routerlink="register"]').should('be.visible').and('contain.text', 'Register')
    cy.visit('/sessions/detail/1') // Essaye d'accéder à une page protégée sans être connecté
    cy.url().should('include', '/login') // Vérifie que l'utilisateur est redirigé vers la page de login
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com')
    cy.get('input[formcontrolname="password"]').type('test!1234')
    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions') // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.visit('/register') // Essaye d'acceder à une page qui ne devrait plus etre accessible
  })

    it('Create and verifiy Admin routes', () => {
    // Vérifie la page de login
    cy.visit('/login')
    cy.url().should('include', '/login')
    cy.get('input[formcontrolname="email"]').type('yoga@studio.com')
    cy.get('input[formcontrolname="password"]').type('test!1234')
    cy.get('button[type=submit]').click()

    //  Vérifie la page sessions
    cy.url().should('include', '/sessions')
    cy.contains('Yoga app')
    cy.contains('Rentals available')
    cy.get('span.link[routerlink="sessions"]').should('be.visible').and('contain.text', 'Sessions')
    cy.get('span.link[routerlink="me"]').should('be.visible').and('contain.text', 'Account')
    cy.get('span.link').should('be.visible').and('contain.text', 'Logout')
    cy.get('button[mat-raised-button]')
        .should('be.visible')
        .and('contain.text', 'Create')

    // Vérifie la page account
    cy.get('span.link[routerlink="me"]').click()
    cy.url().should('include', '/me')
    cy.contains('Yoga app')
    cy.get('span.link[routerlink="sessions"]').should('be.visible').and('contain.text', 'Sessions')
    cy.get('span.link[routerlink="me"]').should('be.visible').and('contain.text', 'Account')
    cy.get('span.link').should('be.visible').and('contain.text', 'Logout')
    cy.contains('User information')
    cy.contains('Name: Admin ADMIN')
    cy.contains('Email: yoga@studio.com')
    cy.contains('You are admin')
    cy.contains('Create at:')
    cy.get('mat-icon').contains('arrow_back').should('be.visible').click()
    cy.url().should('include', '/sessions')
    cy.contains('button','Create').click()// prend la direction de la création de session
    // Vérifie la page sessions
    



    // Vérifie la page de création de session
    cy.url().should('include', '/create')
    cy.contains('Yoga app')
    cy.get('span.link[routerlink="sessions"]').should('be.visible').and('contain.text', 'Sessions')
    cy.get('span.link[routerlink="me"]').should('be.visible').and('contain.text', 'Account')
    cy.get('span.link').should('be.visible').and('contain.text', 'Logout')
    
    cy.get('input[formcontrolname="name"]')
        .should('exist')
        .should('be.visible')
    cy.get('input[formcontrolname="date"]')
        .should('exist')
        .should('be.visible')
    cy.get('mat-select[formcontrolname="teacher_id"]')
        .should('exist')
        .should('be.visible')
    cy.get('textarea[formcontrolname="description"]')
        .should('exist')
        .should('be.visible')
    cy.get('mat-icon').contains('arrow_back').should('be.visible').click()   // test du bouton de retour
    cy.url().should('include', '/sessions')
    cy.contains('button','Create').click()
    cy.url().should('include', '/sessions/create')

    // Vérifie la création de session
    cy.get('input[formcontrolname="name"]').type('Test Session')

    const todayDate = new Date()
    const today = todayDate.toISOString().split('T')[0];
    const options = { year: 'numeric', month: 'long', day: 'numeric' } as const;
    const formattedDate = todayDate.toLocaleDateString('en-US', options);

    cy.get('input[matinput][type="date"][formcontrolname="date"]').type(today)
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('mat-select[formcontrolname="teacher_id"] .mat-select-value-text')
        .should('contain', 'Margot DELAHAYE');
    cy.get('textarea[formcontrolname="description"]').type('Test description')
    cy.get('button[type=submit]').click() // soumet le formulaire et retourne à la page session

    // Vérifie la session créer dans la page session
    cy.url().should('include', '/sessions') 
    cy.get('mat-card-subtitle').contains(formattedDate).should('exist')
    cy.get('img.picture[alt="Yoga session"][src="assets/sessions.png"]').should('be.visible')
    cy.contains('Test description').should('exist')
    cy.get('button[mat-raised-button]').contains('Detail').should('exist')
    cy.get('button[mat-raised-button]').contains('Edit').should('exist')
    cy.contains('mat-card', 'Test Session').last().within(() => {
    cy.contains('button', 'Edit').click();
    });
    
    // Vérifie la page de mise à jour de session
    cy.url().should('include', '/update/');
    cy.get('input[formcontrolname="name"]')
        .should('have.value', 'Test Session')
    cy.get('input[formcontrolname="date"]')
        .should('have.value', today)
    cy.get('mat-select[formcontrolname="teacher_id"]')
        .contains('Margot DELAHAYE')
    cy.get('textarea[formcontrolname="description"]')
        .should('have.value', 'Test description')
    cy.get('button[type=submit]').should('exist')
        .should('be.visible')
    cy.get('mat-icon').contains('arrow_back').should('be.visible').click()   // test du bouton de retour
    cy.url().should('include', '/sessions')
    cy.contains('mat-card', 'Test Session').last().within(() => {
    cy.contains('button', 'Edit').click();
    });

    // Vérifie l'update d'une session
    cy.get('input[formcontrolname="name"]').clear()
    cy.get('input[matinput][type="date"][formcontrolname="date"]').clear()
    cy.get('textarea[formcontrolname="description"]').clear()



    cy.get('input[formcontrolname="name"]').type('Test Session Updated')

    const todayDateUpdated = new Date();
    todayDateUpdated.setFullYear(todayDate.getFullYear() + 1); 
    const todayUpdated = todayDateUpdated.toISOString().split('T')[0];
    const formattedDateUpdated = todayDateUpdated.toLocaleDateString('en-US', options);

    cy.get('input[matinput][type="date"][formcontrolname="date"]').type(todayUpdated)
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('mat-select[formcontrolname="teacher_id"] .mat-select-value-text')
        .should('contain', 'Hélène THIERCELIN');
    cy.get('textarea[formcontrolname="description"]').type('Test description Updated')
    cy.get('button[type=submit]').click() // soumet le formulaire et retourne à la page session

    // Vérifie la session mise à jour dans la page session
    cy.url().should('include', '/sessions')    
    cy.get('mat-card-title.mat-card-title').should('contain.text','Test Session Updated');
    cy.get('mat-card-subtitle.mat-card-subtitle').contains(formattedDateUpdated).should('exist');
    cy.get('img.picture[alt="Yoga session"][src="assets/sessions.png"]').should('be.visible')
    cy.get('button[mat-raised-button]').contains('Detail').should('exist')
    cy.get('mat-card-content.mat-card-content').find('p').should('contain.text', 'Test description Updated');
    cy.contains('mat-card', 'Test Session Updated').within(() => {
    cy.contains('button', 'Detail').click(); // click sur le lien pour detail de la session
    });

    // Vérifie la page de détail de session
    cy.url().should('include', '/detail/');
    cy.get('button[mat-icon-button]').find('mat-icon').should('contain.text', 'arrow_back');
    cy.get('button[mat-raised-button][color="warn"]').contains('Delete').should('be.visible');
    cy.get('button[mat-raised-button][color="warn"]').find('mat-icon').should('contain.text', 'delete');
    cy.get('img.picture[alt="Yoga session"][src="assets/sessions.png"]').should('be.visible')
    cy.get('mat-icon').contains('group').should('exist');
    cy.get('span.ml1').contains('0 attendees').should('exist');
    cy.get('mat-icon').contains('arrow_back').should('be.visible').click()   // test du bouton de retour
    cy.url().should('include', '/sessions')
    cy.contains('mat-card', 'Test Session Updated').within(() => {
    cy.contains('button','Detail').click()
    });
    cy.get('mat-icon').contains('calendar_month').should('exist');
    cy.get('span.ml1').contains(formattedDateUpdated).should('exist');
    cy.contains('Description:').should('exist');
    cy.contains('Test description Updated').should('exist');
    cy.get('i').contains('Create at:').parent().should('contain.text', formattedDate);
    cy.get('i').contains('Last update:').parent().should('contain.text', formattedDate);
    // Clique sur le bouton "Delete"
    cy.get('button[mat-raised-button][color="warn"]').contains('Delete').click();

    // Vérifie le retour sur la liste des sessions
    cy.url().should('include', '/sessions');

    // Vérifie que "Test Session" n'existe plus dans la liste
    cy.contains('Test Session Updated').should('not.exist');
})
})
