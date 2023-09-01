# Yoga Studio Application
    This is a global documentation of Yoga application

## Start the project
Run the application:

Clone project from here: 

> https://github.com/Marvin-Silva/Testez-une-application-full-stack/tree/test

## Frontend
After go to frontend folder 

>cd front

Install dependencies:

>npm install

And run the frontend application

>npm run start


## Backend

Go to backend folder

>cd back

And Run the backend application
> mvn springboot:run / IDE

### Run tests:

Jest 
> npm run test / npm run test:watch (Follow changes)

Cypress
>npm run e2e
    
### Generate test reports

Jest: 
>npm run test --coverage

Cypress:
>npm run e2e:coverage

>npm run cypress:run (Run all cypress test)

Reports are generated here : 
>coverage>jest>lcov-report>index.html

>coverage>lcov-report>index.html

## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

