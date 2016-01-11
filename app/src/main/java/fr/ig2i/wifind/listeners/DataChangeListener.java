package fr.ig2i.wifind.listeners;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 */
public interface DataChangeListener<T> {

    /**
     * Appelé lorsque des données sont mises à jour
     * @param data
     */
    public void onDataChange(T data);
}
