package com.excitingboat.freshmanspecial.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excitingboat.freshmanspecial.R;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public abstract class NavigationBarMarginRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_NAVIGATION_MARGIN = 213542;

    @Override
    public final int getItemCount() {
        return getArrayItemCount() + 1;
    }

    public abstract int getArrayItemCount();

    @Override
    public final int getItemViewType(int position) {
        if (position == getArrayItemCount()) {
            return VIEW_TYPE_NAVIGATION_MARGIN;
        } else {
            return getArrayItemViewType(position);
        }
    }

    public int getArrayItemViewType(int position) {
        return 0;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NAVIGATION_MARGIN) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_freshman_special__navigation_bar_margin_view, parent, false);
            return new NavigationBarMarginViewHolder(v);
        } else {
            return onCreateArrayViewHolder(parent, viewType);
        }
    }

    public abstract VH onCreateArrayViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getArrayItemCount()) {
            onBindArrayViewHolder((VH) holder, position);
        }
    }

    public abstract void onBindArrayViewHolder(VH holder, int position);

    public class NavigationBarMarginViewHolder extends RecyclerView.ViewHolder {

        public NavigationBarMarginViewHolder(View itemView) {
            super(itemView);
        }

    }
}
