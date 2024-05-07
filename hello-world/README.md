# Ring WebSocket Chat Example

This is a basic 'Hello World' example project in [Ring][].

This project uses [Reitit][] for routing, and [Ring-Defaults][] to
add sensible default middleware. Building the application is handled via
[tools.deps][].

To start the server:

    clj -X:server

By default, the server can be accessed at: <http://localhost:8080>

You can also specify the port number explicitly:

    clj -X:server :port 3000

[Ring]: https://github.com/ring-clojure/ring
[Reitit]: https://github.com/metosin/reitit
[Ring-Defaults]: https://github.com/ring-clojure/ring-defaults
[tools.deps]: https://github.com/clojure/tools.deps
