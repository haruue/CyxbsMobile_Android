package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.NoScrollGridView;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.ui.adapter.NavigationBarMarginRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by skylineTan on 2016/4/19 10:59.
 */
public class EmptyAdapter extends NavigationBarMarginRecyclerAdapter<EmptyRoom,
        EmptyAdapter.ViewHolder> {


    public EmptyAdapter(List<EmptyRoom> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindArrayData(ViewHolder holder, EmptyRoom data, int position) {
        if (position == 0) {
            holder.vLine.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        holder.tvBuilding.setText(data.getFloor());
        holder.gvEmptyRoom.setAdapter(new EmptyGvAdapter(mContext, data
                .getEmptyRooms()));
    }


    @Override
    public ViewHolder onCreateArrayViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empty,
                        parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.empty_left_line)
        View vLine;
        @Bind(R.id.item_empty_tv_building)
        TextView tvBuilding;
        @Bind(R.id.item_empty_gv)
        NoScrollGridView gvEmptyRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
