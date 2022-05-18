package services;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.estimote.proximity.MyApplication;
import com.estimote.proximity.UserInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.SharedPreferencesKeys;
import model.volley.MyJsonArrayRequest;

public class CamundaService {

    private final UserInfoActivity userInfoActivity;

    public CamundaService(UserInfoActivity userInfoActivity){
        super();
        this.userInfoActivity = userInfoActivity;
    }

    public void checkExecutionIdAndThrowSignalEvent() {
        // Camunda list executions request (https://docs.camunda.org/manual/7.14/reference/rest/execution/post-query/)
        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                MyApplication.CAMUNDA_REST_API_HOST + "execution",
                getExecutionBodyRequest(
                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.PERSONAL_ID.name(), ""),
                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), "")
                ),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Get execution id
                        try {
                            if(response.length() > 0){
                                String executionId = response.getJSONObject(0).getString("id");
                                Log.i(this.getClass().getSimpleName(), String.format("Execution id found: %s", executionId));
                                throwSignalEvent(executionId);
                            }
                        } catch (JSONException e) {
                            Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getSimpleName(), error.getLocalizedMessage(), error);
                        Log.e(
                                this.getClass().getSimpleName(),
                                String.format("Doesn't exist any process instance with %s = %s and signal = %s",
                                        MyApplication.CAMUNDA_PATIENT_PERSONAL_ID_VARIABLE_NAME,
                                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.PERSONAL_ID.name(), ""),
                                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), ""))
                        );
                    }
                }
        );
        // Add the request to the RequestQueue
        userInfoActivity.volleyService.addToRequestQueue(request);
    }

    private JSONObject getExecutionBodyRequest(String patientPersonalId, String camundaSignal) {
        Map<String, Object> jsonBodyMap = new HashMap<>();
        jsonBodyMap.put("processDefinitionKey", MyApplication.CAMUNDA_PROCESS_DEFINITION_KEY);
        Map<String, String> processVariablesMap = new HashMap<>();
        processVariablesMap.put("name", MyApplication.CAMUNDA_PATIENT_PERSONAL_ID_VARIABLE_NAME);
        processVariablesMap.put("operator", "eq");
        processVariablesMap.put("value", patientPersonalId);
        List<Map<String, String>> processVariablesList = new ArrayList<>();
        processVariablesList.add(processVariablesMap);
        jsonBodyMap.put("processVariables", processVariablesList);
        jsonBodyMap.put("signalEventSubscriptionName", camundaSignal);
        return new JSONObject(jsonBodyMap);
    }

    private void throwSignalEvent(String executionId) {
        // Camunda throw signal event request (https://docs.camunda.org/manual/7.14/reference/rest/signal/post-signal/)
        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                MyApplication.CAMUNDA_REST_API_HOST + "signal",
                throwSignalEventBodyRequest(
                        executionId,
                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), "")
                ),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Not content from request
                        Log.i(
                                this.getClass().getSimpleName(),
                                String.format("Camunda signal event %s for executionId %s",
                                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), ""),
                                        executionId)
                        );
                        setNextCamundaSignalOnSharedPreferences();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getSimpleName(), error.getLocalizedMessage(), error);
                        Log.e(
                                this.getClass().getSimpleName(),
                                String.format("Can not throw signal event for executionId = %s and signal = %s",
                                        executionId,
                                        userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), "")
                                )
                        );
                    }
                }
        );
        // Add the request to the RequestQueue
        userInfoActivity.volleyService.addToRequestQueue(request);
    }

    private JSONObject throwSignalEventBodyRequest(String executionId, String camundaSignal) {
        Map<String, Object> jsonBodyMap = new HashMap<>();
        jsonBodyMap.put("name", camundaSignal);
        jsonBodyMap.put("executionId", executionId);
        return new JSONObject(jsonBodyMap);
    }

    private void setNextCamundaSignalOnSharedPreferences() {
        SharedPreferences.Editor editor = userInfoActivity.sharedPreferences.edit();
        int nextCamundaSignalIndex = Arrays.asList(MyApplication.CAMUNDA_SIGNALS).indexOf(userInfoActivity.sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), "")) + 1;
        editor.putString(
                SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(),
                MyApplication.CAMUNDA_SIGNALS.length > nextCamundaSignalIndex ? MyApplication.CAMUNDA_SIGNALS[nextCamundaSignalIndex] : MyApplication.CAMUNDA_SIGNALS[0]
        );
        editor.apply();
        userInfoActivity.setPreferenceValues();
    }
}
