const codeCoverageTask = require('@cypress/code-coverage/task');

export default (on: any, config: any) => {
  codeCoverageTask(on, config);
  return config;
};
