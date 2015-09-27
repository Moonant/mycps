/**
 * @author Raghav Sood
 * @version 1
 * @date 26 January, 2013
 */
package net.bingyan.hustpass.widget;

import net.bingyan.hustpass.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * The Class CircularSeekBar.
 */
public class CircularSeekBar extends View {

    int app_blue = getContext().getResources().getColor(R.color.app_blue);
    int app_yellow = getContext().getResources().getColor(R.color.app_yellow);
	
	Paint abovePaint;
	Paint belowPaint;
	private Context mContext;
	private OnSeekChangeListener mListener;
	private int angle = 0;
	private int startAngle = 270;
	private int width;
	private int height;
	private int maxProgress = 60;
	/** The current progress */
	private int progress;
	/** The progress percent */
	private int progressPercent;
	private float cx;
	private float cy;

	/** The left bound for the circle RectF */
	private float left;
	private float right;
	private float top;
	private float bottom;

	/** The X coordinate for the top left corner of the marking drawable */
	private float dx;
	private float dy;

	/**
	 * The X coordinate for the current position of the marker, pre adjustment
	 * to center
	 */
	private float markPointX;
	private float markPointY;

	/**
	 * The adjustment factor. This adds an adjustment of the specified size to
	 * both sides of the progress bar, allowing touch events to be processed
	 * more user friendlily (yes, I know that's not a word)
	 */
	private float adjustmentFactor = 100;

	/** The flag to see if view is pressed */
	private boolean IS_PRESSED = false;

	/**
	 * The flag to see if the setProgress() method was called from our own
	 * View's setAngle() method, or externally by a user.
	 */
	private boolean CALLED_FROM_ANGLE = false;
	
	private Bitmap progressMark;
	private float  Ro;
	
	private Paint clearPaint;
	private Paint textPaint;

	/** The rectangle containing our circles and arcs. */
	private RectF rect = new RectF();

	{
		mListener = new OnSeekChangeListener() {
			@Override
			public void onProgressChange(CircularSeekBar view, int newProgress) {
			}
		};
		
	}
	public CircularSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDrawable();
	}
	public CircularSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initDrawable();
	}
	public void initDrawable() {
		progressMark = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.seekbar_dot);
		abovePaint = new Paint();
		abovePaint.setColor(app_blue);
		abovePaint.setAntiAlias(true);
		abovePaint.setStyle(Paint.Style.STROKE);
		abovePaint.setStrokeWidth(15);
		
		belowPaint = new Paint();
		belowPaint.setColor(app_yellow);
		belowPaint.setStyle(Paint.Style.STROKE);
		belowPaint.setAntiAlias(true);
		belowPaint.setStrokeWidth(25);
		
		clearPaint = new Paint();
		clearPaint.setStyle(Paint.Style.STROKE);
		clearPaint.setStrokeWidth(12);	
		clearPaint.setAntiAlias(true);
		clearPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		
		textPaint = new Paint();
		textPaint.setColor(app_blue);
		textPaint.setTextAlign(Align.CENTER);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	int padding = 30;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getWidth(); // Get View Width
		height = getHeight();// Get View Height

		int size = (width > height) ? height : width; 
		cx = width / 2; // Center X for circle
		cy = height / 2; // Center Y for circle

		Ro = size/2-padding;
		left = cx - Ro; // Calculate left bound of our rect
		right = cx + Ro;// Calculate right bound of our rect
		top = cy - Ro;// Calculate top bound of our rect
		bottom = cy + Ro;// Calculate bottom bound of our rect

		markPointX = cx; // 12 O'clock X coordinate
		markPointY = cy - Ro;// 12 O'clock Y coordinate

		rect.set(left, top, right, bottom); // assign size to rect
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */

	@Override
	protected void onDraw(Canvas ca) {
		Bitmap fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(fullImage);
		dx = getXFromAngle();
		dy = getYFromAngle();
			

		canvas.drawArc(rect, startAngle+angle, 360-angle, false, abovePaint);
		canvas.drawArc(rect, startAngle, angle, false, belowPaint);

		
		canvas.save();
		canvas.translate(cx, cy); //将位置移动画纸的坐标点:		
		for(int i=2;i<angle;i+=6){
			canvas.rotate(6,0,0);
			canvas.drawLine(0, 0, 0,  -canvas.getHeight(), clearPaint);
		}		
		canvas.restore();
		


		textPaint.setTextSize(200);
		FontMetrics fontMetrics = textPaint.getFontMetrics();
		float fontheight = fontMetrics.bottom - fontMetrics.top;
		canvas.drawText(String.valueOf(getProgress()),cx,cy+(fontheight/5.0f)-10, textPaint);
		textPaint.setTextSize(36);
		canvas.drawText("千瓦-时",cx,cy+(fontheight/5.0f)+30, textPaint);
		
		canvas.drawBitmap(progressMark, dx, dy, null);
		ca.drawBitmap(fullImage, 0, 0, null);
	}

	/**
	 * Gets the X coordinate of the arc's end arm's point of intersection with
	 * the circle
	 * 
	 * @return the X coordinate
	 */
	public float getXFromAngle() {
		int size1 = progressMark.getWidth();
		int adjust = size1 ;
		markPointX = cx + (float) (Ro*Math.cos(Math.toRadians((double)getAngle())- (Math.PI /2)));
		float x = markPointX - (adjust / 2);
		return x;
	}
	public float getYFromAngle() {
		int size1 = progressMark.getHeight();
		int adjust = size1;		
		markPointY = cy + (float) (Ro*Math.sin(Math.toRadians((double)getAngle())- (Math.PI /2)));
		float y = markPointY - (adjust / 2);
		return y;
	}

	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
