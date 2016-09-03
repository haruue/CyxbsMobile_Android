package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;

import static com.mredrock.cyxbs.util.UIUtils.hasSoftKeys;

/**
 * 自适应的 Navigation Bar 空白 View ，请加入到 ScrollView 底端以及作为 RecyclerView 的最后一个 View
 * 需要适配的内容: values/style.xml - AppTheme.FitsSystemWindow
 * SDK_INT < 19 - 沉浸式不可用，navigation margin = 0
 * SDK_INT >= 19 - 沉浸式可用，如果有 navigation bar, margin 为其高度
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class NavigationBarMarginView extends View {

    public NavigationBarMarginView(Context context) {
        super(context);
    }

    public NavigationBarMarginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isNeedMargin(getContext())) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getNavigationBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public static boolean isNeedMargin(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasSoftKeys(context);
    }
}
