(ns flashcard.server.core
  (:require [org.httpkit.server :as server]
            [flashcard.server.handler :as handler]
            [clojure.tools.namespace.repl :refer [refresh-all]])
  (:gen-class))

(defonce server (atom {}))
(def port 5000)

(defn stop-server
  []
  (when-not (nil? (:app @server))
    (println (:app @server))
    (do
      ((:app @server) :timeout 100)
      (swap! server assoc :app nil)
      (refresh-all))))

(defn start-server
  []
  (swap! server assoc :app (server/run-server (handler/app-handler) {:port port})))

(defn restart-server
  []
  (do
    (stop-server)
    (start-server)))

(defn -main
  "Runs the server"
  [& [port]]
  (let [port (Integer/parseInt (or port (System/getenv "PORT") "5000"))]
    (println (str "run server on port " port))
    (start-server)))