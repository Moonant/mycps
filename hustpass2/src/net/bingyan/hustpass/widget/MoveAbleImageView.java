package net.bingyan.hustpass.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.bingyan.hustpass.util.AppLog;

/**
 * Created by ant on 14-8-4.
 */
public class MoveAbleImageView extends ImageView implements View.OnTouchListener{

    AppLog mLog = new AppLog(getClass());

    private int _xDelta;
    private int _yDelta;
    int is_moved_x = 0;
    int is_moved_y = 0;
    int is_moved_xy = 0;

    int sensitivity = 8;

    OnClickListener onClickListener;

    public  MoveAbleImageView(Context context){
        super(context);
        setOnTouchListener(this);
    }
    /**
     * 该构造方法在静态引入XML文件中是必须的
     * @param context
     * @param paramAttributeSet
     */
    public MoveAbleImageView(Context context,AttributeSet paramAttributeSet){
        super(context,paramAttributeSet);
        setOnTouchListener(this);
    }

    public void setSensitivity(int sensitivity){
        this.sensitivity = sensitivity;
    }


    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                is_moved_x = X;
                is_moved_y = Y;
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                _xDelta = X;
                _yDelta = Y;
                break;
            case MotionEvent.ACTION_UP:
                if (is_moved_xy < sensitivity) {
                    mLog.v("go to onclick");
                    if(this.onClickListener!=null){
                        onClickListener.onClick(this);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                is_moved_xy = Math.abs(X - is_moved_x)
                        + Math.abs(Y - is_moved_y);
                layoutParams.rightMargin -= X - _xDelta;
                layoutParams.bottomMargin -= Y - _yDelta;
                view.setLayoutParams(layoutParams);
                _xDelta = X;
                _yDelta = Y;
                break;
        }
        return true;
    }
}
