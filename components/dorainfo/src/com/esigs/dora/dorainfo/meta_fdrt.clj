(ns com.esigs.dora.dorainfo.meta-fdrt
  (:require [com.esigs.dora.dorainfo.meta-util :as util]))



(defn has-fail [batch]
    (if (some #(= :fail (:event %)) batch)
            batch))

(defn calc-fdrt [batch]
  (if batch 
    (util/time-between-event :fdrt (last batch) (first batch))))

(defn remove-index [batch]
  (map last batch))

(defn calculate-fdrt [event col]
  (let [batches (util/generate-batches-short event col)
        no-index (vec (map #(remove-index %) batches))
        has-failures (map #(has-fail %) no-index)
        has-no-failures (filter #(not= :fail (:event %)) col)
        with-fdrt (remove nil? (map #(calc-fdrt %) has-failures))
        ]
  (sort-by :time > 
           (concat has-no-failures with-fdrt))))
