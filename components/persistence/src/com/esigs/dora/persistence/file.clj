(ns com.esigs.dora.persistence.file
  (:require [com.esigs.dora.persistence.util :as util]))

(defn do-split [line]
  (str/split line #"\n"))

(defn fetch-from-file! [options]
  (let [dir (:directory options)
        file (:file-name options)
        path (str dir "/" file)]
    (map #(util/parse %) (do-split (slurp path)))))

