(ns com.github.philipmw.clebs.file-reader
  (:require [clojure.xml])
  (:import [java.io FileInputStream]
           [java.time LocalDate]
           [java.time.format DateTimeFormatter]
           ))

(defn- xml-task-map-to-native
  [task-map]
  (let [k (get task-map :tag)
        v (first (get task-map :content))]
    (cond (= :date k) [k (LocalDate/from (.parse DateTimeFormatter/ISO_LOCAL_DATE v))]
          (= :actualTime k) [k (Double/parseDouble v)]
          (= :estimatedTime k) [k (Double/parseDouble v)]
          true [k v])))

(defn- xml-task-to-native
  "Convert XML `task` element to native data structure"
  [xml-task]
  (into {}
        (map xml-task-map-to-native (get xml-task :content))))

(defn- get-evidence-tasks
  "Verify top-level XML structure and return the set of evidence tasks"
  [whole-xml]
  ;(println "Whole XML:" whole-xml)
  (if (= :evidence (get whole-xml :tag))
    (get whole-xml :content)
    (throw (Error. "expected top-level XML element to be 'evidence'"))))

(defn- get-plan-tasks
  "Verify top-level XML structure and return the set of plan tasks"
  [whole-xml]
  ;(println "Whole XML:" whole-xml)
  (if (= :plan (get whole-xml :tag))
    (get whole-xml :content)
    (throw (Error. "expected top-level XML element to be 'evidence'"))))

(defn read-evidence
  "Read in the evidence file"
  [evidence-filename]
  (let [whole-xml (clojure.xml/parse (FileInputStream. evidence-filename))
        tasks-xml (get-evidence-tasks whole-xml)]
    ;(println "Tasks XML:" tasks-xml)
    (map xml-task-to-native tasks-xml)))

(defn read-plan
  "Read in the plan file"
  [plan-filename]
  (let [whole-xml (clojure.xml/parse (FileInputStream. plan-filename))
        tasks-xml (get-plan-tasks whole-xml)]
    ;(println "Tasks XML:" tasks-xml)
    (map xml-task-to-native tasks-xml)))
