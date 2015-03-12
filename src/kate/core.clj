(ns kate.core
  (:require [clojure.string :as str]
            [clojure.java.io :refer [file]]
            [clojure.pprint :refer [pprint]]
            [clojure.zip :as zip]))

(def _file (nth (file-seq (file "site")) 1))

(def _file-bean (bean _file))

(def FS
  [[:site]
   [:site :drawing]
   [:site :drawing :tehe.jpg]
   [:site :illustration]
   [:site :illustration :jacolyn]
   [:site :illustration :jacolyn :1-jacolyn.jpg]
   [:site :illustration :jacolyn :description.md]
   [:site :illustration :war-machine]
   [:site :illustration :war-machine :1-war-machine.jpg]
   [:site :illustration :war-machine :description.md]])

(defn make-file-map [file-map file]
  (let [path (-> file :path (str/split #"/"))
        path-vec (mapv keyword path)
        is-file (re-find #"\." (str (last path-vec)))
        category-path (if (not= (count path-vec) 1)
                        (subvec path-vec 0 2)
                        path-vec)
        has-meta (get-in file-map category-path)
        new-file-map (if has-meta
                       file-map
                       (assoc-in file-map category-path
                               {:meta {} :entries []}))]
    (if is-file
      (let [project-name (name (nth path-vec (- (count path-vec) 2)))
            file-map {:name (name (last path-vec)) :file "file"}
            entries-path (conj category-path :entries)
            _entries (get-in new-file-map entries-path)
            entries (conj _entries file-map)
            new-file-map (assoc-in new-file-map entries-path entries)]
        new-file-map)
      new-file-map)))

; what I'm working towards
{:site {:blog {:meta {}
               :entries []}
        :drawing {:meta {}
                  :entries []}
        :illustration {:meta {}
                       :entries []}}}

(def files (map bean (file-seq (file "site"))))

(let [file-map (reduce make-file-map {} files)]
  (pprint file-map))
