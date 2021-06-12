package com.rc.ac.util;

import android.content.Context;

import com.rc.ac.R;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataUtil {

    private static String TAG = DataUtil.class.getSimpleName();

    public static List<NavigationItem> getUserMenu(Context context) {
        List<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(NavigationId.APPOINTMENTS, R.drawable.ic_menu_home, false, R.color.color_white));
        navigationItems.add(new NavigationItem(NavigationId.ADD_APPOINTMENT, R.drawable.ic_menu_order, false, R.color.color_white));
        navigationItems.add(new NavigationItem(NavigationId.LOGOUT, R.drawable.ic_menu_logout, false, R.color.color_white));

        return navigationItems;
    }
}