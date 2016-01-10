package fr.ig2i.wifind.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import fr.ig2i.wifind.R;
import fr.ig2i.wifind.bootstrap.AppBootstrap;
import fr.ig2i.wifind.bootstrap.BootstrapListener;
import fr.ig2i.wifind.bootstrap.ExampleTask;

/**
 * Classe permettant de gérer le SplashScreen
 *
 * @author Thomas Coeugnet
 * @version 1.0
 */
public class SplashScreenActivity extends Activity implements BootstrapListener {

    /**
     * Vaudra true après le commencement du chargement; Ne reviendra jamais à false
     */
    private static boolean chargement = false;

    /**
     * Queue de chargement
     */
    private static AppBootstrap bootstrap;

    /**
     * Vaudra true une fois que tout est chargé, false sinon.
     */
    private static boolean charge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //On ne relance pas le chargement lors du retour sur l'activité après une pause (en cas d'appui sur le bouton Back ou Home par ex.)
        if (!chargement) {
            chargement = true;
            bootstrap = new AppBootstrap(this);
            bootstrap.setTextView((TextView) this.findViewById(R.id.loadingText));

            //---- Ajouter ici toutes les taches de chargement.
            bootstrap.ajouterTache(new ExampleTask());

            //----

            bootstrap.charger();
        }

        //Si le chargement a déjà été effectué, on va directement sur l'activité suivante
        if (charge) {
            this.onComplete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Ici, comme l'activity a été détruite lorsqu'on a quitté l'app, elle a été recréée (et les vues aussi)
        //Il faut donc les réassigner au bootstrap (qui lui a été sauvegardé car il est déclaré static)
        bootstrap.setTextView((TextView) this.findViewById(R.id.loadingText));
        bootstrap.majStatut();
        bootstrap.setListener(this);
    }

    @Override
    public void onComplete() {
        charge = true;

        //On démarre la prochaine activité et on détruit celle-ci
        Intent intent = new Intent(this.getApplicationContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intent);
        this.finish();
    }
}
