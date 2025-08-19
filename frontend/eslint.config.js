import js from '@eslint/js';
import tseslint from '@typescript-eslint/eslint-plugin';
import tsparser from '@typescript-eslint/parser';
import angular from '@angular-eslint/eslint-plugin';
import angularTemplate from '@angular-eslint/eslint-plugin-template';
import angularParser from '@angular-eslint/template-parser';

export default [
  // Global ignores
  {
    ignores: [
      'projects/**/*',
      'dist/**/*',
      'node_modules/**/*',
      'target/**/*',
      '.yarn/**/*',
      '*.js',
      'out-tsc/**/*',
      'coverage/**/*',
      'e2e/**/*',
      'src/environments/environment.local.ts',
      'src/environments/environment.prod.ts',
    ],
  },

  // Base configuration for TypeScript files
  {
    files: ['src/**/*.ts'],
    ignores: ['src/test.ts'],
    languageOptions: {
      parser: tsparser,
      parserOptions: {
        ecmaVersion: 2022,
        sourceType: 'module',
        project: ['./src/tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      globals: {
        // Browser globals
        window: 'readonly',
        document: 'readonly',
        console: 'readonly',
        setTimeout: 'readonly',
        clearTimeout: 'readonly',
        setInterval: 'readonly',
        clearInterval: 'readonly',
        confirm: 'readonly',
        alert: 'readonly',
        HTMLElement: 'readonly',
        HTMLInputElement: 'readonly',
        HTMLStyleElement: 'readonly',
        FormData: 'readonly',
        // Node.js globals
        module: 'readonly',
        process: 'readonly',
        Buffer: 'readonly',
      },
    },
    plugins: {
      '@typescript-eslint': tseslint,
      '@angular-eslint': angular,
    },
    rules: {
      // Use base rules but turn off problematic ones
      ...js.configs.recommended.rules,
      // Don't spread the entire recommended configs to avoid conflicts
      // Use base rules but turn off problematic ones
      ...js.configs.recommended.rules,
      // Don't spread the entire recommended configs to avoid conflicts

      // TypeScript ESLint rules (turned off as per your config)
      '@typescript-eslint/ban-ts-comment': 'off',
      '@typescript-eslint/ban-types': 'off',
      '@typescript-eslint/explicit-function-return-type': 'off',
      '@typescript-eslint/explicit-module-boundary-types': 'off',
      '@typescript-eslint/member-ordering': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-floating-promises': 'off',
      '@typescript-eslint/no-non-null-assertion': 'off',
      '@typescript-eslint/no-shadow': 'off',
      '@typescript-eslint/no-unnecessary-condition': 'off',
      '@typescript-eslint/no-unsafe-argument': 'off',
      '@typescript-eslint/no-unsafe-assignment': 'off',
      '@typescript-eslint/no-unsafe-call': 'off',
      '@typescript-eslint/no-unsafe-member-access': 'off',
      '@typescript-eslint/no-unsafe-return': 'off',
      '@typescript-eslint/no-unused-vars': 'off',
      '@typescript-eslint/no-redundant-type-constituents': 'off',
      '@typescript-eslint/no-non-null-asserted-optional-chain': 'off',
      '@typescript-eslint/no-unsafe-enum-comparison': 'off',
      '@typescript-eslint/prefer-nullish-coalescing': 'off',
      '@typescript-eslint/prefer-optional-chain': 'off',
      '@typescript-eslint/unbound-method': 'off',

      // General ESLint rules (turned off as per your config)
      'arrow-body-style': 'off',
      curly: 'off',
      eqeqeq: 'off',
      'guard-for-in': 'off',
      'no-bitwise': 'off',
      'no-caller': 'off',
      'no-console': 'off',
      'no-eval': 'off',
      'no-labels': 'off',
      'no-new': 'off',
      'no-new-wrappers': 'off',
      'no-unused-vars': 'off', // Turn off base rule to use TypeScript version
      'no-useless-escape': 'off',
      'object-shorthand': 'off',
      radix: 'off',
      'spaced-comment': 'warn',
    },
  },

  // Configuration for test bootstrap file
  {
    files: ['src/test.ts'],
    languageOptions: {
      parser: tsparser,
      parserOptions: {
        ecmaVersion: 2022,
        sourceType: 'module',
        project: ['./src/tsconfig.spec.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      globals: {
        // Browser globals
        window: 'readonly',
        document: 'readonly',
        console: 'readonly',
        // Karma globals
        __karma__: 'readonly',
      },
    },
    plugins: {
      '@typescript-eslint': tseslint,
    },
    rules: {
      // Use base rules but turn off problematic ones
      ...js.configs.recommended.rules,
      // Turn off strict rules for test bootstrap
      '@typescript-eslint/no-explicit-any': 'off',
      'no-console': 'off',
    },
  },

  // Configuration for test files (Jasmine/Karma globals)
  {
    files: ['src/**/*.spec.ts'],
    languageOptions: {
      parser: tsparser,
      parserOptions: {
        ecmaVersion: 2022,
        sourceType: 'module',
        project: ['./src/tsconfig.spec.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      globals: {
        // Jasmine test framework globals
        describe: 'readonly',
        it: 'readonly',
        beforeEach: 'readonly',
        beforeAll: 'readonly',
        afterEach: 'readonly',
        afterAll: 'readonly',
        expect: 'readonly',
        spyOn: 'readonly',
        fail: 'readonly',
        pending: 'readonly',
        // Karma-specific globals
        karma: 'readonly',
        // Jasmine clock
        jasmine: 'readonly',
        // For e2e tests
        browser: 'readonly',
        element: 'readonly',
        by: 'readonly',
      },
    },
  },

  // Configuration for Angular HTML templates
  {
    files: ['src/**/*.html'],
    languageOptions: {
      parser: angularParser,
    },
    plugins: {
      '@angular-eslint/template': angularTemplate,
    },
    rules: {
      // Turn off the eqeqeq rule for templates since your original config had it off
      '@angular-eslint/template/eqeqeq': 'off',
    },
  },
];
