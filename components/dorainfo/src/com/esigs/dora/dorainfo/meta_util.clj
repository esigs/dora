(ns com.esigs.dora.dorainfo.meta-util)

(defn meta-average [k col]
  (let [items (map k (filter k col))
        sum (reduce + items)]
    (int (Math/ceil (double (/ sum (count items)))))))

(defn time-between-event [k this-event latest]
  (assoc this-event k 
         (int (Math/ceil 
                (Math/abs
                  (double (- (:time latest) (:time this-event))))))))

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

(defn index-and-filter-by-event [event col]
  (let [indexed (map-indexed vector col)]
    (map last (filter-by-event event indexed))))

(defn generate-batches-short [event col]
  (let [indexed (map-indexed vector col)
        batchbys (filter-by-event event indexed)
        batches (map #(generate-batches % batchbys indexed) batchbys)]
    batches))

(comment

(generate-batches-short :deploy sample)

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
