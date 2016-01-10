package fr.ig2i.wifind.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by Thomas on 09/01/2016.
 */
public class WiFindApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        WiFindApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return WiFindApplication.context;
    }

}
