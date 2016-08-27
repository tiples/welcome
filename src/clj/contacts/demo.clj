(ns contacts.demo
  (:require [contacts.server :as contacts]))

(contacts/add-contact! {:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"})
(contacts/add-contact! {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"})
(contacts/add-contact! {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"})
(contacts/add-contact! {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"})
(contacts/add-contact! {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"})
(contacts/add-contact! {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"})
