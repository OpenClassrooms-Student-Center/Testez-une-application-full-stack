# Numdev Project

![NumDev Logo](./ressources/images/banner-numdev.png)

## Overview

The goal of this project is to complete the testing phase for a yoga studio application called Savasana. Becoming responsible for finishing the testing process, encompassing Front-End, Back-End, and end-to-end functionalities. Thorough testing is essential to ensure a minimum code coverage of 80%, with at least 30% originating from integration tests. Once accomplished, the project requires submission of coverage reports and code on GitHub, alongside a README outlining how to launch the application. Preparation for a presentation to the manager is also expected.

## Table of contents

- [Numdev Project](#numdev-project)
  - [Overview](#overview)
  - [Table of contents](#table-of-contents)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Installation procedure](#installation-procedure)
    - [Front-End](#front-end)
    - [Back-End](#back-end)
  - [Code coverage reports](#code-coverage-reports)
    - [Front-End](#front-end-1)
    - [Back-End](#back-end-1)
  - [Miscellaneous](#miscellaneous)

## Prerequisites

Before you begin, ensure that the following software is installed on your system:

- **Java Development Kit (JDK):** Follow the instructions below to install the JDK

- **Apache Maven:** Install [Maven](https://maven.apache.org/) for building and managing the project's dependencies.

- **MySQL:** Install and set up MySQL as the database for the NumDev Back-End. You can follow the installation steps [here](https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql/7152681-installez-le-sgbd-mysql).

- **Node.js:** Install [Node.js LTS](https://nodejs.org/en) to install the Front-End dependencies

## Configuration

1. **Java Development Kit (JDK):** Install Java version 8 Zulu (JavaSE-1.8) using [SDKMAN](https://sdkman.io/), a tool for managing software development kits. SDKMAN simplifies the installation process and version management.

- **Install SDKMAN:**

If you do not have 7zip installed, you can install from [their website](https://www.7-zip.org/)

Then, in a GitBash terminal, run as an administrator these commands:

  ```shell
# To install 7zip
ln -s /c/Program\ Files/7-Zip/7z.exe /c/Program\ Files/Git/mingw64/bin/zip.exe

# To install SDK Man
export SDKMAN_DIR="/c/sdkman" && curl -s "https://get.sdkman.io" | bash
```

To install Java version 8, run the following command:

```shell
sdk install java 8.0.302-zulu
```

Ensure that the Java environment variable is correctly configured on your system. This variable is essential for Java applications to run. You can set it up by following the steps specific to your operating system.

- Windows:

1. Open the System Properties.
2. Click on the `Advanced` tab.
3. Click the `Environment Variables` button.
4. Under `System variables`, create a new variable named `JAVA_HOME`.
5. Add the path to your JDK's binary directory (e.g., `C:\sdkman\candidates\java\[JAVA VERSION NAME]\bin`)
6. Click `OK` to save your changes

Restart your computer and then run the command to verify that you have the correct Java version installed:

```shell
java -version
```

2. **MySQL Database:**

Follow these steps to configure MySQL Workbench for your Java application:

Open MySQL Workbench.

Connect to your MySQL Server instance.

Create a new database for your application and add all the tables to your database:

```sql
-- Create the database
DROP DATABASE IF EXISTS `p5-numdev`;

CREATE DATABASE `p5-numdev`;

-- Switch to the new database
USE `p5-numdev`;

-- Create the tables
CREATE TABLE `TEACHERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50),
  `description` VARCHAR(2000),
  `date` TIMESTAMP,
  `teacher_id` int,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
  `user_id` INT, 
  `session_id` INT
);

-- Add foreign keys to the tables
ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

-- Insert values into the tables
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
('User', 'User', false, 'user@user.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');
```

## Installation procedure

**Cloning the project:**
To clone this repository from GitHub, run the following command: `git clone https://github.com/LePhenix47/Lahouiti_Younes_P5_30122023 .`

### Front-End

1. Install the dependencies:

To start the Angular Front-End project, follow these steps:

- Navigate to the Front-End directory in your terminal:

```shell
cd numdev-frontend
```

- Install project dependencies using npm:

```shell
npm install
```

2. Starting the server

- After the dependencies are installed, you can start the development server by running:

```shell
npm start
```

This command will compile the Angular application and start a development server.
You can then access the application in your browser at `http://localhost:4200`.

### Back-End

1. Configure the application in the `application.properties` file

Once you have cloned the repository, you'll need to add the `application.properties` file on the `src/main/resources/` folder containing these properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/p5-numdev?allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Az&rty1234

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=openclassrooms
oc.app.jwtExpirationMs=86400000

# TomCat server
server.port=8080
```

2. Install the project dependencies using the following command: `mvn clean install`

3. Run the application using your IDE or by running `mvn spring-boot:run` in the project directory.

4. To generate the code coverage of the back-end, run the following command: `mvn clean test`

## Code coverage reports

### Front-End

Get Test Code Coverage for Front-End (Angular):

- To run test and get code coverage for the Angular Front-End, you can use the Jest command:

```shell
jest -t --coverage
```

Upon completion, the terminal displays the tests outcome (pass/fail), accompanied by a comprehensive table showing the code coverage %

### Back-End

Get Test Code Coverage for Back-End (Spring Boot):

- Run the following command in the terminal to execute tests and generate a coverage report using JaCoCo for the Spring Boot backend:

```shell
mvn clean site
```

Following successful execution, locate and open in your browser the `index.html` file for the coverage report under the `target/site/jacoco` directory in the project.

## Miscellaneous

<details>
  <summary>
📚 API documentation
  </summary>
  <table>
  <thead>
    <tr>
       <th>Endpoint</th>
       <th>Method</th>
       <th>Description</th>
    </tr>
  </thead>
  <tbody>
  <tr>
        <td>/api/auth/login</td>
        <td>POST</td>
        <td>User authentication</td>
    </tr>
    <tr>
        <td>/api/auth/register</td>
        <td>POST</td>
        <td>User registration</td>
    </tr>
    <tr>
        <td>/api/session</td>
        <td>GET</td>
        <td>Retrieve all sessions</td>
    </tr>
    <tr>
        <td>/api/session</td>
        <td>POST</td>
        <td>Create a new session</td>
    </tr>
    <tr>
        <td>/api/session/{id}</td>
        <td>DELETE</td>
        <td>Delete a session by ID</td>
    </tr>
    <tr>
        <td>/api/session/{id}</td>
        <td>GET</td>
        <td>Retrieve a session by ID</td>
    </tr>
    <tr>
        <td>/api/session/{id}</td>
        <td>PUT</td>
        <td>Update a session by ID</td>
    </tr>
    <tr>
        <td>/api/session/{id}/participate/{userId}</td>
        <td>DELETE</td>
        <td>Remove user participation</td>
    </tr>
    <tr>
        <td>/api/session/{id}/participate/{userId}</td>
        <td>POST</td>
        <td>Add user participation</td>
    </tr>
    <tr>
        <td>/api/teacher</td>
        <td>GET</td>
        <td>Retrieve all teachers</td>
    </tr>
    <tr>
        <td>/api/teacher/{id}</td>
        <td>GET</td>
        <td>Retrieve a teacher by ID</td>
    </tr>
    <tr>
        <td>/api/user/{id}</td>
        <td>DELETE</td>
        <td>Delete a user by ID</td>
    </tr>
    <tr>
        <td>/api/user/{id}</td>
        <td>GET</td>
        <td>Retrieve a user by ID</td>
    </tr>
  </tbody>
</table>
</details>

<details>
  <summary>🔗 Link to the original Repository</summary>
  <a href="https://github.com/OpenClassrooms-Student-Center/Testez-une-application-full-stack">
    Link to the original code
  </a>
</details>
