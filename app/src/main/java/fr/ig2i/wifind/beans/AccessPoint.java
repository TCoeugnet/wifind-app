package fr.ig2i.wifind.beans;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class AccessPoint {

    /**
     * SSID du réseau
     */
    @Serializable
    private String SSID = "";

    /**
     * BSSID de l'AP
     */
    @Serializable
    private String BSSID = "";

    /**
     * Constructeur
     */
    public AccessPoint() {

    }

    /**
     * Constructeur
     * @param SSID Le SSID de l'AP
     * @param BSSID Le BSSID de l'AP
     */
    public AccessPoint(String SSID, String BSSID) {
        this.SSID = SSID;
        this.BSSID = BSSID;
    }

    /**
     * Obtenir le SSID de l'AP
     * @return
     */
    public String getSSID() {
        return SSID;
    }

    /**
     * Modifier le SSID de l'AP
     * @param SSID
     */
    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    /**
     * Obtenir le SSID de l'AP
     * @return
     */
    public String getBSSID() {
        return BSSID;
    }

    /**
     * Modifier le BSSID de l'AP
     * @param BSSID
     */
    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

}
