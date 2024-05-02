(ns ring.example.websocket-chat
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as adapter]))

(defn chat-handler [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello World</h1>"})

(def app-handler
  (rr/ring-handler
   (rr/router ["/chat" chat-handler])
   (rr/routes
    (rr/create-resource-handler {:path "/"})
    (rr/create-default-handler))))

(defn run-server [options]
  (adapter/run-jetty app-handler options))
