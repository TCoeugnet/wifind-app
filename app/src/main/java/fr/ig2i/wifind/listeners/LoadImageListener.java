package fr.ig2i.wifind.listeners;

import android.graphics.Bitmap;

import fr.ig2i.wifind.beans.Image;

/**
 * Created by Thomas on 10/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public interface LoadImageListener {

    /***
     * Appelé lorsque la tâche est terminée (image chargée ou non)
     * @param image L'image a charger, avec le hash mis à jour
     * @param bmp L'image chargée, ou null si elle n'a pas pu l'être
     */
    public void onBitmapLoaded(Image image, Bitmap bmp);

}
