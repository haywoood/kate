(ns kate.core
  (:require [clojure.string :as str]
            [clojure.java.io :refer [file]]
            [clojure.pprint :refer [pprint]]
            [frontmatter.core :refer [parse]]))

;; figure out how to check java.io.file encoding
(defn is-utf8 [path]
  (let [ext (-> path (str/split #"\.") last)]
    (= ext "md")))

(defn readdir [path]
  (->> (file-seq (file path))
       (filter #(.isFile %))
       (mapv #(.getPath %))))

(defn readfile [path]
  (let [utf8 (is-utf8 path)]
    (if utf8
      (let [{:keys [body frontmatter]} (parse path)]
        {:contents body
         :frontmatter frontmatter
         :file (bean (file path))})
      {:contents (slurp path)
       :frontmatter nil
       :file (bean (file path))})))

(defn memo [memo data]
  (assoc memo (:path data) (:file data)))

(defn Kate [{:keys [source]
             :or {:source "src"}
             :as config}]
  (let [paths (readdir source)
        files (mapv readfile paths)
        data (mapv (fn [path file]
               {:path (keyword path) :file file}) paths files)]
    (reduce memo {} data)))

;; the dream
;; (-> (Kate "site")
;;      markdown
;;      templates
;;      build)

(Kate {:source "site"})
