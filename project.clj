(defproject flexiana-task "0.1.0-SNAPSHOT"
  :description "Task for Flexiana"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.3"]
                 [org.clojure/tools.logging "1.3.0"]
                 [mount "0.1.18"]
                 [ring/ring-core "1.12.1"]
                 [ring/ring-jetty-adapter "1.12.1"]
                 [metosin/muuntaja "0.6.10"]
                 [ring/ring-mock "0.4.0"]
                 [metosin/ring-http-response "0.9.3"]
                 [org.clojure/test.check "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [metosin/malli "0.16.1"]
                 [metosin/reitit "0.7.0"]]
  :main flexiana-task.server
  :repl-options {:init-ns flexiana-task.server})
