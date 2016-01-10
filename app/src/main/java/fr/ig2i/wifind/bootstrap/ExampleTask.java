package fr.ig2i.wifind.bootstrap;

import android.util.Log;

/**
 * Created by Thomas on 09/01/2016.
 *
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class ExampleTask implements BootstrapTask<String> {

    @Override
    public String getNom() {
        return "Chargement des modules";
    }

    @Override
    public String charger() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exc) {

        }
        return "ExampleTask chargée !";
    }

    @Override
    public BootstrapTaskListener<String> getTaskListener() {
        return new BootstrapTaskListener<String>() {
            @Override
            public void onComplete(String value) {
                Log.d("ExampleTask", value);
            }
        };
    }
}
