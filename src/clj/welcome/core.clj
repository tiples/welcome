(ns welcome.core
  (:require [tiples.core :as tiples]
            [tiples.users :as users]))

(def handler tiples/routes)
