package fr.ig2i.wifind.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import fr.ig2i.wifind.R;
import fr.ig2i.wifind.beans.Image;
import fr.ig2i.wifind.beans.Mesure;
import fr.ig2i.wifind.bootstrap.AppBootstrap;
import fr.ig2i.wifind.bootstrap.BootstrapListener;
import fr.ig2i.wifind.bootstrap.BootstrapTaskListener;
import fr.ig2i.wifind.bootstrap.LoadMapTask;
import fr.ig2i.wifind.core.Configuration;
import fr.ig2i.wifind.core.JsonFactory;
import fr.ig2i.wifind.core.WiFindApplication;

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

    private static Image carte = new Image("http://192.168.137.1:28423/Content/Images/3eme.jpg", "4b762c7490cbdb70129301714615f7a9", "Plan du 3eme");

    private BootstrapTaskListener<Bitmap> loadMapListener = new BootstrapTaskListener<Bitmap>() {
        @Override
        public boolean onComplete(Bitmap value) {

            //Si la carte est chargée
            if(value != null) {
                //Si tout s'est bien déroulé, la carte est enregistrée sur le téléphone et sera chargée à partir du cache au besoin
                return true;
            } else {
                Toast.makeText(WiFindApplication.getContext(), Configuration.get("error-impossible-charger-carte", "Erreur chargement carte"), Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        WiFindApplication.setContext(this.getApplicationContext());

        //On ne relance pas le chargement lors du retour sur l'activité après une pause (en cas d'appui sur le bouton Back ou Home par ex.)
        if (!chargement) {
            chargement = true;
            bootstrap = new AppBootstrap(this);
            bootstrap.setTextView((TextView) this.findViewById(R.id.loadingText));

            //Log.d("", new JsonFactory<Mesure>(Mesure.class).serialize(new Mesure()).toString());

            //---- Ajouter ici toutes les taches de chargement.
            bootstrap.ajouterTache(new LoadMapTask(this.loadMapListener, carte));

            //Vérifier si le wifi est activé
            //Récupérer position
            //Récupérer carte
            //Récupérer offres

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

    @Override
    public void onError() {
        Log.d("e", "ERROR LOADING IMAGE");

        Log.d("e", "ERROR LOADING IMAGE");
    }
}
