describe('submit', ()=>{
    it('Update session for admin', ()=>{
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
        cy.url().should('include', '/session');

          //session object
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

        //Detail

        cy.intercept('GET', '/session/detail/1');
        cy.intercept('GET', '/api/session/1',session);
        cy.intercept('GET', '/api/teacher/1', teacher);
        cy.contains('Detail').click();

        //Button back
        const arrowBackButton = cy.get('button.mat-icon-button').should('be.visible');
        arrowBackButton.find('mat-icon').should('be.visible');

        cy.contains(session.name ? 'Test' : 'test').should('be.visible');
        cy.contains(teacher.firstName + teacher.lastName ? 'Margot DELAHAYE' : 'DELAHAYE Margot').should('be.visible');
        cy.contains(session.users.length ? '0 attendees' : 0).should('be.visible');
        cy.contains(session.date ? 'July 27, 2023' : '2023-07-27T00:00:00').should('be.visible');
        cy.contains(session.description ? 'test description' : 'Test description').should('be.visible');
        cy.contains(session.createdAt ? 'July 28, 2023' : '2023-07-28T00:00:00').should('be.visible');
        cy.contains(session.updatedAt ? 'July 29, 2023' : '2023-07-29T00:00:00').should('be.visible');
    });

    it('Update session for non admin', ()=>{
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 2,
                username: 'toto3@toto.com',
                firstName: 'toto',
                lastName: 'TOTO',
                admin: false
            }
        });

        cy.intercept('GET', '/api/session',{
            body:  [{
                id: 1,
                name: "test",
                date: "2023-07-27T00:00:00",
                teacher_id: 1,
                description: "test description",
                users: [],
                createdAt: "2023-07-28T00:00:00",
                updatedAt: "2023-07-29T00:00:00"
            }, 
            { 
                createdAt: "2023-08-21T10:49:00",
                date: "2023-08-21T08:49:00.000+00:00",
                description: "Description for Frida Kalo",
                id: 2,
                name: "Frida",
                teacher_id: 2,
                updatedAt: "2023-08-25T15:22:27",
                users: []
            }]
        });

        cy.get('input[formControlName="email"]').type('toto3@toto.com');
        cy.get('input[formControlName="password"]').type('test!1234');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', 'sessions');

          //session object
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

        //Detail

        cy.intercept('GET', '/sessions/detail/1').as('getSessionDetail');
        cy.intercept('GET', '/api/session/1',{
            body:{
                id: 1,
                name: "test",
                date: "2023-07-27T00:00:00",
                teacher_id: 1,
                description: "test description",
                users: [],
                createdAt: "2023-07-28T00:00:00",
                updatedAt: "2023-07-29T00:00:00"
            }
        }).as('getSession');
        cy.intercept('GET', '/api/teacher/1', teacher).as('getTeacher');
        cy.contains('Detail').click();

        cy.url().should('include', 'detail/1')

        //Button back
        const arrowBackButton = cy.get('button.mat-icon-button').should('be.visible');
        arrowBackButton.find('mat-icon').should('be.visible');

        cy.contains(session.name ? 'Test' : 'test').should('be.visible');
        
        
        //Participate
        cy.intercept('POST', '/api/session/1/participate/2',{}).as('Participate');
            
            cy.intercept('GET', '/api/session/1', {
                body: {
                    id: 1,
                    name: "test",
                    date: "2023-07-27T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "test description",
                    users: [2],
                    createdAt: "2023-07-28T00:00:00",
                    updatedAt: "2023-08-25T16:48:47"
                }
            });
            cy.intercept('GET', '/api/teacher/1', teacher);

            const isParticipate = cy.get('button.mat-raised-button').should('be.visible');
            isParticipate.should('contain', 'person_add');
            isParticipate.should('contain','Participate').click();

            cy.contains('1 attendees').should('be.visible');

            //Do not participate

            cy.intercept('DELETE', '/api/session/1/participate/2', {}).as('Do not participate')
            cy.intercept('GET', '/api/session/1', { 
                body: {
                id: 1,
                name: "test",
                date: "2023-07-27T00:00:00.000+00:00",
                teacher_id: 1,
                description: "test description",
                users: [],
                createdAt: "2023-07-28T00:00:00",
                updatedAt: "2023-08-25T16:48:47"
            }
        });
        cy.intercept('GET', '/api/teacher/1', teacher);

        const DoNotParticipate = cy.get('button.mat-raised-button').should('be.visible');
        DoNotParticipate.should('contain', 'person_remove');
        DoNotParticipate.contains('Do not participate').click();
    
        isParticipate.get('.ml1').should('contain', 'Participate');

        cy.contains('0 attendees').should('be.visible');

          
    });
});