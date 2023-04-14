import { defineConfig } from 'cypress';

export default defineConfig({
  projectId: 'iya2j9',
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    testIsolation: false,
    setupNodeEvents(on, config) {
      return require('./cypress/plugins/index.ts').default(on, config);
    },
    baseUrl: 'http://localhost:4200',
  },
});
