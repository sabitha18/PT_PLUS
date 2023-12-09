package com.pt_plus.Fragment;

import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.pt_plus.Activitys.SuperActivity;


public class SuperFragment extends Fragment {
    private LinearLayout lnrActionBar, lnrBottomMenu;
    public DrawerLayout drawerLayout;

    protected SuperActivity getFragmentActivity() {
        SuperActivity fragmentActivity;
        fragmentActivity = (SuperActivity) getActivity();
        return fragmentActivity;
    }




    protected void toastInFragment(String message) {
        getFragmentActivity().toast( message);
    }

    protected void getFragmentActionBar(Fragment fragment) {
//        getFragmentActivity().initActionBar();
    }

//    protected boolean isNetworkAvailableFragment() {
////        return getFragmentActivity().isNetworkAvailable();
//    }

}
