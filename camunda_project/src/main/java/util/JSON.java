package util;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSON {
    private final static Logger logger = Logger.getLogger(JSON.class.getName());

    private static ObjectMapper OM = new ObjectMapper();

    public static <T> T readValue(String content, Class<T> clazz) {
        T res = null;
        try {
            res = OM.readValue(content, clazz);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return res;
    }

    public static String writeValueAsString(Object o) {
        try {
            return OM.writeValueAsString(o);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static ObjectMapper getOM() {
        return OM;
    }

    /**
     * Given a string, transformed it as JSONObject and return all values string (only if value is a instance of String) separated by space blank
     * 
     * @param imput
     * @return
     */
    public String getAllValuesFromJsonAsString(String imput) {
        JSONObject jsonObject = new JSONObject(imput);
        return handleValue(jsonObject);
    }

    private String handleValue(Object value) {
        if (value instanceof JSONObject) {
            return handleJSONObject((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return handleJSONArray((JSONArray) value);
        } else {
            return (value instanceof String) ? (String) value : "";
        }
    }

    private String handleJSONObject(JSONObject jsonObject) {
        String result = "";
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = jsonObject.get((String) key);
            String tmp = handleValue(value);
            if (StringUtils.isNotBlank(tmp)) {
                result = result + " " + tmp;
            }
        }
        return result;
    }

    private String handleJSONArray(JSONArray jsonArray) {
        String result = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            String tmp = handleValue(jsonArray.getJSONObject(i));
            if (StringUtils.isNotBlank(tmp)) {
                result = result + " " + tmp;
            }
        }
        return result;
    }

}
