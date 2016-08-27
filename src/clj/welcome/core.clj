(ns welcome.core
  (:require [tiples.core :as tiples]
            [tiples.users :as users]
            [contacts.demo]))

(users/add-capability :welcome)

(users/add-user! "Fred" "fred" {:welcome {:full-name "Freddy Krueger"}
                                :contacts {}})
(users/add-user! "Sam" "sam" {:welcome {:full-name "Sam I Am"}
                              :contacts {}})
(users/add-user! "Kris" "kris" {:welcome {:full-name "Kris Kringle"}})

(def handler tiples/routes)
