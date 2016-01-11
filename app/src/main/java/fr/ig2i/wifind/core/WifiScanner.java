package fr.ig2i.wifind.core;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.receivers.ScanReceiver;

/**
 * Created by Thomas on 11/01/2016.
 */
public class WifiScanner {

    //TODO Commenter

    private WifiManager manager;

    private Context context;

    private DataChangeListener listener;

    private ScanReceiver receiver;

    public WifiScanner(DataChangeListener listener) {
        this.manager = (WifiManager) (this.context = WiFindApplication.getContext()).getSystemService(Context.WIFI_SERVICE);
        this.listener = listener;
    }

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
            context.unregisterReceiver(this.receiver);
        } catch(Exception exc) {
            ErrorHandler.continuer("Receiver déjà retiré.", exc);
        }
    }

    public void resume() {
        this.manager.startScan();
    }


}
