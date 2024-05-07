# Ring WebSocket Chat Example

This is an example project that demonstrates how to use WebSockets in
[Ring][] to create a simple chat web application.

This project uses [Reitit][] for routing, and [Haslett][] to handle
WebSockets client-side. Building the application is handled via
[tools.deps], and compiling the ClojureScript is managed by
[shadow-cljs].

To run it, first compile the ClojureScript using:

    clj -M:cljs compile client

Then start the server with:

    clj -X:server

By default, the server can be accessed at: <http://localhost:8080>

[Ring]: https://github.com/ring-clojure/ring
[Reitit]: https://github.com/metosin/reitit
[Haslett]: https://github.com/weavejester/haslett
[tools.deps]: https://github.com/clojure/tools.deps
[shadow-cljs]: https://github.com/thheller/shadow-cljs
