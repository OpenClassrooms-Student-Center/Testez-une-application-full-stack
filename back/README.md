# Yoga App (backend)

Petit README pour compiler, lancer l'application, exécuter les tests et générer le rapport de couverture JaCoCo.

Prerequis

- Java 8+ (le projet utilise java.version 1.8 dans le `pom.xml`)
- Maven 3.6+

Lancer l'application

- Compiler + packager :

```bash
mvn clean package
```

- Lancer avec Spring Boot (développement) :

```bash
mvn spring-boot:run
```

- Ou lancer le jar produit :

```bash
java -jar target/yoga-app-0.0.1-SNAPSHOT.jar
```

Exécuter les tests

- Lancer tous les tests unitaires :

```bash
mvn test
```

- Lancer un seul test (classe ou méthode) :

```bash
# ex : une classe de test
mvn -Dtest=SessionControllerTest test
# ex : une méthode spécifique
mvn -Dtest=SessionControllerTest#findById_ok test
```

Générer le rapport de couverture (JaCoCo)

Le plugin JaCoCo est configuré dans le `pom.xml`. Deux façons courantes :

- Exécuter les tests puis générer le rapport HTML :

```bash
mvn test
mvn jacoco:report
```

- Ou lancer la phase `verify` qui exécute les tests et génère le rapport configuré :

```bash
mvn verify
```

Le rapport HTML est généré ici :

```
target/site/jacoco/index.html
```

Pour ouvrir le rapport sur macOS :

```bash
open target/site/jacoco/index.html
```

Remarques

- Le plugin JaCoCo dans ce projet instrumente le code pendant les tests. Si le dossier `target/site/jacoco` n'apparaît pas, vérifiez que les tests ont bien été exécutés et que la commande `mvn jacoco:report` ou `mvn verify` a été lancée.
- Pour exécuter des tests avec les filtres de sécurité activés/désactivés, consultez les annotations `@AutoConfigureMockMvc(addFilters = false)` ou les tests d'intégration (`@SpringBootTest`) dans le dossier `src/test`.
