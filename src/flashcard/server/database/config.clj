(ns flashcard.server.database.config)

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "flashcard.db"})