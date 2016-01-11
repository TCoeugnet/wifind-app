package fr.ig2i.wifind.activities;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import fr.ig2i.wifind.R;
import fr.ig2i.wifind.beans.Image;
import fr.ig2i.wifind.core.Configuration;
import fr.ig2i.wifind.core.ImageLoader;
import fr.ig2i.wifind.core.WiFindApplication;
import fr.ig2i.wifind.listeners.LoadImageListener;


public class MapActivity extends ActionBarActivity implements LoadImageListener {

    public static Bitmap bmp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ((ImageView)this.findViewById(R.id.imageView)).setImageBitmap(bmp);
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
        ((ImageView)this.findViewById(R.id.imageView)).setImageBitmap(bmp);
    }
}
