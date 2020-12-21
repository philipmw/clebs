(ns com.github.philipmw.clebs.start
  (:require [clojure.string :as string]
            [clojure.java.io :refer [file]]
            [clojure.tools.cli :refer [parse-opts]]
            [com.github.philipmw.clebs.ebs :refer [run-ebs]]
            )
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
   ["-h" "--help"]])

(defn- usage [options-summary]
  (->> ["== CL Evidence-Based Scheduling =="
        ""
        "Usage: clebs [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn- error-msg [errors]
  (str "The following errors occurred in parsing command-line arguments:\n\n"
       (string/join \newline errors)))

(defn- validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}
      errors
      {:exit-message (error-msg errors)}
      (and (contains? options :evidence)
           (contains? options :plan)
           (= 0 (count arguments)))
      {:options options}
      :else
      {:exit-message (usage summary)}
      )))

(defn- exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (run-ebs options)
      )))
