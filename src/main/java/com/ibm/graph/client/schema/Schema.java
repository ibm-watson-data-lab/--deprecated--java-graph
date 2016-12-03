package com.ibm.graph.client.schema;

import com.ibm.graph.client.Entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by markwatson on 11/15/16.
 */
public class Schema extends JSONObject {

    private static Logger logger =  LoggerFactory.getLogger(Schema.class);

    private PropertyKey[] propertyKeys = {};
    private VertexLabel[] vertexLabels = {};
    private EdgeLabel[] edgeLabels = {};
    private VertexIndex[] vertexIndexes = {};
    private EdgeIndex[] edgeIndexes = {};
    
    /**
     * Constructor.
     */
    public Schema() throws Exception {
        this.init();
    }

    /**
     * Constructor.
     */
    public Schema(PropertyKey[] propertyKeys, VertexLabel[] vertexLabels, EdgeLabel[] edgeLabels, VertexIndex[] vertexIndexes, EdgeIndex[] edgeIndexes) throws Exception {
        if(propertyKeys != null)
            this.propertyKeys = propertyKeys;
        if(vertexLabels != null)
            this.vertexLabels = vertexLabels;
        if(edgeLabels != null)
            this.edgeLabels = edgeLabels;
        if(vertexIndexes != null)
            this.vertexIndexes = vertexIndexes;
        if(edgeIndexes != null)
            this.edgeIndexes = edgeIndexes;
        this.init();
    }

    public boolean definesPropertyKeys() {
        return this.propertyKeys.length > 0;
    }

    public PropertyKey[] getPropertyKeys() {
        return this.propertyKeys;
    }

    public boolean definesVertexLabels() {
        return this.vertexLabels.length > 0;
    }

    public VertexLabel[] getVertexLabels() {
        return this.vertexLabels;
    }

    public boolean definesEdgeLabels() {
        return this.edgeLabels.length > 0;
    }

    public EdgeLabel[] getEdgeLabels() {
        return this.edgeLabels;
    }

    public boolean definesVertexIndexes() {
        return this.vertexIndexes.length > 0;
    }

    public VertexIndex[] getVertexIndexes() {
        return this.vertexIndexes;
    }

    public boolean definesEdgeIndexes() {
        return this.edgeIndexes.length > 0;
    }

    public EdgeIndex[] getEdgeIndexes() {
        return this.edgeIndexes;
    }

    private void init() throws Exception {
        JSONArray propertyKeysArray = new JSONArray();
        if (this.propertyKeys != null && this.propertyKeys.length > 0) {
            for (PropertyKey propertyKey : this.propertyKeys) {
                propertyKeysArray.add(propertyKey);
            }
        }
        JSONArray vertexLabelsArray = new JSONArray();
        if (this.vertexLabels != null && this.vertexLabels.length > 0) {
            for (VertexLabel vertexLabel : this.vertexLabels) {
                vertexLabelsArray.add(vertexLabel);
            }
        }
        JSONArray edgeLabelsArray = new JSONArray();
        if (this.edgeLabels != null && this.edgeLabels.length > 0) {
            for (EdgeLabel edgeLabel : this.edgeLabels) {
                edgeLabelsArray.add(edgeLabel);
            }
        }
        JSONArray vertexIndexesArray = new JSONArray();
        if (this.vertexIndexes != null && this.vertexIndexes.length > 0) {
            for (VertexIndex vertexIndex : this.vertexIndexes) {
                vertexIndexesArray.add(vertexIndex);
            }
        }
        JSONArray edgeIndexesArray = new JSONArray();
        if (this.edgeIndexes != null && this.edgeIndexes.length > 0) {
            for (EdgeIndex edgeIndex : this.edgeIndexes) {
                edgeIndexesArray.add(edgeIndex);
            }
        }
        this.put("propertyKeys", propertyKeysArray);
        this.put("vertexLabels", vertexLabelsArray);
        this.put("edgeLabels", edgeLabelsArray);
        this.put("vertexIndexes", vertexIndexesArray);
        this.put("edgeIndexes", edgeIndexesArray);
    }

    /**
     * Instantiates a Schema object based on json's properties.
     * Required properties: 
     * Optional properties: propertyKeys, vertexLabels, edgeLabels, vertexIndexes, edgeIndexes
     * @param json defines the properties of the Schema object
     * @return Schema a Schema object
     * @throws Exception if an error was encountered
     */
    public static Schema fromJSONObject(JSONObject json) throws Exception {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");

        if(logger.isDebugEnabled())
            logger.trace(" fromJSONObject input: " + json.toString());

        PropertyKey[] propertyKeys = {};
        if((json.has("propertyKeys")) && (json.optJSONArray("propertyKeys") != null)) 
            propertyKeys = populateArray(json, "propertyKeys", (jsonObject) -> PropertyKey.fromJSONObject(jsonObject)).toArray(new PropertyKey[0]);

        VertexLabel[] vertexLabels = {};
        if((json.has("vertexLabels")) && (json.optJSONArray("vertexLabels") != null)) 
            vertexLabels = populateArray(json, "vertexLabels", (jsonObject) -> VertexLabel.fromJSONObject(jsonObject)).toArray(new VertexLabel[0]);

        EdgeLabel[] edgeLabels = {};
        if((json.has("edgeLabels")) && (json.optJSONArray("edgeLabels") != null)) 
            edgeLabels = populateArray(json, "edgeLabels", (jsonObject) -> EdgeLabel.fromJSONObject(jsonObject)).toArray(new EdgeLabel[0]);
        
        VertexIndex[] vertexIndexes = {};
        if((json.has("vertexIndexes")) && (json.optJSONArray("vertexIndexes") != null)) 
            vertexIndexes = populateArray(json, "vertexIndexes", (jsonObject) -> VertexIndex.fromJSONObject(jsonObject)).toArray(new VertexIndex[0]);
        
        EdgeIndex[] edgeIndexes = {};
        if((json.has("edgeIndexes")) && (json.optJSONArray("edgeIndexes") != null)) 
            edgeIndexes = populateArray(json, "edgeIndexes", (jsonObject) -> EdgeIndex.fromJSONObject(jsonObject)).toArray(new EdgeIndex[0]);
        
        Schema schema = new Schema(propertyKeys, vertexLabels, edgeLabels, vertexIndexes, edgeIndexes);
        if(logger.isDebugEnabled())
            logger.trace(" fromJSONObject output: " + schema.toString());
        return schema;
    }

    private static <E> List<E> populateArray(JSONObject jsonObject, String propertyName, JSONParser<E> jsonParser) throws Exception {
        ArrayList<E> list = new ArrayList<E>();
        JSONArray jsonArray = jsonObject.getJSONArray(propertyName);
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i=0; i<jsonArray.length(); i++) {
                list.add(jsonParser.fromJsonObject(jsonArray.getJSONObject(i)));
            }
        }
        return list;
    }

    private interface JSONParser<E> {
        E fromJsonObject(JSONObject jsonObject) throws Exception;
    }

}
