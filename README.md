# Experimental Java Client for IBM Graph

This is an Experimental Java library for working with IBM Graph.

[![Build Status](https://travis-ci.org/ibm-cds-labs/java-graph.svg?branch=master)](https://travis-ci.org/ibm-cds-labs/java-graph)

Use with caution!

Currently the library supports:
 
 - Executing Gremlin queries
 - Adding/switching/deleting graphs
 - Adding/getting/updating/deleting vertices
 - Adding/getting/updating/deleting edges
 - Creating/loading/updating schema
 - Deleting indexes
 - Data loading from GraphSON
 - Data loading from GraphML

# How to build 

To build and unit test the library

```
$ git clone https://github.com/ibm-cds-labs/java-graph.git
$ cd java-graph
$ cf create-service "IBM Graph" Standard ibm-graph-test
$ cf create-service-key ibm-graph-sample Credentials-1
$ cf service-key ibm-graph-test Credentials-1
$ export TEST_API_URL=<apiURL from Credentials-1>
$ export TEST_USERNAME=<username from Credentials-1>
$ export TEST_PASSWORD=<password from Credentials-1>
$ mvn clean package
```

> Source code and javadoc are located in the `target` directory.

To build the library and skip the JUNIT test

```
$ git clone https://github.com/ibm-cds-labs/java-graph.git
$ cd java-graph
$ mvn clean package -Dmaven.test.skip=true
```

To build and install the library in your local Maven repository 

```
$ git clone https://github.com/ibm-cds-labs/java-graph.git
$ cd java-graph
$ mvn clean install -Dmaven.test.skip=true -Dgpg.skip=true
```

# How to test 

To run the JUnit tests

```
$ git clone https://github.com/ibm-cds-labs/java-graph.git
$ cd java-graph
$ cf create-service "IBM Graph" Standard ibm-graph-test
$ cf create-service-key ibm-graph-sample Credentials-1
$ cf service-key ibm-graph-test Credentials-1
$ export TEST_API_URL=<apiURL from Credentials-1>
$ export TEST_USERNAME=<username from Credentials-1>
$ export TEST_PASSWORD=<password from Credentials-1>
$ mvn clean test
```
> Edit `/src/test/resources/simplelogger.properties` and follow the instructions to enable DEBUG output.

# How to consume

Add the following dependency to your application's `pom.xml`:

```
   <dependency>
      <groupId>com.ibm.graph</groupId>
      <artifactId>graphclient</artifactId>
      <version>0.1.0</version>
    </dependency> 
```    

> Use [this link](http://search.maven.org/#search|ga|1|g%3A%22com.ibm.graph%22) to identify the latest library version. 

The following example illustrates how to create a graph and connect to it. Refer to the [Sample application](https://github.com/ibm-cds-labs/hello-graph-java) or the [Javadoc](http://search.maven.org/remotecontent?filepath=com/ibm/graph/graphclient/0.1.0/graphclient-0.1.0-javadoc.jar) for details on how to manipulate and traverse a graph using this library.


```
// connect to IBM Graph service
IBMGraphClient graphClient = new IBMGraphClient(apiURL, username, password);
// create a new graph
String graphId = graphClient.createGraph();
// switch to new graph
graphClient.setGraph(graphId);
```
