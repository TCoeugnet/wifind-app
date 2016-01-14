package fr.ig2i.wifind.bootstrap;

import java.util.concurrent.CountDownLatch;

import fr.ig2i.wifind.beans.Position;
import fr.ig2i.wifind.core.ErrorHandler;
import fr.ig2i.wifind.core.Localisation;
import fr.ig2i.wifind.listeners.DataChangeListener;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public class LocalizeTask implements BootstrapTask<Position>, DataChangeListener<Position> {

    /**
     * Verrou pour la synchronisation
     */
    private CountDownLatch lock = new CountDownLatch(1);

    /**
     * Position
     */
    private Position pos = null;

    /**
     * Le listener à appeler une fois que la tâche est terminée
     */
    private BootstrapTaskListener listener;

    /**
     * Constructeur
     * @param listener Le listener à appeler une fois que la tâche est terminée
     */
    public LocalizeTask(BootstrapTaskListener listener) {
        this.listener = listener;
    }

    /**
     * Obtenir le nom de la tâche
     * @return Le nom de la tâche
     */
    @Override
    public String getNom() {
        return "Localisation en cours";
    }

    /**
     * Obtenir la localisation de la personne
     * @return
     */
    @Override
    public Position charger() {
        Localisation loc = new Localisation();
        loc.setListener(this); //On appellera onDataChange à la fin de la localisation

        loc.localiser(); //Asynchrone

        try {
            lock.await(); //Pour synchroniser
        } catch (InterruptedException exc) {
            ErrorHandler.continuer("Thread interrompu.",exc);
        }

        return this.pos;
    }

    @Override
    public BootstrapTaskListener<Position> getTaskListener() {
        return this.listener;
    }

    @Override
    public void onDataChange(Position pos) {
        this.pos = pos; //On sauvegarde la localisation
        lock.countDown(); //On repart sur le await
    }
}
