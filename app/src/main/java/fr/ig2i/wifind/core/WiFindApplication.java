package fr.ig2i.wifind.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by Thomas on 09/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class WiFindApplication extends Application {

    /**
     * Utilisé pour stocké le contexte de l'application
     */
    private static Context context;

    /**
     * Appelé automatiquement lors de la création de l'application, cependant il est impossible de déterminer quand sera créée l'application
     */
    public void onCreate() {
        super.onCreate();
        setContext(getApplicationContext());
    }

    /**
     * Comme on ne sait pas quand sera créée l'application, on peut forcer le contexte avec cette méthode
     * @param ctx Le contexte de l'application
     */
    public static void setContext(Context ctx) {
        ErrorHandler.continuer("[INFO] Le contexte a changé.");
        context = ctx;
    }

    /**
     * Obtenir le contexte
     * @return Le contexte de l'application
     */
    public static Context getContext() {
        return WiFindApplication.context;
    }

}
