package com.rc.ac.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.rc.ac.R;
import com.rc.ac.base.BaseActivity;
import com.rc.ac.enumeration.AppointmentType;
import com.rc.ac.fragment.AddAppointmentFragment;
import com.rc.ac.fragment.AppointmentsFragment;
import com.rc.ac.util.AllConstants;
import com.rc.ac.util.AppUtil;
import com.rc.ac.util.DataUtil;
import com.rc.ac.util.FragmentUtilsManager;
import com.rc.ac.util.Logger;
import com.reversecoder.library.storage.SessionManager;

import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;
import io.armcha.ribble.presentation.navigationview.adapter.NavigationViewAdapter;
import io.armcha.ribble.presentation.navigationview.adapter.RecyclerArrayAdapter;
import io.armcha.ribble.presentation.utils.extensions.ViewExKt;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.rc.ac.util.AllConstants.BUNDLE_KEY_MESSAGE;
import static com.rc.ac.util.AllConstants.INTENT_KEY_APPOINTMENT_TYPE;
import static com.rc.ac.util.AllConstants.SESSION_IS_USER_LOGGED_IN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeActivity extends BaseActivity {

    //Navigation drawer view
    private String TRANSLATION_X_KEY = "TRANSLATION_X_KEY";
    private String CARD_ELEVATION_KEY = "CARD_ELEVATION_KEY";
    private String SCALE_KEY = "SCALE_KEY";
    private NavigationViewAdapter navViewAdapter;
    private RecyclerView navRecyclerView;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private CardView contentHome;
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;
    private NavigationItem lastSelectedItem;

    //Header view
    private ImageView userAvatar;
    private TextView userName;
    private TextView userInfo;
    private LinearLayout llRightMenu;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);

        navView = (NavigationView) findViewById(R.id.navigation_view);
        navRecyclerView = (RecyclerView) navView.findViewById(R.id.navigation_view_recycler_view);
        userAvatar = (ImageView) navView.findViewById(R.id.userAvatar);
        userName = (TextView) navView.findViewById(R.id.userName);
        userInfo = (TextView) navView.findViewById(R.id.userInfo);
        contentHome = (CardView) findViewById(R.id.mainView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        //Set app user information
        updateUserInfo();
        initNavigationDrawer();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

//        llRightMenu.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.ADD_APPOINTMENT);
//                llRightMenu.setVisibility(View.GONE);
//                changeFragment(lastSelectedItem, new AddAppointmentFragment());
//            }
//        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                int backStackCount = fragmentManager.getBackStackEntryCount();
                Logger.d(TAG, TAG + ">>backStackCount: " + backStackCount);
                if (backStackCount > 0) {
                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
                    ;
                    if (currentFragment != null) {
                        AddAppointmentFragment addAppointmentFragment = (AddAppointmentFragment) currentFragment;
                        if (addAppointmentFragment != null) {
                            Logger.d(TAG, TAG + ">>currentFragment: " + currentFragment.getClass().getSimpleName());
                            addAppointmentFragment.initFragmentBackPress();
                        }
                        fragmentManager.popBackStack();
                    } else {
                        Logger.d(TAG, TAG + ">>No visible fragment is found");
                    }
                } else {
                    showCloseAppDialog(getResources().getString(R.string.dialog_close_app_title), getResources().getString(R.string.dialog_do_you_want_to_close_the_app));
                }
            } else {
                showCloseAppDialog(getResources().getString(R.string.dialog_close_app_title), getResources().getString(R.string.dialog_do_you_want_to_close_the_app));
            }
        }
    }

    @Override
    public void initActivityDestroyTasks() {

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /*****************************
     * Navigation drawer methods *
     *****************************/
    private void updateUserInfo() {
        AppUtil.loadImage(getActivity(), userAvatar, "http://bepza.gov.bd/app/webroot/ckfinder/userfiles/files/chairman.jpg", false, true, false);
        userName.setText("Major General S M Salahuddin Islam");
    }

    private void showLogoutDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SessionManager.setBooleanSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN, false);
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }))
                .show();
    }

    public void initNavigationDrawer() {
        //Initialize navigation menu
        navViewAdapter = new NavigationViewAdapter(getActivity());
        navViewAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleNavigationItemClick(navViewAdapter.getItem(position));
            }
        });
        navRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        navRecyclerView.setAdapter(navViewAdapter);
        navRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        navViewAdapter.addAll(DataUtil.getUserMenu(getActivity()));
        //For the very first time when activity is launched, need to initialize main fragment
        lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.APPOINTMENTS);
        Log.e("lastSelectedItem", lastSelectedItem.toString() + ">>");
        changeNavigationFragment(lastSelectedItem, new AppointmentsFragment());

        //Initialize drawer
        drawerLayout.setDrawerElevation(0f);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float moveFactor = navView.getWidth() * slideOffset;
                contentHome.setTranslationX(moveFactor);
                ViewExKt.setScale(contentHome, 1 - slideOffset / 4);
                contentHome.setCardElevation(slideOffset * AppUtil.toPx(HomeActivity.this, 10));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setLeftMenu(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setLeftMenu(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        // Drawer started opening
                    } else {
                        // Drawer started closing
                    }
                }
            }
        });

        //Initialize menu action
        setLeftMenu(true);
    }

    private void handleNavigationItemClick(final NavigationItem item) {
        if (lastSelectedItem != null && lastSelectedItem.getNavigationId() != item.getNavigationId()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (item.getNavigationId()) {
                        case APPOINTMENTS:
                            changeNavigationFragment(item, new AppointmentsFragment());
                            break;
                        case ADD_APPOINTMENT:
                            changeNavigationFragment(item, new AddAppointmentFragment());
                            break;
                        case LOGOUT:
                            showLogoutDialog(getResources().getString(R.string.dialog_log_out_title), getResources().getString(R.string.dialog_do_you_want_to_log_out_from_the_app));
                            break;
                    }
                }
            }, AllConstants.NAVIGATION_DRAWER_CLOSE_DELAY);
        }

        //Close drawer for any type of navigation item click
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void changeNavigationFragment(NavigationItem item, Fragment fragment) {
        setToolBarTitle(item.getNavigationId().getValue());

        lastSelectedItem = item;
        navViewAdapter.selectNavigationItem(lastSelectedItem.getNavigationId());
        Logger.d(TAG, "Clicked navigation item: " + lastSelectedItem.getNavigationId().getValue());
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_MESSAGE, lastSelectedItem.getNavigationId().getValue());
        if (lastSelectedItem.getNavigationId().getValue().equalsIgnoreCase(NavigationId.ADD_APPOINTMENT.getValue())) {
            bundle.putString(INTENT_KEY_APPOINTMENT_TYPE, AppointmentType.ADD.name());
        }
        fragment.setArguments(bundle);
        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, fragment);
    }

    public void changeFragment(Fragment fragment, String title) {
        setToolBarTitle(title);
        setLeftMenu(false);
        FragmentUtilsManager.changeSupportFragmentWithAnim(HomeActivity.this, fragment, true, false, FragmentUtilsManager.TransitionType.SlideHorizontal);
    }

    public void setLeftMenu(boolean isHamburger) {
        if (isHamburger) {
            leftMenu.setAnimatedImage(R.drawable.hamb, 0L);
            leftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        } else {
            leftMenu.setAnimatedImage(R.drawable.arrow_left, 0L);
            leftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initActivityBackPress();
                }
            });
        }
    }

    public void setRightMenu(boolean visibility, int drawableResId, View.OnClickListener onClickListener) {
        rightMenu.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        rightMenu.setBackgroundResource(drawableResId);
        rightMenu.setOnClickListener(onClickListener);
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    public void setLockMode(boolean isLocked) {
        drawerLayout.setDrawerLockMode(isLocked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putFloat(TRANSLATION_X_KEY, contentHome.getTranslationX());
            outState.putFloat(CARD_ELEVATION_KEY, ViewExKt.getScale(contentHome));
            outState.putFloat(SCALE_KEY, contentHome.getCardElevation());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            contentHome.setTranslationX(savedState.getFloat(TRANSLATION_X_KEY));
            ViewExKt.setScale(contentHome, savedState.getFloat(CARD_ELEVATION_KEY));
            contentHome.setCardElevation(savedState.getFloat(SCALE_KEY));
        }
    }

    private void openFragment(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof AppointmentsFragment) {
                    ((AppointmentsFragment) fragment).onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private void showCloseAppDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }))
                .show();
    }
}