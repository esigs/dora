(ns com.esigs.dora.dorainfo.meta-clt)

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
