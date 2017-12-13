package com.tdyh.android.common.views.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class AutoScrollRecyclerView extends RecyclerView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int orientation = VERTICAL;

    private boolean isScrollFasterToTop=false;

    public AutoScrollRecyclerView(Context context) {
        super(context);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setScrollOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            this.orientation = HORIZONTAL;
        } else {
            this.orientation = VERTICAL;
        }

    }

    private final int MSG_WATH_START = 0x11;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WATH_START:
                    if (HORIZONTAL == orientation) {
                        smoothScrollBy(3, 0);
                    } else {
                        smoothScrollBy(0, 3);
                        if (isScrollFasterToTop && isSlideToBottom()) {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scrollToPosition(0);
                                    startScroll();
                                }
                            },3000);
                        }
                    }
                    Message message = new Message();
                    message.what = MSG_WATH_START;
                    mHandler.removeMessages(MSG_WATH_START);
                    mHandler.sendMessageDelayed(message, 20);
                    break;
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
            startScroll();
    }


    public void setIsScrollFasterToTop(boolean isScrollFasterToTop){
        this.isScrollFasterToTop=isScrollFasterToTop;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);

    }

    public void startScroll() {
        mHandler.removeMessages(MSG_WATH_START);
        Message msg = new Message();
        msg.what = MSG_WATH_START;
        mHandler.sendMessageDelayed(msg, 250);
    }

    public  boolean isSlideToBottom() {

        if (this == null || this.getChildCount()==0) {
            return false;
        }

        if (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
                >= this.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }
}
