(ns flashcard.server.users
  (:require
   [cemerick.friend.credentials :as creds]))

(def users (atom {"friend" {:username "friend"
                              :password (creds/hash-bcrypt "clojure")
                              :pin "1234" ;; only used by multi-factor
                              :roles #{::user}}
                  "friend-admin" {:username "friend-admin"
                                  :password (creds/hash-bcrypt "clojure")
                                  :pin "1234" ;; only used by multi-factor
                                  :roles #{::admin}}}))

(derive ::user ::admin)