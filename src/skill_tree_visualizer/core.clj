(ns skill-tree-visualizer.core
  (:require [clojure.string :refer [split split-lines trim]]
            [flatland.useful.seq :refer [partition-between]]
            [skill-tree-visualizer.html :refer [render]])
  (:gen-class))

(defn- to-level-entries [rows]
  (map (fn [row]
         (let [depth (/ (count (take-while #(= \space %) row)) 2)]
           [depth (trim row)]))
       rows))

(defn- data-string->data [data-string]
  (let [[sym name date] (split data-string #";")]
    {:symbol sym
     :name name
     :unlock-date date}))

(defn- entries-to-tree
  ([entries]
   (entries-to-tree 1 entries))
  ([depth entries]
   (for [part (partition-between (fn [[_ [d _]]] (= depth d)) entries)
         :let [[[depth data-string] & more] part
               data (data-string->data data-string)
               subtree (entries-to-tree (inc depth) more)]]
     (if (seq subtree)
       (conj [data] (vec subtree))
       [data []]))))

(defn- skill-rows->tree [skill-rows]
  (->> skill-rows
       to-level-entries
       entries-to-tree
       vec))

(defn- into-tree [[name & skill-rows]]
  {:name name
   :skill-tree (skill-rows->tree skill-rows)})

(defn- skill-rows->skill-trees [skill-rows]
  (->> (partition-by empty? skill-rows)
       (remove #(= [""] %))
       (mapv #(into-tree %))))

(defn- lines->data [[name class _ & skill-rows]]
  {:name name
   :class class
   :skill-trees (skill-rows->skill-trees skill-rows)})

(defn generate-skill-tree-html []
  (println "Starting...")
  (-> (slurp "input.data")
      split-lines
      lines->data
      (render "skillz.html"))
  (println "Done."))

(defn -main []
  (generate-skill-tree-html))
