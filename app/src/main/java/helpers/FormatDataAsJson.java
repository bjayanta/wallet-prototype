package helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jayanta on 7/30/2017.
 */

public class FormatDataAsJson {

    final JSONObject root = new JSONObject();
    String [] fields;
    String [] inputs;

    public FormatDataAsJson(String[] keys, String[] values) {
        this.fields = keys;
        this.inputs = values;
    }

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

}
