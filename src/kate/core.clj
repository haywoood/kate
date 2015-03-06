(ns kate.core
  (:require [clojure.string :as str]))

(def dir (file-seq (clojure.java.io/file "site")))

(def file (first (filter-files files)))

;; lifted from https://groups.google.com/d/msg/clojure/UdFLYjLvNRs/NqlA7wnLCE0J
(defn deep-merge
  "Recursively merges maps. If keys are not maps, the last value wins."
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))

(defn path-vec [file]
  (let [path (-> file bean :path (str/split #"/"))]
    (mapv keyword path)))

(defn add-file [coll file]
  (let [path (path-vec file)
        shit (assoc-in {} path file)]
    (deep-merge coll shit)))

(defn convert-dir [dir]
  (reduce add-file {} dir))

(clojure.pprint/pprint (convert-dir dir))
