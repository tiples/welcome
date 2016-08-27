(ns welcome.server
  (:require [tiples.users :as users]))

(users/add-capability :welcome)
