(ns com.esigs.dora.persistence.core
  (:require [com.esigs.dora.persistence.util :as u]
            [com.esigs.dora.persistence.file :as f]
            [com.esigs.dora.persistence.sc :as sc]))

(def qfunc {:git sc/fetch-from-sc!
            :file f/fetch-from-file!})

(defn run-funcs [conf]
  (if-let [func ((:name conf) qfunc)]
    (func conf)
    (throw (ex-info (str "Unknown storage name " conf) conf))))
