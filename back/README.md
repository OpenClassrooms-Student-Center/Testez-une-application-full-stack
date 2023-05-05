# Yoga App !

## Description

This is a simple app to manage yoga classes.

## Requirements

- Java 11
- Maven 3.6.3
- Docker & Docker-Compose

## How to run

Run the docker-compose to start the database:
From the root folder of the project, go to ressources/docker and run:
> docker-compose up

Then run the script sql in the database to create and populate the tables. You will find it in ressources/sql from the root of the project.

Then go back to the back folder and run the app:
> mvn spring-boot:run

To stop the docker-compose:
> docker-compose stop

To stop and remove the docker-compose:
> docker-compose down

For launch and generate the jacoco code coverage:
> mvn clean test

GL
