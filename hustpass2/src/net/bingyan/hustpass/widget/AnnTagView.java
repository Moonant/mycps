package net.bingyan.hustpass.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import net.bingyan.hustpass.R;

/**
 * Created by ant on 14-8-31.
 */
public class AnnTagView extends View {
    Paint paint = new Paint();
    final float scale = getResources().getDisplayMetrics().density;

    int tagColor = getContext().getResources().getColor(R.color.app_blue);
    int circleRadius = 23;
    int lineWidth = 3;
    int mode = MODE_TOP;



    static final public int MODE_TOP = 0;
    static final public int MODE_MID = 1;
    static final public int MODE_BOTTOM = 2;
    static final public int MODE_SINGLE = 3;


    public AnnTagView(Context paramContext) {
        super(paramContext);
    }

    public AnnTagView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public AnnTagView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void setMode(int mode, int colorId) {
        tagColor = getContext().getResources().getColor(colorId);
        this.mode = mode;

        try {
            postInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        lineWidth = 3;
        switch (mode) {
            case MODE_TOP:
                circleRadius = 23;
                break;
            case MODE_MID:
                circleRadius = 11;
                break;
            case MODE_BOTTOM:
                circleRadius = 11;
                break;
            case MODE_SINGLE:
                circleRadius = 23;
                break;
        }

        lineWidth = (int) (lineWidth * scale + 0.5f);
        circleRadius = (int) (circleRadius * scale + 0.5f);

        float width = getWidth();
        float height = getHeight();

        paint.setColor(tagColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(width / 2, height / 2, circleRadius, paint);

        paint.setStrokeWidth(lineWidth);

        switch (mode) {
            case MODE_TOP:
                canvas.drawLine(width / 2, height / 2, width / 2, height, paint);
                break;
            case MODE_MID:
                canvas.drawLine(width / 2, 0, width / 2, height, paint);
                break;
            case MODE_BOTTOM:
                canvas.drawLine(width / 2, height / 2, width / 2, 0, paint);
                break;
        }
    }
}
