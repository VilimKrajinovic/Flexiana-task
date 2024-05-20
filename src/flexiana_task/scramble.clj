(ns flexiana-task.scramble)

(defn- count-chars [str]
  (reduce (fn [counts c]
            (update counts c (fnil inc 0)))
          {}
          str))

(defn scramble? [str1 str2]
  (let [counts1 (count-chars str1)
        counts2 (count-chars str2)]
    (every? (fn [[char count]]
              (>= (get counts1 char 0) count))
            counts2)))

(comment
  ; should return true
  (scramble? "rekqodlw" "world")
  (scramble? "cedewaraaossoqqyt" "codewars")
  (scramble? nil nil)
  (scramble? "" "")

  ; TODO: what should this return?
  (scramble? "something" nil)
  (scramble? "something" "")

  ; should return false
  (scramble? "katas" "steak")
  (scramble? nil "something")
  (scramble? "" "something"))
