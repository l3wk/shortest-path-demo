# Shortest Path Demo
A simple application demonstrating how to calculate the shortest path between two nodes in a graph.

## Overview
This demo application uses Dijkstra's algorithm to search a social network of software developers for the shortest path containing the strongest developers that connects the selected source and target.

## Requirements
- Java 1.8
- Maven 3

## How To Run

Running the tests:

```bash
mvn test
```

Running the demo:

```bash
mvn -q exec:java -Dexec.args="--sourceId=<source_id> --targetId=<target_id> --dataFile=<data_file>"
```

Where:
- *source_id* is an integer representing the user in the graph from which to start the search.
- *target_id* is an integer representing the user in the graph which is the target of the search.
- *data_file* is a json file containing the records which represent the social network graph.

> Note: The first time a command is executed, maven will download any dependencies required, resulting in slightly longer execution times than usual. Omitting the -q flag will enable verbose maven log output, which will provide status information regarding the progress of the downloads.