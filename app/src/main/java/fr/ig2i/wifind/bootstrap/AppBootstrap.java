package fr.ig2i.wifind.bootstrap;

import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import fr.ig2i.wifind.core.Configuration;
import fr.ig2i.wifind.core.ErrorHandler;

/**
 * Created by Thomas on 09/01/2016.
 * Classe permettant de faire des tâches de chargement de façon séquentielle
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class AppBootstrap {

    //TODO : Implémenter un resume() pour reprendre le chargement après correction de l'erreur

    /**
     * Utilisé pour bien rester synchro entre les threads et attendre l'execution du onComplete avant de poursuivre
     */
    private CountDownLatch lock = new CountDownLatch(1);

    /**
     * Le listener appelé lors de la réussite du chargement ou lors d'une erreur
     */
    BootstrapListener listener;

    /**
     * Le texte du statut, sans les points
     */
    private String statutTexte = null;

    /**
     * Le nombre de points affichés après le texte du statut
     */
    private int nbPoints = 1;

    /**
     * La textview utilisée pour l'affichage du statut
     */
    private TextView statut = null;

    /**
     * La liste des taches présentes dans la file d'attente
     */
    private List<BootstrapTask> taches = new ArrayList<BootstrapTask>();

    /**
     * Le nombre de points maximum affichés lors du chargement après le statut. Quand ce nombre est atteint, on revient à 1 point.
     */
    private static int nbPointsMax = 3;

    /**
     * Vaudra true lors du chargement, false sinon
     */
    private boolean chargement = false;

    /**
     * Vaudra true si on a stoppé le chargement et qu'on doit appeler le onError du listener, false sinon
     */
    private boolean stoppe = false;

    /**
     * Tache appelée lors du chargement complet
     */
    private AsyncTask chargementComplet = new AsyncTask() {

        @Override
        protected Object doInBackground(Object[] params) {

            //On charge séquentiellement les tâches
            for (BootstrapTask tache : AppBootstrap.this.taches) {
                lock = new CountDownLatch(1);

                this.publishProgress("statut", tache.getNom()); //On retourne sur le thread UI car mise à jour d'une textview
                this.publishProgress("complete", tache.charger(), tache.getTaskListener()); //On doit retourner sur le thread UI car on peut avoir à modifier des vues (cf listener)

                try {
                    lock.await(); //On attend la synchro
                } catch(InterruptedException exc ) {}

                if (AppBootstrap.this.stoppe) { //Si on a stoppe le chargement, on ne charge pas les autres
                    break;
                }
            }

            AppBootstrap.this.chargement = false; //On n'est plus en train de charger

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {

            //Le type d'opération
            String operation = (String) values[0];

            switch(operation) {

                case "statut" : { //Mise à jour de la textview pour afficher la tâche en cours
                    String tache = (String) values[1];
                    if (tache == null) {
                        ErrorHandler.continuer("Le tâche du Boostrap n'est pas défini.");
                        tache = Configuration.get("tache-par-defaut", "Chargement"); //Par défaut..
                    }

                    AppBootstrap.this.changerStatut(tache);
                    break;
                }

                case "complete" : { //Une tâche vient de se terminer
                    Object result = values[1];
                    BootstrapTaskListener listener = (BootstrapTaskListener) values[2];
                    if(listener != null) { //Si un listener est défini, on l'appelle. Sinon on considère qu'il n'y a pas d'erreur
                        AppBootstrap.this.stoppe = !listener.onComplete(result);
                    }
                    //onComplete renverra false si une erreur est survenue.
                    //On met donc stoppe a true en cas d'erreur, ce qui stoppera la file de chargement et executera onError du listener principal
                    lock.countDown(); //On peut poursuivre sur l'autre thread
                    break;
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!AppBootstrap.this.stoppe) { //Le chargement a eu lieu avec succès
                listener.onComplete();
            } else { //Le chargement s'est soldé par une erreur
                listener.onError();
            }
        }
    };

    /**
     * Tâche asynchrone utilisée pour mettre à jour le statut (et ajouter des points toutes les secondes)
     */
    private AsyncTask tictocTask = new AsyncTask() {

        @Override
        protected Object doInBackground(Object[] params) {

            //Ici on va venir mettre à jour la textview toutes les secondes
            while (AppBootstrap.this.chargement && !AppBootstrap.this.stoppe) {
                try {
                    Thread.sleep(1000); //Délai de 1 seconde
                } catch (InterruptedException exc) {
                    ErrorHandler.continuer("Thread tictoc interrompu !", exc);
                }

                this.publishProgress(); //On doit retourner sur le thread UI pour modifier la textview
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            //Il arrive que ce thread se stoppe après l'execution de onError, et que les données de la textview soient donc écrasées.
            //Cette condition empêche ceci d'arriver
            if(!AppBootstrap.this.stoppe) {
                AppBootstrap.this.tictoc();
            }
        }
    };

    /**
     * Constructeur
     * @param listener le listener qui sera appelé à la fin de l'execution ou en cas d'erreur
     */
    public AppBootstrap(BootstrapListener listener) {
        this.listener = listener;
    }

    /**
     * Ajouter une tâche à la file d'attente
     * @param tache
     */
    public void ajouterTache(BootstrapTask tache) {
        this.taches.add(tache);
    }

    /**
     * Modifer la textview qui affichera la tâche en cours
     * @param texte
     */
    public void setTextView(TextView texte) {
        this.statut = texte;
    }

    /**
     * Permet de faire l'effet de chargement avec les points (. puis .. puis ... puis re .)
     */
    private void tictoc() {
        this.nbPoints = this.nbPoints % nbPointsMax + 1; //1, 2 ou 3
        this.majStatut();
    }

    /**
     * Changer le texte du statut
     * @param statut Le nouveau texte
     */
    private void changerStatut(String statut) {
        this.statutTexte = statut;
        this.majStatut();
    }

    /**
     * Met à jour le texte affiché par la textview
     */
    public void majStatut() {
        if(this.statut == null) {
            ErrorHandler.continuer("La textview de mise à jour du statut n'est pas définie.");
        } else {
            this.statut.setText(this.statutTexte + "...".substring(0, nbPoints));
        }
    }

    /**
     * Arrêter le chargement et déclencher l'appel de onError sur le listener
     */
    public void stop() {
        this.stoppe = true;
    }

    /**
     * Modifier le listener appelé lors de la fin du chargement ou lors d'une erreur
     * @param listener Le nouveau listener
     */
    public void setListener(BootstrapListener listener) {
        this.listener = listener;
    }

    /**
     * Commencer le chargement.
     * Démarre 2 threads : le thread de chargement (les chargements sont effectués de façon synchrone) et le thread de mise à jour du texte de statut.
     */
    public void charger() {
        this.chargement = true;
        tictocTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        chargementComplet.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
