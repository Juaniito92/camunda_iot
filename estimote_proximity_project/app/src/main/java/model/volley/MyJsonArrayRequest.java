package model.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

// TODO: Implement a custom Volley request class to send HTTP request with json object body and json array response (https://stackoverflow.com/a/45978831)
public class MyJsonArrayRequest extends JsonRequest<JSONArray> {

    public MyJsonArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        Response<JSONArray> result = Response.success(null, null);
        try {
            if(response != null && response.data.length != 0){
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                result = Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException | JSONException e) {
            result = Response.error(new ParseError(e));
        }
        return result;
    }
}
