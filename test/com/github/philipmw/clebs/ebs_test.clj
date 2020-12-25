(ns com.github.philipmw.clebs.ebs-test
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.ebs :refer :all])
  (:import [java.time Duration]
           [java.util Random]))

(deftest velocities-test
  (testing "velocities"
    (let [tasks [{:estDur (Duration/ofDays 1) :actualDur (Duration/ofDays 2)}
                 {:estDur (Duration/ofDays 2) :actualDur (Duration/ofDays 3)}
                 {:estDur (Duration/ofMinutes 1) :actualDur (Duration/ofMinutes 2)}]]
      (is (= '(1/2 2/3 1/2) (velocities tasks))))))

(deftest plan-sum-test
  (testing "plan-sum"
    (let [tasks [{:estDur (Duration/ofDays 1)} {:estDur (Duration/ofDays 3)} {:estDur (Duration/ofDays 7)}]]
      (is (= (Duration/ofDays 11) (plan-sum tasks))))))

(deftest simulated-execution-test
  (testing "constant velocities"
    (let [tasks [{:estDur (Duration/ofHours 1)} {:estDur (Duration/ofHours 1)}]
          velocities [1/2] ; work was slower than planned
          exp-sim-times [(Duration/ofHours 2) (Duration/ofHours 2)] ; so simulated times are longer than estimated
          rng (Random. 12345)]
      (is (= exp-sim-times (simulated-execution rng velocities tasks)))))
  (testing "varying velocities"
    (let [tasks [{:estDur (Duration/ofHours 1)} {:estDur (Duration/ofHours 1)} {:estDur (Duration/ofHours 1)}]
          velocities [1/2 2 1]
          exp-sim-times [(Duration/ofHours 2) (Duration/ofHours 1) (Duration/ofMinutes 30)]
          rng (Random. 12)]                                 ; chosen to yield three diff values
      (is (= exp-sim-times (simulated-execution rng velocities tasks)))))
  )

(deftest n-estimates-test
  (testing "n-estimates"
    (let [tasks [{:estDur (Duration/ofHours 1)} {:estDur (Duration/ofHours 1)} {:estDur (Duration/ofHours 1)}]
          velocities [1]
          rng (Random. 12345)]
      (is (= (repeat 5 (Duration/ofHours 3)) (n-estimates rng velocities tasks 5))))
    ))

(deftest pN-test
  ; test data from https://en.wikipedia.org/wiki/Percentile#The_nearest-rank_method
  (testing "p50"
    (is (= 35 (pN [15, 20, 35, 40, 50] 50)))))

(deftest friendly-dur-test
  (testing "duration with hours"
    (is (= "1 days, 3 hours" (friendly-dur (Duration/ofHours 27)))))
  (testing "duration with whole days"
    (is (= "2 days" (friendly-dur (Duration/ofDays 2))))))
