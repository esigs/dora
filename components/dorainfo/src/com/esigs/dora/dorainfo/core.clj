(ns com.esigs.dora.dorainfo.core
  (:require [clojure.string :as str]
            [com.esigs.dora.dorainfo.report :as rep]
            [com.esigs.dora.dorainfo.record :as rec]
            ))

(defn report []
  (rep/report))

(defn record [{:keys [sha event] :as m}]
  (rec/record m))

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
