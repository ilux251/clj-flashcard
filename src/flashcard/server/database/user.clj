(ns flashcard.server.database.user
  (:require [clojure.java.jdbc :as j]
            [flashcard.server.database.config :refer [db]]))

(defn get-user-info
  [uuid]
  (j/query db (str "select * from user where uuid = '" uuid "'")))

(defn auth-user
  [username password]
  (let [result (j/query db (str "select count(*) as auth from user where username = '" username "' and password = '" password "'"))]
    result))