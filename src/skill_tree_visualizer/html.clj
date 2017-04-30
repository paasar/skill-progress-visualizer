(ns skill-tree-visualizer.html
  (:require [hiccup.core :refer :all]
            [clojure.string :as s]))

(defn- create-head [{name :name}]
  [:head
    [:title (str "Skill tree of " name)]
    [:style (slurp "resources/skill-tree-viz.css")]])

(defn- create-branch [skill-branch]
  [:div.level
   (for [[{:keys [symbol name unlock-date]} child-branches] skill-branch]
     [:div.skill
      [:div.symbol symbol]
      [:div.skill-name name]
      [:div.unlock-date unlock-date]
      (create-branch child-branches)])])

(defn- create-tree [{:keys [name skill-tree]}]
  [:div.tree
   [:div.category name]
   [:div.skills
     (create-branch skill-tree)]])

(defn- create-trees [trees]
  [:div.trees
   (for [tree trees]
     (create-tree tree))])

(defn- create-body [{:keys [name class skill-trees]}]
  [:body
   [:div.content
    [:h1.name-and-class (str name ", " class)]
    (create-trees skill-trees)]])

(defn render [data]
  (html [:html (create-head data)
               (create-body data)]))

(defn- create-file-name [name suffix]
  (str (-> name s/lower-case (s/replace #" " "_")) "_" suffix))

(defn render-to-file [data output-file-suffix]
  (->> data
       render
       (spit (create-file-name (:name data) output-file-suffix))))
