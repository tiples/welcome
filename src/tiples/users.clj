(ns tiples.users
  (:require [tiples.server :as tiples]))

(def capabilities (atom []))

(def common-data (atom {}))

(defn add-capability
  [capability]
  (swap! capabilities conj capability))

(defrecord user [name password user-data])

(def users (atom (sorted-map)))

(defn add-user!
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

(defrecord session [client-id name user-capabilities])

(def by-client-id (atom {}))
(def by-name (atom {}))

(defn get-client-user
  [client-id]
  (let [session (@by-client-id client-id)]
    (if session
      (get-user (:name session))
      nil)))

(defn get-common-data
  [capability]
  (capability @common-data))

(defn swap-common-data!
  [capability f default]
  (swap! common-data
         (fn [cd]
           (let [capability-data (get cd capability default)
                 capability-data (f capability-data)]
             (assoc cd capability capability-data)))))

(defn get-client-data
  [capability client-id]
  (let [client (get-client-user client-id)]
    (if client
      (let [user-data (:user-data client)]
        (get user-data capability))
      nil)))

(defn swap-client-data!
  [capability client-id f]
  (try
    (let [session (@by-client-id client-id)]
      (if session
        (let [name (:name session)]
          (swap! users
                 (fn [us]
                   (let [user (@users name)
                         user-data (if user
                                           (get user :user-data)
                                           (throw (Exception. "no such user")))
                         capability-data (get user-data capability)
                         capability-data (if (not (nil? capability-data))
                                           (f capability-data)
                                           (throw (Exception. "unauthorized capability")))
                         user-data (assoc user-data capability capability-data)
                         user (assoc user :user-data user-data)
                         us (assoc us name user)]
                     us)))
          true)
        (throw (Exception. "no session"))))
    (catch Exception e false)))

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
  (let [session (@by-name name)]
    (if session
      (logout session)))
  (let [user (get-user name)
        user-data (:user-data user)
        user-capabilities (keys user-data)
        select-capabilities (reduce
                              (fn [r a]
                                (if (user-data a)
                                  (conj r a)
                                  r))
                              []
                              @capabilities)
        session (->session client-id name user-capabilities)
        select-common-data (select-keys @common-data user-capabilities)]
    (swap! by-client-id assoc client-id session)
    (swap! by-name assoc name session)
    (tiples/chsk-send! client-id
                       [:users/logged-in
                        [select-capabilities select-common-data user-data]
                        ]
                       )
    (broadcast! :users/logged-in-notice [name user-capabilities])
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
