(ns ring.example.chat.client)

(defn query [query]
  (.querySelector js/document query))

(defn append-html [element html]
  (.insertAdjacentHTML element "beforeend" html))

(defn message-html [author message]
  (str "<li><span class='author'>" author "</span>"
       "<span class='message'>" message "</span></li>"))

(defn send-message [_]
  (let [message (query "#message")
        author  (query "#author")
        html    (message-html (.-value author) (.-value message))]
    (append-html (query "#message-log") html)
    (set! (.-value message) "")
    (.focus message)))

(defn handle-enter [e]
  (when (= (.-code e) "Enter")
    (send-message e)))

(defn on-load [_]
  (.addEventListener (query "#send") "click" send-message)
  (.addEventListener (query "#message") "keyup" handle-enter))

(defn init []
  (.addEventListener js/window "load" on-load))
