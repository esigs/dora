(ns com.esigs.dora.dorainfo.meta
  (:require [com.esigs.dora.dorainfo.meta-cfp :as cfp]
            [com.esigs.dora.dorainfo.meta-clt :as clt]
            [com.esigs.dora.dorainfo.meta-df :as df]
            [com.esigs.dora.dorainfo.meta-util :as util]))

(defn calculate-clt [event col]
  (let [indexed (map-indexed vector col)
        batchbys (util/filter-by-event event indexed)
        batches (map #(util/generate-batches % batchbys indexed) batchbys)
        with-clt (map #(clt/add-clt %) batches)]
    (sort-by :time > (apply concat with-clt))))

(defn calculate-df [event col]
  (let [indexed (map-indexed vector col)
        batchbys (map last (util/filter-by-event event indexed))
        without-deploys (filter #(not= event (:event %)) col)
        without-df (last batchbys) ; we don't have a df for this guy because it's the first deploy
        parts (partition 2 1 batchbys)
        with-df  (map #(df/calc-df %) parts)]
    (sort-by :time > 
             (into without-deploys 
                   (conj with-df 
                         without-df)))))

(defn get-meta [col]
  (->> col
       (calculate-clt :deploy)
       (calculate-df :deploy)))

(defn dora [col]
  (let [m (get-meta col)
        adf (util/meta-average :df m)
        aclt (util/meta-average :clt m)
        cfp (cfp/change-fail-percentage m)]
    {:dep-avg-sec adf
     :clt-avg-sec aclt
     :cfp-percent cfp
     :data (into [] m)}))


;(defn calculate-fdrt [event col]
;  (let [indexed (map-indexed vector col)
;        batchbys (util/filter-by-event event indexed)
;        batches (map #(util/generate-batches % batchbys indexed) batchbys)
;        no-fails (apply concat (map #(no-fail %) batches))
;        fails (concat (map #(has-fail %) batches))
;        with-fdrt (map #(calc-fdrt %) fails)
;        ]
;    fails))

(comment

;  (def b (calculate-fdrt :deploy sample))
;  (map #(calc-fdrt %) b)
;
;  (calc-fdrt b)(first b) 
;  (last b)
;  (has-fail n)
;
;  (def n [
;          {:sha "749909c", :event :deploy, :time 1733775765}
;          {:sha "749909c", :event :commit, :time 1733775746}
;          ])
;
;  (def w [
;          {:sha "749909c", :event :deploy, :time 1733775765}
;          {:sha "749909c", :event :commit, :time 1733775746}
;          {:sha "05b46c3", :event :fail, :time 1733775385}
;          ])
;  
;  (util/generate-batches % ({:sha "de31332", :event :deploy, :time 1733776117}
;                            {:sha "749909c", :event :deploy, :time 1733775765}
;                            {:sha "05b46c3", :event :deploy, :time 1733775380}) indexed)
;
;
;  (dora sample)
;
;  (util/filter-by-event :fail 
;
;  (change-fail-percentage (get-meta sample))
;
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
