package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by eunhye on 2015-10-27.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
       /* Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);*/
    }
}
