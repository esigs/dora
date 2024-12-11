(ns com.esigs.dora.dorainfo.report
  (:require [clojure.string :as str]
            [com.esigs.dora.config.interface :as config]
            [com.esigs.dora.sourcecontrol.interface :as s]))

(defn parse [line]
  (let [cleaned (str/replace line #"\"" "")
        record (str/split cleaned #":")]
    {:sha (first record)
     :event (keyword (second record))
     :time (Long/parseLong (last record))}))

(defn do-split [line]
  (str/split line #"\n"))

(defn fetch-from-sc! [options]
  (let [dir (:directory options)]
    (map #(parse %) 
         (s/query {:cmd :log :dir dir}))))

(defn fetch-from-file! [options]
  (let [dir (:directory options)
        file (:file-name options)
        path (str dir "/" file)]
    (map #(parse %) (do-split (slurp path)))))

(def qfunc {:git fetch-from-sc!
            :file fetch-from-file!})

(defn run-funcs [conf]
  (if-let [func ((:name conf) qfunc)]
    (func conf)
    (throw (ex-info (str "Unknown storage name" conf) conf))))

(defn storage []
  (mapv run-funcs 
        (:storage (config/app-config))))

(defn report []
  (vec 
    (apply concat 
           (storage))))

(comment

  !!!!!!!!!!!!!!!!!! I DUNNO IF GOING SMALLER A GOOD IDEA !!!!!!!!!!!!!


  (report)
  (fetch-from-sc! {})

         )
