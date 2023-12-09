package com.pt_plus.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.pt_plus.Dialog.BottomSheetDilaog.LangugeChageBoottomSheetDilaog;
import com.pt_plus.Fragment.FragmentAboutUs;
import com.pt_plus.Fragment.FragmentAccount;
import com.pt_plus.Fragment.FragmentCenters;
import com.pt_plus.Fragment.Explore.FragmentExplore;
import com.pt_plus.Fragment.FragmentChatDetails;
import com.pt_plus.Fragment.FragmentCondactUs;
import com.pt_plus.Fragment.FragmentGeneralTextView;
import com.pt_plus.Fragment.FragmentHome;
import com.pt_plus.Fragment.FragmentSettings;
import com.pt_plus.Fragment.FragmentStore;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.AppProgressDialog;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;


public class SuperActivity extends BaseActivity {
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public ImageView imgDrawer,imgNotifications;

    //    public final Fragment fragmentHome = new FragmentHome();
    private View appbar;
    public ImageBadgeView imageBadgeView;
    private BottomNavigationView bottomNavigationView;

    public void initAppbar() {
        imgDrawer = findViewById(R.id.img_drawer);
        imgNotifications = findViewById(R.id.img_notifiations);


        drawerLayout = findViewById(R.id.my_drawer_layout);
        appbar = findViewById(R.id.appbar_deffault);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(bottomNavItemLisner);
        setStatusBarHight(appbar);

        imageBadgeView = findViewById(R.id.img_cart);
        imageBadgeView.setBadgeValue(0)
                .setBadgeOvalAfterFirst(true)
                .setBadgeTextSize(8)
                .setMaxBadgeValue(99)
                .setBadgeBackground(getResources().getDrawable(R.drawable.cart_badge))
                .setBadgePosition(BadgePosition.TOP_RIGHT)
                .setBadgeTextStyle(Typeface.NORMAL)
                .setShowCounter(true)
                .setBadgePadding(4);

        setSideNav();
    }

    public void setSideNav() {
        try {

            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.login);
            if (isUserLoged()) {
                menuItem.setTitle(R.string.login_out);
            } else {
                menuItem.setTitle(getString(R.string.login));
            }
            MenuItem actionItem = menu.findItem(R.id.item_lang);
            View actionView = actionItem.getActionView();
            TextView textView = actionView.findViewById(R.id.txt_lang);
            if (isEnglish()) {
                textView.setText("English");
            } else {
                textView.setText("عربي");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCartCount(int qty) {
        imageBadgeView.setBadgeValue(qty);
    }

    private AppProgressDialog appProgressDialog;

    public void showProgress(boolean isShow) {
        if (appProgressDialog == null) {
            appProgressDialog = new AppProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
            appProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            appProgressDialog.setTitle("Please wait");
            appProgressDialog.setCancelable(false);
        }
        if (!appProgressDialog.isShowing() && isShow) {
            appProgressDialog.show();
        } else {
            if (appProgressDialog.isShowing()) {
                appProgressDialog.dismiss();
            }
        }
    }

    public void isCartShow(boolean show) {
        if (imageBadgeView != null) {
            if (show) {
                imageBadgeView.setVisibility(View.VISIBLE);
            } else {
                imageBadgeView.setVisibility(View.GONE);
            }
        }
    }

    public void setDrawerLock(boolean value) {
        try {
            if (drawerLayout != null) {
                if (value) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setStatusBarHight(View view) {
        try {
            view.setPadding(0, getStatusBarHeight(), 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
//        return result;
        int statusBarHeight = (int) Math.ceil(25 * this.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    public void isDefaultAppBarShow(boolean show) {
        if (appbar != null) {
            if (show) {
                appbar.setVisibility(View.VISIBLE);
            } else {
                appbar.setVisibility(View.GONE);
            }
        }
    }

    public void isDefaultBottomNavigationBarShow(boolean show) {
        if (bottomNavigationView != null) {

            if (show) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }

        }
    }

    private final NavigationBarView.OnItemSelectedListener bottomNavItemLisner = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home: {
                    if (getVisibleFragment() instanceof FragmentHome != true) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.lnr_content, new FragmentHome());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    break;
                }
                case R.id.explore: {
                    if (getVisibleFragment() instanceof FragmentExplore != true) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.lnr_content, new FragmentExplore());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                    break;
                }
                case R.id.store: {
                    if (getVisibleFragment() instanceof FragmentStore != true) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.lnr_content, new FragmentStore());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    break;
                }
                case R.id.centers: {
                    if (getVisibleFragment() instanceof FragmentCenters != true) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.lnr_content, new FragmentCenters());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    break;
                }
                case R.id.account: {
                    if (isUserLoged()) {
                        if (getVisibleFragment() instanceof FragmentAccount != true) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.lnr_content, new FragmentAccount());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    } else {
                        Intent intent = new Intent(SuperActivity.this, ActivityLogin.class);
                        startActivityForResult(intent, 111);
                        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    }

                    break;
                }


            }
            item.setChecked(true);
            return false;
        }
    };

