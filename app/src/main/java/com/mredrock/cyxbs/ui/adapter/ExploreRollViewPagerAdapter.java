package com.mredrock.cyxbs.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.mredrock.cyxbs.R;

public class ExploreRollViewPagerAdapter extends LoopPagerAdapter {

    private static final int[] IMAGE_RES = {
            R.drawable.img_cqupt1,
            R.drawable.img_cqupt2,
            R.drawable.img_cqupt3,
    };

    public ExploreRollViewPagerAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {
        ImageView view = new ImageView(viewGroup.getContext());
        Glide.with(viewGroup.getContext()).
                load(IMAGE_RES[i]).
                into(view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    protected int getRealCount() {
        return IMAGE_RES.length;
    }
}