package com.tdyh.android.common.views.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 自动滚动
 *
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
        setLayoutFrozen(true);
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

        if ( this.getChildCount()==0) {
            return false;
        }

        if (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
                >= this.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }

    /**
     * item点击监听
     */
    public static class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

        private int mLastDownX,mLastDownY;
        //该值记录了最小滑动距离
        private int touchSlop ;
        private OnItemClickListener mListener;
        //是否是单击事件
        private boolean isSingleTapUp = false;
        //是否是长按事件
        private boolean isLongPressUp = false;
        private boolean isMove = false;
        private long mDownTime;

        //内部接口，定义点击方法以及长按方法
        public  interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        public RecyclerViewClickListener(Context context, OnItemClickListener listener){
            touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            mListener = listener;
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            switch (e.getAction()){
                /**
                 *  如果是ACTION_DOWN事件，那么记录当前按下的位置，
                 *  记录当前的系统时间。
                 */
                case MotionEvent.ACTION_DOWN:
                    mLastDownX = x;
                    mLastDownY = y;
                    mDownTime = System.currentTimeMillis();
                    isMove = false;
                    break;
                /**
                 *  如果是ACTION_MOVE事件，此时根据TouchSlop判断用户在按下的时候是否滑动了，
                 *  如果滑动了，那么接下来将不处理点击事件
                 */
                case MotionEvent.ACTION_MOVE:
                    if(Math.abs(x - mLastDownX)>touchSlop || Math.abs(y - mLastDownY)>touchSlop){
                        isMove = true;
                    }
                    break;
                /**
                 *  如果是ACTION_UP事件，那么根据isMove标志位来判断是否需要处理点击事件；
                 *  根据系统时间的差值来判断是哪种事件，如果按下事件超过1s，则认为是长按事件，
                 *  否则是单击事件。
                 */
                case MotionEvent.ACTION_UP:
                    if(isMove){
                        break;
                    }
                    if(System.currentTimeMillis()-mDownTime > 1000){
                        isLongPressUp = true;
                    }else {
                        isSingleTapUp = true;
                    }
                    break;
            }
            if(isSingleTapUp ){
                //根据触摸坐标来获取childView
                View childView = rv.findChildViewUnder(e.getX(),e.getY());
                isSingleTapUp = false;
                if(childView != null){
                    //回调mListener#onItemClick方法
                    mListener.onItemClick(childView,rv.getChildLayoutPosition(childView));
                    return true;
                }
                return false;
            }
            if (isLongPressUp ){
                View childView = rv.findChildViewUnder(e.getX(),e.getY());
                isLongPressUp = false;
                if(childView != null){
                    mListener.onItemLongClick(childView, rv.getChildLayoutPosition(childView));
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
