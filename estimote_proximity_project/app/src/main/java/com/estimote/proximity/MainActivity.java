package com.estimote.proximity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import model.SharedPreferencesKeys;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreateContinuation() {
        setContentView(R.layout.activity_main);
        bindLayoutElements(R.id.editTextFirstName, R.id.editTextLastName, R.id.editTextPersonalId, R.id.editTextPhone, null);
        setPreferenceValues();

        if(!sharedPreferences.getAll().isEmpty() && getIntent().getAction() != "EDIT"){
            navigateToUserInfoActivity();
        }
    }

    public void onClickListenerButtonSave(View view) {
        String etFirstName = firstName.getText().toString().trim();
        String etLastName = lastName.getText().toString().trim();
        String etPersonalId = personalId.getText().toString().trim();
        String etPhone = phone.getText().toString().trim();
        if (!etFirstName.isEmpty() && !etLastName.isEmpty() && !etPersonalId.isEmpty() && !etPhone.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SharedPreferencesKeys.FIRSTNAME.name(), etFirstName);
            editor.putString(SharedPreferencesKeys.LASTNAME.name(), etLastName);
            editor.putString(SharedPreferencesKeys.PERSONAL_ID.name(), etPersonalId);
            editor.putString(SharedPreferencesKeys.PHONE.name(), etPhone);
            editor.putString(SharedPreferencesKeys.NEXT_CAMUNDA_SIGNAL.name(), MyApplication.CAMUNDA_SIGNALS[0]);
            editor.apply();
            Log.i(this.getClass().getSimpleName(), "SHARED PREFERENCES SAVED:\n"
                    + sharedPreferences.getString(SharedPreferencesKeys.FIRSTNAME.name(), "") + "\n"
                    + sharedPreferences.getString(SharedPreferencesKeys.LASTNAME.name(), "") + "\n"
                    + sharedPreferences.getString(SharedPreferencesKeys.PERSONAL_ID.name(), "") + "\n"
                    + sharedPreferences.getString(SharedPreferencesKeys.PHONE.name(), ""));
            navigateToUserInfoActivity();
        } else {
            Toast.makeText(MainActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUserInfoActivity() {
        if(getIntent().getAction() == "EDIT"){
            finish();
        } else {
            Intent userInfoActivityIntent = new Intent(this, UserInfoActivity.class);
            userInfoActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userInfoActivityIntent);
        }
    }

}
