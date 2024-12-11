(ns com.esigs.dora.dorainfo.utils
  (:require [clojure.string :as str]))

(defn do-split [line]
  (str/split line #"\n"))
