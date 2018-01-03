package com.tdyh.android.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author gzh
 * @date 2018/1/3 0003
 */

public class BaseRecyclerHolder extends RecyclerView.ViewHolder {
    private ViewHolder viewHolder;

    public BaseRecyclerHolder(View itemView) {
        super(itemView);
        viewHolder= ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }
}
