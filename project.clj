(defproject flashcard "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-defaults "0.3.2"]
                 [http-kit "2.3.0"]
                 [ring/ring-json "0.5.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.namespace "1.1.0"]
                 
                 ;; Authentication
                 [com.cemerick/friend "0.2.3"]]
  :main ^:skip-aot flashcard.server.core
  :target-path "target/%s"
  :profiles {:dev-server  {:dependencies [[com.jakemccrary/reload "0.1.0"]
                                          [ring/ring-mock "0.3.2"]]}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})