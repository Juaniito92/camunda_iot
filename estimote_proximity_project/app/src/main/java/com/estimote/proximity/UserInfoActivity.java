package com.estimote.proximity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity.estimote.ProximityContentAdapter;
import com.estimote.proximity.estimote.ProximityContentManager;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import services.VolleyService;

public class UserInfoActivity extends AbstractActivity {

    public VolleyService volleyService;
    public ProximityContentManager proximityContentManager;
    public ProximityContentAdapter proximityContentAdapter;

    @Override
    protected void onCreateContinuation() {
        setContentView(R.layout.activity_user_info);
        bindLayoutElements(R.id.textViewValueFirstName, R.id.textViewValueLastName, R.id.textViewValuePersonalId, R.id.textViewValuePhone);

        volleyService = new VolleyService(this);
        proximityContentAdapter = new ProximityContentAdapter(this);
        requestAndroidPermissions();
    }

    private void requestAndroidPermissions() {
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(this,
                // onRequirementsFulfilled
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        Log.d(this.getClass().getSimpleName(), "requirements fulfilled");
                        startProximityContentManager();
                        return null;
                    }
                },
                // onRequirementsMissing
                new Function1<List<? extends Requirement>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends Requirement> requirements) {
                        Log.e(this.getClass().getSimpleName(), "requirements missing: " + requirements);
                        return null;
                    }
                },
                // onError
                new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Log.e(this.getClass().getSimpleName(), "requirements error: " + throwable);
                        return null;
                    }
                });
    }

    private void startProximityContentManager() {
        proximityContentManager = new ProximityContentManager(this);
        proximityContentManager.start(sharedPreferences);
    }

    public void onClickListenerButtonEdit(View view){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra(EXTRA_ACTION, "EDIT");
        startActivity(mainActivityIntent);
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