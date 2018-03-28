package edu.osu.urban_security.security_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class CallDetectionService extends Service{

    private OutgoingCallDetector outgoingCallDetector;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        outgoingCallDetector = new OutgoingCallDetector(this);
        int r = super.onStartCommand(intent, flags, startId);
        outgoingCallDetector.start();
        return r;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        outgoingCallDetector.stop();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
