(ns com.github.philipmw.clebs.ebs
  (:require [com.github.philipmw.clebs.file-reader :refer [read-evidence read-plan]])
  (:import [java.time Duration]
           [java.util Random]))

(def ^:const NUM_SIMULATED_EXECUTIONS 10000)

(defn velocities
  "Compute velocities of evidence tasks"
  [evidence-tasks]
  (let [actualDurSecs #(.toSeconds (get %1 :actualDur))
        estDurSecs #(.toSeconds (get %1 :estDur))]
    (map #(/ (estDurSecs %1) (actualDurSecs %1)) evidence-tasks)))

(defn plan-sum
  "Compute sum of time estimated for planned tasks"
  [plan-tasks]
  (reduce #(.plus %1 (get %2 :estDur)) Duration/ZERO plan-tasks))

(defn- select-random
  "Select a random item from the given vector"
  [rng vec]
  (let [idx (.nextInt rng (count vec))]
    (vec idx)))

(defn simulated-execution
  "Generate a simulated execution of the plan, using a random selection of velocities"
  [rng velocities plan-tasks]
  (let [estimates (map #(get %1 :estDur) plan-tasks)
        velo-vec (vec velocities)]                          ; vector for constant-time random access
    ; ideally we'd use `Duration/dividedBy`, but that takes only long, not double.
    ; We need double so that Duration can increase.
    (map #(Duration/ofSeconds (/ (.toSeconds %1) (select-random rng velo-vec))) estimates)))

(defn- sumDur [xs] (reduce #(.plus %1 %2) xs))

(defn n-estimates
  "Generate N simulated estimates of plan completion"
  [rng velocities plan-tasks n]
  (map (fn [x] (sumDur (simulated-execution rng velocities plan-tasks))) (range n)))

(defn pN
  "Find the Nth percentile"
  [vec n]
  ; https://en.wikipedia.org/wiki/Percentile#The_nearest-rank_method
  (let [ordinal-rank (int (Math/ceil (* (count vec) (/ n 100))))
        idx (- ordinal-rank 1)]
    (vec idx)))

(defn friendly-dur-gen
  "Duration in a human-friendly format"
  [workdayDur]
  (if (nil? workdayDur)
    (fn [dur]
      ; Why not days? Because days are too easy to confuse with workdays.
      (format "%d hours" (.toHours dur)))
    (fn [dur]
      ; For workdays, I divide manually instead of using `.dividedBy` because
      ; `.dividedBy` rounds down, whereas I want to round up to be conservative.
      (format "%.0f workdays (%s)" (Math/ceil (/ (.getSeconds dur) (.getSeconds workdayDur))) dur))))

(defn run-ebs [options]
  (let [{:keys [evidence plan workday]} options
        rng (Random.)
        friendly-dur (friendly-dur-gen workday)]
    (let [evidence-tasks (read-evidence evidence)
          plan-tasks (read-plan plan)]
      (println "You estimated your project to take" (friendly-dur (plan-sum plan-tasks)))
      (println "Simulating" NUM_SIMULATED_EXECUTIONS "executions of your project...")
      (let [velocities (velocities evidence-tasks)
            sorted-est-vec (vec (sort (n-estimates rng velocities plan-tasks NUM_SIMULATED_EXECUTIONS)))]
        (println "p5 execution:" (friendly-dur (pN sorted-est-vec 5)))
        (println "p50 execution:" (friendly-dur (pN sorted-est-vec 50)))
        (println "p95 execution:" (friendly-dur (pN sorted-est-vec 95)))
        ))))