//		Log.i(TAG, "angle:"+angle);
		this.angle = angle;
		float donePercent = (((float) this.angle) / 360) * 100;
		float progress = (donePercent / 100) * getMaxProgress();
		setProgressPercent(Math.round(donePercent));
		CALLED_FROM_ANGLE = true;
//		Log.i(TAG, "setAngle progress:"+progress);
		setProgress(Math.round(progress));
	}


	/**
	 * Sets the seek bar change listener.
	 * 
	 * @param listener
	 *            the new seek bar change listener
	 */
	public void setSeekBarChangeListener(OnSeekChangeListener listener) {
		mListener = listener;
	}
	public OnSeekChangeListener getSeekBarChangeListener() {
		return mListener;
	}
	public interface OnSeekChangeListener {
		public void onProgressChange(CircularSeekBar view, int newProgress);
	}
	public int getMaxProgress() {
		return maxProgress;
	}
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
//		Log.i(TAG, "progress:"+progress);
		if (this.progress != progress) {
			this.progress = progress;
			if (!CALLED_FROM_ANGLE) {
//				Log.i(TAG, "progress:"+progress);
				float newPercent = ((float)this.progress* 100 / this.maxProgress) ;
				int newAngle = (int)(newPercent  * 360/ 100);
				this.setAngle(newAngle);
				
			}
			mListener.onProgressChange(this, this.getProgress());
			CALLED_FROM_ANGLE = false;
		}
	}
	public int getProgressPercent() {
		return progressPercent;
	}
	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}
	public void setProgressColor(int color) {
		abovePaint.setColor(color);
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

	/**
	 * Moved.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param up
	 *            the up
	 */
	private void moved(float x, float y, boolean up) {
		float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
		if (distance < Ro + adjustmentFactor && distance > Ro - adjustmentFactor && !up) {
			IS_PRESSED = true;

			markPointX = (float) (cx + Ro * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI /2)));
			markPointY = (float) (cy + Ro * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI /2)));
			
			float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 360.0)) % 360.0);
			// and to make it count 0-360
			if (degrees < 0) {
				degrees += 2 * Math.PI;
			}

			setAngle(Math.round(degrees));
			invalidate();

		} else {
			IS_PRESSED = false;
			invalidate();
		}

	}

	public float getAdjustmentFactor() {
		return adjustmentFactor;
	}
	public void setAdjustmentFactor(float adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}
}
