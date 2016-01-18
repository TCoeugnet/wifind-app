package fr.ig2i.wifind.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ig2i.wifind.core.Localisation;
import fr.ig2i.wifind.listeners.DataChangeListener;
import fr.ig2i.wifind.listeners.ImageScrollListener;
import fr.ig2i.wifind.views.MapImageView;
import fr.ig2i.wifind.R;
import fr.ig2i.wifind.beans.Image;
import fr.ig2i.wifind.beans.Position;
import fr.ig2i.wifind.core.ErrorHandler;
import fr.ig2i.wifind.core.ImageLoader;
import fr.ig2i.wifind.core.JsonFactory;
import fr.ig2i.wifind.listeners.LoadImageListener;
import fr.ig2i.wifind.listeners.ScrollableOnTouchListener;
import fr.ig2i.wifind.views.Marker;


public class MapActivity extends ActionBarActivity implements LoadImageListener, DataChangeListener<Position>, ImageScrollListener {

    public static Bitmap bmp = null;

    public static MapImageView carte;

    private static Marker marker;

    private static Localisation l = new Localisation();

    private ImageButton boutonCentre = null;

    private boolean centrer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        carte = (MapImageView)this.findViewById(R.id.imageView);

        String extra = getIntent().getSerializableExtra("position").toString(); //Texte passé en extra à l'intent
        Position pos = null;
        Image image;
        marker = new Marker();

        try {
            pos = new JsonFactory<>(Position.class).unserialize(new JSONObject(extra)); //Deserialisation de la position
        } catch (JSONException exc) {
            ErrorHandler.arreter("Impossible de charger la carte.", exc);
        }

        image = pos.getEtage().getPlan().getImage();
        new ImageLoader(this).asyncLoad(image); //Chargement de la carte
        marker.move(pos.getX(), pos.getY());
        carte.setMarker(marker);
        setTitle(pos.getEtage().getCentreCommercial().getNom());

        l.setListener(this);

        boutonCentre = (ImageButton) findViewById(R.id.imageButton);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBitmapLoaded(Image image, Bitmap bmp) {
        ScrollableOnTouchListener listener = new ScrollableOnTouchListener();
        listener.setImageScrollListener(this);
        carte.setOnTouchListener(listener);
        carte.setImageBitmap(bmp); //On met l'image chargée.
        carte.scrollToMarker();
        l.localiser();
    }

    @Override
    public void onDataChange(Position data) {
        marker.move(data.getX(), data.getY());
        if(centrer) {
            carte.scrollToMarker();
        }
        l.localiser();
    }

    public void onClickCentrer(View view) {
        carte.scrollToMarker();
        if(centrer == false) {
            boutonCentre.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.bouton_centrer_presse));
            centrer = true;
        }
    }

    @Override
    public void onScroll() {
        if(centrer == true) {
            centrer = false;
            boutonCentre.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.bouton_centrer));
        }
    }
}
