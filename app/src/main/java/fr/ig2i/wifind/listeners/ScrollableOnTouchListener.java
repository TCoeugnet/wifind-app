package fr.ig2i.wifind.listeners;

import android.util.Log;
import android.view.View;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Thomas on 15/12/2015.
 */
public class ScrollableOnTouchListener implements View.OnTouchListener {

    //TODO Refactor + Commentaires
    //TODO Débuguer

    private PointF origine = new PointF(0, 0);
    private PointF scrollMax = new PointF(0, 0);

    private boolean locked = false;
    private boolean lockedX = false;
    private boolean lockedY = false;

    private ImageScrollListener listener;

    public void setImageScrollListener(ImageScrollListener listener) {
        this.listener = listener;
    }


    public ScrollableOnTouchListener() {
    }

    public void lock() {
        this.locked = true;
    }

    public void unlobk() {
        this.locked = false;
    }

    public boolean toggle() {
        this.locked = !this.locked;
        return this.locked;
    }

    public boolean isLocked() {
        return this.locked;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(locked)
            return false;

        ImageView img = (ImageView) v; //On caste une fois ici plutôt qu'après
        Drawable bitmap = img.getDrawable();

        if(bitmap.getIntrinsicWidth() < img.getWidth()) {
            lockedX = true;
        }

        if(bitmap.getIntrinsicHeight() < img.getHeight()) {
            lockedY = true;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                origine.set(event.getX(), event.getY());
                scrollMax.set((bitmap.getIntrinsicWidth() - img.getWidth()) / 2, (bitmap.getIntrinsicHeight() - img.getHeight()) / 2);
            }
            break;
            case MotionEvent.ACTION_MOVE: {

                PointF touche   = new PointF(event.getX(), event.getY());
                PointF decalage = new PointF(origine.x - touche.x - img.getTranslationX(), origine.y - touche.y - img.getTranslationY());
                PointF scroll   = new PointF(img.getScrollX(), img.getScrollY());

                //Blocage au niveau des bordures verticales
                if(decalage.x < 0) { //Gauche
                    if(scroll.x + decalage.x < 0 - scrollMax.x) {
                        decalage.x = - scrollMax.x - scroll.x;
                    }
                } else if(decalage.x > 0) { //Droite
                    if(scroll.x + decalage.x > scrollMax.x) {
                        decalage.x = scrollMax.x - scroll.x;
                    }
                }

                //Blocage au niveau des bordures horizontales
                if(decalage.y < 0) { //Gauche
                    if(scroll.y + decalage.y < 0 - scrollMax.y) {
                        decalage.y = - scrollMax.y - scroll.y;
                    }
                } else if(decalage.y > 0) { //Droite
                    if(scroll.y + decalage.y > scrollMax.y) {
                        decalage.y = scrollMax.y - scroll.y;
                    }
                }

                img.scrollBy(lockedX ? 0 : (int) decalage.x, lockedY ? 0 : (int) decalage.y); //Décalage de l'image

                origine.set(touche);

                if(this.listener != null) {
                    this.listener.onScroll();
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                //On ne fait rien pour l'instant
            }
        }
        return true;
    }
}

