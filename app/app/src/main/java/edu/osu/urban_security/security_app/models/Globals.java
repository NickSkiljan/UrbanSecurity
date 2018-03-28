package edu.osu.urban_security.security_app.models;

/**
 * Created by sunnypatel on 3/27/18.
 */

public class Globals {
    private static Globals instance;

    public User user;

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

    /**
     * Restrict the constructor from being instantiated
     */
    private Globals(){}
}
