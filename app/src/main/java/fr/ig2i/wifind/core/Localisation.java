package fr.ig2i.wifind.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import fr.ig2i.wifind.beans.Mesure;
import fr.ig2i.wifind.beans.Position;
import fr.ig2i.wifind.listeners.DataChangeListener;

/**
 * Created by Thomas on 14/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Localisation implements DataChangeListener<List<Mesure>> {

    /**
     * Listener appelé lors de la récupération de la position
     */
    private DataChangeListener<Position> listener;

    /**
     * Scanner pour le WiFi
     */
    private WifiScanner scanner;

    /**
     * Position récupérée
     */
    private Position pos;

    /**
     * Constructeur
     */
    public Localisation() {
        this.scanner = new WifiScanner(this);
    }

    /**
     * Modifier le listener appelé lors de la récupération de la position
     * @param listener
     */
    public void setListener(DataChangeListener<Position> listener) {
        this.listener = listener;
    }

    /**
     * Localisation asynchrone
     */
    public void localiser() {
        this.scanner.start(); //On scanne les réseaux WiFi
    }

    @Override
    public void onDataChange(List<Mesure> mesures) {
        JsonFactory<Mesure> factory = new JsonFactory(Mesure.class);
        JSONArray data = factory.serializeArray(mesures); //On prépare les données pour l'envoi

        //Envoyer les données

        AsyncHttpRequest.setListener(new DataChangeListener<JSONObject>() {
            @Override
            public void onDataChange(JSONObject data) {
                if (data != null) {
                    pos = new JsonFactory<Position>(Position.class).unserialize(data); //On récupère l'objet si des données ont été reçues
                } else {
                    pos = null; //Sinon on récupère null
                }
                scanner.pause(); //On met en pause le scanner
                listener.onDataChange(pos); //On notifie de la réception des données
            }
        });

        AsyncHttpRequest.AsyncPost(API.GetBaseURL() + "/api/position", data.toString()); //Appel asynchronne de l'API
    }
}
