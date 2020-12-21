(ns com.github.philipmw.clebs.ebs-test
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.ebs :refer :all])
  (:import [java.util Random]))

(deftest velocities-test
  (testing "velocities"
    (let [tasks [
                 {:actualTime 2 :estimatedTime 1}
                 {:actualTime 3 :estimatedTime 2}
                 {:actualTime 2 :estimatedTime 1}
                 ]]
      (is (= '(1/2 2/3 1/2) (velocities tasks))))))

(deftest plan-sum-test
  (testing "plan-sum"
    (let [tasks [{:estimatedTime 1} {:estimatedTime 3} {:estimatedTime 7}]]
      (is (= 11 (plan-sum tasks))))))

(deftest simulated-execution-test
  (testing "constant velocities"
    (let [tasks [{:estimatedTime 1} {:estimatedTime 1} {:estimatedTime 1}]
          velocities [1/2]                                  ; work was slower than planned
          exp-sim-times [2 2 2]                             ; so simulated times are longer than estimated
          rng (Random. 12345)]
      (is (= exp-sim-times (simulated-execution rng velocities tasks)))))
  (testing "varying velocities"
    (let [tasks [{:estimatedTime 1} {:estimatedTime 1} {:estimatedTime 1}]
          velocities [1/2 2 1]
          rng (Random. 12)]                                 ; chosen to yield three diff values
      (is (= [2 1 1/2] (simulated-execution rng velocities tasks)))))
  )

(deftest n-estimates-test
  (testing "n-estimates"
    (let [tasks [{:estimatedTime 1} {:estimatedTime 1} {:estimatedTime 1}]
          velocities [1]
          rng (Random. 12345)]
      (is (= [3 3 3 3 3] (n-estimates rng velocities tasks 5))))
    ))

(deftest pN-test
  ; test data from https://en.wikipedia.org/wiki/Percentile#The_nearest-rank_method
  (testing "p50"
    (is (= 35 (pN [15, 20, 35, 40, 50] 50)))))