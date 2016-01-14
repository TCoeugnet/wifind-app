package fr.ig2i.wifind.core;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.receivers.ScanReceiver;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class WifiScanner {

    //TODO Voir pourquoi c'est si long !

    /**
     * Manager utilisé pour scanner les réseaux WiFi
     */
    private WifiManager manager;

    /**
     * Contexte de l'application
     */
    private Context context;

    /**
     * Listener appelé après avoir terminé le scan
     */
    private DataChangeListener listener;

    /**
     * Receiver enregistré pour obtenir les résultats du scan et les convertir
     */
    private ScanReceiver receiver;

    /**
     * Constructeur
     * @param listener Listener appelé après avoir terminé le scan
     */
    public WifiScanner(DataChangeListener listener) {
        this.manager = (WifiManager) (this.context = WiFindApplication.getContext()).getSystemService(Context.WIFI_SERVICE);
        this.listener = listener;
    }

    /**
     * Démarrer le scan des réseaux WiFi
     */
    public void start() {
        this.receiver = new ScanReceiver(manager, listener);

        this.context.registerReceiver(
                receiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
        this.manager.startScan();
    }

    /**
     * Mettre en pause le scanner
     */
    public void pause() {
        try {
            context.unregisterReceiver(this.receiver); //Il est possible que la fonction pause() soit appelée plusieurs fois...
        } catch(Exception exc) {
            ErrorHandler.continuer("Receiver déjà retiré.", exc);
        }
    }

    /**
     * Reprendre le scan
     */
    public void resume() {
        this.manager.startScan();
    }


}
