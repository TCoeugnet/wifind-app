package fr.ig2i.wifind.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import fr.ig2i.wifind.R;
import fr.ig2i.wifind.core.WiFindApplication;

/**
 * Created by Thomas on 16/01/2016.
 */
public class Marker extends Drawable {

    //TODO Refactor and comment

    private Resources res;

    private PointF position = new PointF();
    private PointF drawPosition;
    private float scale = 0.75f;
    private int incertitude = Incertitude.FAIBLE.getValue();

    public void move(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public PointF getPosition() {
        return new PointF(this.position.x, this.position.y);
    }

    public void setIncertitude(Incertitude incertitude) {
        this.incertitude = incertitude.getValue();
    }

    public PointF getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(PointF drawPosition) {
        this.drawPosition = drawPosition;
    }

    @Override
    public void draw(Canvas canvas) {

        res = WiFindApplication.getContext().getResources();

        if (canvas != null) {
            Paint border = new Paint(), inner = new Paint(), outer = new Paint(), area = new Paint(), areaborder = new Paint();

            areaborder.setColor(res.getColor(R.color.pin_area_border_color));


            outer.setColor(res.getColor(R.color.pin_outer_color));

            border.setColor(res.getColor(R.color.pin_inner_border_color));
            border.setShadowLayer(scale * (res.getDimension(R.dimen.pin_shadow_width)), 0, 0, res.getColor(R.color.pin_drop_shadow_color));

            inner.setColor(res.getColor(R.color.pin_inner_color));

            areaborder.setAntiAlias(true);
            area.setAntiAlias(true);
            outer.setAntiAlias(true);
            border.setAntiAlias(true);
            inner.setAntiAlias(true);

            areaborder.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(drawPosition.x, drawPosition.y, incertitude + 1, areaborder);
            areaborder.setStyle(Paint.Style.FILL);
            areaborder.setColor(res.getColor(R.color.pin_area_color));
            canvas.drawCircle(drawPosition.x, drawPosition.y, incertitude, areaborder);
            canvas.drawCircle(drawPosition.x, drawPosition.y, scale * res.getDimension(R.dimen.pin_outer_diameter), outer);
            canvas.drawCircle(drawPosition.x, drawPosition.y, scale * (res.getDimension(R.dimen.pin_inner_border) + res.getDimension(R.dimen.pin_inner_diameter)), border);
            canvas.drawCircle(drawPosition.x, drawPosition.y, scale * res.getDimension(R.dimen.pin_inner_diameter), inner);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public enum Incertitude {
        FAIBLE(250),
        MOYENNE(350),
        ELEVEE(450);

        private int value;

        Incertitude(int val) {
            this.value = val;
        }

        int getValue() {
            return value;
        }
    }
}
