(ns tiples.users
  (:require [tiples.core :as tiples]))

(def common-data (atom {}))

(defrecord user [name password user-data])

(def users (atom (sorted-map)))

(defn add-user
  [name password user-data]
  (swap! users assoc name (->user name password user-data)))

(add-user "Fred" "fred" {})
(add-user "Sam" "sam" {})

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

(defn broadcast! [msg-id data]
  (let [uids (keys @by-client-id)
        msg [msg-id data]]
    (doseq [uid uids]
      (tiples/chsk-send! uid msg))))

(defn logout
  [session]
  (close-session session)
  (tiples/chsk-send! (:client-id session) [:users/logged-in nil])
  (broadcast! :users/logged-in-notice [(:name session) false]))

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
  (let [session (->session client-id name)
        user (get-user name)
        user-data (:user-data user)]
    (swap! by-client-id assoc client-id session)
    (swap! by-name assoc name session)
    (tiples/chsk-send! :client-id [:users/logged-in [@common-data user-data]]))
  (broadcast! :users/logged-in-notice [name true]))

(defmethod tiples/event-msg-handler :users/login
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (event 1)
        name (:name ?data)
        password (:password ?data)]
    (if (validate-user name password)
      (add-session client-id name)
      (tiples/chsk-send! client-id [:users/login-error]))))
