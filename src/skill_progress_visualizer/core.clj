(ns skill-progress-visualizer.core
  (:require [clojure.string :refer [lower-case split split-lines trim] :as s]
            [flatland.useful.seq :refer [partition-between]]
            [skill-progress-visualizer.html :refer [render]])
  (:gen-class))

(defn- data-string->data [data-string]
  (let [[sym name activation] (split data-string #";")]
    {:symbol [sym activation]
     :name [name activation]
     :activation activation}))

(defn- skill-rows->tree [skill-rows]
  (->> skill-rows
       (map data-string->data)
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
