(ns com.github.philipmw.clebs.start-test
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.start :refer :all]))

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

  (testing "action=simulate and valid files"
    (let [args '("simulate" "--evidence" "./test/data/evidence.xml" "--plan" "./test/data/plan.xml")
          {:keys [action exit-message]} (validate-args args)]
      (is (nil? exit-message))
      (is (= "simulate" action))
      ))

  (testing "action=simulate and invalid evidence"
    (let [args '("simulate" "--evidence" "./test/data/nofile" "--plan" "./test/data/plan.xml")
          {:keys [exit-message ok?]} (validate-args args)]
      (is (not ok?))
      (is (re-find #"Cannot read given evidence file" exit-message))
      ))

  (testing "action=simulate and invalid plan"
    (let [args '("simulate" "--evidence" "./test/data/evidence.xml" "--plan" "./test/data/nofile")
          {:keys [exit-message ok?]} (validate-args args)]
      (is (not ok?))
      (is (re-find #"Cannot read given plan file" exit-message))
      ))
  )