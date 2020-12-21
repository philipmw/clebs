(defproject clebs "0.1.0-SNAPSHOT"
  :description "A project estimator based on Joel Spolsky's \"Evidence Based Scheduling\""
  :url "https://github.com/philipmw/clebs"
  :license {:name "MIT"
            :url "https://spdx.org/licenses/MIT.html"}
  :dependencies [[org.clojure/clojure "1.10.1"],
                 [org.clojure/tools.cli "1.0.194"]
                 ]
  :main com.github.philipmw.clebs.start
  :aot [com.github.philipmw.clebs.start]
  :jvm-opts ["--illegal-access=deny"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["--illegal-access=deny" "-Dclojure.compiler.direct-linking=true"]}})
