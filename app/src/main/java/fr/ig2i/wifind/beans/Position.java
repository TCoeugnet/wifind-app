package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.1
 */
public class Position {

    /**
     * Position X
     */
    @Serializable
    private float x = 0;

    @Serializable
    public int id = 0;

    /**
     * Position Y
     */
    @Serializable
    private float y = 0;

    /**
     * Etage
     */
    @Serializable
    private Etage etage = null;

    /**
     * Obtenir la coordonnée X
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Modifier la coordonnée X
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Obtenir la coordonnée Y
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Modifier la coordonnée Y
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Obtenir l'etage
     * @return
     */
    public Etage getEtage() {
        return etage;
    }

    /**
     * Modifier l'etage
     * @param etage
     */
    public void setEtage(Etage etage) {
        this.etage = etage;
    }

    /**
     * Constructeur
     */
    public Position() {

    }

    /**
     * Constructeur
     * @param x Coordonnée X
     * @param y Coordonnée Y
     */
    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", etage=" + etage +
                '}';
    }
}
