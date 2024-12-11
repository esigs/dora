(ns com.esigs.dora.persistence.sc
  (:require [com.esigs.dora.persistence.util :as u]
            [com.esigs.dora.sourcecontrol.interface :as s]))

(defn fetch-from-sc! [options]
  (let [dir (:directory options)]
    (map #(u/parse %) 
         (s/query {:cmd :log :dir dir}))))
