# clebs #

**clebs** is a project estimator based on Joel Spolsky's [*Evidence-Based Scheduling*](https://www.joelonsoftware.com/2007/10/26/evidence-based-scheduling/).
This README and project assume you've read that article and have become familiar with the concepts and terms.

The "cl" in **clebs** stands for [command-line](https://en.wikipedia.org/wiki/Command-line_interface),
or [Clojure](https://clojure.org), or both.

## Usage

This software runs on the command line.

The main benefit of the command line over a single-page application is giving
you greater control and flexibility of your data.  You own the data files, and
you can track multiple projects in multiple files and easily try "what-if" scenarios.

Follow these steps to start using **clebs**:

### 1. download _clebs_

Download the JAR from GitHub, or build it yourself using instructions below.

### 2. create the evidence

The *evidence* file has your past estimates and actual durations for tasks.
The estimates and actual durations are expressed as
[ISO 8601 durations](https://en.wikipedia.org/wiki/ISO_8601#Durations),
and the dates of estimation are expressed as
[ISO 8601 dates](https://en.wikipedia.org/wiki/ISO_8601#Dates).

See a sample evidence file at `./test/data/evidence.xml`.

### 3. create the plan

The *plan* file has the critical path for your project.

For now, **clebs** assumes you'll execute exactly one task at a time from your plan until
the project is finished.  Thus, the task order and dependencies do not matter.

The plan's estimates are in ISO 8601 durations, just like the evidence estimates.

See a sample plan file at `./test/data/plan.xml`.

### 4. estimate

    $ java -jar clebs.jar \
        estimate \
        --workday PT8H \
        --evidence evidence.xml \
        --plan project.xml

Sample output:

    You estimated your project to take 9 workdays (PT73H)
    Simulating 10000 executions of your project...
    p5 execution: 8 workdays (PT61H)
    p50 execution: 10 workdays (PT77H)
    p95 execution: 14 workdays (PT109H)

This means:

> You estimated the project to take 9 workdays.
> Given your past, you may be right.
> In the best case (5th percentile), it may actually take you only 8 workdays.
> In the average case (50th percentile), it'll take you 10 workdays.
> In the worst case (95th percentile), it'll take as long as 14 workdays.

The _workday_ is a customizable duration, 8 hours in this example, to help make sense
of the total duration in the context of normal office work.  **clebs** internally deals
with durations that are not aware of when you are sleeping versus working on the project.
Unless your project involves non-stop work, always set the _workday_ value.

## Tracking your time

If you track your time precisely, in hours or minutes, here are some tools to consider.
All these tools do not have a monthly subscription.

* iOS/ iPadOS: [MultiTimer](https://apps.apple.com/us/app/multitimer-multiple-timers/id973421278)
* macOS: [Caato Time Tracker](http://www.caato.de/en/products/time-tracker-for-mac.html) (I have not personally tried it.)
* CLI: [wtime](http://wtime.sourceforge.net/)

## Comparison to OmniPlan

[OmniPlan](https://www.omnigroup.com/omniplan) is a commercial project management tool.
With the _Pro_ license, it offers to
[use simulations to estimate project completion](https://support.omnigroup.com/documentation/omniplan/mac/4.2.2/en/gantt-view/#using-simulations-to-estimate-milestone-completion-pro).

On the whole, **OmniPlan** is a much more comprehensive and user-friendly tool than **clebs**.
But **clebs**' strength is using _evidence_ to simulate a range of task durations,
whereas **OmniPlan** uses a fixed range such as 50% -- 150%.

Using _evidence_ from developers allows for more accurate project completion estimates than
using a fixed range.  See the
[*Evidence-Based Scheduling*](https://www.joelonsoftware.com/2007/10/26/evidence-based-scheduling/)
article for the rationale.

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
