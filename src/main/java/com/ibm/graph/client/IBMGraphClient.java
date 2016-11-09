package com.ibm.graph.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

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
    private String basicAuthHeader;
    private String gdsTokenAuth;

    public IBMGraphClient() throws Exception {
        Map envs = System.getenv();
        if (envs.get("VCAP_SERVICES") != null) {
            String graphServiceName = "IBM Graph";
            JSONObject vcapSvcs = new JSONObject(envs.get("VCAP_SERVICES").toString());
            if (!vcapSvcs.isNull(graphServiceName)) {
                JSONObject creds = vcapSvcs.getJSONArray(graphServiceName).getJSONObject(0).getJSONObject("credentials");
                this.apiURL = creds.getString("apiURL");
                this.username = creds.getString("username");
                this.password = creds.getString("password");
                this.init();
            }
        }
    }

    public IBMGraphClient(String apiURL, String username, String password) {
        this.apiURL = apiURL;
        this.username = username;
        this.password = password;
        this.init();
    }

    private void init() {
        this.basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes());
        this.baseURL = this.apiURL.substring(0, this.apiURL.length() - 2);
    }

    private void initSession() throws Exception {
        if (this.baseURL == null) {
            throw new RuntimeException("Invalid configuration. Please specify a valid apiURL, username, and password.");
        }
        // Get Session Token
        HttpGet httpGet = new HttpGet(this.baseURL + "/_session");
        httpGet.setHeader("Authorization", this.basicAuthHeader);
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
        finally {
            try {
                httpResponse.close();
            }
            catch(IOException ioe) {}
        }
    }

    public Vertex addVertex(Vertex vertex) throws Exception {
        String url = this.apiURL + "/vertices";
        JSONObject jsonContent = this.postJSONObjectToUrl(new JSONObject(vertex), url);
        JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
        if (data.length() > 0) {
            return Vertex.fromJSONObject(data.getJSONObject(0));
        }
        return null;
    }

    public Edge addEdge(Edge edge) throws Exception {
        String url = this.apiURL + "/edges";
        JSONObject jsonContent = this.postJSONObjectToUrl(new JSONObject(edge), url);
        JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
        if (data.length() > 0) {
            return Edge.fromJSONObject(data.getJSONObject(0));
        }
        return null;
    }

    public Element[] runGremlinQuery(String query) throws Exception {
        if (this.gdsTokenAuth == null) {
            this.initSession();
        }
        String url = this.apiURL + "/gremlin";
        JSONObject postData = new JSONObject();
        postData.put("gremlin", String.format("def g = graph.traversal(); %s",query));
        JSONObject jsonContent = this.postJSONObjectToUrl(postData, url);
        JSONArray data = jsonContent.getJSONObject("result").getJSONArray("data");
        List<Element> elements = new ArrayList<Element>();
        if (data.length() > 0) {
            for (int i = 0 ; i < data.length(); i++) {
                elements.add(Element.fromJSONObject(data.getJSONObject(i)));
            }
        }
        return elements.toArray(new Element[0]);
    }

    private JSONObject postJSONObjectToUrl(JSONObject json, String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", this.gdsTokenAuth);
        httpPost.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            return new JSONObject(content);
        }
        finally {
            httpResponse.close();
        }
    }
}