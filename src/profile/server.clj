(ns profile.server
  (:require [tiples.users :as users]))

(users/add-capability :profile)
