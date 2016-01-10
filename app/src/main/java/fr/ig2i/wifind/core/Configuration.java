package fr.ig2i.wifind.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Thomas on 09/01/2016.
 *
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Configuration {

    /**
     * Nom du fichier de configuration de l'application
     */
    private final static String FICHIER_CONFIGURATION = "wifind.properties";

    /**
     * Vaudra vrai si les proprietes ont été chargées.
     * Les propriétés ne seront chargées qu'une seule fois pendant toute la durée de vie de l'application
     */
    private static boolean initialise = false;

    /**
     * Variable contenant toutes les propriétés de l'application.
     */
    private static Properties config = null;

    /**
     * Initialiser le module de configuration
     */
    private static void init() {
        //Il est possible que le contexte n'ait pas été initialisé
        Context context = WiFindApplication.getContext();

        if(context != null) {
            Resources resources = context.getResources();
            AssetManager assetManager = resources.getAssets();

            try {
                InputStream inputStream = assetManager.open(FICHIER_CONFIGURATION);
                config = new Properties();
                config.load(inputStream);
                initialise = true;
            } catch (IOException exc) {
                //La configuration n'a pas pu être chargée. Dans ce cas, on retournera la valeur par défaut.
                //L'application ne s'arrête pas pour autant

                ErrorHandler.continuer(String.format("IOException : Impossible d'ouvrir le fichier de configuration '%s'.", FICHIER_CONFIGURATION), exc);
            }
        }
    }

    /**
     *
     * @param cle Nom du paramètre
     * @param defaut Valeur par défaut si ce paramètre n'est pas défini
     * @return La valeur du paramètre si celui ci a été trouvé, la valeur par défaut sinon
     */
    public static String get(String cle, String defaut) {
        String valeur = null;

        if(!initialise) {
            //Si c'est la première fois qu'on charge un parametre, ou si la derniere fois ça a échoué
            init();
        }

        if(initialise) {
            //Si c'est ok pour renvoyer la valeur
            valeur = config.getProperty(cle, null);

            if(valeur == null) {
                ErrorHandler.continuer(String.format("Configuration chargée mais clé '%s' non définie.", cle));
            }
        }
        return valeur == null ? valeur : defaut;
    }

}
