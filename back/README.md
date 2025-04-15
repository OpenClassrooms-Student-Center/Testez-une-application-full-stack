## Backend – Yoga Application Testing Guide

### Requirements
- OpenJDK 11
- MySQL
- Git

---

### Database Setup

1. Start MySQL server.
2. Connect to MySQL and run:
```sql
CREATE DATABASE test_yoga;
USE test_yoga;
SOURCE /path_to_file/ressources/sql/script.sql;
```

---

### Running Backend Tests

#### Option 1
Run tests using build lifecycle:
- clean
- test

#### Option 2
From terminal:
```bash
cd /path_to_file/back
mvn clean test
```

---

### Code Coverage Report

After running the tests, open the coverage report:
```
/path_to_file/back/target/site/jacoco/index.html
```

