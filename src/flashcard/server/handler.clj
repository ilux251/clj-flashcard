(ns flashcard.server.handler
  (:require [compojure.core :as cp :refer [GET POST defroutes context]]
            [compojure.route :as route]
            [ring.middleware.defaults :as ring]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :refer [response status]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [com.jakemccrary.middleware.reload :as reload]
            [flashcard.server.database.user :as user]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [hiccup.page :as h]
            [flashcard.server.users :as users :refer (users)]))

(derive ::admin ::user)

(def login-form
  [:div {:class "row"}
   [:div {:class "columns small-12"}
    [:h3 "Log-in"]
    [:div {:class "row"}
     [:form {:method "POST" :action "login" :class "columns small-4"}
      [:div "Nutzername: " [:input {:type "text" :name "username"}]]
      [:div "Password: " [:input {:type "text" :name "password"}]]
      [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]])

(defroutes app-routes
  (GET "/" [] "Hello everyone <form action=\"logout\" method=\"post\"><button>Submit</button></form>")
  (GET "/auth" [] (friend/authorize #{::admin} "Hello authorized"))
  (GET "/login" [] (h/html5 login-form))
  (GET "/role-user" req
    (friend/authorize #{::user} "You're a user!"))
  (GET "/role-admin" req
    (friend/authorize #{::admin} "You're an admin!"))
  (friend/logout (POST "/logout" [] "logging out")))

(defn- do-login
  [req]
  (let [credential-fn (get-in req [::friend/auth-config :credential-fn])
        user-cred (select-keys (:params req) [:username :password])]
    (workflows/make-auth (credential-fn user-cred))))

(defn- password-credential-fn
  [{:keys [username password]}]
  (when-let [user (-> (user/get-user-by-username username)
                      first)]
    (when (= (:password user) password)
      {:identity (:uuid user) :roles #{::user} :user user})))

(defn password-workflow
  [req]
  (when (and (= (:request-method req) :post)
             (= (:uri req) "/login"))
    (do-login req)))

(defn app-handler
  []
  (-> app-routes
      (friend/authenticate {:credential-fn password-credential-fn
                            :workflows [password-workflow]
                            :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                                       response
                                                       (status 401))})
      (wrap-keyword-params)
      (wrap-params)
      (wrap-session)))