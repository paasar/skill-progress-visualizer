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

(defn- create-branch [skill-branch]
  [:div.sibling-skills
   (for [[{:keys [symbol name active]} child-branches] skill-branch]
     [:div.skill (when-not active {:class "inactive"})
      [:div.symbol-name-active
       [:div.symbol symbol]
       [:div.skill-name name]
       [:div.active
        (when active "ACHIEVED")
        [:div active]]]
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