    public void initSideMenu() {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.about_us: {
                        if (getVisibleFragment() instanceof FragmentAboutUs != true) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.lnr_content, new FragmentAboutUs());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.home: {
                        if (getVisibleFragment() instanceof FragmentHome != true) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.lnr_content, new FragmentHome());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.item_condact_us: {
                        if (getVisibleFragment() instanceof FragmentCondactUs != true) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.lnr_content, new FragmentCondactUs());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.item_privacy: {
                        if (getVisibleFragment() instanceof FragmentGeneralTextView != true) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("type", AppCons.GENERAL_WEBVIEW_TYPE_PRIVACY);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentGeneralTextView fragment2 = new FragmentGeneralTextView();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.item_lang: {
                        LangugeChageBoottomSheetDilaog dialogFragment = new LangugeChageBoottomSheetDilaog(SuperActivity.this);
//                    dialogFragment.setArguments(args);
                        dialogFragment.show(SuperActivity.this.getSupportFragmentManager(), "My  Fragment");

                        break;
                    }
                    case R.id.item_terms: {
                        if (getVisibleFragment() instanceof FragmentGeneralTextView != true) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("type", AppCons.GENERAL_WEBVIEW_TYPE_TERMECE);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentGeneralTextView fragment2 = new FragmentGeneralTextView();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.item_settings: {
                        if (getVisibleFragment() instanceof FragmentSettings != true) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.lnr_content, new FragmentSettings());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }

                        break;
                    }
                    case R.id.login: {
                        logout();


                        break;
                    }


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        /*actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lnr_content, new FragmentHome())
                .commit();
//        navigationView.getMenu().getItem(0).setChecked(true);
        View headerView = navigationView.getHeaderView(0);

        ImageView headerImage = headerView.findViewById(R.id.img_close);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void logout() {
        if (isUserLoged()) {
            UiUtils.getInstance().showConfirm(11, getString(R.string.are_you_sure_to_log_out), new PopUpCallBack() {
                @Override
                public void onOK() {
                    try {
                        AuthService authService = new AuthService(SuperActivity.this,_BaseCallBack);
                        authService.logout(ServiceNames.REQUEST_ID_LOGOUT);
                        showProgress(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancel() {

                }
            }, SuperActivity.this);
        } else {
            Intent intent = new Intent(SuperActivity.this, ActivityLogin.class);
            startActivityForResult(intent, 111);
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        }

    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public Boolean isUserLoged() {
        String access_token = (String) PrefUtils.getFromPrefs(this, PrefKeys.PREF_SESSION, "");
        if (access_token != null && !access_token.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isEnglish() {
        return (Boolean) PrefUtils.getFromPrefs(this, PrefKeys.PREF_LANG, true);

    }

    public void setScreenDir(boolean isEnglsh) {
        Locale locale = null;
        if (isEnglsh) {
            locale = new Locale("en");
        } else {
            locale = new Locale("ar");
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    private final CallBack _BaseCallBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            showProgress( false);
            try {
                if(serviceId == ServiceNames.REQUEST_ID_LOGOUT){
                    toast(AppUtils.getStringValueFromJson(jsonObject,"message"));
                    WeakReference<Context> contextWeakReference = new WeakReference<Context>(SuperActivity.this);
                    SharedPreferences sharedPrefs =
                            PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(SuperActivity.this, ActivitySpashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };
}
