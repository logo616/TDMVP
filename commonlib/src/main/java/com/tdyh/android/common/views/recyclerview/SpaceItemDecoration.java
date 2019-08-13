package com.tdyh.android.common.views.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private boolean isVertical;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
       /* if (parent.getChildAdapterPosition(view) == 0 && isVertical) {
            outRect.top = mSpace;
        }*/
        outRect.top = mSpace;
    }

    public SpaceItemDecoration(int space) {
        this(space, true);
    }

    public SpaceItemDecoration(int space, boolean isVertical) {
        this.mSpace = space;
        this.isVertical = isVertical;
    }
}
