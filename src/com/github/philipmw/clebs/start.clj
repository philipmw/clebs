(ns com.github.philipmw.clebs.start
  (:require [clojure.string :as string]
            [clojure.java.io :refer [file]]
            [clojure.tools.cli :refer [parse-opts]]
            [com.github.philipmw.clebs.ebs :refer [run-ebs]])
  (:import [java.time Duration])
  (:gen-class))

(def cli-options
  [
   [nil "--evidence <filename>" "Evidence file"
    :parse-fn #(file %1)
    :validate [#(.canRead %1) "Cannot read given evidence file"]]

   [nil "--plan <filename>" "Plan file"
    :parse-fn #(file %1)
    :validate [#(.canRead %1) "Cannot read given plan file"]
    ]

   [nil "--workday <duration>" "Workday duration"
    :parse-fn #(Duration/parse %1)
    ]

   ["-h" "--help"]])

(defn- usage [options-summary]
  (->> ["== CL Evidence-Based Scheduling =="
        ""
        "Usage: clebs action [options]"
        ""
        "Actions:"
        "  simulate   Simulate the project with the given evidence"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn- error-msg [errors]
  (str "The following errors occurred in parsing command-line arguments:\n\n"
       (string/join \newline errors)))

(defn validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors)}

      (and (contains? options :evidence)
           (contains? options :plan)
           (= 1 (count arguments)))
      {:action (first arguments) :options options}

      :else
      {:exit-message (usage summary)}
      )))

(defn- exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (if (= "simulate" action)
        (run-ebs options)
        (throw (IllegalArgumentException. "Action is not recognized.")))
      )))
