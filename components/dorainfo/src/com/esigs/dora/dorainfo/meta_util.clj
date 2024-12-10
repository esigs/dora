(ns com.esigs.dora.dorainfo.meta-util)

(defn time-between-event [k this-event latest]
  (assoc this-event k 
         (int (Math/ceil 
                (double (- (:time latest) (:time this-event)))))))

(defn next-event [this-event indexed-col]
  (let [i (first this-event)
        r (filter #(and (not= i (first %))
                        (>= (first %) i)) 
                  indexed-col)]
    (first r)))

(defn event-time [event]
  (if (not (nil? event))
    (:time (last event))
    0))

(defn generate-batches [this-event batch-col indexed-col]
  (let [this-time (:time (last this-event))
        next-time (event-time (next-event this-event batch-col))]
    (filter #(and (>= this-time (:time (last %)))
                  (< next-time (:time (last %)))) indexed-col)))

(defn filter-by-event [event indexed-col]
  (let [f (filter #(= event (:event (last %))) indexed-col)]
    f))
