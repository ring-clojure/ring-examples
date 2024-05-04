(ns ring.example.chat.client
  (:require [cljs.core.async :as a :refer [<! >! go go-loop]]
            [haslett.client :as ws]))

(defn- query [query]
  (.querySelector js/document query))

(defn- append-html [element html]
  (.insertAdjacentHTML element "beforeend" html))

(defn- message-html [author message]
  (str "<li><span class='author'>" author "</span>"
       "<span class='message'>" message "</span></li>"))

(defn- send-message [stream]
  (let [message (query "#message")
        author  (query "#author")
        html    (message-html (.-value author) (.-value message))]
    (go (>! (:out stream) html))
    (set! (.-value message) "")
    (.focus message)))

(defn- start-listener [stream message-log]
  (go-loop []
    (when-some [message (<! (:in stream))]
      (append-html message-log message)
      (recur))
    (js/console.log (pr-str (<! (:close-status stream))))))

(defn- websocket-url [path]
  (let [loc   (.-location js/window)
        proto (if (= "https" (.-protocol loc)) "wss" "ws")]
    (str proto "://" (.-host loc) path)))

(defn- on-load [_]
  (go (let [stream (<! (ws/connect (websocket-url "/chat")))]
        (start-listener stream (query "#message-log"))
        (.addEventListener (query "#send") "click"
                           (fn [_] (send-message stream)))
        (.addEventListener (query "#message") "keyup"
                           (fn [e]
                             (when (= (.-code e) "Enter")
                               (send-message stream)))))))

(defn init []
  (.addEventListener js/window "load" on-load))
