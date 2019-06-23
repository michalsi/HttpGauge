#!/usr/bin/env bash
export PROJECT_VERSION=1.0

export URL="https://jakarta.apache.org"
export POLLING_TIME_SECONDS=60
export REQUEST_TIMEOUT_SECONDS=30

usage="$(basename "$0") [-h] [-d] [-t]
Script builds and executes HttpGauge program
Optional parameters to pass
    -h  Show this help text
    -p  Polling time frequency in seconds
    -r  HTTP Requests timeout in seconds
    -u  Target URL
    "

while getopts "h p: r: u:" opt; do
    if [[ ! "$OPTARG" =~ ^- ]];then
        case ${opt} in
            h)
                echo "$usage"
                exit 1
                ;;
            p)
                POLLING_TIME_SECONDS=$OPTARG
                ;;
            r)
                REQUEST_TIMEOUT_SECONDS=$OPTARG
                ;;
            u)
                URL=$OPTARG
                ;;
            \?)
                echo "Invalid option -$OPTARG" >&2
                ;;
            :)
                echo "Option -$OPTARG requires an argument." >&2
                exit 1
        esac
    else
            echo "Provided option is not right:" "$OPTARG"
    fi
done

echo "===================================================="
echo "Execution parameters:"
echo "URL: ${URL}"
echo "POLLING_TIME_SECONDS: ${POLLING_TIME_SECONDS}"
echo "REQUEST_TIMEOUT_SECONDS: ${REQUEST_TIMEOUT_SECONDS}"
echo "===================================================="

mvn package

java -jar ./target/HttpGauge-${PROJECT_VERSION}.jar