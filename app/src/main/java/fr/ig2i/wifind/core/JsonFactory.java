package fr.ig2i.wifind.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import fr.ig2i.wifind.annotations.Serializable;

/**
 * Created by Thomas on 11/01/2016.
 */
public class JsonFactory<T> {

    /**
     * Référence vers la classe concernée
     */
    private Class<T> ref;

    /**
     * Constructeur
     * @param reference Une référence vers la classe concernée
     */
    public JsonFactory(Class<T> reference) {
        this.ref = reference;
    }

    /**
     * Serialiser un objet en JSON
     * TODO Gérer les tableaux
     * @param objet L'objet à sérialiser
     * @return Le JSONObject correspondant à l'objet serialisé
     */
    public JSONObject serialize(T objet) {

        Field[] membres = ref.getDeclaredFields(); //Liste des membres de la classe
        JSONObject obj = new JSONObject(); //Objet JSON sérialisé

        for(Field membre : membres) { //On parcourt chacun des membres de la classe
            if(membre.isAnnotationPresent(Serializable.class)) { //La présence de l'annotation Serialisable indique qu'on doit l'ajouter au JSON
                membre.setAccessible(true); //On rend le membre accessible

                if(membre.getType().isPrimitive()) { //Si c'est un type primitf, on peut le serialiser directement
                    try {
                        obj.put(membre.getName(), membre.get(objet));
                    } catch (JSONException exc) {
                        ErrorHandler.continuer(String.format("Impossible de serialiser le membre '%s'.", membre.getName()), exc);
                    } catch (IllegalAccessException exc) {
                        ErrorHandler.continuer(String.format("Accès illégal au membre '%s'.", membre.getName()), exc);
                    }
                } else { //Si ce n'est pas un type primitif, on essaie de le sérialiser
                    try {
                        obj.put(membre.getName(), new JsonFactory(membre.getType()).serialize(membre.get(objet)));
                    } catch (JSONException exc) {
                        ErrorHandler.continuer(String.format("Impossible de serialiser le membre '%s'.", membre.getName()), exc);
                    } catch (IllegalAccessException exc) {
                        ErrorHandler.continuer(String.format("Accès illégal au membre '%s'.", membre.getName()), exc);
                    }
                }


            }
        }

        return obj;
    }

    public T unserialize(String data) {

        //TODO À coder

        try {
            return this.ref.getConstructor().newInstance();
        } catch (Exception exc) {
            ErrorHandler.arreter("Impossible d'instantier la classe.", exc);
            return null;
        }
    }

}
