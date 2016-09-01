(ns welcome.demo
  (:require [tiples.users :as users]
            [welcome.server]))

(users/add-user! "Fred" "fred" {:welcome {:full-name "Freddy Krueger"}
                                :profile {}
                                :contacts {}})
(users/add-user! "Sam" "sam" {:welcome {:full-name "Sam I Am"}
                              :profile {}
                              :contacts {}})
(users/add-user! "Kris" "kris" {:welcome {:full-name "Kris Kringle"}
                                :profile {}})
