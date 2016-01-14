package fr.ig2i.wifind.beans;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ig2i.wifind.annotations.Serializable;
import fr.ig2i.wifind.core.API;
import fr.ig2i.wifind.core.ErrorHandler;

/**
 * Created by Thomas on 10/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.1
 */
public class Image {

    /**
     * Chemin/URL de l'image
     */
    @Serializable
    private String chemin;

    /**
     * Hash des données de l'image. Utilisé pour trouver les doublons et faire fonctionner le cache.
     */
    @Serializable
    private String hash;

    /**
     * Description de l'image
     */
    @Serializable
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
        return API.GetBaseURL() + chemin;
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
     * Constructeur
     */
    public Image() {

    }

    @Override
    public String toString() {
        return "Image{" +
                "description='" + description + '\'' +
                ", chemin='" + chemin + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
