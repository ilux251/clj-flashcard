(ns flashcard.server.core
  (:require [org.httpkit.server :as server]
            [flashcard.server.handler :as handler])
  (:gen-class))

(defonce server (atom nil))

(defn stop-server
  []
  (when-not (nil? @server)
    (do
      (@server :timeout 100)
      (reset! server nil))))

(defn start-server
  [port]
  (reset! server (server/run-server (handler/app-handler) {:port port})))

(defn restart-server
  [port]
  (stop-server)
  (start-server port))

(defn -main
  "Runs the server"
  [& [port]]
  (let [port (Integer/parseInt (or port (System/getenv "PORT") "5000"))]
    (println (str "run server on port " port))
    (start-server port)))