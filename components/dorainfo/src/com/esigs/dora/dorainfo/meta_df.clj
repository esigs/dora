(ns com.esigs.dora.dorainfo.meta-df
  (:require [com.esigs.dora.dorainfo.meta-util :as util]))

(defn calc-df [batch]
  (util/time-between-event :df (first batch) (last batch)))

(defn calculate-df [event col]
  (let [indexed (map-indexed vector col)
        batchbys (map last (util/filter-by-event event indexed))
        without-deploys (filter #(not= event (:event %)) col)
        without-df (last batchbys) ; we don't have a df for this guy because it's the first deploy
        parts (partition 2 1 batchbys)
        with-df  (map #(calc-df %) parts)]
    (sort-by :time > 
             (into without-deploys 
                   (conj with-df 
                         without-df)))))

(comment

  (def x [[{:sha "de31332", :event :deploy, :time 1733776117}
           {:sha "749909c", :event :deploy, :time 1733775765}]
           [{:sha "749909c", :event :deploy, :time 1733775765}
            {:sha "05b46c3", :event :deploy, :time 1733775380}]])

  (calc-df (first x))

         )
