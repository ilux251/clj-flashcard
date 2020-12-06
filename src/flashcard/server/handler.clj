(ns flashcard.server.handler
  (:require [compojure.core :as cp :refer [GET POST defroutes context]]
            [compojure.route :as route]
            [ring.middleware.defaults :as ring]
            [ring.middleware.json :refer [wrap-json-response]]
            [com.jakemccrary.middleware.reload :as reload]
            
            [flashcard.server.database.user :as user]))

(defroutes app-routes
  (context "/user" []
    (GET "/info" [uuid] (user/get-user-info uuid))
    (POST "/auth" [username password] (user/auth-user username password)))
  (route/not-found "<h1>Page not found</h1>"))

(def ^:private cors-headers
  {"Access-Control-Allow-Origin"  "*:*"
   "Access-Control-Allow-Headers" "*"
   "Access-Control-Allow-Methods" "GET, POST, OPTIONS"})

(defn- cors-handler
  [handler]
  (fn [request]
    (let [response (handler request)]
      (println (get response :body))
      (assoc response :headers cors-headers))))

(defn app-handler
  []
  (-> #'app-routes
      (wrap-json-response)
      (ring/wrap-defaults ring/api-defaults)
      (cors-handler)
      (reload/wrap-reload)))