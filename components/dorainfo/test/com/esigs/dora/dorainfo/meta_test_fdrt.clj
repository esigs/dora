(ns com.esigs.dora.dorainfo.meta-test-fdrt
  (:require [clojure.test :as test :refer :all]
            [com.esigs.dora.dorainfo.meta-fdrt :as fdrt]))

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

(deftest calculate-fdrt
    (testing
      (let [expected [{:sha "de31332", :event :deploy, :time 1733776117}
                      {:sha "de31332", :event :commit, :time 1733775882}
                      {:sha "e1fce7f", :event :commit, :time 1733775766}
                      {:sha "749909c", :event :deploy, :time 1733775765}
                      {:sha "749909c", :event :commit, :time 1733775746}
                      {:sha "05b46c3", :event :fail, :time 1733775385 :fdrt 380}
                      {:sha "05b46c3", :event :deploy, :time 1733775380}
                      {:sha "05b46c3", :event :commit, :time 1733764694}
                      {:sha "eff5a8b", :event :commit, :time 1733764641}
                      {:sha "af04ef4", :event :commit, :time 1733764626}]
            actual (fdrt/calculate-fdrt :deploy sample)]
        (is (= expected actual)))))


(def has-fail-sample [{:sha "749909c", :event :deploy, :time 1733775765}
                       {:sha "749909c", :event :commit, :time 1733775746}
                       {:sha "05b46c3", :event :fail, :time 1733775385}])

(def has-no-fail-sample [{:sha "749909c", :event :deploy, :time 1733775765}
                          {:sha "749909c", :event :commit, :time 1733775746}])

(deftest has-fail-should-return-batch-if-it-has-a-failure
    (testing
      (let [expected [{:sha "749909c", :event :deploy, :time 1733775765}
                       {:sha "749909c", :event :commit, :time 1733775746}
                       {:sha "05b46c3", :event :fail, :time 1733775385}]
            actual (fdrt/has-fail has-fail-sample)]
        (is (= expected actual)))))

(deftest has-fail-should-return-batch-nil-if-batch-has-no-failures
    (testing
      (let [expected nil
            actual (fdrt/has-fail has-no-fail-sample)]
        (is (= expected actual)))))
