(ns com.esigs.dora.dorainfo.meta-test-dora
  (:require [clojure.test :as test :refer :all]
            [com.esigs.dora.dorainfo.meta :as meta]))

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

(deftest calculate-dora
    (testing
      (let [expected {:dep-avg-sec 369,
                      :clt-avg-sec 5464,
                      :cfp-percent 0.33,
                      :fdrt-avg-sec 380,
                      :data [{:sha "de31332", :event :deploy, :time 1733776117, :df 352}
                             {:sha "de31332", :event :commit, :time 1733775882, :clt 235}
                             {:sha "e1fce7f", :event :commit, :time 1733775766, :clt 351}
                             {:sha "749909c", :event :deploy, :time 1733775765, :df 385}
                             {:sha "749909c", :event :commit, :time 1733775746, :clt 19}
                             {:sha "05b46c3", :event :fail, :time 1733775385, :fdrt 380}
                             {:sha "05b46c3", :event :deploy, :time 1733775380}
                             {:sha "05b46c3", :event :commit, :time 1733764694, :clt 10686}
                             {:sha "eff5a8b", :event :commit, :time 1733764641, :clt 10739}
                             {:sha "af04ef4", :event :commit, :time 1733764626, :clt 10754}]}
            actual (meta/dora sample)]
      (is (= expected actual)))))

