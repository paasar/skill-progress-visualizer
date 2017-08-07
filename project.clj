(defproject skill-progress-visualizer "0.1.0"
  :description "A skill progress visualizer that turns a simple input file into nice HTML"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [hiccup "1.0.5"]]
  :main skill-progress-visualizer.core
  :source-paths ["src" "resources"]
  :auto {:default {:file-pattern #"\.(clj|css)$"}})
