(ns flashcard.server.database.user
  (:require [clojure.java.jdbc :as j]
            [flashcard.server.database.config :refer [db]]))

(defn get-user-info
  [uuid]
  (j/query db (str "select * from user where uuid = '" uuid "'")))

(defn get-user-by-username
  [username]
  (j/query db (str "select * from user where username = '" username "'")))