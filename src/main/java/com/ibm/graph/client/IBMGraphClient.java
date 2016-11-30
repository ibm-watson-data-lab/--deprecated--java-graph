package com.ibm.graph.client;

import com.ibm.graph.GraphException;
import com.ibm.graph.client.GraphClientException;
import com.ibm.graph.client.schema.Schema;
import com.ibm.graph.ResultSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by markwatson on 11/8/16.
 */
public class IBMGraphClient {

    private String apiURL;
    private String username;
    private String password;
    private String baseURL;
    private String gdsTokenAuth;
    private String graphId;

    private static Logger logger =  LoggerFactory.getLogger(IBMGraphClient.class);

    /**
     * Constructor. Creates a IBMGraphClient that provides access to the IBM Graph service
     * instance identified by the first IBM Graph entry in the VCAP_SERVICES environment
     * variable.
     *
     * @throws GraphClientException if VCAP_SERVICES is not defined or not valid
     */
    public IBMGraphClient() throws GraphClientException {
        Map envs = System.getenv();
        if (envs.get("VCAP_SERVICES") != null) {
            String graphServiceName = "IBM Graph";
            try {
                JSONObject vcapSvcs = new JSONObject(envs.get("VCAP_SERVICES").toString());
                if (!vcapSvcs.isNull(graphServiceName)) {
                    JSONObject creds = vcapSvcs.getJSONArray(graphServiceName).getJSONObject(0).getJSONObject("credentials");
                    this.apiURL = creds.getString("apiURL");
                    this.username = creds.getString("username");
                    this.password = creds.getString("password");
                    this.init();
                }
            }
            catch(JSONException jsonex) {
                throw new GraphClientException("IBMGraphClient cannot be initialized. Environment variable VCAP_SERVICES is invalid: " + jsonex.getMessage());    
            }
        }
        else {
            throw new GraphClientException("IBMGraphClient cannot be initialized. Environment variable VCAP_SERVICES is not defined.");
        }
    }

    /**
     * Constructor. Creates a IBMGraphClient that provides access to the IBM Graph service
     * using the provided credentials.
     * 
     * @param apiURL IBM Graph service instance API URL
     * @param username IBM Graph instance user name
     * @param password IBM Graph instance password
     * @throws GraphClientException if the credentials are missing or invalid 
     */
    public IBMGraphClient(String apiURL, String username, String password) throws GraphClientException {
        this.apiURL = apiURL;
        this.username = username;
        this.password = password;
        this.init();
    }

    private void init() throws GraphClientException {
        if((this.apiURL == null) || (this.username == null) || (this.password == null)) {
            throw new GraphClientException("IBMGraphClient cannot be initialized. apiURL: " + this.apiURL + " username: " + this.username + " password: " + this.password);
        }
        // TODO error checking
        this.baseURL = this.apiURL.substring(0, this.apiURL.lastIndexOf('/'));
        this.graphId = this.apiURL.substring(this.apiURL.lastIndexOf('/') + 1);
    }

