# Experimental Java Client for IBM Graph

This is an Experimental Java library for working with IBM Graph.

[![Build Status](https://travis-ci.org/ibm-cds-labs/java-graph.svg?branch=master)](https://travis-ci.org/ibm-cds-labs/java-graph)

Use with caution!

Currently the library supports:
 
 - Executing Gremlin queries
 - Adding/switching/deleting graphs
 - Adding/updating/deleting vertices
 - Adding/updating/deleting edges
 - Creating/loading/updating schema
 - Deleting indexes

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
$ mvn clean install
```

> Source code and javadoc are located in the `target` directory.

To build the library and skip the JUNIT test

```
$ git clone https://github.com/ibm-cds-labs/java-graph.git
$ cd java-graph
$ mvn clean install -Dmaven.test.skip=true
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

```
import com.ibm.graph.client.*;
import com.ibm.graph.client.schema.*;

String apiURL = "https://ibmgraph-alpha.ng.bluemix.net/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/g";
String username = "";
String password = "";
IBMGraphClient graphClient = new IBMGraphClient(apiURL, username, password);

Schema schema = schema = new Schema(
    new PropertyKey[]{
        new PropertyKey("name", "String", "SINGLE")
    },
    new VertexLabel[]{
        new VertexLabel("person")
    },
    new EdgeLabel[]{
        new EdgeLabel("friend")
    },
    new VertexIndex[]{
        new VertexIndex("vertexByName", new String[]{"name"}, true, true)
    },
    new EdgeIndex[]{}
);
schema = graphClient.saveSchema(schema);

Vertex vertex = new Vertex("person", new HashMap() {{
    put("name", "John");
}});
vertex = graphClient.addVertex(vertex);

Edge edge = new Edge("friend", outVertex.getId(), inVertex.getId(), new HashMap() {{
    put("date", d);
}});
edge = graphClient.addEdge(edge);
```

Work in progress!
