## Frontend – Yoga Application Testing Guide

### Requirements
- Node.js  
- Git  

### Setup and Run

1. Open terminal:
```bash
cd /path_to_file/front
npm install
npm run start
```

2. Open the application in your browser:
```
http://localhost:4200
```

---

### Jest Tests

To run frontend jest tests and generate coverage report:
```bash
npm run test:coverage
```

Coverage report:
```
/path_to_file/front/coverage/jest/lcov-report/index.html
```

---

### End-to-End (E2E) Tests with Cypress

To start Cypress and run e2e tests:
```bash
npm run e2e
```

Then select the test file `main.cy.js` and run it.

After running e2e tests:
```bash
npm run e2e:coverage
```

Coverage report:
```
/path_to_file/front/coverage/lcov-report/index.html
```

