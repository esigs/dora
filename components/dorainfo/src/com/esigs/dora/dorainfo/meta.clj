(ns com.esigs.dora.dorainfo.meta
  (:require [com.esigs.dora.dorainfo.meta-clt :as clt]
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
