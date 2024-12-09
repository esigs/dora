(ns com.esigs.dora.sourcecontrol.git
  (:require [clojure.java.shell :as sh]))

(defn cmds [] 
  {:status ["status"]
   :log ["log" "--simplify-by-decoration" "--pretty=\"%h:commit:%at\""]})

(defn valid [{:keys [dir cmd] :as m}]
  (if (nil? cmd)
    (merge m {:error "Must supply git commands"})
    m))

(defn make-cmd [{:keys [dir cmd] :as m}]
  (concat ["git"] (cmd (cmds)) [:dir dir]))

(defn query [{:keys [dir cmd] :as m}]
  (let [valid (valid m)]
    (if (nil? (:error valid))
      (apply sh/sh (make-cmd m))
      valid)))
