(ns com.esigs.dora.dorainfo.meta-test-clt
  (:require [clojure.test :as test :refer :all]
            [com.esigs.dora.dorainfo.meta-clt :as clt]))

(def clt-batched-sample [
                         [; batch 1
                          [0 {:sha "de31332", :event :deploy, :time 1733776117}]
                          [1 {:sha "de31332", :event :commit, :time 1733775882}]
                          [2 {:sha "e1fce7f", :event :commit, :time 1733775766}]
                          ]

                         [; batch 2
                          [3 {:sha "749909c", :event :deploy, :time 1733775765}]
                          [4 {:sha "749909c", :event :commit, :time 1733775746}]
                          [5 {:sha "05b46c3", :event :fail, :time 1733774385}]
                          ]

                         [; batch 3
                          [6 {:sha "05b46c3", :event :deploy, :time 1733775380}]
                          [7 {:sha "05b46c3", :event :commit, :time 1733764694}]
                          [8 {:sha "eff5a8b", :event :commit, :time 1733764641}]
                          [9 {:sha "af04ef4", :event :commit, :time 1733764626}]
                          ]
                         ])

(deftest clt-returns-expected
  (do
    (testing "Calculate the correct change lead time for each batch"
      (let [this-batch (first clt-batched-sample)
            expected [
                      {:sha "de31332" :event :deploy, :time 1733776117}
                      {:sha "de31332" :event :commit, :time 1733775882 :clt 235}
                      {:sha "e1fce7f" :event :commit, :time 1733775766 :clt 351}
                      ]
            actual (clt/add-clt this-batch)]
        (is (= expected actual)))

    (testing "Calculate the correct change lead time for each batch
              This should never include fail info in the clt or it messes up the clt scores"
      (let [this-batch (second clt-batched-sample)
            expected [
                      {:sha "749909c", :event :deploy, :time 1733775765}
                      {:sha "749909c", :event :commit, :time 1733775746 :clt 19}
                      {:sha "05b46c3", :event :fail, :time 1733774385}
                      ]
            actual (clt/add-clt this-batch)]
        (is (= expected actual)))
    ))))






