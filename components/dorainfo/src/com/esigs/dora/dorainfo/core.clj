(ns com.esigs.dora.dorainfo.core
  (:require [clojure.string :as str]
            [clojure.java.shell :as sh]
            [com.esigs.dora.sourcecontrol.interface :as s]))

(def dir "/home/e/Repos/spike" ) 
(def file (str dir "/.dora")) 

(defn events []
  {:deploy "deploy"
   :fail "fail"
   :commit "commit" })

(defn parse [line]
  (let [cleaned (str/replace line #"\"" "")
        record (str/split cleaned #":")]
    {:sha (first record)
     :event (keyword (second record))
     :time (Long/parseLong (last record))}))

(defn do-split [line]
  (str/split line #"\n"))

(defn report []
  (let [s (do-split (:out (s/query {:cmd :log :dir dir})))
        f (do-split (slurp file))]
    (concat (map parse f) (map parse s))))

(defn seconds []
  (long (/ (System/currentTimeMillis) 1000)))

(defn record [{:keys [sha event] :as m}]
  (spit file (str sha ":" (event (events)) ":" (seconds) "\n") :append true))

(comment


  (record {:sha "05b46c3" :event :fail})
  (def r (report))
  (sort-by :time > r)

         )
