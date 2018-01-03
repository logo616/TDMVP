package com.tdyh.android.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * @author gzh
 * @date 2018/1/3 0003
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    public List<T> list;

    private Context context;

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), parent,false);

        return new BaseRecyclerHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);



    @Override
    public void onViewRecycled(final BaseRecyclerHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerHolder holder, final int position) {

        onBindViewHolder(holder.getViewHolder(), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, position, holder.getItemId());
                }
            });
        }

    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
