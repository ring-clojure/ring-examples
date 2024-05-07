(ns ring.example.chat.client
  (:require [cljs.core.async :as a :refer [<! >! go go-loop]]
            [clojure.string :as str]
            [haslett.client :as ws]
            [haslett.format :as wsfmt]))

(defn- query [query]
  (.querySelector js/document query))

(defn- append-html [element html]
  (.insertAdjacentHTML element "beforeend" html))

(defn- message-html [{:keys [author message]}]
  (str "<li><span class='author'>"
       (if (str/blank? author) "Anonymous" author) "</span>"
       "<span class='message'>" message "</span></li>"))

(defn- send-message [stream]
  (let [message (query "#message")
        author  (query "#author")
        data    {:author  (.-value author)
                 :message (.-value message)}]
    (go (>! (:out stream) data))
    (set! (.-value message) "")
    (.focus message)))

(defn- start-listener [stream message-log]
  (go-loop []
    (when-some [message (<! (:in stream))]
      (append-html message-log (message-html message))
      (recur))))

(defn- websocket-url [path]
  (let [loc   (.-location js/window)
        proto (if (= "https" (.-protocol loc)) "wss" "ws")]
    (str proto "://" (.-host loc) path)))

(defn- websocket-connect [path]
  (ws/connect (websocket-url path) {:format wsfmt/transit}))

(defn- on-load [_]
  (go (let [stream  (<! (websocket-connect "/chat"))
            message (query "#message")]
        (start-listener stream (query "#message-log"))
        (.addEventListener (query "#send") "click"
                           (fn [_] (send-message stream)))
        (.focus message)
        (.addEventListener message "keyup"
                           (fn [e]
                             (when (= (.-code e) "Enter")
                               (send-message stream)))))))

(defn init []
  (.addEventListener js/window "load" on-load))
