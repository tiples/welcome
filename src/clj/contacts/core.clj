(ns contacts.core
  (:require [tiples.users :as users]
            [tiples.core :as tiples]))

(defn add-contact!
  [contact]
  (swap! users/common-data
         (fn [common]
           (let [contacts-set (get common :contacts #{})
                 contacts-set (conj contacts-set contact)]
             (assoc common :contacts contacts-set)))))

(defmethod tiples/event-msg-handler :contacts/delete
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [contact (:contact ?data)]
    (if (users/swap-client-data! :contacts
                                 (:client-id ev-msg)
                                 (fn [contacts] (disj contacts contact)))
      (users/broadcast! :contacts/deleted contact))))

  (defmethod tiples/event-msg-handler :contacts/add
    [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
    (let [contact (:contact ?data)]
      (if (users/swap-client-data! :contacts
                                   (:client-id ev-msg)
                                   (fn [contacts] (conj contacts contact)))
        (users/broadcast! :contacts/added contact))))
