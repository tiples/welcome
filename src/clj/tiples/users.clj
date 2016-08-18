(ns tiples.users
  (:require [tiples.core :as tiples]))

(defrecord user [name password])

(def users (atom (sorted-map)))

(defn add-user
  [name password]
  (swap! users assoc name (->user name password)))

(add-user "Fred" "fred")
(add-user "Sam" "sam")

(defn get-user
  [name]
  (@users name))

(defn validate-user
  [name password]
  (let [user (get-user name)]
    (if user
      (= password (:password user))
      false)))

(defrecord session [client-id name])

(def by-client-id (atom {}))
(def by-name (atom {}))

(defn get-client-user
  [client-id]
  (let [session (by-client-id client-id)]
    (if session
      (get-user (:name session))
      nil)))

(defn close-session
  [session]
  (swap! by-client-id disj (:client-id session))
  (swap! by-name disj (:name session)))

(defn logout
  [session]
  (close-session session)
  (tiples/chsk-send! (:client-id session) [:users/logged-in false]))

(defmethod tiples/event-msg-handler :users/logout
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (event 1)
        session (@by-client-id client-id)]
    (if session
      (logout session)))
  )

(defmethod tiples/event-msg-handler :chsk/uidport-close
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (event 1)
        session (@by-client-id client-id)]
    (if session
      (close-session session)))
  )

(defn add-session
  [client-id name]
  (let [session (@by-client-id client-id)]
    (if session
      (logout session)))
  (let [session (->session client-id name)]
    (swap! by-client-id assoc client-id session)
    (swap! by-name assoc name session))
  (tiples/chsk-send! client-id [:users/logged-in true]))

(defmethod tiples/event-msg-handler :users/login
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (event 1)
        name (:name ?data)
        password (:password ?data)]
    (if (validate-user name password)
      (add-session client-id name)
      (tiples/chsk-send! client-id [:users/login-error]))))
