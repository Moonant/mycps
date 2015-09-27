package net.bingyan.hustpass.widget;

import net.bingyan.hustpass.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LineSeekBar extends View {

    int app_blue = getContext().getResources().getColor(R.color.app_blue);
    int app_yellow = getContext().getResources().getColor(R.color.app_yellow);

	Context mContext;
	Paint mPaint;
	Bitmap progressMark;

	int maxProgress;
	int progress;

	Boolean IS_PRESSED;

	int lWidth;
	int width;
	int height;
	int left, right;

	float dx, dy;
	OnSeekChangeListener mListener;

	public LineSeekBar(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		mContext = context;
		initDrawable();
	}

	public LineSeekBar(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		initDrawable();
	}

	private void initDrawable() {
		// TODO Auto-Pgenerated method stub
		mPaint = new Paint();
		
		
		
		
		progressMark = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.seekbar_dot);
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setOnSeekChangeListener(OnSeekChangeListener listener) {
		mListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth(); // Get View Width
		lWidth = width - 50;
		left = 25;
		right = width - 25;
		height = getHeight();// Get View Height
		dy = height / 2;
	}

	protected void onDraw(Canvas ca) {
		Bitmap fullImage = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(fullImage);
		dx = getDx();

		mPaint.setColor(app_yellow);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeWidth(20);
		canvas.drawLine(left, dy, lWidth + left, dy, mPaint);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(8);
		mPaint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.CLEAR));
		for (int i = 1; i < 6; i++) {
			canvas.drawLine((float) i * lWidth / 6 + left, 0, (float) i
					* lWidth / 6 + left, height, mPaint);
		}
		mPaint.reset();
		mPaint.setColor(app_blue);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeWidth(20);
		canvas.drawLine(dx, dy, lWidth + left, dy, mPaint);

		canvas.drawBitmap(progressMark, dx - progressMark.getWidth() / 2, dy
				- progressMark.getHeight() / 2, null);

		ca.drawBitmap(fullImage, 0, 0, null);
	}

	public float getDx() {
		dx = left + ((float) progress) / maxProgress * lWidth;
		return dx;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		mListener.onProgressChange(progress);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean up = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moved(x, y, up);
			break;
		case MotionEvent.ACTION_MOVE:
			moved(x, y, up);
			break;
		case MotionEvent.ACTION_UP:
			up = true;
			moved(x, y, up);
			break;
		}
		return true;
	}

	private void moved(float x, float y, boolean up) {
		if (!up) {
			IS_PRESSED = true;
			if (x >= right) {
				setProgress(6);
			} else if (x <= left) {
				setProgress(0);
			} else {
				setProgress((int) ((x - left) / lWidth * maxProgress));
			}
			invalidate();
		} else {
			IS_PRESSED = false;
			invalidate();
		}
	}

	public interface OnSeekChangeListener {
		public void onProgressChange(int newProgress);
	}
}
