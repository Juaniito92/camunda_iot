package com.estimote.proximity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import model.SharedPreferencesKeys;

public abstract class AbstractActivity extends AppCompatActivity {

    // TODO: Object to store preferences
    public SharedPreferences sharedPreferences;
    protected TextView firstName, lastName, personalId, phone, nextCamundaSignal;
    protected ActivityResultLauncher<Intent> activityResultLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "-- Enter to " + this.getClass().getSimpleName());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        onCreateContinuation();
    }

    protected abstract void onCreateContinuation();

    protected void bindLayoutElements(Integer firstNameId, Integer lastNameId, Integer personalIdId, Integer phoneId, Integer nextCamundaSignalId) {
        firstName = findViewById(firstNameId);
        lastName = findViewById(lastNameId);
        personalId = findViewById(personalIdId);
        phone = findViewById(phoneId);
        if(nextCamundaSignalId != null){
            nextCamundaSignal = findViewById(nextCamundaSignalId);
        }
    }

    public void setPreferenceValues() {
        firstName.setText(sharedPreferences.getString(SharedPreferencesKeys.FIRSTNAME.name(), ""));
        lastName.setText(sharedPreferences.getString(SharedPreferencesKeys.LASTNAME.name(), ""));
        personalId.setText(sharedPreferences.getString(SharedPreferencesKeys.PERSONAL_ID.name(), ""));
        phone.setText(sharedPreferences.getString(SharedPreferencesKeys.PHONE.name(), ""));
        if(nextCamundaSignal != null){
            nextCamundaSignal.setText(sharedPreferences.getString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), ""));
        }
    }
}
