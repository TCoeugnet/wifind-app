package fr.ig2i.wifind.beans;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ig2i.wifind.core.ErrorHandler;

/**
 * Created by Thomas on 10/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Image {

    //TODO JsonSerializable

    /**
     * Chemin/URL de l'image
     */
    private String chemin;

    /**
     * Hash des données de l'image. Utilisé pour trouver les doublons et faire fonctionner le cache.
     */
    private String hash;

    /**
     * Description de l'image
     */
    private String description;

    /**
     * Contenu du fichier
     */
    private byte[] data;

    /**
     * Obtenir le chemin
     * @return Chemin de l'image
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Modifier le chemin de l'image
     * @param chemin Le nouveau chemin de l'image
     */
    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    /**
     * Obtenir le hash de l'image
     * @return Le hash de l'image
     */
    public String getHash() {
        return hash;
    }

    /**
     * Modifier le hash de l'image
     * @param hash Le nouveau hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Obtenir la description de l'image
     * @return La description de l'image
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifier la description de l'image
     * @param description La nouvelle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtenir le contenu du fichier image
     * @return Le contenu sous forme de byte array
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Sauvegarder le contenu du fichier
     * @param data Le contenu du fichier
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Constructeur
     * @param chemin Le chemin/URL de l'image
     * @param hash Le hash de l'image
     * @param description La description de l'image
     */
    public Image(String chemin, String hash, String description) {
        this.chemin = chemin;
        this.hash = hash;
        this.description = description;
    }

    /**
     * Création d'un objet Image à partir de JSON
     * @param obj Représentation JSON de l'Image
     * @throws JSONException
     */
    public Image(JSONObject obj) {
        try {
            this.chemin = obj.getString("chemin");
            this.hash = obj.getString("hash");
            this.description = obj.getString("description");
        } catch (JSONException exc) {
            ErrorHandler.continuer(String.format("Impossible de créer l'objet Image à partir de l'objet JSON '%s'.", obj.toString()), exc);
        }
    }
}
