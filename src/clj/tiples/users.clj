(ns tiples.users
  (:require [tiples.core :as tiples]))

(def common-data (atom {}))

(defrecord user [name password user-data])

(def users (atom (sorted-map)))

(defn add-user
  [name password user-data]
  (swap! users assoc name (->user name password user-data)))

(defn get-user
  [name]
  (@users name))

(defn validate-user
  [name password]
  (let [user (get-user name)]
    (if user
      (= password (:password user))
      false)))

(defrecord session [client-id name capabilities])

(def by-client-id (atom {}))
(def by-name (atom {}))

(defn get-client-user
  [client-id]
  (let [session (by-client-id client-id)]
    (if session
      (get-user (:name session))
      nil)))

(defn broadcast! [msg-id data]
  (let [uids (keys @by-client-id)
        msg [msg-id data]]
    (doseq [uid uids]
      (tiples/chsk-send! uid msg))))

(defn close-session
  [session]
  (swap! by-client-id dissoc (:client-id session))
  (swap! by-name dissoc (:name session))
  (broadcast! :users/logged-in-notice [(:name session) nil])
  )

(defn logout
  [session]
  (tiples/chsk-send! (:client-id session) [:users/logged-in nil])
  (close-session session))

(defmethod tiples/event-msg-handler :users/logout
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (:client-id ev-msg)
        session (@by-client-id client-id)]
    (if session
      (logout session)))
  )

(defmethod tiples/event-msg-handler :chsk/uidport-close
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (:client-id ev-msg)
        session (@by-client-id client-id)]
    (if session
      (close-session session)))
  )

(defn add-session
  [client-id name]
  (let [session (@by-client-id client-id)]
    (if session
      (logout session)))
  (let [user (get-user name)
        user-data (:user-data user)
        capabilities (keys user-data)
        session (->session client-id name capabilities)
        select-common-data (select-keys @common-data capabilities)]
    (swap! by-client-id assoc client-id session)
    (swap! by-name assoc name session)
    (tiples/chsk-send! client-id
                       [:users/logged-in
                        [select-common-data user-data]
                        ]
                       )
    (broadcast! :users/logged-in-notice [name capabilities])
    ))

(defmethod tiples/event-msg-handler :users/login
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (:client-id ev-msg)
        name (:name ?data)
        password (:password ?data)]
    (if (validate-user name password)
      (add-session client-id name)
      (do
        (tiples/chsk-send! client-id [:users/login-error nil])))))
