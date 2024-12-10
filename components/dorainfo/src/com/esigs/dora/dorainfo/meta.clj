(ns com.esigs.dora.dorainfo.meta)

;;(defn filter-event-deploys [data]
;;  (filter #(= :deploy (:event (last %))) 
;;          data))

;;(defn since-last-deploy [deploys indexed]
;;  (let [removeIfLargerThanIndex (first (first (rest deploys)))]
;;    (filter #(> removeIfLargerThanIndex (first %)) indexed)))

(defn exclude-fail [batched-col]
  (filter #(not= :fail (:event %)) batched-col))

(defn clt-calc [batched-col]
  (let [latest (:time (first batched-col))
        oldest (:time (last (exclude-fail batched-col)))
        in-seconds (int (Math/ceil (double (- latest oldest))))
        in-minutes (int (Math/ceil (/ (double (- latest oldest)) 60)))]
    {:batch (vec batched-col)
     :clt-sec in-seconds 
     :clt-min in-minutes }))

(defn add-clt [batched-col]
  "Change lead time"
  (let [no-index (vec (map last batched-col))
        with-clt (clt-calc no-index)]
    with-clt))

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

(defn batch-by-event [event col]
  (let [indexed (map-indexed vector col)
        batchbys (filter-by-event event indexed)
        batches (map #(generate-batches % batchbys indexed) batchbys)]
    batches))

(comment

  
  (filter-by-event :deploy indexed)

  (batch-by-event :deploy sample)

  (map #(generate-batches % (batch-by-event :deploy sample) indexed) (batch-by-event :deploy sample))

  (thisBatch c n indexed)
  (def n (nextEvent c b))
  (batch c b)

  (def c (first b))

  (nextEvent (second b) b)
  (def b (batch-by-event :deploy sample))

  (clt sample)

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


  (def indexed (map-indexed vector sample))
  (def deploys (filter #(= :deploy (:event (last %))) indexed))

  (:time (last (first indexed)))

  (def thisD (first deploys))
  (def nextD (second deploys))

  (filter #(and (> (:time (last thisD)) (:time (last %)))
                (< (:time (last nextD)) (:time (last %)))) indexed)




  (def removeIfLarger (first (rest deploys)))

  (def final (filter #(> (first removeIfLarger) (first %)) indexed))

  (let [f (:time (last (first final)))
        l (:time (last (last final)))]
    (int (Math/ceil (/ (double (- f l)) 60))))

  (>= 3 4)
  

  )
