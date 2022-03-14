package com.estimote.proximity;

import android.Manifest;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity.estimote.ProximityContentAdapter;
import com.estimote.proximity.estimote.ProximityContentManager;

import services.VolleyService;

public class UserInfoActivity extends AbstractActivity {

    public VolleyService volleyService;
    public ProximityContentManager proximityContentManager;
    public ProximityContentAdapter proximityContentAdapter;

    @Override
    protected void onCreateContinuation() {
        setContentView(R.layout.activity_user_info);
        bindLayoutElements(R.id.textViewValueFirstName, R.id.textViewValueLastName, R.id.textViewValuePersonalId, R.id.textViewValuePhone, R.id.textViewValueNextCamundaSignal);
        setPreferenceValues();

        Log.i(this.getClass().getSimpleName(), "SHARED PREFERENCES RETRIEVED:\n" + sharedPreferences.getString("firstName", "") + "\n" + sharedPreferences.getString("lastName", "") + "\n" + sharedPreferences.getString("personalId", "") + "\n" + sharedPreferences.getString("phone", ""));

        volleyService = new VolleyService(this);
        proximityContentAdapter = new ProximityContentAdapter(this);
        requestAndroidPermissions();
    }

    private void requestAndroidPermissions() {
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(this,
                // onRequirementsFulfilled
                () -> {
                    Log.d(UserInfoActivity.class.getSimpleName(), "requirements fulfilled");
                    checkBluetoothPermission();
                    return null;
                },
                // onRequirementsMissing
                requirements -> {
                    Log.e(UserInfoActivity.class.getSimpleName(), "requirements missing: " + requirements);
                    return null;
                },
                // onError
                throwable -> {
                    Log.e(UserInfoActivity.class.getSimpleName(), "requirements error: " + throwable);
                    return null;
                });
    }

    private void checkBluetoothPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED){
            startProximityContentManager();
        } else {
            requestPermissions(new String[] { Manifest.permission.BLUETOOTH_SCAN }, PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PackageManager.PERMISSION_GRANTED:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    startProximityContentManager();
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    private void startProximityContentManager() {
        proximityContentManager = new ProximityContentManager(this);
        proximityContentManager.start();
    }

    public void onClickListenerButtonEdit(View view){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setAction("EDIT");
        startActivityForResult(mainActivityIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setPreferenceValues();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (volleyService.getRequestQueue() != null) {
            volleyService.getRequestQueue().cancelAll(VolleyService.VOLLEY_REQUESTS_TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proximityContentManager != null)
            proximityContentManager.stop();
    }
}