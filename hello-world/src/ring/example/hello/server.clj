(ns ring.example.hello.server
  (:require [reitit.ring :as rr]
            [ring.middleware.defaults :as def]
            [ring.adapter.jetty :as adapter]
            [ring.util.response :as resp]))

(defn hello-world [_request]
  (-> (resp/response
       "<!DOCTYPE html><title>Hello World</title><h1>Hello World</h1>")
      (resp/content-type "text/html")
      (resp/charset "UTF-8")))

(def handler
  (rr/ring-handler
   (rr/router ["/" hello-world])
   (rr/routes
    (rr/create-resource-handler {:path "/"})
    (rr/create-default-handler))
   {:middleware [[def/wrap-defaults def/site-defaults]]}))

(defn run-server [options]
  (adapter/run-jetty handler options))
