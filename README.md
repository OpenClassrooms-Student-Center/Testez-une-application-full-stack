# Testez une application full-stack

## Installation du projet

### Installer la BDD
- Utiliser un éditeur comme **MySQL WorkBench**.
- Créer une connexion avec :
  - **Hostname** : `localhost`
  - **Port** : `3306`
  - **Username** : `root`
  - **Password** : à définir
- Importer ensuite la base à partir du script SQL :  
  📂 `ressources/sql/script.sql`

---

## Front

### Cloner le projet
Dans l’IDE de votre choix, exécutez :
```sh
git clone https://github.com/Yann-Rethore/Testez-une-application-full-stack.git
cd yoga/front
```

### Installer les dépendances
```sh
npm install
```



## Back-end

### Cloner et installer le projet
Dans le terminal de votre IDE
```sh
git clone https://github.com/Yann-Rethore/Testez-une-application-full-stack.git
cd Testez-une-application-full-stack/Yoga/back
mvn install
```
### Properties et Back End

Le fichier properties ne peut être exporté sur GITHUB avec des données sensible .
Voici un exemple avec la base de donnée et les autres informations indispensables :
```sh
server.port=8080
spring.profiles.active=test
spring.jpa.open-in-view=false

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=Votre_Token
oc.app.jwtExpirationMs=86400000

spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=Votre_Mot_De_Passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Le mot de passe de votre base de données et le token seront les votres.
Il existe des programmes simple en Java pour générer les tokens , en cas de soucis vous pouvez me contacter via openClassRoom

## Accès au projet

### Lancer le Front-end
A partir du terminal de votre IDE Front
```sh
npm run start
```

### Lancer la BDD
A Partir de votre gestionnaire de BDD lancer la base de données de Yoga

### Lancer le Back-end
A partir de votre IDE Java, lancer le projet.

### Accès au site
Vous pouvez maintenant accèder au site en local à partir de l'adresse : http://localhost:4200/
L'accès admin se fait via le formulaire de login : 
login: yoga@studio.com
password: test!1234

Vous pourrez y enregistrer d'autres utilisiateurs , créer ect.

## Couverture de tests

### Lancer les tests Front-end
Dans le terminal de votre IDE:
```sh
npx jest --coverage
```
📂 **Rapport de couverture** :  
Le rapport de couverture se trouve dans le dossier:
`Testez-une-application-full-stack/front/coverage/jest/lcov-report/index.html`

---

### Tests End-to-End (E2E)
#### Exécuter Cypress :
A partir de L'IDE de votre front :
```sh
npx cypress open
```
1. Ouvrir **E2E Testing**.
2. Choisir **Electron** et **Start Testing**.
3. Exécuter la spécification **testE2E**.
---
📂 **Rapport E2E** :

Le rapport de couverture se trouve dans le dossier :
`Testez-une-application-full-stack/front/coverage/lcov-report/index.html`

---

### Couverture Back-end

Modifier le fichier properties , en effet les tests emploient une autre base (H2) qui nécessite que des informations de l'application.properties du back soient commentées :
```sh
server.port=8080
spring.profiles.active=test
spring.jpa.open-in-view=false

#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=Votre_Token
oc.app.jwtExpirationMs=86400000

#spring.datasource.url=jdbc:mysql://localhost:3306/test
#spring.datasource.username=root
#spring.datasource.password=Votre_Mot_De_Passe
#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
Exécuter ensuite les tests avec **JaCoCo** à partir du Terminal de votre IDE Java :
```sh
mvn clean test jacoco:report
```

 Le rapport de couverture se trouve dans votre IDE sous :
`Testez-une-application-full-stack/back/target/site/jacoco/index.html`


