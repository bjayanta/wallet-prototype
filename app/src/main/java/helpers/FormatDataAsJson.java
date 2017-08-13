package helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jayanta on 7/30/2017.
 */

public class FormatDataAsJson {

    JSONObject root = new JSONObject();
    HashMap<String, String> rootMap = new HashMap<String, String>();

    String [] fields;
    String [] inputs;

    /**
     *
     * @param keys
     * @param values
     */
    public FormatDataAsJson(String[] keys, String[] values) {
        this.fields = keys;
        this.inputs = values;
    }

    public FormatDataAsJson(HashMap<String, String> dataMap) {
        rootMap = dataMap;
    }

    /**
     *
     * @return
     */
    public String format() {
        for(int i = 0;i < fields.length; i++) {
            try {
                root.put(fields[i], inputs[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return root.toString();
    }

    /**
     *
     * @return
     */
    public String jsonMap() {
        root = new JSONObject(rootMap);
        return root.toString();
    }

}
