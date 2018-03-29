package edu.osu.urban_security.security_app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import edu.osu.urban_security.security_app.models.Globals;


public class OutgoingCallDetector {

    private SharedPreferences sharedPreferences;

    public static class OutgoingCallReceiver extends BroadcastReceiver {

        Globals g = Globals.getInstance();

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("CALL RECCEIVED", number);
            g.pushSOS();
        }
    }

    private Context ctx;
    private OutgoingCallReceiver outgoingCallReceiver;

    public OutgoingCallDetector(Context ctx) {
        this.ctx = ctx;
        outgoingCallReceiver = new OutgoingCallReceiver();
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        ctx.registerReceiver(outgoingCallReceiver, intentFilter);
        Log.d("CALL DETECTOR STARTED", "blank");
    }

    public void stop(){
        ctx.unregisterReceiver(outgoingCallReceiver);
        Log.d("CALL DETECTOR STOPPED", "blank");
    }
}
