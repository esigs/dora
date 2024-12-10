(ns com.esigs.dora.dorainfo.meta
  (:require [com.esigs.dora.dorainfo.meta-cfp :as cfp]
            [com.esigs.dora.dorainfo.meta-clt :as clt]
            [com.esigs.dora.dorainfo.meta-fdrt :as fdrt]
            [com.esigs.dora.dorainfo.meta-df :as df]
            [com.esigs.dora.dorainfo.meta-util :as util]))

(defn get-meta [col]
  (->> col
       (clt/calculate-clt :deploy)
       (df/calculate-df :deploy)
       (fdrt/calculate-fdrt :deploy)))

(defn dora [col]
  (let [m (get-meta col)
        adf (util/meta-average :df m)
        aclt (util/meta-average :clt m)
        cfp (cfp/change-fail-percentage m)
        fdrt (util/meta-average :fdrt m)
        ]
    {:dep-avg-sec adf
     :clt-avg-sec aclt
     :cfp-percent cfp
     :fdrt-avg-sec fdrt
     :data (into [] m)}))

(comment

  (def sample [{:sha "de31332", :event :deploy, :time 1733776117}
               {:sha "de31332", :event :commit, :time 1733775882}
               {:sha "e1fce7f", :event :commit, :time 1733775766}
               {:sha "749909c", :event :deploy, :time 1733775765}
               {:sha "749909c", :event :commit, :time 1733775746}
               {:sha "05b46c3", :event :fail, :time 1733775385}
               {:sha "05b46c3", :event :deploy, :time 1733775380}
               {:sha "05b46c3", :event :commit, :time 1733764694}
               {:sha "eff5a8b", :event :commit, :time 1733764641}
               {:sha "af04ef4", :event :commit, :time 1733764626}])

  )
