package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 13/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class CentreCommercial {

    /**
     * Le nom du centre commercial
     */
    @Serializable
    private String nom = "";

    /**
     * Obtenir le nom du centre commercial
     * @return Le nom du centre commercial
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifier le nom du centre commercial
     * @param nom Le nouveau nom du centre commercial
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "CentreCommercial{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
