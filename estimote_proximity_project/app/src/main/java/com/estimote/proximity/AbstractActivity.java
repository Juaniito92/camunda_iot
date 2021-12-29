package com.estimote.proximity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public abstract class AbstractActivity extends AppCompatActivity {

    public static final String EXTRA_ACTION = "com.estimote.proximity.ProximityContent.ACTION";

    // TODO: Object to store preferences
    protected SharedPreferences sharedPreferences;
    protected TextView firstName, lastName, personalId, phone;
    protected String extraActionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "-- Enter to " + this.getClass().getSimpleName());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        if(intent != null){
            extraActionValue = intent.getStringExtra(EXTRA_ACTION);
        }
        onCreateContinuation();
    }

    protected abstract void onCreateContinuation();

    protected void bindLayoutElements(int firstNameId, int lastNameId, int personalIdId, int phoneId) {
        firstName = findViewById(firstNameId);
        lastName = findViewById(lastNameId);
        personalId = findViewById(personalIdId);
        phone = findViewById(phoneId);
        // If exists any previous values, they will be placed into each EditText
        setPreferenceValues();
    }

    private void setPreferenceValues() {
        firstName.setText(sharedPreferences.getString("firstName", ""));
        lastName.setText(sharedPreferences.getString("lastName", ""));
        personalId.setText(sharedPreferences.getString("personalId", ""));
        phone.setText(sharedPreferences.getString("phone", ""));
    }
}
