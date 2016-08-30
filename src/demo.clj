(ns demo
   (:require [tiples.server :as tiples]
             [welcome.demo]
             [profile.server]
             [contacts.demo]))

(def handler tiples/routes)
