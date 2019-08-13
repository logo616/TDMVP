package com.tdyh.android.common.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.tdyh.android.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzh on 2017/9/22 0022.
 */

public class HorizontalScrollTextView extends TextView {

    private float step = 0f;
    private Paint mPaint;
    private String text;
    private float width;
    private List<String> textList = new ArrayList<String>();    //分行保存textview的显示信息。

    public HorizontalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public HorizontalScrollTextView(Context context) {
        super(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        culLines();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        culLines();
        startScroll();
    }


    private void culLines() {
        try {
            width = getWidth();
            text = getText().toString();
            float length = 0;
            if (text == null || text.length() == 0) {
                return;
            }

            //下面的代码是根据宽度和字体大小，来计算textview显示的行数。

            textList.clear();
            mPaint = getPaint();

            int lines = getLineCount();
            int linesHeight = getLineHeight();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                Log.e("textviewscroll", "" + i + text.charAt(i));
                if (length < width) {
                    builder.append(text.charAt(i));
                    length += mPaint.measureText(text.substring(i, i + 1));
                    if (i == text.length() - 1) {
                        Log.e("textviewscroll", "" + i + text.charAt(i));
                        textList.add(builder.toString());
                    }
                } else {
                    textList.add(builder.toString().substring(0, builder.toString().length() - 1));
                    builder.delete(0, builder.length() - 1);
                    length = mPaint.measureText(text.substring(i, i + 1));
                    i--;
                }

            }
        } catch (Exception ex) {

        }

    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      /*  if(textList.size()==0)  return;
        for (int i = 0; i < textList.size(); i++) {
            canvas.drawText(textList.get(i), 0, (i+1)* getFontHeight(mPaint)-step, getPaint());
        }

        float totalTextHeight=textList.size()*getFontHeight(mPaint);
        if (totalTextHeight>getHeight()) {
            invalidate();
//            step = step + 0.2f;
            step = step + 1f;
            if (step + getScrollY()>= totalTextHeight) {
                step = 0;

                if (mOnScrollerFinishListener!=null){
                    mOnScrollerFinishListener.onScrollerFinish();
                    requestLayout();
                }
                scrollTo(0, 0);
            }
        }*/
    }


    public void setOnScrollerFinishListener(OnScrollerFinishListener mOnScrollerFinishListener) {
        this.mOnScrollerFinishListener = mOnScrollerFinishListener;
    }

    OnScrollerFinishListener mOnScrollerFinishListener;

    public interface OnScrollerFinishListener {
        void onScrollerFinish();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        textList.clear();
        mHandler.removeCallbacksAndMessages(null);
    }

    private final static int MSG_WATH_SCROLL = 0x11;
    private final static int MSG_WATH_FINISH = 0x12;
    private final static long SHOW_DURATION = 10 * 1000L;
    private long duration = SHOW_DURATION;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WATH_SCROLL:
                    scrollBy(1, 0);
//                    LogUtil.d("getWidth==" +getWidth() +"   getScrollX==="+getScrollX());
                    if (getScrollX() > getWidth()) {
                        LogUtil.d("滚动完成");
                        mHandler.removeCallbacksAndMessages(null);
                        Message message = mHandler.obtainMessage();
                        message.what = MSG_WATH_FINISH;
                        mHandler.removeMessages(MSG_WATH_SCROLL);
                        mHandler.sendMessageDelayed(message, 200);

                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = MSG_WATH_SCROLL;
                        mHandler.removeMessages(MSG_WATH_SCROLL);
//                        long delay = (long) (getHeight()*0.001*5);
                        mHandler.sendMessageDelayed(message, duration / getWidth());
                    }
                    break;
                case MSG_WATH_FINISH:
                    scrollTo(0, 0);
                    finishScroll();
                    break;
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void startScroll() {
   /*     if (getLineHeight()*getLineCount() >getWidth() ) {
            mHandler.removeCallbacksAndMessages(null);
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_WATH_SCROLL;
            mHandler.sendMessageDelayed(msg, 1500);
        }else {

            Message msg = mHandler.obtainMessage();
            msg.what = MSG_WATH_FINISH;
            mHandler.sendMessageDelayed(msg, duration);
        }*/
        mHandler.removeCallbacksAndMessages(null);
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_WATH_SCROLL;
        mHandler.sendMessageDelayed(msg, 1500);
    }

    public void startScrollInvalidate() {
        mHandler.removeCallbacksAndMessages(null);
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_WATH_SCROLL;
        mHandler.sendMessageDelayed(msg, 0);
        invalidate();
    }

    private void finishScroll() {
        if (this.mOnScrollerFinishListener != null) {
            this.mOnScrollerFinishListener.onScrollerFinish();
        }
    }
}
