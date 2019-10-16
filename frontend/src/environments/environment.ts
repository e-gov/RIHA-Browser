// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  api: {
    environmentUrl: '/api/v1/environment',
    userInfoUrl: '/api/v1/user',
    classifiersUrl: '/api/v1/environment/classifiers',
    systemsUrl: '/api/v1/systems',
    issuesUrl: '/api/v1/issues',
    myOrganizationUrl: '/my/organization/users'
  }
};
