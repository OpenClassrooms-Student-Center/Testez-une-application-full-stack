import { defineConfig } from 'cypress';

export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  e2e: {
    setupNodeEvents(on, config) {
      const registerCodeCoverageTasks = require('@cypress/code-coverage/task');
      registerCodeCoverageTasks(on, config);
      return config;
    },
    baseUrl: 'http://localhost:4200',
  },
});
