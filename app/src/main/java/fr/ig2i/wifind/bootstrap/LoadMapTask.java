package fr.ig2i.wifind.bootstrap;

import android.graphics.Bitmap;

import java.util.concurrent.CountDownLatch;

import fr.ig2i.wifind.beans.Image;
import fr.ig2i.wifind.core.Configuration;
import fr.ig2i.wifind.core.ErrorHandler;
import fr.ig2i.wifind.core.ImageLoader;
import fr.ig2i.wifind.listeners.LoadImageListener;

/**
 * Created by Thomas on 10/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class LoadMapTask implements BootstrapTask<Bitmap> {

    /**
     * L'image chargée
     */
    private Bitmap bitmap;
    /**
     * Le listener appelé lors du chargement asynchrone de l'image
     */
    private LoadImageListener listenerSynchrone = new LoadImageListener() {
        @Override
        public void onBitmapLoaded(Image image, Bitmap bmp) {
            //Si on arrive ici c'est que la tâche asynchrone vient de se terminer

            LoadMapTask.this.image.setHash(image.getHash());//On met à jour le hash
            LoadMapTask.this.bitmap = bmp; //On enregistre le bitmap
            lock.countDown(); //Et on libère le verrou pour reprendre l'execution de charger()
        }
    };
    /**
     * Le verrou utilisé pour transformer l'appel asynchrone de LoadImage en synchrone
     */
    private CountDownLatch lock = new CountDownLatch(1);
    /**
     * Le listener a appeler lorsque cette tâche est terminée
     */
    private BootstrapTaskListener<Bitmap> listener = null;
    /**
     * L'image à charger
     */
    private Image image;

    /**
     * Constructeur
     *
     * @param listener
     */
    public LoadMapTask(BootstrapTaskListener<Bitmap> listener) {
        this.listener = listener; //Le listener a appeler lorsque la tâche est terminée
    }

    /**
     * Modifier l'image à charger
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }


    /**
     * Constructeur, sans listener
     *
     * @param image
     */
    public LoadMapTask(Image image) {
        this.image = image; //L'image a charger
    }

    @Override
    public String getNom() {
        return Configuration.get("tache-chargement-carte", "Chargement carte");
    }

    @Override
    public Bitmap charger() {

        //Ici la classe ImageLoader fonctionne en asynchrone
        //Or, la fonction charger() doit être synchrone, on doit donc mettre un verou tant que le callback n'a pas été appelé

        ImageLoader loader = new ImageLoader(this.listenerSynchrone);

        if (this.image != null) {
            loader.asyncLoad(this.image); //Chargement asynchrone
        } else {
            ErrorHandler.continuer("L'image a charger vaut (null).");
            return null; //On renvoie null si l'image a charger n'est pas valide
        }

        try {
            lock.await(); //On attend que le verrou soit libéré
        } catch (InterruptedException exc) {
            ErrorHandler.continuer("Le verrou a été interrompu.", exc); //On risque d'avoir bitmap == null ici...
        }

        return bitmap;
    }

    @Override
    public BootstrapTaskListener<Bitmap> getTaskListener() {
        return this.listener;
    }
}
