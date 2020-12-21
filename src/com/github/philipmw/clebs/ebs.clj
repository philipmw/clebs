(ns com.github.philipmw.clebs.ebs
  (:require [com.github.philipmw.clebs.file-reader :refer :all])
  (:import (java.util Random)))

(def ^:const NUM_SIMULATED_EXECUTIONS 10000)

(defn velocities
  "Compute velocities of evidence tasks"
  [evidence-tasks]
  (let [taskActualTime #(get %1 :actualTime)
        taskEstimatedTime #(get %1 :estimatedTime)]
    (map #(/ (taskEstimatedTime %1) (taskActualTime %1)) evidence-tasks)))

(defn plan-sum
  "Compute sum of time estimated for planned tasks"
  [plan-tasks]
  (reduce #(+ %1 (get %2 :estimatedTime)) 0 plan-tasks))

(defn- select-random
  "Select a random item from the given vector"
  [rng vec]
  (let [idx (.nextInt rng (count vec))]
    (vec idx)))

(defn simulated-execution
  "Generate a simulated execution of the plan, using a random selection of velocities"
  [rng velocities plan-tasks]
  (let [estimates (map #(get %1 :estimatedTime) plan-tasks)
        velo-vec (vec velocities)]                          ; vector for constant-time random access
    (map #(/ %1 (select-random rng velo-vec)) estimates)))

(defn- sum [xs] (reduce + xs))

(defn n-estimates
  "Generate N simulated estimates of plan completion"
  [rng velocities plan-tasks n]
  (map (fn [x] (sum (simulated-execution rng velocities plan-tasks))) (range n)))

(defn pN
  "Find the Nth percentile"
  [vec n]
  ; https://en.wikipedia.org/wiki/Percentile#The_nearest-rank_method
  (let [ordinal-rank (int (Math/ceil (* (count vec) (/ n 100))))
        idx (- ordinal-rank 1)]
    (vec idx)))

(defn run-ebs [options]
  (let [{:keys [evidence plan]} options
        rng (Random.)]
    (let [evidence-tasks (read-evidence evidence)
          plan-tasks (read-plan plan)]
      (println "You estimated your project to take" (plan-sum plan-tasks) "units of time.")
      (println "Simulating" NUM_SIMULATED_EXECUTIONS "executions of your plan...")
      (let [velocities (velocities evidence-tasks)
            sorted-est-vec (vec (sort (n-estimates rng velocities plan-tasks NUM_SIMULATED_EXECUTIONS)))]
        (println "Fastest execution:" (first sorted-est-vec))
        (println "p50 execution:" (pN sorted-est-vec 50))
        (println "p90 execution:" (pN sorted-est-vec 90))
        (println "p99 execution:" (pN sorted-est-vec 99))
        (println "Slowest execution:" (last sorted-est-vec))
        ))))
