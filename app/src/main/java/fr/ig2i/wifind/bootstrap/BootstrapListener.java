package fr.ig2i.wifind.bootstrap;

/**
 * Created by Thomas on 09/01/2016.
 */
public interface BootstrapListener {

    /**
     * Fonction appelée lorsque le chargement complet de l'application est terminé.
     */
    public void onComplete();

    /**
     * Fonction appelée lorsqu'une erreur est survenue. On ne donne pas d'info sur l'erreur, il faut gérer ça manuellement lors de l'implémentation de l'interface.
     */
    public void onError();
}
