package com.ibm.graph.client;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a path in a graph, including the vertices and edges that make up the path.
 */
public class Path extends Element {

    private String[][] labels;
    private Entity[] objects;

    public Path(String[][] labels, Entity[] objects) throws Exception {
        this.labels = labels;
        this.objects = objects;
        this.put("labels", this.labels);
        this.put("objects", this.objects);
    }

    public String[][] getLabels() {
        return labels;
    }

    public Entity[] getObjects() {
        return objects;
    }

    public static Path fromJSONObject(JSONObject json) throws Exception {
        JSONArray labelArray = json.getJSONArray("labels");
        List<String[]> labels = new ArrayList<String[]>();
        if (labelArray.length() > 0) {
            for (int i = 0 ; i < labelArray.length(); i++) {
                JSONArray innerLabelArray = labelArray.getJSONArray(i);
                List<String> innerLabels = new ArrayList<String>();
                if (innerLabelArray.length() > 0) {
                    for (int j = 0 ; j < innerLabelArray.length(); i++) {
                        innerLabels.add(innerLabelArray.getString(i));
                    }
                }
                labels.add(innerLabels.toArray(new String[0]));
            }
        }
        JSONArray objectArray = json.getJSONArray("objects");
        List<Entity> objects = new ArrayList<Entity>();
        if (objectArray.length() > 0) {
            for (int i = 0 ; i < objectArray.length(); i++) {
                objects.add(Entity.fromJSONObject(objectArray.getJSONObject(i)));
            }
        }
        return new Path(labels.toArray(new String[0][]), objects.toArray(new Entity[0]));
    }

}
