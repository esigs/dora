(ns com.esigs.dora.dorainfo.meta-clt
  (:require [com.esigs.dora.dorainfo.meta-util :as util]))

(defn calculate-clt-for-event [this-event latest]
  (assoc this-event :clt 
         (int (Math/ceil 
                (double (- (:time latest) (:time this-event)))))))

(defn calculate-clt-for-batch [batched-col]
  (let [latest (first batched-col)
        fails (filter #(= :fail (:event %)) batched-col)
        commits (filter #(not= :fail (:event %)) (rest batched-col))
        with-clt (map #(calculate-clt-for-event % latest) commits)]
    (into [] (concat (conj with-clt latest) fails))))

(defn add-clt [batched-col]
    (calculate-clt-for-batch 
      (mapv last batched-col)))

(defn calculate-clt [event col]
  (let [indexed (map-indexed vector col)
        batchbys (util/filter-by-event event indexed)
        batches (map #(util/generate-batches % batchbys indexed) batchbys)
        with-clt (map #(add-clt %) batches)]
    (sort-by :time > (apply concat with-clt))))


