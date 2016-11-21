# Experimental Java Client for IBM Graph

This is an Experimental Java library for working with IBM Graph.

Use with caution!

Currently the library supports:
 
 - Running Gremlin queries
 - Adding/updating/deleting vertices
 - Adding/updating/deleting edges
 - Creating/updating schema
 - Deleting indexes

# How to Run

```
import com.ibm.graph.client.*;
import com.ibm.graph.client.schema.*;

String apiURL = "https://ibmgraph-alpha.ng.bluemix.net/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/g";
String username = "";
String password = "";
IBMGraphClient graphClient = new IBMGraphClient(apiURL, username, password)

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
