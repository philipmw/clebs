(ns com.github.philipmw.clebs.file-reader-test
  (:import [java.time LocalDate])
  (:require [clojure.test :refer :all]
            [com.github.philipmw.clebs.file-reader :refer :all]))

(deftest read-evidence-test
  (testing "good evidence"
    (let [test-input-filename "./test/data/evidence.xml"
          expected-evidence `({:date ~(LocalDate/of 2020 10 24), :name "some name 2", :estimatedTime 1.0, :actualTime 1.0}
                              {:date ~(LocalDate/of 2020 10 24), :name "some name", :estimatedTime 1.0, :actualTime 1.5})]
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
          expected-plan `({:date ~(LocalDate/of 2020 10 24), :name "some name 2", :estimatedTime 1.0}
                          {:date ~(LocalDate/of 2020 10 24), :name "some name", :estimatedTime 1.0})]
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
