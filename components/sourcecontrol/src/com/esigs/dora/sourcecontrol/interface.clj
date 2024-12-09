(ns com.esigs.dora.sourcecontrol.interface
  (:require [com.esigs.dora.sourcecontrol.git :as git]))

(defn query [{:keys [dir cmd] :as m}]
  (git/query m))
