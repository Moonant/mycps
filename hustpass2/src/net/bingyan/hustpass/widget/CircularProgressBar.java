package net.bingyan.hustpass.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.bingyan.hustpass.R;

public class CircularProgressBar extends View {
	Paint paint;
	Path path;
	float totalNum, keyNum;
	//已使用
	float persent = 0.7f;
	String name = "201室";

    int app_blue = getContext().getResources().getColor(R.color.app_blue);
    int app_yellow = getContext().getResources().getColor(R.color.app_yellow);

	public CircularProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		path = new Path();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(float totalNum, float keyNum) {
		persent = keyNum / totalNum;
		this.keyNum = keyNum;
		this.totalNum = totalNum;
	}

	public void commit() {
		invalidate();
	}

	float strokeWidthBlue = 14;
	float strokeWidthYellow = 16;
	float paddingYellow = strokeWidthYellow / 2;
	float paddingBlue = strokeWidthBlue / 2 + strokeWidthYellow
			- strokeWidthBlue;
	float space = 3;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float R = getWidth() > getHeight() ? getHeight() / 2 : getWidth() / 2;
		float cx = getWidth() / 2;
		float cy = getHeight() / 2;
		paint.reset();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(app_blue);
		paint.setStrokeWidth(strokeWidthBlue);
		float padding = paddingBlue;
		RectF rect = new RectF(cx - R + padding, cy - R + padding, cx + R
				- padding, cy + R - padding);
		canvas.drawArc(rect, 90 + space, (int) (persent * 360) - space * 2,
				false, paint);
		paint.setColor(app_yellow);
		paint.setStrokeWidth(strokeWidthYellow);
		padding = paddingYellow;
		rect = new RectF(cx - R + padding, cy - R + padding, cx + R - padding,
				cy + R - padding);
		canvas.drawArc(rect, (int) (persent * 360) + 90,
				(int) ((1 - persent) * 360), false, paint);

		paint.reset();
		paint.setColor(app_blue);
		paint.setTextSize(30);
		paint.setTextAlign(Align.CENTER);
		FontMetrics fontMetrics = paint.getFontMetrics();
		float fontheight = fontMetrics.bottom - fontMetrics.top;
		canvas.drawText(name, cx, cy + (fontheight / 5.0f) + 3, paint);
	}
}
