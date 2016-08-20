(ns welcome.core
  (:require [tiples.core :as tiples]
            [tiples.users :as users]))

(users/add-user "Fred" "fred" {:welcome {:full-name "Freddy Krueger"}})
(users/add-user "Sam" "sam" {:welcome {:full-name "Sam I Am"}})

(def handler tiples/routes)
