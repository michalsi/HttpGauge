#!/usr/bin/env bash

export URL="https://jakarta.apache.org"
export POLLING_TIME_SECONDS=60
export REQUEST_TIMEOUT_SECONDS=30

mvn package

java -jar ./target/HttpGauge-1.0.jar