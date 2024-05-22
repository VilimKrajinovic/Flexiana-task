(ns cljs.task.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [cljs.core.async :refer [go <!]]
            [cljs-http.client :as http]))

(def scramble-route "http://localhost:3000/scramble")

(def result (r/atom nil))

(defn handle-click [str1 str2]
  (go (let [response (<! (http/get scramble-route
                                   {:with-credentials? false
                                    :query-params {:str1 @str1
                                                   :str2 @str2}}))]
        (reset! result (->> response :body :result str)))))

(defn str-input [placeholder on-change]
  [:input {:type "text"
           :style {:display "block"}
           :placeholder placeholder
           :on-change on-change}])

(defn scramble [str1 str2]
  [:div
   [str-input "First string" #(reset! str1 (-> % .-target .-value))]
   [str-input "Second string" #(reset! str2 (-> % .-target .-value))]
   [:input {:type "button"
            :value "Scramble?"
            :on-click #(handle-click str1 str2)}]])

(defn render-result []
  (if @result
    [:p "Result: " @result]
    nil))

(defn init []
  (let [str1 (r/atom "")
        str2 (r/atom "")]
    (fn []
      [:div
       [scramble str1 str2]
       [render-result]])))

(defn ^:export run []
  (rdom/render [init] (js/document.getElementById "app")))

(run)
