(ns com.esigs.dora.dorainfo.meta-df
  (:require [com.esigs.dora.dorainfo.meta-util :as util]))

(defn calc-df [batch]
  (util/time-between-event :df (last batch) (first batch)))
