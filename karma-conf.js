module.exports = function(karma) {

  karma.set({

    // base path, that will be used to resolve files and exclude
    basePath: '',


    // frameworks to use
    frameworks: ['jasmine-jquery', 'jasmine-ajax', 'jasmine'],


    // list of files / patterns to load in the browser
    files: [
      'src/main/resources/static/js/vendor/jquery-3.1.1.min.js',
      'src/main/resources/static/js/vendor/jquery.dataTables.min.js',
      'src/main/resources/static/js/**/*.js',
      'src/test/js/**/*.js',
      {
        pattern: 'src/main/resources/templates/**/*.html',
        watched: true,
        included: false,
        served: true
      },

      'src/test/js/spec_helper.js'
    ],

    preprocessors: {
      // source files, that you wanna generate coverage for
      // do not include tests or libraries
      // (these files will be instrumented by Istanbul)
      'src/main/java/resources/js/{*.js,!(vendor)/**/*.js}': ['coverage']
    },

    // list of files to exclude
    exclude: [],

    plugins: [
      "karma-junit-reporter",
      "karma-jasmine",
      "karma-jasmine-jquery",
      "karma-jasmine-ajax",
      "karma-coverage",
      "karma-phantomjs2-launcher"
    ],

    // test results reporter to use
    // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
    reporters: ['progress', 'junit'],

    junitReporter: {
      outputFile: 'build/reports/javascript_tests.xml'
    },

    // web server port
    port: 8945,

    // cli runner port
    runnerPort: 9100,

    // enable / disable colors in the output (reporters and logs)
    colors: true,

    // level of logging
    // possible values: karma.LOG_DISABLE || karma.LOG_ERROR || karma.LOG_WARN || karma.LOG_INFO || karma.LOG_DEBUG
    logLevel: karma.LOG_INFO,

    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['PhantomJS2'],


    // If browser does not capture in given timeout [ms], kill it
    captureTimeout: 60000,


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: true
  });
};
