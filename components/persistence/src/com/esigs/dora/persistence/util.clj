(ns com.esigs.dora.persistence.util
  (:require [clojure.string :as str]))

(defn parse [line]
  (let [cleaned (str/replace line #"\"" "")
        record (str/split cleaned #":")]
    {:sha (first record)
     :event (keyword (second record))
     :time (Long/parseLong (last record))}))
