
# Yoga App
This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.
## Start the project
### Git clone:
> git clone https://github.com/alexia-pratensi/Tests-app-fullstack.git

### Go inside folder:
> cd yoga

### Install dependencies:

Frontend
> cd front

> npm install


Backend
> cd back

> mvn clean install


### Launch Back-end:
> mvn spring-boot:run

### Launch Front-end:
> npm run start

## Ressources
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
  
## For Testing
### Launching Frontend tests
#### E2E
Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unit & integration tests

Launching tests:

> npm run test

Generate coverage report :

> npm jest:coverage

for following change:

> npm run test:watch
------
### Launching backend tests
#### Unit & integration tests

For launch and generate the jacocco code coverage

> mvn clean test

Report is available here:

> file:///[your_path]/Tests-app-fullstack/back/target/site/jacoco/index.html

## Author

Alexia Pratensi

