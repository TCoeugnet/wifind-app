package fr.ig2i.wifind.beans;

import android.net.wifi.WifiManager;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class Mesure {

    /**
     * Point d'accès sur lequel est effectué la mesure
     */
    @Serializable
    private AccessPoint ap = new AccessPoint();

    /**
     * RSSI, force du signal
     */
    @Serializable
    private int rssi = 0;

    /**
     * Position de la mesure
     */
    @Serializable
    private Position position = new Position();

    /**
     * Obtenir le point d'accès associé à la mesure
     * @return
     */
    public AccessPoint getAp() {
        return ap;
    }

    /**
     * Modifier le point d'accès associé à la mesure
     * @param ap
     */
    public void setAp(AccessPoint ap) {
        this.ap = ap;
    }

    /**
     * Obtenir le RSSI associé à la mesure
     * @return
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * Modifier le RSSI associé à la mesure
     * @param rssi
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    /**
     * Obtenir la position associée à la mesure
     * @return
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Modifier la position associée à la mesure
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Renvoie le niveau de signal compris entre 0 (mauvaise qualité) et 99 (très bonne qualité)
     * @return int
     */
    public int calculerNiveau() {
        return WifiManager.calculateSignalLevel(this.rssi, 100);
    }

    /**
     * Constructeur
     */
    public Mesure() {

    }

    /**
     * Constructeur
     * @param ap Le point d'accès associé à la mesure
     * @param rssi Le RSSI associé à la mesure
     * @param position La position associée à la mesure
     */
    public Mesure(AccessPoint ap, int rssi, Position position) {
        this.ap = ap;
        this.rssi = rssi;
        this.position = position;
    }
}
