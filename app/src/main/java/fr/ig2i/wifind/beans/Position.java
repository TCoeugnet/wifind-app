package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Position {

    /**
     * Position X
     */
    @Serializable
    private float x = 0;

    /**
     * Position Y
     */
    @Serializable
    private float y = 0;

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
}
