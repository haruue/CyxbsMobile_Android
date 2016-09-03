package com.mredrock.cyxbs.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Created by Stormouble on 16/4/26.
 */
public class UIUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        Utils.checkNotNullWithException(fragmentManager);
        Utils.checkNotNullWithException(fragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, fragment.getClass().getName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public static void startAnotherFragment(@NonNull FragmentManager fragmentManager,
                                            @NonNull Fragment fromFragment,
                                            @NonNull Fragment toFragment, int frameId) {
        Utils.checkNotNullWithException(fragmentManager);
        Utils.checkNotNullWithException(fromFragment);
        Utils.checkNotNullWithException(toFragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(frameId, toFragment, toFragment.getClass().getName());
        transaction.hide(fromFragment);
        transaction.addToBackStack(toFragment.getClass().getName());
        transaction.commit();
    }

    /**
     * 判断有没有虚拟导航键
     * @param ctx Context 实例
     * @return 有没有虚拟导航键
     */
    public static boolean hasSoftKeys(Context ctx) {
        boolean hasSoftwareKeys;
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = manager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            boolean hasMenuKey = ViewConfiguration.get(ctx).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    private UIUtils() {
    }

}
