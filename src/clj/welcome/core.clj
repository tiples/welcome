(ns welcome.core
  (:require [tiples.core :as tiples]
            [tiples.users :as users]))

(def handler tiples/routes)

(def my-contacts
  (atom #{{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
          {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
          {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
          {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
          {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
          {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}}))

(defmethod tiples/event-msg-handler :contacts/get-contacts
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (tiples/chsk-send! (:client-id ev-msg) [(:resp-id ?data) @my-contacts]))

(defmethod tiples/event-msg-handler :contacts/delete
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [contact (:contact ?data)]
    (swap! my-contacts disj contact)
    (tiples/broadcast! :contacts/deleted contact)))

(defmethod tiples/event-msg-handler :contacts/add
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [contact (:contact ?data)]
    (swap! my-contacts conj contact)
    (tiples/broadcast! :contacts/added contact)))
