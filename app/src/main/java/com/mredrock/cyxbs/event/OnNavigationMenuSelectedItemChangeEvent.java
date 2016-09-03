package com.mredrock.cyxbs.event;

import android.support.annotation.IdRes;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class OnNavigationMenuSelectedItemChangeEvent {

    private int itemId;

    public OnNavigationMenuSelectedItemChangeEvent(@IdRes int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }
}
