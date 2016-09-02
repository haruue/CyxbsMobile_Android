package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;

/**
 * 自适应 Status Bar 的空白 View ，请加入到所有 Activity 以及 Drawer 的 Layout 顶部
 * 需要适配的内容 values/style.xml - AppTheme.FitsSystemWindow
 * SDK_INT < 21, 沉浸式不可用, margin = 0
 * SDK_INT >= 21, 沉浸式可用， margin 为 status bar 高度
 * Drawer 在 19 上的 status bar 中的绘制没有解决，似乎 Google 也是这样想的。。。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StatusBarMarginView extends View {

    public StatusBarMarginView(Context context) {
        super(context);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getStatusBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

}
