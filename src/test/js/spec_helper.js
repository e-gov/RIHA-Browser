jasmine.getFixtures().fixturesPath = "/base/src/main/resources/templates/";

beforeEach(function() {
  window.setTimeoutCallbacks = [];
});

var promise = function(resolveValue) {
  return $.Deferred().resolve(resolveValue);
};

window.setTimeout = function(fn) {
  window.setTimeoutCallbacks.push(fn);
};