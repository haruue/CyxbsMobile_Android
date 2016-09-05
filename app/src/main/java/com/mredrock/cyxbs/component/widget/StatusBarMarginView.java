package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;

/**
 * 自适应 Status Bar 的空白 View ，请加入到所有 Activity 以及 Drawer 的 Layout 顶部
 * 需要适配的内容 values/style.xml - AppTheme.FitsSystemWindow
 * SDK_INT < 19, 沉浸式不可用, margin = 0
 * SDK_INT >= 19, 沉浸式可用， margin 为 status bar 高度
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StatusBarMarginView extends View {

    public StatusBarMarginView(Context context) {
        super(context);
        initialize();
    }

    public StatusBarMarginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initialize();
    }

    public StatusBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setFitsSystemWindows(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 兼容 SDK 19
        if (isNeedMargin()) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getStatusBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public static boolean isNeedMargin() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
