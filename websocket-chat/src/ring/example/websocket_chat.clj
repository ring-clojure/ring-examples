(ns ring.example.websocket-chat
  (:require [ring.adapter.jetty :as adapter]))

(defn handler [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello World</h1>"})

(defn -main [port]
  (adapter/run-jetty handler {:port (Integer/parseInt port)}))
