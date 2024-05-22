(ns scramble-test
  (:require [task.scramble :as scramble :refer [scramble?]]
            [clojure.test :as t :refer [deftest is testing]]))

(deftest test-scrambling
  (testing "Testing cases expected to be true"
    (is (= true (scramble? "rekqodlw" "world")))
    (is (= true (scramble? "cedewaraaossoqqyt" "codewars")))
    (is (= true (scramble? nil nil)))
    (is (= true (scramble? "" "")))
    (is (= true (scramble? "something" nil)))
    (is (= true (scramble? "something" ""))))
  (testing "Testing cases expected to be false"
    (is (= false (scramble? "katas" "steak")))
    (is (= false (scramble? nil "something")))
    (is (= false (scramble? "" "something")))))

(comment
  (test-scrambling))
