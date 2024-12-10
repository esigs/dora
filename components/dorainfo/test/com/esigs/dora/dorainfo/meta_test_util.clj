(ns com.esigs.dora.dorainfo.meta-test-util
  (:require [clojure.test :as test :refer :all]
            [com.esigs.dora.dorainfo.meta-util :as util]))

(def deploy-sample [[0 {:sha "de31332", :event :deploy, :time 1733776117}]
                    [3 {:sha "749909c", :event :deploy, :time 1733775765}]
                    [6 {:sha "05b46c3", :event :deploy, :time 1733775380}]])

(deftest next-event-returns-expected
  (do 
    (testing 
      (let [this-event (second deploy-sample)
            expected 6
            result (util/next-event this-event deploy-sample)]
        (is (= expected (first result)))))))

(deftest event-time-results-expected
  (do 

    (testing "Get the time of the event passed"
      (let [next-event (second deploy-sample)
            expected 1733775765
            result (util/event-time next-event)]
        (is (= expected result)))
      "We got the wrong time back")
    
    (testing "Get the default 0 back"
      (let [next-event nil
            expected 0
            result (util/event-time next-event)]
        (is (= expected result)))
      "If no more events are passed, we return 0")))


(def batches-indexed-sample [[0 {:sha "de31332", :event :deploy, :time 1733776117}]
                             [1 {:sha "de31332", :event :commit, :time 1733775882}]
                             [2 {:sha "e1fce7f", :event :commit, :time 1733775766}]
                             [3 {:sha "749909c", :event :deploy, :time 1733775765}]
                             [4 {:sha "749909c", :event :commit, :time 1733775746}]
                             [5 {:sha "05b46c3", :event :fail, :time 1733775385}]
                             [6 {:sha "05b46c3", :event :deploy, :time 1733775380}]
                             [7 {:sha "05b46c3", :event :commit, :time 1733764694}]
                             [8 {:sha "eff5a8b", :event :commit, :time 1733764641}]
                             [9 {:sha "af04ef4", :event :commit, :time 1733764626}]])

(def batches-batch-sample [[0 {:sha "de31332", :event :deploy, :time 1733776117}]
                           [3 {:sha "749909c", :event :deploy, :time 1733775765}]
                           [6 {:sha "05b46c3", :event :deploy, :time 1733775380}]])

(deftest generate-batches-returns-expected
  (do

    (testing "We should get the deploy event and all :commit and :fail events since the last :deploy"
      (let [this-event (first batches-batch-sample)
            expected [[0 {:sha "de31332", :event :deploy, :time 1733776117}]
                      [1 {:sha "de31332", :event :commit, :time 1733775882}]
                      [2 {:sha "e1fce7f", :event :commit, :time 1733775766}]]
            result (vec (util/generate-batches this-event 
                                          batches-batch-sample 
                                          batches-indexed-sample))]
        (is (= expected result)))
      "We got :event that are unexpected")
    
    (testing "We should get the deploy event and all :commit and :fail events since the last :deploy"
      (let [this-event (second batches-batch-sample)
            expected [[3 {:sha "749909c", :event :deploy, :time 1733775765}]
                      [4 {:sha "749909c", :event :commit, :time 1733775746}] 
                      [5 {:sha "05b46c3", :event :fail, :time 1733775385}]]
            result (vec (util/generate-batches this-event 
                                          batches-batch-sample 
                                          batches-indexed-sample))]
        (is (= expected result)))
      "We got :event that are unexpected")
    
    (testing "We should get the deploy event and all :commit and :fail events since the last :deploy
              This should happen correctly even when we reach the end of the list and there is no 
              'next event batch'"
      (let [this-event (last batches-batch-sample)
            expected [[6 {:sha "05b46c3", :event :deploy, :time 1733775380}]
                      [7 {:sha "05b46c3", :event :commit, :time 1733764694}]
                      [8 {:sha "eff5a8b", :event :commit, :time 1733764641}]
                      [9 {:sha "af04ef4", :event :commit, :time 1733764626}]]
            result (vec (util/generate-batches this-event 
                                          batches-batch-sample 
                                          batches-indexed-sample))]
        (is (= expected result)))
      "We got :event that are unexpected")
    ))

(deftest filter-by-event
  (do
    (testing "We should get back the events we want to batch by"
      (let [expected [[0 {:sha "de31332", :event :deploy, :time 1733776117}]
                      [3 {:sha "749909c", :event :deploy, :time 1733775765}]
                      [6 {:sha "05b46c3", :event :deploy, :time 1733775380}]]
            actual (util/filter-by-event :deploy batches-indexed-sample)]
        (is (= expected actual)))
      "We did not get the expected :event batches")))
