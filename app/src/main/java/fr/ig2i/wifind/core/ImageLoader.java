package fr.ig2i.wifind.core;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.ig2i.wifind.beans.Image;
import fr.ig2i.wifind.listeners.LoadImageListener;

/**
 * Created by Thomas on 10/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class ImageLoader {

    /**
     * Format du de l'image sauvegardée en interne
     */
    public static String IMG_INTERNAL_NAME      = "cachedimg-%.3s-%s.%s";

    /**
     * Sous dossier stockant les images
     */
    public static String IMG_INTERNAL_DIR       = "images";

    /**
     * Algorithme de hachage utilisé lors de la comparaison des contenus de fichier
     */
    public static String HASH_ALGORITHM         = "md5";

    /**
     * Contexte de l'application
     */
    private Context ctx;

    /**
     * Listener appelé lors du chargement de l'image
     */
    private LoadImageListener listener;

    /**
     * Constructeur
     * @param listener le listener a appeler lors du chargement de l'image
     */
    public ImageLoader(LoadImageListener listener) {
        this.ctx = WiFindApplication.getContext();
        this.listener = listener;
    }

    /**
     * Chargement asynchrone de l'image
     * @param img L'image a charger
     */
    public void asyncLoad(Image img) {
        new LoadImageTask(img, listener).execute((Void) null);
    }

    /**
     * Chargement de l'image, depuis une URL ou depuis le cache
     * @param image L'image a charger
     * @return Le bitmap chargé, ou null si le chargement a échoué
     */
    private Bitmap loadBitmap(Image image) {

        Bitmap bmp = null;
        byte[] data = null;

        if(this.isInInternalStorage(image)) { //Si l'image est dans le cache
            bmp = this.loadFromInternalStorage(image); //Alors on la charge depuis le cache
        } else {
            bmp = this.loadFromURL(image); //Sinon on charge depuis l'URL

            if(bmp != null) { //Si l'image est bien chargée
                if (this.isInInternalStorage(image)) { //Ca ne sera jamais vrai ici
                    this.deleteFromInternalStorage(image); //TODO Trouver une méthode pour supprimer les anciennes images
                }
                image.setHash(this.computeHash(image.getData())); //On change le hash

                Log.d("ImageLoader", "Hash : " + image.getHash());
                this.saveToInternalStorage(image); //Sauvegarde dans le stockage interne
            }
        }

        return bmp;
    }

    /**
     * Calculer un hash à partir d'un byte array
     * @param data Les données à hasher
     * @return Le hash
     */
    private String computeHash(byte[] data) {

        StringBuilder sb = new StringBuilder(); //On utilise un StringBuilder pour éviter les concaténations de string assez couteuses
        MessageDigest md = null; //Objet utilisé pour calculer un hash
        byte[] bytes;

        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM); //On récupère le MessageDigest associé à l'algorithme choisi
        } catch (NoSuchAlgorithmException exc) {
            ErrorHandler.arreter(String.format("Impossible de charger l'algorithme de hachage '%s'.", HASH_ALGORITHM), exc);
        }


        bytes = md.digest(data); //Calcul du hash
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        String result = sb.toString().toLowerCase();
        return result;
    }

    /**
     * Calculer le nom de fichier dans le stockage interne
     * @param name Le nom original du fichier
     * @param hash Le hash du contenu du fichier
     * @return Le nom du fichier dans le stockage interne du téléphone
     */
    private String computeInternalStorageName(String name, String hash) {
        return String.format(IMG_INTERNAL_NAME, name.substring(name.lastIndexOf("/") + 1), hash.toLowerCase(), name.substring(name.lastIndexOf(".") + 1));
    }

    /**
     * Vérifier si un fichier est présent dans le stockage interne
     * @param image Le fichier à tester
     * @return true s'il est présent, false sinon
     */
    private boolean isInInternalStorage(Image image) {
        return this.getInternalStorageFile(image.getChemin(), image.getHash()).exists();
    }

    /**
     * Chargement de l'image depuis une URL
     * @param image L'image à charger
     * @return Le bitmap de l'image chargée
     */
    private Bitmap loadFromURL(Image image) {

        Bitmap bmp = null; //Le bitmap à charger
        URL url = null; //L'URL de l'image
        byte[] data = new byte[4096]; //Le contenu du fichier image
        int length = 0; //La longueur des données lues

        try {
            //Initialisation dela connexion
            url = new URL(image.getChemin());
            URLConnection connec = url.openConnection();
            connec.setConnectTimeout(Integer.parseInt(Configuration.get("request-timeout", "10000"))); //On définit un timeout pas trop long
            InputStream stream = connec.getInputStream();
            ByteArrayBuffer buffer = new ByteArrayBuffer(4096);

            //Lecture de la stream
            while((length = stream.read(data, 0, data.length)) != -1) {
                buffer.append(data, 0, length);
            }

            //Decodage de l'image
            bmp = BitmapFactory.decodeByteArray(buffer.buffer(), 0, buffer.length());

            //Sauvegarde du contenu (pas du bitmap) dans l'objet Image
            image.setData(buffer.toByteArray());

            //Libération des ressources
            buffer.clear();

        } catch (MalformedURLException exc) {
            ErrorHandler.continuer(String.format("URL malformée '%s'.", image.getChemin()), exc);
        } catch (IOException exc) {
            ErrorHandler.continuer(String.format("Impossible de charger l'image '%s'.", image.getChemin()), exc);
        }

        return bmp;
    }

    /**
     * Charge une image depuis la mémoire (~cache) du téléphone
     * @param image L'image a charger
     * @return Le bitmap de l'image chargée
     */
    private Bitmap loadFromInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), image.getHash()); //On récupère le fichier dans le stockage du téléphone
        Bitmap bmp = null;

        if(imageFile != null) {
            if(imageFile.exists()) {
                try {
                    bmp = BitmapFactory.decodeStream(new FileInputStream(imageFile)); //On décode le contenu du fichier et on le renvoie en bitmap
                } catch (FileNotFoundException exc) {
                    ErrorHandler.continuer(String.format("Impossible de charger l'image '%s'.", imageFile.getName()));
                }
            } else {
                ErrorHandler.continuer(String.format("L'image '%s' n'existe pas.", imageFile.getName()));
            }
        } else {
            ErrorHandler.continuer(String.format("Impossible de récupérer l'image '%s'.", image.getChemin()));
        }

        return bmp;
    }

    /**
     * Récupérer le fichier dans le stockage du téléphone
     * @param fileLocation
     * @param hash
     * @return
     */
    private File getInternalStorageFile(String fileLocation, String hash) {
        ContextWrapper cw = new ContextWrapper(this.ctx); //Objet permettant d'accéder au stockage interne du téléphone dédié à l'application
        File imageDir = cw.getDir(IMG_INTERNAL_DIR, Context.MODE_PRIVATE), //Répertoire de stockage de l'application
                imageFile;

        if(fileLocation != null) {
            imageFile = new File(imageDir, this.computeInternalStorageName(fileLocation, hash)); //Récupération du fichier
            return imageFile;
        } else {
            ErrorHandler.continuer(String.format("Fichier '%s' introuvable.", this.computeInternalStorageName(fileLocation, hash)));
            return null;
        }
    }

    /**
     * Sauvegarder une image dans le stockage interne du téléphone
     * @param image Les métadata de l'image a sauvegarder
     */
    private void saveToInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), this.computeHash(image.getData())); //On récupère l'image à sauvegarder
        FileOutputStream stream = null;

        if(!imageFile.exists()) { //Si le fichier n'existe pas encore
            try {
                stream = new FileOutputStream(imageFile);
                stream.write(image.getData()); //On sauvegarde l'image
            } catch (FileNotFoundException exc) {
               ErrorHandler.continuer("Fichier introuvable", exc);
            } catch (IOException exc) {
                ErrorHandler.continuer("Impossible de sauvegarder l'image.", exc);
            } finally {
                try {
                    stream.close(); //On n'oublie pas de refermer la stream
                } catch (IOException exc) {
                    ErrorHandler.continuer("Impossible de refermer la stream.", exc);
                }
            }
        }
    }

    /**
     * Suppression d'une image du stockage interne du téléphone
     * @param image L'image à supprimer
     */
    private void deleteFromInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), image.getHash());

        if(imageFile.exists()) {
            imageFile.delete();
        }
    }

    /**
     * Tâche asynchrone pour le chargement de l'image
     */
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private Image img;

        public LoadImageTask(Image image, LoadImageListener listener) {
            this.img = image;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return ImageLoader.this.loadBitmap(this.img); //On execute simplement la méthode loadBitmap de ImageLoader
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            listener.onBitmapLoaded(this.img, bitmap); //On appelle le listener
        }
    }

}
