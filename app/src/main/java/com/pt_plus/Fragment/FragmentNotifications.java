package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt_plus.Adapter.NotificationListAdapter;
import com.pt_plus.Adapter.OrderHistoryListAdapter;
import com.pt_plus.Dialog.BottomSheetDilaog.ProductSortBoottomSheetDilaog;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.Callback.PopUpCallBack;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentNotifications extends SuperFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_notifications, container, false);
     initView(view);
        return view;
    }
    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    GeneralService generalService;
    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getString(R.string.notifications));

        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_notications);
        notificationListAdapter = new NotificationListAdapter(getFragmentActivity(), null, null);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(notificationListAdapter);

        generalService = new GeneralService(getFragmentActivity(),_callBack);
        generalService.getNotifications(ServiceNames.REQUEST_ID_GET_NOTIFICATIONS);
        getFragmentActivity().showProgress(true);
    }
    NotificationListAdapter notificationListAdapter;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            if(serviceId == ServiceNames.REQUEST_ID_GET_NOTIFICATIONS){
try {
    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
    if (jsonArray != null) {
        notificationListAdapter.updateData(jsonArray);
    }
}catch (Exception e){
    e.printStackTrace();
}
            }
            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }



            }
        }
    };

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}