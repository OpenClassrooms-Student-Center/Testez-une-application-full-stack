# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

```` bash
git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing
````
Go inside folder:

```bash
cd yoga
````

Install dependencies:

```bash
npm install
````
Launch Front-end:

```bash 
npm run start;
````

## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

``
ressources/postman/yoga.postman_collection.json 
``

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E

Launching e2e test:

````bash
npm run e2e
````

Generate coverage report (you should launch e2e test before):

````bash
npm run e2e:coverage
````


Report is available here:

````bash
front/coverage/lcov-report/index.html
````

#### Unitary test

Launching test:

````bash
npm run test
````

for following change:

````bash
npm run test:watch
````
