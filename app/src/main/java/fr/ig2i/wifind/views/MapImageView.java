package fr.ig2i.wifind.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Thomas on 16/01/2016.
 */
public class MapImageView extends ImageView {

    //TODO Refactor and comment
    //TODO Scroll maximum

    private Marker marker;

    public MapImageView(Context context) {
        super(context);
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private PointF calculatePhysicalPosition() {

        PointF phys = new PointF();
        PointF log = this.marker.getPosition();

        phys.x = log.x + (this.getWidth() - this.marker.getIntrinsicWidth()) / 2;
        phys.y = log.y + (this.getHeight() - this.marker.getIntrinsicHeight()) / 2;

        return phys;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (getDrawable() != null) {
            PointF physicalPosition = this.calculatePhysicalPosition();
            marker.setDrawPosition(physicalPosition);
            marker.draw(canvas);
        }

    }

    public void scrollToMarker() {
        PointF scroll = this.marker.getPosition();
        Drawable drawable = this.getDrawable();

        if(drawable != null) {


            if (scroll.y < - (drawable.getIntrinsicHeight() / 2 - this.getHeight() / 2)) {
                scroll.y = - (drawable.getIntrinsicHeight() - this.getHeight()) / 2;
            }

            if (scroll.y > drawable.getIntrinsicHeight() / 2 - this.getHeight() / 2) {
                scroll.y = drawable.getIntrinsicHeight() / 2 - this.getHeight() / 2;
            }

            if (scroll.x < -(drawable.getIntrinsicWidth() / 2 - this.getWidth() / 2)) {
                scroll.x = - (drawable.getIntrinsicWidth() - this.getWidth()) / 2;
            }

            if (scroll.x > drawable.getIntrinsicWidth() / 2 - this.getWidth() / 2) {
                scroll.x = drawable.getIntrinsicWidth() / 2 - this.getWidth() / 2;
            }
        }

        scrollTo((int) scroll.x, (int) scroll.y);
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}

