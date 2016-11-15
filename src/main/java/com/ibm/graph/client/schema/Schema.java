package com.ibm.graph.client.schema;

import com.ibm.graph.client.Entity;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markwatson on 11/15/16.
 */
public class Schema extends JSONObject {

    private PropertyKey[] propertyKeys;
    private VertexLabel[] vertexLabels;
    private EdgeLabel[] edgeLabels;
    private VertexIndex[] vertexIndexes;
    private EdgeIndex[] edgeIndexes;
    
    public Schema(PropertyKey[] propertyKeys, VertexLabel[] vertexLabels, EdgeLabel[] edgeLabels, VertexIndex[] vertexIndexes, EdgeIndex[] edgeIndexes) throws Exception {
        this.propertyKeys = propertyKeys;
        this.vertexLabels = vertexLabels;
        this.edgeLabels = edgeLabels;
        this.vertexIndexes = vertexIndexes;
        this.edgeIndexes = edgeIndexes;
        this.init();
    }

    public PropertyKey[] getPropertyKeys() {
        return propertyKeys;
    }

    public VertexLabel[] getVertexLabels() {
        return vertexLabels;
    }

    public EdgeLabel[] getEdgeLabels() {
        return edgeLabels;
    }

    public VertexIndex[] getVertexIndexes() {
        return vertexIndexes;
    }

    public EdgeIndex[] getEdgeIndexes() {
        return edgeIndexes;
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

    public static Schema fromJSONObject(JSONObject json) throws Exception {
        PropertyKey[] propertyKeys = populateArray(json, "propertyKeys", (jsonObject) -> PropertyKey.fromJSONObject(jsonObject)).toArray(new PropertyKey[0]);
        VertexLabel[] vertexLabels = populateArray(json, "vertexLabels", (jsonObject) -> VertexLabel.fromJSONObject(jsonObject)).toArray(new VertexLabel[0]);
        EdgeLabel[] edgeLabels = populateArray(json, "edgeLabels", (jsonObject) -> EdgeLabel.fromJSONObject(jsonObject)).toArray(new EdgeLabel[0]);
        VertexIndex[] vertexIndexes = populateArray(json, "vertexIndexes", (jsonObject) -> VertexIndex.fromJSONObject(jsonObject)).toArray(new VertexIndex[0]);
        EdgeIndex[] edgeIndexes = populateArray(json, "edgeIndexes", (jsonObject) -> EdgeIndex.fromJSONObject(jsonObject)).toArray(new EdgeIndex[0]);
        return new Schema(propertyKeys, vertexLabels, edgeLabels, vertexIndexes, edgeIndexes);
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
