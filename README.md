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
The estimates and actual times are in unitless real numbers;
these may be your hours, days, or weeks; you decide, just be consistent.

See a sample evidence file at `./test/data/evidence.xml`.

### 3. create the plan

The *plan* file has the critical path for your project.

For now, **clebs** assumes you'll execute exactly one task at a time from your plan until
the project is finished.  Thus, the task order and dependencies do not matter.

The plan's estimated times are in unitless real numbers, just like the evidence times.

See a sample plan file at `./test/data/plan.xml`.

### 4. estimate

    $ java -jar clebs.jar \
        --evidence evidence.xml \
        --plan project.xml

Sample output:

    You estimated your project to take 2.0 units of time.
    Simulating 10000 executions of your plan...
    Fastest execution: 2.0
    p50 execution: 2.5
    p90 execution: 3.0
    p99 execution: 3.0
    Slowest execution: 3.0

One possible interpretation of this output:

> You estimated that your project will take two weeks.
> Given your history (evidence), you may be right; in the best case it'll take you two weeks.
> But in the average case, it'll take you two-and-a-half weeks,
> and in the worst case, it'll take you three weeks.

If any of this does not make sense, remember that this README assumes you've read
through Joel Spolsky's [*Evidence-Based Scheduling*](https://www.joelonsoftware.com/2007/10/26/evidence-based-scheduling/).

## Feedback, contributions

I hope you find this project useful.

Please let me know if you use this project.

I welcome your ideas or code for making it better.

## Development

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
