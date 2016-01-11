package fr.ig2i.wifind.core;

import android.app.Activity;
import android.util.Log;

/**
 * Created by Thomas on 10/01/2016.
 *
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class ErrorHandler {

    /**
     * Signaler l'erreur mais continuer l'execution de l'app
     * @param exc Exception levée
     */
    public static void continuer(Throwable exc) {
        continuer(null, exc);
    }

    /**
     * Signaler l'erreur mais continuer l'execution de l'app
     * @param message Message personnalisé
     * @param exc Exception levée
     */
    public static void continuer(String message, Throwable exc) {
        Log.w("ErrorHandler/CONT", message, exc);
    }

    /**
     * Signaler l'erreur mais continuer l'execution de l'app
     * @param message Message personnalisé
     */
    public static void continuer(String message) {
        continuer(message, null);
    }

    /**
     * Signaler l'erreur et propager l'exception
     * @param exc Exception levée
     */
    public static void arreter(Throwable exc){
        arreter(null, exc);
    }

    /**
     * Signaler l'erreur et propager l'exception
     * @param exc Exception levée
     * @param message Message personnalisé
     */
    public static void arreter(String message, Throwable exc) throws RuntimeException {
        Log.e("ErrorHandler/STOP", message, exc);
        throw new RuntimeException(exc);
    }

    /**
     * Signaler l'erreur et propager l'exception
     * @param message Message personnalisé
     */
    public static void arreter(String message){
        arreter(message, null);
    }

}
