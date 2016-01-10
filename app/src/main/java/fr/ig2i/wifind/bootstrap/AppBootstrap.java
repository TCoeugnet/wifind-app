package fr.ig2i.wifind.bootstrap;

import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 09/01/2016.
 */
public class AppBootstrap {

    //TODO Commenter cette classe

    BootstrapListener listener;

    private String statutTexte = null;

    private int nbPoints = 1;

    private TextView statut = null;

    private List<BootstrapTask> taches = new ArrayList<BootstrapTask>();

    private int nbPointsMax = 3; //Integer.parseInt(Configuration.get("nombre-max-points-chargement"));

    private boolean chargement = false;

    private boolean stoppe = false;

    private AsyncTask chargementComplet = new AsyncTask() {

        @Override
        protected Object doInBackground(Object[] params) {
            for (BootstrapTask tache : AppBootstrap.this.taches) {
                this.publishProgress(tache.getNom());
                tache.getTaskListener().onComplete(tache.charger());

                if (AppBootstrap.this.stoppe) {
                    break;
                }
            }

            AppBootstrap.this.chargement = false;

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            AppBootstrap.this.changerStatut(values[0].toString());
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!AppBootstrap.this.stoppe) {
                listener.onComplete();
            }
        }
    };

    private AsyncTask tictocTask = new AsyncTask() {

        @Override
        protected Object doInBackground(Object[] params) {

            while (AppBootstrap.this.chargement && !AppBootstrap.this.stoppe) {
                try {
                    Thread.sleep(1000); //Integer.parseInt(Configuration.get("intervale-points-chargement")));
                } catch (InterruptedException exc) {
                    //TODO Gestion des erreurs
                }

                this.publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            AppBootstrap.this.tictoc();
        }
    };

    public AppBootstrap(BootstrapListener listener) {
        this.listener = listener;
    }

    public void ajouterTache(BootstrapTask tache) {
        this.taches.add(tache);
    }

    public void setTextView(TextView texte) {
        this.statut = texte;
    }

    private void tictoc() {
        this.nbPoints = this.nbPoints % nbPointsMax + 1; //1, 2 ou 3
        this.majStatut();
    }

    private void changerStatut(String statut) {
        this.statutTexte = statut;
        this.majStatut();
    }

    public void majStatut() {
        this.statut.setText(this.statutTexte + "...".substring(0, nbPoints));
    }

    public void stop() {
        this.stoppe = true;
    }

    public void setListener(BootstrapListener listener) {
        this.listener = listener;
    }

    public void charger() {
        this.chargement = true;
        tictocTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        chargementComplet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
