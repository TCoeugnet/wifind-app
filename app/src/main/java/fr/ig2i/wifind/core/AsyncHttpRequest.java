package fr.ig2i.wifind.core;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import fr.ig2i.wifind.listeners.DataChangeListener;

/**
 * Created by Thomas on 11/01/2016.
 * @author Thomas Coeugnet
 * @version 1.0.0
 * TODO Créer classe HttpResponse qu'on instantierai avec la JsonFactory ?
 */
public class AsyncHttpRequest {

    /**
     * Requete HTTP Asynchrone
     */
    private static class AsyncRequest extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            HttpUriRequest request = (HttpUriRequest) params[0];
            ResponseHandler<String> handler = (ResponseHandler) params[1];
            String response = "";

            try {
                response = client.execute(request, handler); //Envoi de la requête
            } catch(IOException exc) {
                ErrorHandler.continuer("Erreur lors de l'execution de la requête.", exc);
            }finally {
                client.getConnectionManager().closeExpiredConnections(); //On ferme les connexions
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(s != null) {
                    listener.onDataChange(new JSONObject(s));
                }
                else {
                    listener.onDataChange(null);
                }
            } catch (JSONException exc) {
                ErrorHandler.continuer(String.format("Impossible de convertir l'objet JSON '%s'.", s), exc);
                listener.onDataChange(null);
            }
        }
    };

    final static HttpParams httpParams = new BasicHttpParams();
    /**
     * Client utilisé pour envoyer les requêtes
     */
    private static HttpClient client;
    static {
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000); //Timeout 10 secondes
        client = new DefaultHttpClient(httpParams);
    }

    /**
     * Listener appelé lorsqu'une réponse arrive
     */
    private static DataChangeListener<JSONObject> listener;

    /**
     * Modifier le listener
     * @param listener
     */
    public static void setListener(DataChangeListener<JSONObject> listener) {
        AsyncHttpRequest.listener = listener;
    }

    /**
     * Execute une requête GET
     * @param URL URL a contacter
     * @param params Paramètres GET
     * @return
     */
    public static void AsyncGet(String URL, List<NameValuePair> params) {
        HttpGet request = new HttpGet(URL); //Requête GET
        ResponseHandler<String> handler = new BasicResponseHandler(); //Pour recevoir la réponse
        String response = null; //Sring qui contiendra la réponse
        JSONObject ret = new JSONObject(); //Traduction de la réponse en JSONObject
        HttpParams parameters = new BasicHttpParams(); //Paramètres de la requête

        if(params != null) { //Si des paramètres ont été fournis...
            for (NameValuePair pair : params) {
                parameters.setParameter(pair.getName(), pair.getValue()); //On les ajoute à la requête
            }
        }

        request.setParams(parameters);

        AsyncRequest asyncReq = new AsyncRequest();
        asyncReq.execute(request, handler);
    }

    /**
     * Executer une requête POST
     * @param URL URL à contacter
     * @param data Corps de la requête
     * @return
     */
    public static void AsyncPost(String URL, String data) {
        HttpPost request = new HttpPost(URL);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = "{}";
        JSONObject ret = new JSONObject();

        try {
            if(data != null) {
                request.setEntity(new StringEntity(data.toString()));
                request.setHeader("Content-Type", "application/json");
            }
        } catch (UnsupportedEncodingException exc) {
           ErrorHandler.continuer("Encodage non supporté", exc);
        }

        AsyncRequest asyncReq = new AsyncRequest();
        asyncReq.execute(request, handler);
    }
}
