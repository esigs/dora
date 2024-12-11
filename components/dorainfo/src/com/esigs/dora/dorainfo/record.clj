(ns com.esigs.dora.dorainfo.record
  (:require [clojure.string :as str]
            [com.esigs.dora.dorainfo.report :as r]
            [com.esigs.dora.sourcecontrol.interface :as s]
            [com.esigs.ezutils.env :as env]))

(def dir "/home/e/Repos/spike" ) 
(def file (str dir "/.dora")) 

(defn events []
  {:deploy "deploy"
   :fail "fail"
   :commit "commit" })

(defn seconds []
  (long (/ (System/currentTimeMillis) 1000)))

(defn record [{:keys [sha event] :as m}]
  (spit file (str sha ":" (event (events)) ":" (seconds) "\n") :append true))
