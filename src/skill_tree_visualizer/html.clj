(ns skill-tree-visualizer.html
  (:require [hiccup.core :refer :all]))

(defn- create-head [{name :name}]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:title (str "Skill tree of " name)]
   [:link {:href "https://fonts.googleapis.com/css?family=Montserrat"
           :rel "stylesheet"}]
   [:style (slurp "resources/skill-tree-viz.css")]])

(defn- create-skill-columns [skill-tree]
  (let [names (map :name skill-tree)
        symbols (map :symbol skill-tree)
        activations (map :activation skill-tree)]
    [:div.skills
     [:div.column.skill-names
      (map (fn [[name active]]
             [:div.skill-name {:class (when active "active")} name]) names)]
     [:div.column.symbols
      (map (fn [[symbol active]]
             [:div.symbol {:class (when active "active")} symbol]) symbols)]
     [:div.column.activations
      (map (fn [activation]
             [:div.activation
             (when activation "ACHIEVED")
             [:div.activation-text activation]]) activations)]]))

(defn- create-tree [{:keys [name skill-tree]}]
  [:div.tree
   [:div.category name]
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

