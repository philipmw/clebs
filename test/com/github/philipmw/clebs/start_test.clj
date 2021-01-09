(ns com.github.philipmw.clebs.start-test
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.start :refer :all])
  (:import [java.time Duration]))

(deftest validate-args-test
  (testing "--help"
    (let [args '("--help")
          {:keys [exit-message ok?]} (validate-args args)]
      (is ok?)
      (is (not (nil? exit-message)))
      ))

  (testing "no action"
    (let [args '("--evidence" "./test/data/evidence.xml" "--plan" "./test/data/plan.xml")
          {:keys [exit-message ok?]} (validate-args args)]
      (is (not ok?))
      (is (not (nil? exit-message)))
      ))

  (testing "action=estimate and valid files"
    (let [args '("estimate" "--evidence" "./test/data/evidence.xml" "--plan" "./test/data/plan.xml")
          {:keys [action exit-message]} (validate-args args)]
      (is (nil? exit-message))
      (is (= "estimate" action))
      ))

  (testing "action=estimate, valid files, and valid workday"
    (let [args '("estimate" "--evidence" "./test/data/evidence.xml" "--plan" "./test/data/plan.xml" "--workday" "PT6H")
          {:keys [action exit-message options]} (validate-args args)]
      (is (nil? exit-message))
      (is (= "estimate" action))
      (is (= (Duration/ofHours 6) (get options :workday)))
      ))

  (testing "action=estimate and invalid evidence"
    (let [args '("estimate" "--evidence" "./test/data/nofile" "--plan" "./test/data/plan.xml")
          {:keys [exit-message ok?]} (validate-args args)]
      (is (not ok?))
      (is (re-find #"Cannot read given evidence file" exit-message))
      ))

  (testing "action=estimate and invalid plan"
    (let [args '("estimate" "--evidence" "./test/data/evidence.xml" "--plan" "./test/data/nofile")
          {:keys [exit-message ok?]} (validate-args args)]
      (is (not ok?))
      (is (re-find #"Cannot read given plan file" exit-message))
      ))
  )