# clebs #

**clebs** is a project estimator based on Joel Spolsky's [*Evidence-Based Scheduling*](https://www.joelonsoftware.com/2007/10/26/evidence-based-scheduling/).
This README and project assume you've read that article and have become familiar with the concepts and terms.

The "cl" in **clebs** stands for command-line, or Clojure, or both.

## Usage

This software runs on the command line.

The main benefit of the command line over a single-page application is giving
you greater control and flexibility of your data.  You own the data files, and
you can track multiple projects in multiple files and easily try "what-if" scenarios.

Follow these steps to start using **clebs**:

### 1. download _clebs_

Download the JAR from GitHub, or build it yourself using instructions below.

### 2. create the evidence

The *evidence* file has your past estimates and actual times for tasks.
The estimates are expressed as [ISO 8601 durations](https://en.wikipedia.org/wiki/ISO_8601#Durations),
and actual times are calculated from your start and finish timestamps.
The start and finish timestamps are expressed as
[ISO 8601 dates](https://en.wikipedia.org/wiki/ISO_8601#Dates),
and, optionally,
[times](https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations).

See a sample evidence file at `./test/data/evidence.xml`.

### 3. create the plan

The *plan* file has the critical path for your project.

For now, **clebs** assumes you'll execute exactly one task at a time from your plan until
the project is finished.  Thus, the task order and dependencies do not matter.

The plan's estimates are in ISO 8601 durations, just like the evidence estimates.

See a sample plan file at `./test/data/plan.xml`.

### 4. estimate

    $ java -jar clebs.jar \
        simulate \
        --evidence evidence.xml \
        --plan project.xml

Sample output:

    You estimated your project to take 3 days, 1 hours
    Simulating 10000 executions of your project...
    p5 execution: 2 days, 13 hours
    p50 execution: 3 days, 5 hours
    p95 execution: 4 days, 13 hours

If any of this does not make sense, please re-read Joel Spolsky's
[*Evidence-Based Scheduling*](https://www.joelonsoftware.com/2007/10/26/evidence-based-scheduling/).

## Feedback, contributions

I hope you find this project useful.

Please let me know if you use this project.

I welcome your ideas or code for making it better.

## Development

![Continuous Integration status](https://github.com/philipmw/clebs/workflows/Clojure%20CI/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/philipmw/clebs/badge.svg?branch=main)](https://coveralls.io/github/philipmw/clebs?branch=main)

I develop this software on macOS, using [Intellij IDEA](https://www.jetbrains.com/idea/)
and [Cursive](https://cursive-ide.com/) IntelliJ plugin.

Initial setup:

    $ brew install leiningen

Development:

    $ lein test

    $ lein run

    $ lein run simulate --evidence ./test/data/evidence.xml --plan ./test/data/plan.xml

    $ lein cloverage

Release:

    $ lein uberjar
