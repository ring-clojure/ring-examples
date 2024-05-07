(ns ring.example.chat.server
  (:require [clojure.core.async :as a]
            [reitit.ring :as rr]
            [ring.adapter.jetty :as adapter]
            [ring.websocket.async :as wsa]
            [ring.websocket.transit :as wst]
            [ring.websocket.keepalive :as wska]))

;; In this function we define a channel to be the main writer, and a 'mult' to
;; distribute the values placed on the channel to multiple readers.
;;
;; When a WebSocket is opened, we 'tap' the mult with the WebSocket's output
;; channel, and pipe the WebSocket's input channel to the writer channel. This
;; will take any input from this WebSocket, and output it to all WebSockets
;; currently connected to the server.
(defn make-chat-handler []
  (let [writer  (a/chan)
        readers (a/mult writer)]
    (fn handler [_request]
      (wsa/go-websocket [in out]
        (a/tap readers out)
        (a/pipe in writer false)))))

;; We use Reitit for the routes, defining a "/chat" route for the websocket,
;; and a resource handler to serve up the static files, such as the index,
;; the compile ClojureScript and CSS styling.
;;
;; We'll also add in two middleware functions: one to parse the WebSocket input
;; using Transit, a Clojure/Script library for efficiently passing Clojure data
;; structures from JavaScript. The other middleware will periodically ping the
;; client to prevent it from timing out ('keepalive').
(defn make-app-handler []
  (rr/ring-handler
   (rr/router ["/chat" (make-chat-handler)])
   (rr/routes
    (rr/create-resource-handler {:path "/"})
    (rr/create-default-handler))
   {:middleware [[wst/wrap-websocket-transit]
                 [wska/wrap-websocket-keepalive]]}))

(defn run-server [options]
  (adapter/run-jetty (make-app-handler) options))
