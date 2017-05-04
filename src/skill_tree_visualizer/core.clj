(ns skill-tree-visualizer.core
  (:require [clojure.string :refer [lower-case split split-lines trim] :as s]
            [flatland.useful.seq :refer [partition-between]]
            [skill-tree-visualizer.html :refer [render]])
  (:gen-class))

(defn- to-level-entries [rows]
  (map (fn [row]
         (let [depth (/ (count (take-while #(= \space %) row)) 2)]
           [depth (trim row)]))
       rows))

(defn- data-string->data [data-string]
  (let [[sym name active] (split data-string #";")]
    {:symbol sym
     :name name
     :active active}))

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

(defn- lines->data [[name class description _ & skill-rows]]
  {:name name
   :description description
   :class class
   :skill-trees (skill-rows->skill-trees skill-rows)})

(defn- parse-input []
  (-> (slurp "input.data" :encoding "UTF-8")
      split-lines
      lines->data))

(defn- create-file-name [name suffix]
  (str (-> name lower-case (s/replace #" " "_")) "_" suffix))

(defn- write-to-file [content name]
  (spit (create-file-name name "skills.html") content :encoding "UTF-8"))

(defn generate-skill-tree-html []
  (println "Starting...")
  (let [data (parse-input)]
    (write-to-file (render data) (:name data)))
  (println "Done."))

(defn -main []
  (generate-skill-tree-html))
