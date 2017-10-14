(ns skill-progress-visualizer.core
  (:require [clojure.string :refer [lower-case split split-lines trim] :as s]
            [skill-progress-visualizer.html :refer [render]])
  (:gen-class))

(defn- data-string->data [data-string]
  (let [[exp name activation] (split data-string #";")]
    {:exp [exp activation]
     :name [name activation]
     :activation activation}))

(defn- into-tree [[name & skill-rows]]
  {:name name
   :skill-tree (mapv data-string->data skill-rows)})

(defn- skill-rows->skill-trees [skill-rows]
  (->> (partition-by empty? skill-rows)
       (remove #(= [""] %))
       (mapv #(into-tree %))))

(defn- gained-exp [skill-trees]
  (->> skill-trees
       (mapcat :skill-tree)
       (map :exp)
       (remove (comp nil? second))
       (map (fn [[num _]]
              (try
                (Long/parseLong num)
                (catch NumberFormatException _
                  (println (str "Could not parse '" num "' as a number. Defaulting to zero."))
                  0))))
       (apply +)))

(defn- level [exp]
  (->> (iterate #(+ 20 %) 10)
       (take-while #(< % exp))
       count))

(defn- lines->data [[name class description _ & skill-rows]]
  (let [skill-trees (skill-rows->skill-trees skill-rows)
        current-exp (gained-exp skill-trees)
        current-level (level current-exp)]
    {:name name
     :description description
     :class class
     :skill-trees skill-trees
     :exp current-exp
     :level current-level}))

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
