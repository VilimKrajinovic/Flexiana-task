(ns server-test
  (:require [clojure.test :refer [deftest testing is use-fixtures run-tests]]
            [cheshire.core :as json]
            [mount.core :as mount]
            [task.server :as server :refer [app]]
            [ring.mock.request :as mock]))

(use-fixtures :once
  (fn [f]
    (mount/start server/server)
    (f)
    (mount/stop server/server)))

(comment
  (json/parse-string (str "{" "\"result\": true" "}") true))

(deftest test-get-endpoint
  (testing "GET /scramble with valid parameters"
    (let [request (mock/request :get "/scramble" {:str1 "rkqodlw" :str2 "world"})
          response (app request)]
      (is (= 200 (:status response)))
      (is (= {:result true} (some-> response :body slurp (json/parse-string true))))))

  (testing "GET /scramble with missing parameters"
    (let [request (mock/request :get "/scramble")
          response (app request)]
      (is (= 400 (:status response)))
      (is (= {:error "Missing or invalid parameters"} (some-> response :body slurp (json/parse-string true))))))

  (testing "GET /scramble with invalid parameters"
    (let [request (mock/request :get "/scramble" {:str1 "abc"})
          response (app request)]
      (is (= 400 (:status response)))
      (is (= {:error "Missing or invalid parameters"} (some-> response :body slurp (json/parse-string true)))))))

(deftest test-post-endpoint
  (testing "POST /scramble with valid parameters"
    (let [request (-> (mock/request :post "/scramble")
                      (mock/json-body {:str1 "rkqodlw" :str2 "world"}))
          response (app request)]
      (is (= 200 (:status response)))
      (is (= {:result true} (some-> response :body slurp (json/parse-string true))))))

  (testing "POST /scramble with missing parameters"
    (let [request (-> (mock/request :post "/scramble")
                      (mock/json-body {}))
          response (app request)]
      (is (= 400 (:status response)))
      (is (= {:error "Missing or invalid parameters"} (some-> response :body slurp (json/parse-string true))))))

  (testing "POST /scramble with invalid parameters"
    (let [request (-> (mock/request :post "/scramble")
                      (mock/json-body {:str1 "abc"}))
          response (app request)]
      (is (= 400 (:status response)))
      (is (= {:error "Missing or invalid parameters"} (some-> response :body slurp (json/parse-string true)))))))

(deftest test-404
  (testing "GET /non-existent"
    (let [request (mock/request :get "/non-existent")
          response (app request)]
      (is (= 404 (:status response)))))
  (testing "POST /non-existent"
    (let [request (-> (mock/request :post "/non-existent")
                      (mock/json-body {:str1 "rkqodlw" :str2 "world"}))
          response (app request)]
      (is (= 404 (:status response))))))

(comment
  (test-get-endpoint)
  (run-tests))
