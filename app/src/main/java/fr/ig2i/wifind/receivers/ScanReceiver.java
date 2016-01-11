package fr.ig2i.wifind.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.wifind.beans.AccessPoint;
import fr.ig2i.wifind.beans.Mesure;
import fr.ig2i.wifind.listeners.DataChangeListener;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class ScanReceiver extends BroadcastReceiver {

    /**
     * Manager utilisé pour scanner les réseaux wifi alentours
     */
    private WifiManager manager = null;

    /**
     * Listener appelé lorsque le scan est terminé
     */
    private DataChangeListener listener = null;

    /**
     * Constructeur
     * @param wifiManager Le WifiManager utilisé pour scanner
     * @param listener Le listenenr appelé à la fin du scan
     */
    public ScanReceiver(WifiManager wifiManager, DataChangeListener<List<Mesure>> listener) {
        super();
        this.manager = wifiManager;
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final List<ScanResult> results = manager.getScanResults(); //Liste des points d'accès du dernier scan
        List<Mesure> mesures = new ArrayList<>(results.size()); //Liste des mesures obtenues

        for(ScanResult result : results) { //On va récupérer les ScanResult et les transformer en Mesure
            AccessPoint AP = new AccessPoint(result.SSID, result.BSSID);
            Mesure mesure = new Mesure(AP, result.level, null);
            mesures.add(mesure); //On ajoute à la liste
        }

        listener.onDataChange(mesures); //On appelle le listener
    }

}
