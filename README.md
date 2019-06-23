# HttpGauge
## Introduction
A simple command line tool that allows measuring response times and error codes for a given url with a defined frequency.
It takes naive approach of counting time difference between timestamps before sending request and after receiving response.

It doesn't take into account few important factors including:
* time added for time measurement
* time it takes to build `send request`
* time to process `response request`

## How to run it

### Prerequisites
Maven
Java 12 - to use lower version change configuration in pom.xml

```   <properties>
        <maven.compiler.source>12</maven.compiler.source>
        <maven.compiler.target>12</maven.compiler.target>
    </properties>
```

### Execute

`./run.sh`

```
Optional parameters
    -h  Show help text
    -p  Polling time frequency in seconds
    -r  HTTP Requests timeout in seconds
    -u  Target URL
```

## Results

Results are printed on the console and stored in `results.csv` file

## TODO
* Tests...
* Storing results in memory for some time
* Global stats e.g. Mean time, Min and Max value, 95th and 99th percentile, number of passed and failed request
* Presenting results on a chart
