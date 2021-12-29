package com.estimote.proximity.estimote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.estimote.proximity.MyApplication;
import com.estimote.proximity.UserInfoActivity;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import services.CamundaService;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ProximityContentManager {

    private final Context context;
    private ProximityContentAdapter proximityContentAdapter;
    private EstimoteCloudCredentials cloudCredentials;
    private CamundaService camundaService;

    private ProximityObserver.Handler proximityObserverHandler;
    // TODO: Add a property to hold the Proximity Observer
    private ProximityObserver proximityObserver;

    public ProximityContentManager(UserInfoActivity activity) {
        this.context = activity.getApplicationContext();
        this.proximityContentAdapter = activity.proximityContentAdapter;
        this.cloudCredentials = ((MyApplication) activity.getApplication()).cloudCredentials;
        this.camundaService = new CamundaService(activity);
    }

    public void start(SharedPreferences sharedPreferences) {

            // TODO: Create the Proximity Observer
            proximityObserver = new ProximityObserverBuilder(context, cloudCredentials)
                    .onError(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {
                            Log.e("app", "proximity observer error: " + throwable);
                            return null;
                        }
                    })
                    .withBalancedPowerMode()
                    .build();

            //TODO: Create the Proximity Zone
            ProximityZone zone = new ProximityZoneBuilder()
                    .forTag("camunda-iot-06e")
                    .inCustomRange(3.0)
                    .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                        @Override
                        public Unit invoke(Set<? extends ProximityZoneContext> contexts) {

                            List<ProximityContent> nearbyContent = new ArrayList<>(contexts.size());

                            for (ProximityZoneContext proximityContext : contexts) {

                                camundaService.checkExecutionIdAndThrowSignalEvent(sharedPreferences);


//                                String title = proximityContext.getAttachments().get("camunda-iot-06e/title");
//                                if (title == null) {
//                                    title = "unknown";
//                                }
//                                String subtitle = Utils.getShortIdentifier(proximityContext.getDeviceId());
//                                Log.d("app", "Context changed: Title = " + title + ", Subtitle = " + subtitle);
//
//                                nearbyContent.add(new ProximityContent(title, subtitle));
                            }

                            proximityContentAdapter.setNearbyContent(nearbyContent);
                            proximityContentAdapter.notifyDataSetChanged();

                            return null;
                        }
                    })
                    .build();

            proximityObserverHandler = proximityObserver.startObserving(zone);
    }

    public void stop() {
        proximityObserverHandler.stop();
    }

}