    private void initSession() throws GraphClientException, GraphException {
        if (this.baseURL == null) {
            throw new GraphClientException("Invalid configuration. Please specify a valid apiURL, username, and password.");
        }
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes());
        HttpGet httpGet = new HttpGet(this.baseURL + "/_session");
        httpGet.setHeader("Authorization", basicAuthHeader);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            this.gdsTokenAuth = "gds-token " + jsonContent.getString("gds-token");
        }
        catch(Exception ex) {
            logger.error("IBMGraphClient cannot establish a session with the IBM Graph service instance.", ex);
            throw new GraphClientException("IBMGraphClient cannot establish a session with the IBM Graph service instance: " + ex.getMessage());
        }
        finally {
            try { 
                    if(httpResponse != null) {
                     httpResponse.close();   
                    }
            }
            catch(IOException ioe) {}
        }
    }

    /*
     * ----------------------------------------------------------------
     * Graph methods:
     *  - getGraphId
     *  - setGraph
     *  - getGraphs
     *  - createGraph
     *  - deleteGraph
     * ----------------------------------------------------------------     
     */

    /**
     * Returns the name of the graph that this client is connected to.
     * @return id of the graph that this client is connected to.
     */
    public String getGraphId() {
        return this.graphId;
    }

    /**
     * Switches to the graph identified by graphId.
     * @param graphId name of the graph on which the client will operate on
     * @throws GraphException if no graph with the specified graphId exists
     */
    public void setGraph(String graphId) throws GraphException {

        if(ArrayUtils.contains(getGraphs(), graphId)) {
            this.graphId = graphId;
            this.apiURL = String.format("%s/%s",this.baseURL,this.graphId);
        }
        else {
            throw new GraphException("No graph named " + graphId + " is defined.");
        }
    }

    /**
     * Returns list of graphs that are defined in this IBM Graph service instance
     * @return An array of strings, identifying graphs that can be switched to.
     * @throws GraphException if the graph list cannot be retrieved
     * @see com.ibm.graph.client.IBMGraphClient#setGraph
     */
    public String[] getGraphs() throws GraphException {

        try {
            String url = this.baseURL + "/_graphs";
            JSONObject jsonContent = this.doHttpGet(url);
            JSONArray data = jsonContent.getJSONArray("graphs");
            List<String> graphIds = new ArrayList<>();
            if (data.length() > 0) {
                for(int i=0; i<data.length(); i++) {
                    graphIds.add(data.getString(i));
                }
            }
            return graphIds.toArray(new String[0]);
        }
        catch(Exception ex) {
            logger.error("Error getting list of graphs: ", ex);
            throw new GraphException("Error getting list of graphs: " + ex.getMessage());
        }
    }

    /**
     * Creates a new graph with a unique id.
     * @return The graph id
     * @throws GraphException if the graph cannot be created
     */
    public String createGraph() throws GraphException {
        return this.createGraph(null);
    }

    /**
     * Creates a new graph identified by the specified graphId.
     * @param graphId unique graph id
     * @return The assigned graph id
     * @throws GraphException if the graph cannot be created
     */
    public String createGraph(String graphId) throws GraphException {
        try {
            String url = String.format("%s/_graphs",this.baseURL);
            if (graphId != null && graphId.trim().length() > 0) {
                url += String.format("/%s",graphId.trim());
            }
            JSONObject jsonContent = this.doHttpPost(null, url);
            return jsonContent.getString("graphId");
        }
        catch(Exception ex) {
            logger.error("Error creating graph: ", ex);
            throw new GraphException("Error creating graph: " + ex.getMessage());               
        }
    }

    /**
     * Deletes the graph identified by graphId.
     * @param graphId id of the graph to be deleted
     * @throws GraphException if the specified graph cannot be deleted
     * @throws GraphClientException if the specified graph cannot be deleted because of a configuration issue
     */
    public void deleteGraph(String graphId) throws GraphClientException, GraphException {
        String url = String.format("%s/_graphs/%s",this.baseURL,graphId.trim());
        this.doHttpDelete(url);
    }

    /*
     * ----------------------------------------------------------------
     * Schema methods:
     *  - getSchema
     *  - saveSchema
     * ----------------------------------------------------------------     
     */

    /**
     * Returns the schema for the current graph
     * @return com.ibm.graph.client.schema.Schema object or null
     * @throws GraphException if the schema information could not be retrieved
     */
    public Schema getSchema() throws GraphException {
        try {
            String url = this.apiURL + "/schema";
            JSONObject jsonContent = this.doHttpGet(url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Schema.fromJSONObject(data.getJSONObject(0));
            }
            return null;
        }
        catch(Exception ex) {
            logger.error("Error getting schema: ", ex);
            throw new GraphException("Error getting schema: " + ex.getMessage());  
        }
    }

    /**
     * Updates the schema in the current graph.
     * @param schema the new schema definition
     * @return com.ibm.graph.client.schema.Schema object or null
     * @throws GraphException if the schema could not be saved
     * @see com.ibm.graph.client.schema.Schema
     */
    public Schema saveSchema(Schema schema) throws GraphException {
        if(schema == null)
            return null;
        try {
            String url = this.apiURL + "/schema";
            JSONObject jsonContent = this.doHttpPost(schema, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Schema.fromJSONObject(data.getJSONObject(0));
            }
            else {
                return null;
            }
        }
        catch(Exception ex) {
            logger.error("Error saving schema: ", ex);
            throw new GraphException("Error saving schema: " + ex.getMessage());  
        }        
    }

    /*
     * ----------------------------------------------------------------
     * Index methods:
     *  - deleteIndex
     * ----------------------------------------------------------------     
     */


    /**
     * Deletes an index in the current graph.
     * @param indexName name of an existing index
     * @return true if indexName was removed
     * @throws GraphException if the index could not be deleted 
     */
    public boolean deleteIndex(String indexName) throws GraphException {
        if(indexName == null)
            return false;
        try {
            String url = this.apiURL + "/index/" + indexName;
            JSONObject jsonContent = this.doHttpDelete(url);
            return jsonContent.getJSONObject("result").getJSONArray("data").getBoolean(0);
        }
        catch(Exception ex) {
            logger.error("Error deleting index: ", ex);
            throw new GraphException("Error deleting index: " + ex.getMessage());  
        }                
    }

    /*
     * ----------------------------------------------------------------
     * Vertex methods:
     *  - getVertex
     *  - addVertex
     *  - updateVertex
     *  - deleteVertex
     * ----------------------------------------------------------------     
     */

    /**
     * Gets the vertex identified by id from the current graph.
     * @param id vertex id
     * @return com.ibm.graph.client.Vertex or null if no vertex with the specified id exists
     * @throws GraphException if an error (other than "not found") occurred
     */
    public Vertex getVertex(Object id) throws GraphException {
        if(id == null)
            return null;
        try {
            String url = String.format("%s/vertices/%s",this.apiURL,id);
            JSONObject jsonContent = this.doHttpGet(url);
            if (jsonContent.containsKey("result")) {
                JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
                if (data.length() > 0) {
                    return Vertex.fromJSONObject(data.getJSONObject(0));
                }
                else {
                    return null;
                }
            }
            else if ((jsonContent.containsKey("code") && (jsonContent.getString("code").equalsIgnoreCase("NotFoundError")))) {
                // vertex not found
                return null;
            }
            else {
                throw new GraphException("Error fetching vertex with id " + id + ": " + jsonContent.getString("message"));
            }
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error fetching vertex with id " + id + ": ", ex);
            throw new GraphException("Error fetching vertex with id " + id + ": " + ex.getMessage());  
        } 
    }

    /**
     * Adds a vertex to the current graph.
     * @param vertex the vertex to be added
     * @return Vertex the vertex object, as returned by IBM Graph, or null
     * @throws GraphException if an error occurred
     */
    public Vertex addVertex(Vertex vertex) throws GraphException {
        if(vertex == null)
            return null;
        try {
            String url = this.apiURL + "/vertices";
            JSONObject jsonContent = this.doHttpPost(vertex, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Vertex.fromJSONObject(data.getJSONObject(0));
            }
            else {
                throw new GraphException("Error adding vertex: " + jsonContent.getString("message"));
            }
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error adding vertex: ", ex);
            throw new GraphException("Error adding vertex: " + ex.getMessage());              
        }
    }

    /**
     * Updates a vertex in the current graph.
     * @param vertex the vertex to be updated
     * @return Vertex the vertex object, as returned by IBM Graph, or null
     * @throws GraphException if an error occurred
     */
    public Vertex updateVertex(Vertex vertex) throws GraphException {
       if(vertex == null)
            return null;
        try {
            String url = this.apiURL + "/vertices/" + vertex.getId();
            JSONObject jsonContent = this.doHttpPut(vertex, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Vertex.fromJSONObject(data.getJSONObject(0));
            }
            else {
                throw new GraphException("Error updating vertex: " + jsonContent.getString("message"));
            }
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error updating vertex: ", ex);
            throw new GraphException("Error updating vertex: " + ex.getMessage());              
        }
    }

    /**
     * Removes the vertex identified by id from the current graph.
     * @param id id of the vertex to be removed
     * @return true if the vertex with the specified id was removed
     * @throws GraphException if an error occurred
     */
    public boolean deleteVertex(Object id) throws GraphException {
        if(id == null)
            return false;
        try {
            String url = this.apiURL + "/vertices/" + id;
            JSONObject jsonContent = this.doHttpDelete(url);
            return jsonContent.getJSONObject("result").getJSONArray("data").getBoolean(0);
        }
        catch(Exception ex) {
            logger.error("Error deleting vertex with id " + id + ": ", ex);
            throw new GraphException("Error deleting vertex with id " + id + ": " + ex.getMessage());          
        }        
    }

    /*
     * ----------------------------------------------------------------
     * Edge methods:
     *  - getEdge
     *  - addEdge
     *  - updateEdge
     *  - deleteEdge
     * ----------------------------------------------------------------     
     */

    /**
     * Gets the edge identified by id from the current graph.
     * @param id edge id
     * @return com.ibm.graph.client.Edge or null if no edge with the specified id exists
     * @throws GraphException if an error occurred
     */
    public Edge getEdge(Object id) throws GraphException {
        if(id == null)
            return null;
        try {
            String url = String.format("%s/edges/%s",this.apiURL,id);
            JSONObject jsonContent = this.doHttpGet(url);
            if (jsonContent.containsKey("result")) {
                JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
                if (data.length() > 0) {
                    return Edge.fromJSONObject(data.getJSONObject(0));
                }
                else {
                    return null;
                }
            }
            else {
                if ((jsonContent.containsKey("code") && (jsonContent.getString("code").equalsIgnoreCase("NotFoundError")))) {
                    return null;
                }
                else {
                    throw new GraphException("Error fetching edge with id " + id + ": " + jsonContent.getString("message"));
                }
            }
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error fetching edge with id " + id + ": ", ex);
            throw new GraphException("Error fetching edge with id " + id + ": " + ex.getMessage());  
        } 
    }

    /**
     * Adds an edge to the current graph.
     * @param edge the edge to be added
     * @return Vertex the vertex object, as returned by IBM Graph
     * @throws GraphException if an error occurred
     */
    public Edge addEdge(Edge edge) throws GraphException {
        if(edge == null)
            return null;
        try {
            String url = this.apiURL + "/edges";
            JSONObject jsonContent = this.doHttpPost(edge, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Edge.fromJSONObject(data.getJSONObject(0));
            }
            else {
                throw new GraphException("Error adding edge: " + jsonContent.getString("message"));
            }            
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error adding edge: ", ex);
            throw new GraphException("Error adding edge: " + ex.getMessage());              
        }        
    }

    /**
     * Updates an edge in the current graph.
     * @param edge the edge to be updated
     * @return Edge the edgex object, as returned by IBM Graph, or null
     * @throws GraphException if an error occurred
     */
    public Edge updateEdge(Edge edge) throws GraphException {
       if(edge == null)
            return null;
        try {
            String url = this.apiURL + "/edges/" + edge.getId();
            JSONObject jsonContent = this.doHttpPut(edge, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            if (data.length() > 0) {
                return Edge.fromJSONObject(data.getJSONObject(0));
            }
            else {
                throw new GraphException("Error updating edge: " + jsonContent.getString("message"));
            }
        }
        catch(GraphException gex) {
            throw gex;
        }
        catch(Exception ex) {
            logger.error("Error updating edge: ", ex);
            throw new GraphException("Error updating edge: " + ex.getMessage());              
        }
    }

    /**
     * Removes an edge from the current graph.
     * @param id id of the edge to be removed
     * @return true if the edge with the specified id was removed
     * @throws GraphException if an error occurred
     */
    public boolean deleteEdge(Object id) throws GraphException {
        if(id == null)
            return false;
        try {
            String url = this.apiURL + "/edges/" + id;
            JSONObject jsonContent = this.doHttpDelete(url);
            return jsonContent.getJSONObject("result").getJSONArray("data").getBoolean(0);
        }
        catch(Exception ex) {
            logger.error("Error deleting edge with id " + id + ": ", ex);
            throw new GraphException("Error deleting edge with id " + id + ": " + ex.getMessage());          
        }                
    }

    /*
     * ----------------------------------------------------------------
     * Gremlin methods:
     *  - runGremlinQuery
     * ----------------------------------------------------------------     
     */

    /**
     * Runs the specified Gremlin traversal
     * @param query the traversal to be performed
     * @return Element[] TBD
     * @throws GraphException if an error occurred
     * @deprecated use traverseGraph
     */
    public Element[] runGremlinQuery(String query) throws GraphException {
        if(query == null) {
            Element[] NO_RESULT = {};
            return NO_RESULT;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Running Gremlin Query: " + query);
        }
        try {
            String url = this.apiURL + "/gremlin";
            JSONObject postData = new JSONObject();
            postData.put("gremlin", String.format("def g = graph.traversal(); %s",query));
            JSONObject jsonContent = this.doHttpPost(postData, url);
            JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
            List<Element> elements = new ArrayList<Element>();
            if (data.length() > 0) {
                for (int i = 0 ; i < data.length(); i++) {
                    elements.add(Element.fromJSONObject(data.getJSONObject(i)));
                }
            }
            return elements.toArray(new Element[0]);
        }
        catch(Exception ex) {
            logger.error("Error processing gremlin query " + query + ": ", ex);
            throw new GraphException("Error processing gremlin query " + query + ": " + ex.getMessage());               
        }
    }
    
    /**
     * Runs the specified Gremlin traversal
     * @param query the traversal to be performed
     * @return ResultSet the result of the graph traversal
     * @throws GraphException if an error occurred
     */
    public ResultSet traverseGraph(String query) throws GraphException {
        if(query == null) {
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Running Gremlin Query: " + query);
        }
        try {
            String url = this.apiURL + "/gremlin";
            JSONObject postData = new JSONObject();
            postData.put("gremlin", String.format("def g = graph.traversal(); %s",query));
            JSONObject jsonContent = this.doHttpPost(postData, url);
            return new ResultSet(jsonContent);
        }
        catch(Exception ex) {
            logger.error("Error processing gremlin query " + query + ": ", ex);
            throw new GraphException("Error processing gremlin query " + query + ": " + ex.getMessage());               
        }
    }


    /*
     * ----------------------------------------------------------------
     * HTTP helper methods
     *  - doHttpGet
     *  - doHttpPost
     *  - doHttpPut
     *  - doHttpDelete
     * ----------------------------------------------------------------     
     */

    private JSONObject doHttpGet(String url) throws GraphClientException, GraphException {
        if (this.gdsTokenAuth == null) {
            this.initSession();
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", this.gdsTokenAuth);
        httpGet.setHeader("Accept", "application/json");
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Making HTTP GET request to %s",url));
        }
        return doHttpRequest(httpGet);
    }

    private JSONObject doHttpPost(JSONObject json, String url) throws GraphClientException, GraphException {
        if (this.gdsTokenAuth == null) {
            this.initSession();
        }
        String payload = (json == null ? "" : json.toString());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", this.gdsTokenAuth);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Making HTTP POST request to %s; payload=%s",url,payload));
        }
        return doHttpRequest(httpPost);
    }

    private JSONObject doHttpPut(JSONObject json, String url) throws GraphClientException, GraphException {
        if (this.gdsTokenAuth == null) {
            this.initSession();
        }
        String payload = json.toString();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Authorization", this.gdsTokenAuth);
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setHeader("Accept", "application/json");
        httpPut.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Making HTTP PUT request to %s; payload=%s",url,payload));
        }
        return doHttpRequest(httpPut);
    }

    private JSONObject doHttpDelete(String url) throws GraphClientException, GraphException {
        if (this.gdsTokenAuth == null) {
            this.initSession();
        }
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Authorization", this.gdsTokenAuth);
        httpDelete.setHeader("Accept", "application/json");
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Making HTTP DELETE request to %s",url));
        }
        return doHttpRequest(httpDelete);
    }

    private JSONObject doHttpRequest(HttpUriRequest request) throws GraphException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {           
            httpResponse = httpclient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Response received from %s = %s",request.getURI(),content));
            
            }
            return new JSONObject(content);
        }
        catch(Exception ex) {
            logger.error("Error processing HTTP request ", ex);            
            throw new GraphException("Error processing HTTP request: " + ex.getMessage());
        }
        finally {
            if(httpResponse != null) {
                try {
                    httpResponse.close();
                }
                catch(IOException ioex) {
                    // ignore
                }
            }
                
        }
    }
}