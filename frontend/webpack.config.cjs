const path = require('path');

module.exports = {
  module: {
    rules: [
      {
        test: /\.scss$|\.sass$/,
        use: [
          {
            loader: 'sass-loader',
            options: {
              sassOptions: {
                quietDeps: true, // Suppress deprecation warnings from dependencies
                verbose: false,
                silence: ['@import'],
              },
              additionalData: `$feature-flags: ("deprecation-warnings": false);`,
            },
          },
        ],
      },
    ],
  },
  ignoreWarnings: [/Deprecation Warning/, /sass @import/i, /Module Warning.*sass-loader/i],
};
