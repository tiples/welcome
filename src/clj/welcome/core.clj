(ns welcome.core
  (:require [tiples.core :as tiples]
            [tiples.users :as users]))

(def handler tiples/routes)

(users/add-user "Fred" "fred" {:welcome {}})
(users/add-user "Sam" "sam" {:welcome {}})
