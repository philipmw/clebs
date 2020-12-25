(ns com.github.philipmw.clebs.file-reader-test
  (:import [java.time Duration LocalDate])
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.file-reader :refer :all]))

(deftest read-evidence-test
  (testing "good evidence"
    (let [test-input-filename "./test/data/evidence.xml"
          expected-evidence `({:name "estimated two days, took two days"
                               :estDate ~(LocalDate/of 2020 12 23)
                               :estDur ~(Duration/ofDays 2)
                               :actualDur ~(Duration/ofDays 2)}
                              {:name "estimated two days, took three days"
                               :estDate ~(LocalDate/of 2020 12 23)
                               :estDur ~(Duration/ofDays 2)
                               :actualDur ~(Duration/ofDays 3)}
                              {:name "estimated 12 hours, took 10 hours"
                               :estDate ~(LocalDate/of 2020 12 23)
                               :estDur ~(Duration/ofHours 12)
                               :actualDur ~(Duration/ofHours 10)}
                              )]
      (is (= expected-evidence (read-evidence test-input-filename))))
    )

  (testing "evidence with wrong clebs-format"
    (let [test-input-filename "./test/data/evidence-wrong-version.xml"]
      (is (thrown-with-msg? IllegalArgumentException #"file supports only clebs-format=1"
                            (read-evidence test-input-filename))))
    )

  (testing "non-evidence file"
    (let [test-input-filename "./test/data/plan.xml"]
      (is (thrown-with-msg? IllegalArgumentException #"expected top-level XML element to be `evidence`"
                            (read-evidence test-input-filename))))
    )
  )

(deftest read-plan-test
  (testing "good plan"
    (let [test-input-filename "./test/data/plan.xml"
          expected-plan `({:name "hour-long task", :estDur ~(Duration/ofHours 1)}
                          {:name "day-long task", :estDur ~(Duration/ofDays 1)}
                          {:name "two-day-long task", :estDur ~(Duration/ofDays 2)})]
      (is (= expected-plan (read-plan test-input-filename))))
    )

  (testing "plan with no clebs-format"
    (let [test-input-filename "./test/data/plan-no-version.xml"]
      (is (thrown-with-msg? IllegalArgumentException #"expected top-level XML element to have `clebs-format` attribute"
                            (read-plan test-input-filename))))
    )

  (testing "non-plan file"
    (let [test-input-filename "./test/data/evidence.xml"]
      (is (thrown-with-msg? IllegalArgumentException #"expected top-level XML element to be `plan`"
                            (read-plan test-input-filename))))
    )
  )
