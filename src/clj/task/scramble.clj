(ns task.scramble)

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
  (scramble? "rekqodlw" "world") ; true
  (scramble? "cedewaraaossoqqyt" "codewars") ; true
  (scramble? "katas" "steak") ; false
  )
