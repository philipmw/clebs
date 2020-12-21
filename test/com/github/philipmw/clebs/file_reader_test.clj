(ns com.github.philipmw.clebs.file-reader-test
  (:import [java.nio.file Paths]
           [java.time LocalDate])
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.file-reader :refer :all]))

(deftest read-evidence-test
  (testing "read-evidence"
    (let [test-input-filename "./test/data/evidence.xml"
          expected-evidence `({:date ~(LocalDate/of 2020 10 24), :name "some name 2", :estimatedTime 1.0, :actualTime 1.0}
                              {:date ~(LocalDate/of 2020 10 24), :name "some name", :estimatedTime 1.0, :actualTime 1.5})]
      (is (= expected-evidence (read-evidence test-input-filename))))
    ))

(deftest read-plan-test
  (testing "read-plan"
    (let [test-input-filename "./test/data/plan.xml"
          expected-plan `({:date ~(LocalDate/of 2020 10 24), :name "some name 2", :estimatedTime 1.0}
                          {:date ~(LocalDate/of 2020 10 24), :name "some name", :estimatedTime 1.0})]
      (is (= expected-plan (read-plan test-input-filename))))
    ))
