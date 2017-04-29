(ns skill-tree-visualizer.html
  (require [hiccup.core :refer :all]))

(defn- create-head [{name :name}]
  [:head [:title (str "Skill tree of " name)]])

(defn- create-body [{:keys [name class skill-trees]}]
  [:body [:h1 (str name ", " class)]])

(defn render [data output-file]
  (html [:html (create-head data)
               (create-body data)]))
