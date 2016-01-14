package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 13/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.1
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

    @Override
    public String toString() {
        return "Etage{" +
                "niveau=" + niveau +
                ", plan=" + plan +
                '}';
    }
}
