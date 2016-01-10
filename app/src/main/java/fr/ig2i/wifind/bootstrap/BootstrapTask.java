package fr.ig2i.wifind.bootstrap;

/**
 * Created by Thomas on 09/01/2016.
 *
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public interface BootstrapTask<T> {

    /**
     * Obtenir le nom de la tache
     * @return Le nom qui sera affiché par le splash screen
     */
    public String getNom();

    /**
     * Fonction de chargement de la tache
     * Cette fonction doit être synchrone
     * @return
     */
    public T charger();

    /**
     * Listener appelé lors de l'execution de la tâche
     * @return Le listener
     */
    public BootstrapTaskListener<T> getTaskListener();

}
