package com.pt_plus.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONObject;


public class FragmentAccount extends SuperFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        return view;
    }

    private View appbar;
    private CollapsingToolbarLayout appBarLayout;
    private LinearLayout lnrEditProfile, lnrBookingHistory, lnrOrderHistory, lnrSavedAddresss, lnrFavoraties, lnrAddAddress;
    private LinearLayout lnrChatHistory, lnrCardDetails, lnrGallery, lnrCenterBookingHistory, lnrLogoUt;
    private ImageView imgBack, imgUser;
    private TextView txtProfileName, txtEmail;

    private void initView(View view) {
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);


        imgUser = view.findViewById(R.id.img_user);
        txtProfileName = view.findViewById(R.id.txt_profile_name);
        txtEmail = view.findViewById(R.id.txt_email);

        appbar = view.findViewById(R.id.appbar);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        getFragmentActivity().setStatusBarHight(appbar);
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) appBarLayout.getLayoutParams();
        p.setScrollFlags(0);
//        p.setScrollFlags(-1);

        appBarLayout.setLayoutParams(p);

        lnrEditProfile = view.findViewById(R.id.lnr_edit_profile);
        lnrEditProfile.setOnClickListener(_click);
        lnrOrderHistory = view.findViewById(R.id.lnr_order_history);
        lnrOrderHistory.setOnClickListener(_click);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        lnrLogoUt = view.findViewById(R.id.lnr_logout);
        lnrLogoUt.setOnClickListener(_click);

        lnrBookingHistory = view.findViewById(R.id.lnr_booking_history);
        lnrBookingHistory.setOnClickListener(_click);

        lnrSavedAddresss = view.findViewById(R.id.lnr_saved_address);
        lnrSavedAddresss.setOnClickListener(_click);

        lnrFavoraties = view.findViewById(R.id.lnr_favorites);
        lnrFavoraties.setOnClickListener(_click);

        lnrAddAddress = view.findViewById(R.id.lnr_add_address);
        lnrAddAddress.setOnClickListener(_click);

        lnrChatHistory = view.findViewById(R.id.lnr_chat_history);
        lnrChatHistory.setOnClickListener(_click);

        lnrCardDetails = view.findViewById(R.id.lnr_card_details);
        lnrCardDetails.setOnClickListener(_click);

        lnrCenterBookingHistory = view.findViewById(R.id.lnr_center_booking_history);
        lnrCenterBookingHistory.setOnClickListener(_click);

        lnrGallery = view.findViewById(R.id.lnr_gallery);
        lnrGallery.setOnClickListener(_click);
        if (getFragmentActivity().isUserLoged()) {
            serProfile();
        }

//        handleBack();
    }

    boolean doubleBackToExitPressedOnce = false;
    private void handleBack() {
        getFragmentActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
//                    super.onBackPressed();
                    getActivity().finish();
                    System.exit(0);
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(getFragmentActivity(), getString(R.string.go_back_exit), Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        });
    }

    private void serProfile() {
        try {

            JSONObject jsonObject = new JSONObject(PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_PROFILE, "").toString());
            txtProfileName.setText(AppUtils.getStringValueFromJson(jsonObject, "name"));
            txtEmail.setText(AppUtils.getStringValueFromJson(jsonObject, "email"));
            Glide.with(getFragmentActivity())
                    .load(AppUtils.getStringValueFromJson(jsonObject, "profile_picture"))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imgUser);
            AppLogger.log("sadasdas  " + AppUtils.getStringValueFromJson(jsonObject, "profile_picture"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }

                case R.id.lnr_edit_profile: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentMyProfile());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_booking_history: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentBookingHistory());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_order_history: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentOrderHistory());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_saved_address: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentSavedAddresses());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_favorites: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentFavorites());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_add_address: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentAddAddress());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_chat_history: {
                    if (getFragmentActivity().isUserLoged()) {
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.lnr_content, new FragmentChatHistory());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {
                        Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                        startActivityForResult(intent, 111);
                        getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    }

                    break;
                }
                case R.id.lnr_card_details: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentCardDetails());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_gallery: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentGallery());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_center_booking_history: {
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentBookingCenterHistory());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_logout: {
                    getFragmentActivity().logout();
                    break;
                }


            }
        }
    };

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}