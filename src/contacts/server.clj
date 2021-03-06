(ns contacts.server
  (:require [tiples.users :as users]
            [tiples.server :as tiples]))

(users/add-capability :contacts)

(defn add-contact!
  [contact]
  (swap! users/common-data
         (fn [common]
           (let [contacts-set (get common :contacts #{})
                 contacts-set (conj contacts-set contact)]
             (assoc common :contacts contacts-set)))))

(defmethod tiples/event-msg-handler :contacts/delete
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (:client-id ev-msg)
        contact (:contact ?data)]
    (when (users/get-client-data :contacts client-id)
      (when (users/swap-common-data! :contacts
                                   (fn [contacts]
                                     (disj contacts contact))
                                   #{})
        (users/broadcast! :contacts/deleted contact)))))

(defmethod tiples/event-msg-handler :contacts/add
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [client-id (:client-id ev-msg)
        contact (:contact ?data)]
    (when (users/get-client-data :contacts client-id)
      (when (users/swap-common-data! :contacts
                                   (fn [contacts] (conj contacts contact))
                                   #{})
        (users/broadcast! :contacts/added contact)))))
