(ns com.esigs.dora.dorainfo.meta-cfp)

(defn change-fail-percentage [col]
  (let [deploys (filter #(= :deploy (:event %)) col)
        fails (filter #(= :fail (:event %)) col)
        percent (double (/ (count fails) (count deploys)))]
    (Double/parseDouble (format "%.2f" percent))))
