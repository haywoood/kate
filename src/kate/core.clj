(ns kate.core
  (:require [clojure.string :as str]
            [clojure.java.io :refer [file]]
            [clojure.pprint :refer [pprint]]))

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
        path-map (assoc-in {} path file)]
    (deep-merge coll path-map)))

(defn convert-dir [pathName]
  (let [dir (file-seq (file pathName))]
    (reduce add-file {} dir)))

(pprint (convert-dir "site"))
