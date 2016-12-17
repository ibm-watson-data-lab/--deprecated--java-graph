package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphClientException;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.ArrayList;

/**
 * Describes a path in a graph, including the vertices and edges that make up the path.
 */
public class Path extends Element {

    private String[][] labels;
    private Object[] objects;

    private Path(String[][] labels, Object[] objects) throws Exception {
        this.labels = labels;
        this.objects = objects;
        this.put("labels", this.labels);
        this.put("objects", this.objects);
    }

    /**
     * Returns the labels for the objects in this path.
     * Example: [["a","b"],[],[]] is returned if the first object in the path 
     * was labeled "a" and "b" and no other objects were labeled.
     * @return String[][] labels for the objects in this path
     */
    public String[][] getLabels() {
        return this.labels;
    }

    /**
     * Returns the objects in this path. An object is of type com.ibm.graph.client.Vertex,
     * com.ibm.graph.client.Edge or one of the supported primitive data types: String, Integer,
     * Boolean, Float
     * 
     * @return Object[] objects in this path
     */
    public Object[] getObjects() {
        return this.objects;
    }

    /**
     * Returns Entity.EntityType.Vertex (for vertices), Entity.EntityType.Edge (for edges) or 
     * Entity.EntityType.Unknown (primitive types, such as String, Boolean, ...) for each object
     * in this path.
     * @return Entity.EntityType[] list of entity types in this path
     */
    public Entity.EntityType[] getObjectTypes() {
        Entity.EntityType[] ets = new Entity.EntityType[this.objects.length];
        for(int i = 0; i < this.objects.length; i++) {
            if(this.objects[i] instanceof Entity)
                ets[i] = ((Entity)this.objects[i]).getType();
            else
                ets[i] = Entity.EntityType.Unknown;
        }
        return ets;
    }

    /**
     * Returns the number of objects in this path.
     * @return int number of objects in this path
     */
    public int getSize() {
        return this.objects.length;
    }

    /**
     * Creates a path object from JSON, if json is not null
     * @param json JSON representation of a path, not null
     * @return a path
     * @throws IllegalArgumentException json is null
     * @throws GraphClientException if an error occurred during the deserialization process
     */
    public static Path fromJSONObject(JSONObject json) throws IllegalArgumentException, GraphClientException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter \"json\" cannot be null");
        if(!json.has("labels")) 
            throw new IllegalArgumentException("Parameter \"json\" does not define property \"labels\"");
        if(!json.has("objects")) 
            throw new IllegalArgumentException("Parameter \"json\" does not define property \"objects\"");
        try {
            /*
               "labels": [
                  ["label", ...],
                  ...
                  ["label", ...]
                ]
            */
            JSONArray labelArray = json.getJSONArray("labels");
            ArrayList<String[]> labels = new ArrayList<String[]>();
            if (labelArray.length() > 0) {
                JSONArray innerLabelArray = null;
                String[] innerLabels = null;
                for (int i = 0 ; i < labelArray.length(); i++) {
                    innerLabelArray = labelArray.getJSONArray(i);
                    innerLabels = new String[innerLabelArray.length()];
                    for (int j = 0 ; j < innerLabelArray.length(); j++) {
                        innerLabels[j] = innerLabelArray.getString(j);
                    }
                    labels.add(innerLabels);
                }
            }

            JSONArray objectArray = json.getJSONArray("objects");
            ArrayList<Object> objects = new ArrayList<Object>();
            Entity e = null;
            Object o = null;
            for (int i = 0 ; i < objectArray.length(); i++) {
                o = objectArray.get(i);
                if(o instanceof JSONObject)
                    objects.add(Entity.fromJSONObject((JSONObject)o));
                else 
                    objects.add(String.valueOf(o));   // // e should be a primitive type (String, Boolean, Integer, Float)         
            }
            return new Path(labels.toArray(new String[0][]), objects.toArray(new Object[0]));
        }
        catch(Exception ex) {
            throw new GraphClientException("Error deserializing Path object. ", ex);
        }
    }

}
