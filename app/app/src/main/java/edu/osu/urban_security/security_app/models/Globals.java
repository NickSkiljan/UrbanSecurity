package edu.osu.urban_security.security_app.models;

import android.util.Log;

/**
 * Created by sunnypatel on 3/27/18.
 */

public class Globals {
    private static Globals instance;

    public User user;
    private static final String TAG = "GLOBALS";

    /**
     * Singleton instance instantiation
     * @return instance of globals.
     */
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    /*
     * TODO: Encryption should happen here
     */
    private String encryptString(String input){
        String output = input;
        return output;
    }
    /**
     * Pushes time stamp to sos
     * Updates user location
     */
    public void pushSOS(){
        Log.d(TAG, "pushing SOS");
    }

    /**
     * Restrict the constructor from being instantiated
     */
    private Globals(){}
}
