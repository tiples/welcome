(ns demo
   (:require [tiples.server :as tiples]
             [welcome.demo]
             [contacts.demo]))

(def handler tiples/routes)
