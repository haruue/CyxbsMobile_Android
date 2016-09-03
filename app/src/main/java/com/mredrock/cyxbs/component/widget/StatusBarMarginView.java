package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;
import com.mredrock.cyxbs.R;

/**
 * 自适应 Status Bar 的空白 View ，请加入到所有 Activity 以及 Drawer 的 Layout 顶部
 * 需要适配的内容 values/style.xml - AppTheme.FitsSystemWindow
 * SDK_INT < 21, 沉浸式不可用, margin = 0
 * SDK_INT >= 21, 沉浸式可用， margin 为 status bar 高度
 * Drawer 在 19 上的 status bar 中的绘制没有解决，似乎 Google 也是这样想的。。。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StatusBarMarginView extends View {

    boolean inNavigation;

    public StatusBarMarginView(Context context) {
        super(context);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusBarMarginView);
        inNavigation = a.getBoolean(R.styleable.StatusBarMarginView_in_navigation, false);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 兼容 SDK 19
        if (isNeedMargin(inNavigation)) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getStatusBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public static boolean isNeedMargin(boolean inNavigation) {
        int sdkVersion = inNavigation ? Build.VERSION_CODES.LOLLIPOP : Build.VERSION_CODES.KITKAT;
        return Build.VERSION.SDK_INT >= sdkVersion;
    }
}
