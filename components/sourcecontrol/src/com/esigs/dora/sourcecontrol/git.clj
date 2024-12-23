(ns com.esigs.dora.sourcecontrol.git
  (:require [clojure.string :as str]
            [clojure.java.shell :as sh]))

(defn cmds [] 
  {:status ["status"]
   :log ["log" "--pretty=\"%h:commit:%at\""]})

(defn valid [{:keys [dir cmd] :as m}]
  (if (nil? cmd)
    (merge m {:error "Must supply git commands"})
    m))

(defn make-cmd [{:keys [dir cmd] :as m}]
  (concat ["git"] (cmd (cmds)) [:dir dir]))

(defn do-split [line]
  (str/split line #"\n"))

(defn query [{:keys [dir cmd] :as m}]
  (let [valid (valid m)]
    (if (nil? (:error valid))
      (do-split (:out (apply sh/sh (make-cmd m))))
      valid)))


(comment


  (make-cmd {:cmd :log})

  (apply str (make-cmd {:cmd :log}))
         )
