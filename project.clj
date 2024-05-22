(defproject flexiana-task "0.1.0-SNAPSHOT"
  :description "Task for Flexiana"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths ["src" "src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :plugins [[lein-shadow "0.4.1"]]
  :shadow-cljs {:nrepl {:port 8777}
                :source-paths ["src/cljs"]
                :builds {:app {:target :browser
                               :output-dir "resources/public/js/compiled"
                               :asset-path "js/compiled"
                               :modules {:main {:init-fn cljs.task.core/init}}}}}
  :aliases {"shadow-cljs" ["trampoline" "run" "-m" "shadow.cljs.devtools.cli"]
            "dev" ["do" "clean," "trampoline" "run" "-m" "task.server," "shadow-cljs" "watch" "app"]}

  :dependencies [[org.clojure/clojure "1.11.3"]
                 [org.clojure/tools.logging "1.3.0"]
                 [thheller/shadow-cljs "2.28.7"]
                 [reagent "1.2.0"]
                 [cljs-http "0.1.48"]
                 [mount "0.1.18"]
                 [ring/ring-core "1.12.1"]
                 [ring/ring-jetty-adapter "1.12.1"]
                 [ring-cors "0.1.13"]
                 [metosin/muuntaja "0.6.10"]
                 [ring/ring-mock "0.4.0"]
                 [metosin/ring-http-response "0.9.3"]
                 [org.clojure/test.check "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [metosin/malli "0.16.1"]
                 [metosin/reitit "0.7.0"]]
  :main task.server
  :repl-options {:init-ns task.server})
