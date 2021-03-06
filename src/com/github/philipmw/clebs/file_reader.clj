(ns com.github.philipmw.clebs.file-reader
  (:require [clojure.xml])
  (:import [java.io FileInputStream]
           [java.time Duration LocalDate]
           [java.time.format DateTimeFormatter]
           ))

; For now, we use the same function for both evidence and plan.
; It works as long as the two files don't give the same name to different shapes.
(defn- xml-task-map-to-native
  [task-map]
  (let [k (get task-map :tag)
        v (first (get task-map :content))]
    (cond (= :estDate k) [k (LocalDate/from (.parse DateTimeFormatter/ISO_LOCAL_DATE v))]
          (= :estDur k) [k (Duration/parse v)]
          (= :actualDur k) [k (Duration/parse v)]
          true [k v])))

(defn- xml-task-to-native
  "Convert XML `task` element to native data structure"
  [xml-task]
  (into {}
        (map xml-task-map-to-native (get xml-task :content))))

(defn- get-evidence-tasks
  "Verify top-level XML structure and return the set of evidence tasks"
  [whole-xml]
  (if (= :evidence (get whole-xml :tag))
    (get whole-xml :content)
    (throw (IllegalArgumentException. "expected top-level XML element to be `evidence`"))))

(defn- get-plan-tasks
  "Verify top-level XML structure and return the set of plan tasks"
  [whole-xml]
  (if (= :plan (get whole-xml :tag))
    (get whole-xml :content)
    (throw (IllegalArgumentException. "expected top-level XML element to be `plan`"))))

(defn- read-clebs-format-version
  "Read clebs file format version of the evidence or plan file"
  [whole-xml]
  (let [format (get (get whole-xml :attrs) :clebs-format)]
    (if (nil? format)
      (throw (IllegalArgumentException. "expected top-level XML element to have `clebs-format` attribute"))
      format)))

(defn- read-evidence-v1
  "Read in v1 format of evidence file"
  [whole-xml]
  (let [tasks-xml (get-evidence-tasks whole-xml)]
    (map xml-task-to-native tasks-xml)))

(defn read-evidence
  "Read in the evidence file"
  [evidence-filename]
  (let [whole-xml (clojure.xml/parse (FileInputStream. evidence-filename))]
    (if (= "1" (read-clebs-format-version whole-xml))
      (read-evidence-v1 whole-xml)
      (throw (IllegalArgumentException. "evidence file supports only clebs-format=1")))))

(defn- read-plan-v1
  "Read in v1 format of plan file"
  [whole-xml]
  (let [tasks-xml (get-plan-tasks whole-xml)]
    (map xml-task-to-native tasks-xml)))

(defn read-plan
  "Read in the plan file"
  [plan-filename]
  (let [whole-xml (clojure.xml/parse (FileInputStream. plan-filename))]
    (if (= "1" (read-clebs-format-version whole-xml))
      (read-plan-v1 whole-xml)
      (throw (IllegalArgumentException. "plan file supports only clebs-format=1")))))
