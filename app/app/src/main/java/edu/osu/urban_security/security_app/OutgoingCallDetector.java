package edu.osu.urban_security.security_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;


public class OutgoingCallDetector {
    public static final String MY_PREF = "MY_PREF";
    public static final String NUMBER_KEY = "NUMBER_KEY";

    private SharedPreferences sharedPreferences;

    public class OutgoingCallReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            sharedPreferences = ctx.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NUMBER_KEY, number);
            Log.d("CALL RECCEIVED", number);
            editor.commit();
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
