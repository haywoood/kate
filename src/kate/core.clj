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
  (merge {:file (bean (file path))}
    (if-let [{:keys [body frontmatter]} (and (is-utf8 path) (parse path))]
      {:contents body
       :frontmatter frontmatter}
      {:contents (slurp path)})))

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
