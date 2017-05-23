(ns skill-progress-visualizer.html
  (:require [hiccup.core :refer :all]))

(defn- create-head [{name :name}]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:title (str "Skill progress of " name)]
   [:link {:href "https://fonts.googleapis.com/css?family=Montserrat"
           :rel "stylesheet"}]
   [:style (slurp "resources/skill-tree-viz.css")]])

(defn- divs-in-between [elem coll]
  (interpose elem coll))

(defn- create-skill-columns [skill-tree]
  (let [names (map :name skill-tree)
        exp-points (map :exp skill-tree)
        activations (map :activation skill-tree)]
    [:div.skills
     [:div.column.skill-names
      (divs-in-between [:div.empty]
        (map (fn [[name active]]
               [:div.skill-name {:class (when active "active")} name])
             names))]
     [:div.column.exp-points
      (divs-in-between [:div.bar-container [:div.bar]]
        (map (fn [[exp active]]
               [:div.exp {:class (when active "active")} exp])
             exp-points))]
     [:div.column.activations
      (divs-in-between [:div.empty]
        (map (fn [activation]
               [:div.activation
                (when activation "ACHIEVED")
                [:div.activation-text activation]])
             activations))]]))

(defn- create-tree [{:keys [name skill-tree]}]
  [:div.tree
   [:div.category-container
    [:div.category name]]
   (create-skill-columns skill-tree)])

(defn- create-trees [trees]
  [:div.trees
   (for [tree trees]
     (create-tree tree))])

(defn- create-body [{:keys [name class description skill-trees]}]
  [:body
   [:div.header
    [:div.name name]
    [:div.character-class class]
    [:div.description description]]
   [:div.content
    (create-trees skill-trees)]])

(defn render [data]
  (html [:html (create-head data)
               (create-body data)]))

