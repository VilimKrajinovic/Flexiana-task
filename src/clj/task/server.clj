(ns task.server
  (:gen-class)
  (:require [task.scramble :refer [scramble?]]
            [muuntaja.core :as muuntaja]
            [reitit.ring :as ring]
            [reitit.coercion.malli :as rcm]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja]
            [mount.core :as mount :refer [defstate]]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.http-response :refer [ok bad-request]]))

(def RequestPayload
  [:map
   [:str1 {:optional true} :string]
   [:str2 {:optional true} :string]])

(def ResponsePayload
  [:map
   [:result :boolean]])

(def ErrorPayload
  [:map
   [:error :string]])

(defn handle-get [request]
  (let [{:keys [str1 str2]} (-> request :parameters :query)]
    (log/info "Received GET request with parameters" {:str1 str1 :str2 str2})
    (if (and str1 str2)
      (ok {:result (scramble? str1 str2)})
      (bad-request {:error "Missing or invalid parameters"}))))

(defn handle-post [request]
  (let [{:keys [str1 str2]} (-> request :parameters :body)]
    (log/info "Received POST request with parameters" {:str1 str1 :str2 str2})
    (if (and str1 str2)
      (ok {:result (scramble? str1 str2)})
      (bad-request {:error "Missing or invalid parameters"}))))

(defn add-cors-headers [handler]
  (fn [request]
    (let [response (handler request)]
      (-> response
          (assoc-in [:headers "Access-Control-Allow-Origin"] "*")
          (assoc-in [:headers "Access-Control-Allow-Methods"] "GET, POST, PUT, DELETE, OPTIONS")
          (assoc-in [:headers "Access-Control-Allow-Headers"] "Content-Type, Authorization")
          (assoc-in [:headers "Access-Control-Allow-Credentials"] "true")))))

(def routes
  [["/scramble" {:get {:parameters {:query RequestPayload}
                       :responses {200 {:body ResponsePayload}
                                   400 {:body ErrorPayload}}
                       :handler handle-get}
                 :post {:parameters {:body RequestPayload}
                        :responses {200 {:body ResponsePayload}
                                    400 {:body ErrorPayload}}
                        :handler handle-post}
                 :options ring/default-options-handler}]])

(def app
  (-> (ring/ring-handler
       (ring/router routes
                    {:data {:muuntaja muuntaja/instance
                            :coercion reitit.coercion.malli/coercion
                            :middleware [reitit.ring.middleware.muuntaja/format-middleware
                                         wrap-params
                                         rrc/coerce-request-middleware
                                         rrc/coerce-exceptions-middleware
                                         rrc/coerce-response-middleware]}})
       (ring/routes
        (ring/create-resource-handler {:path "/"})
        (ring/create-default-handler)))
      add-cors-headers))

(defn- start-server [port]
  (if port
    (do
      (log/info "Starting service on port" port)
      (jetty/run-jetty app {:port port :join? false}))
    (log/warn "No port is specified, so service will not start")))

(defn- stop-server [server]
  (.stop server))

(defstate server
  :start (start-server 3000)
  :stop (some-> server stop-server))

(comment
  (mount/start)
  (mount/stop))

(defn -main [& _args]
  (mount/start)
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. mount/stop)))
(comment
  (-main))
