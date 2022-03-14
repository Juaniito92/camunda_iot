package com.estimote.proximity;

import android.app.Application;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    // TODO: Create EstimoteCloudCredentials global object with parameters provided by Estimote Cloud portal
    public EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials("camunda-iot-06e", "0cde12bfdb74d25c5bb590470a49a215");

    // TODO: Set Camunda properties
    public static final String CAMUNDA_REST_API_HOST = "http://192.168.1.40:8080/engine-rest/";
    public static final String CAMUNDA_PROCESS_DEFINITION_KEY = "medical_admission";
    public static final String CAMUNDA_PATIENT_PERSONAL_ID_VARIABLE_NAME = "patientPersonalId";
    public static final String[] CAMUNDA_SIGNALS = {"accessEvaluation_signal", "accessObservation_signal"};
}
