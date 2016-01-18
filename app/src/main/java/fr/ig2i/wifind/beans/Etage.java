package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 13/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.2
 */
public class Etage {

    /**
     * Niveau de l'etage
     */
    @Serializable
    private int niveau = 0;

    /**
     * Plan associé à l'étage
     */
    @Serializable
    private Plan plan = null;

    /**
     * Centre commercial contenant l'étage

     */
    @Serializable
    private CentreCommercial centreCommercial = null;

    /**
     * Obtenir le niveau
     * @return Le niveau
     */
    public int getNiveau() {
        return niveau;
    }

    /**
     * Modifier le niveau
     * @param niveau Le nouveau niveau
     */
    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    /**
     * Obtenir le plan de l'étage
     * @return le plan de l'étage
     */
    public Plan getPlan() {
        return plan;
    }

    /**
     * Modifier le plan de l'étage
     * @param plan le plan de l'étage
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * Obtenir le centre commercial
     * @return le centre commercial
     */
    public CentreCommercial getCentreCommercial() {
        return centreCommercial;
    }

    /**
     * Modifier le centre commercial
     * @param centreCommercial le centre commercial
     */
    public void setCentreCommercial(CentreCommercial centreCommercial) {
        this.centreCommercial = centreCommercial;
    }

    @Override
    public String toString() {
        return "Etage{" +
                "niveau=" + niveau +
                ", plan=" + plan +
                '}';
    }
}
