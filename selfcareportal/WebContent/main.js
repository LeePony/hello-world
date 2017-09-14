// Kick off the application.
require(["app", "router"], function(app, Router) {
  // Define your master router on the application namespace and trigger all
  // navigation from this instance.
  app.router = new Router();

  // Trigger the initial route and enable HTML5 History API support, set the
  // root folder to '/' by default.  Change in app.js.
  fish.history.start({ pushState: false, root: app.root });
});