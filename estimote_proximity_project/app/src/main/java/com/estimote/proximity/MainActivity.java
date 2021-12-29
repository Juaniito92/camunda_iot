package com.estimote.proximity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreateContinuation() {
        setContentView(R.layout.activity_main);
        bindLayoutElements(R.id.editTextFirstName, R.id.editTextLastName, R.id.editTextPersonalId, R.id.editTextPhone);

        if(extraActionValue == null && !sharedPreferences.getAll().isEmpty()){
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
            editor.putString("firstName", etFirstName);
            editor.putString("lastName", etLastName);
            editor.putString("personalId", etPersonalId);
            editor.putString("phone", etPhone);
            editor.putString("nextCamundaSignal", MyApplication.CAMUNDA_SIGNALS[0]);
            editor.commit();
            Log.i(this.getClass().getSimpleName(), "SHARED PREFERENCES SAVED:\n" + sharedPreferences.getString("firstName", "") + "\n" + sharedPreferences.getString("lastName", "") + "\n" + sharedPreferences.getString("personalId", "") + "\n" + sharedPreferences.getString("phone", ""));
            navigateToUserInfoActivity();
        } else {
            Toast.makeText(MainActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUserInfoActivity() {
        if(extraActionValue == "EDIT"){
            finish();
        } else {
            Intent userInfoActivityIntent = new Intent(this, UserInfoActivity.class);
            userInfoActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userInfoActivityIntent);
        }
    }

}
