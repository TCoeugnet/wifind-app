package fr.ig2i.wifind.core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

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
     * @param objet L'objet à sérialiser
     * @return Le JSONObject correspondant à l'objet serialisé
     */
    public JSONObject serializeObject(T objet) {

        if(objet == null)
            return null;

        Field[] membres = objet.getClass().getDeclaredFields(); //Liste des membres de la classe
        JSONObject obj = new JSONObject(); //Objet JSON sérialisé

        for(Field membre : membres) { //On parcourt chacun des membres de la classe
            if(membre.isAnnotationPresent(Serializable.class)) { //La présence de l'annotation Serialisable indique qu'on doit l'ajouter au JSON
                membre.setAccessible(true); //On rend le membre accessible

                if(membre.getType().isPrimitive() || membre.getType().equals(String.class)) { //Si c'est un type primitf ou un string, on peut le serialiser directement
                    try {
                        if(objet != null && membre.get(objet) != null) {
                            obj.put(membre.getName(), membre.get(objet));
                        }
                    } catch (JSONException exc) {
                        ErrorHandler.continuer(String.format("Impossible de serialiser le membre '%s'.", membre.getName()), exc);
                    } catch (IllegalAccessException exc) {
                        ErrorHandler.continuer(String.format("Accès illégal au membre '%s'.", membre.getName()), exc);
                    }
                } else { //Si ce n'est pas un type primitif, on essaie de le sérialiser
                    try {
                        obj.put(membre.getName(), new JsonFactory(membre.getType()).serializeObject(membre.get(objet)));
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

    /**
     * Sérialiser un tableau en JSON
     * @param array Le tableau à sérialiser
     * @return Le JSONArray correspondant au tableau sérialisé
     */
    public JSONArray serializeArray(List<T> array) {

        JSONArray arr = new JSONArray();

        for(T elt : array) {
            arr.put(serializeObject(elt)); //On boucle simplement sur les éléments
        }

        return arr;
    }

    public T unserialize(JSONObject data) {

        T objet = null;

        try {
            objet = this.ref.getConstructor().newInstance();

            Iterator<String> it = data.keys();
            String cle;
            while(it.hasNext()){
                cle = it.next();
                Field membre = null;
                try {
                    membre = this.ref.getDeclaredField(cle);
                } catch (NoSuchFieldException exc) {}

                if(membre != null && membre.isAnnotationPresent(Serializable.class)) {
                    membre.setAccessible(true);

                    Class typeMembre = membre.getType();
                    if(typeMembre.equals(int.class)) {
                        membre.set(objet, data.getInt(cle));
                    } else if(typeMembre.equals(float.class)) {
                        membre.set(objet, (float) data.getDouble(cle));
                    } else if(typeMembre.equals(double.class)) {
                        membre.set(objet, data.getDouble(cle));
                    } else if(typeMembre.equals(String.class)) {
                        membre.set(objet, data.getString(cle));
                    } else if(typeMembre.equals(boolean.class)) {
                        membre.set(objet, data.getBoolean(cle));
                    } else if(!typeMembre.isPrimitive() && !typeMembre.equals(String.class)) {
                        JsonFactory facto = new JsonFactory(typeMembre);
                        membre.set(objet, facto.unserialize(data.getJSONObject(cle)));
                    } else {
                        ErrorHandler.continuer(String.format("Type non reconnu '%s'.", typeMembre.getName()));
                    }
                }
            }

        } catch (Exception exc) {
            ErrorHandler.arreter("Impossible d'instantier la classe.", exc);
            return null;
        }

        return objet;
    }

}
