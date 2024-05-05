(ns ring.example.chat.server
  (:require [clojure.core.async :as a :refer [<! >!]]
            [reitit.ring :as rr]
            [ring.adapter.jetty :as adapter]
            [ring.websocket.async :as wsa]
            [ring.middleware.websocket-keepalive :as wska]))

(defn make-chat-handler []
  (let [writer  (a/chan)
        readers (a/mult writer)]
    (fn handler [_request]
      (wsa/go-websocket [in out]
        (a/tap readers out)
        (a/pipe in writer false)))))

(defn make-app-handler []
  (rr/ring-handler
   (rr/router ["/chat" (make-chat-handler)])
   (rr/routes
    (rr/create-resource-handler {:path "/"})
    (rr/create-default-handler))
   {:middleware [[wska/wrap-websocket-keepalive]]}))

(defn run-server [options]
  (adapter/run-jetty (make-app-handler) options))
