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

  (do-split (:out (s/query {:cmd :log :dir dir})))

  (record {:sha "05b46c3" :event :deploy})
  (def r (report))
  (sort-by :time > r)


  (def sample [{:sha "de31332", :event :deploy, :time 1733776117}
               {:sha "de31332", :event :commit, :time 1733775882}
               {:sha "e1fce7f", :event :commit, :time 1733775765}
               {:sha "749909c", :event :deploy, :time 1733775765}
               {:sha "749909c", :event :commit, :time 1733775746}
               {:sha "05b46c3", :event :fail, :time 1733775385}
               {:sha "05b46c3", :event :deploy, :time 1733775380}
               {:sha "05b46c3", :event :commit, :time 1733764694}
               {:sha "eff5a8b", :event :commit, :time 1733764641}
               {:sha "af04ef4", :event :commit, :time 1733764626}])

         )
