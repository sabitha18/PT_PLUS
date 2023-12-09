package com.pt_plus.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Adapter.ActivityListAdapter;
import com.pt_plus.Adapter.GeneralTabListAdapter;
import com.pt_plus.Adapter.HorizondalTypeListAdapter;
import com.pt_plus.Adapter.TrainersFilterListAdapter;
import com.pt_plus.Model.ActivitiesModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.TrainerFilterModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentActivitiesList extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities_list, container, false);
        initView(view);
        return view;
    }

    private RecyclerView recyclerViewActivities, recyclerViewTrainerList;
    private TextView txtappBarTitle,txtNodata;
    private HorizondalTypeListAdapter horizondalTypeListAdapter;
    private TrainersFilterListAdapter trainersFilterListAdapter;
    private View appbar;
    private ImageView imgBack;
    RadioGroup radioGroup;
    private LinearLayout lnrFilter, lnrFIlterRating;
    private TrainerService trainerService;

    private void initView(View view) {
        txtNodata= view.findViewById(R.id.txt_no_data);
        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getActivitiesAll(ServiceNames.REQUEST_ID_ACTIVITIES_ALL);
        getFragmentActivity().showProgress(true);



        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.activities);

GridLayoutManager gridLayoutManager = new GridLayoutManager(getFragmentActivity(),3);
        recyclerViewActivities = view.findViewById(R.id.rcy_activities);
        activityListAdapter = new ActivityListAdapter(getFragmentActivity(), null, _handler);
//        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setLayoutManager(gridLayoutManager);
        recyclerViewActivities.setAdapter(activityListAdapter);


    }


    private ActivityListAdapter activityListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
if(serviceId == ServiceNames.REQUEST_ID_ACTIVITIES_ALL){
    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
    if (jsonArray != null && jsonArray.length() > 0) {
        txtNodata.setVisibility(View.GONE);
        activityListAdapter.updateData(processActivities(jsonArray));
    }
}
            } catch (Exception e) {
                e.printStackTrace();
            }

            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<ActivitiesModel> processActivities(JSONArray jsonArray) {
        List<ActivitiesModel> activitiesModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ActivitiesModel activitiesModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    activitiesModel = new ActivitiesModel();
                    activitiesModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    activitiesModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    activitiesModel.imgUrl = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");

                    activitiesModelList.add(activitiesModel);

                }

            }
            return activitiesModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activitiesModelList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 8) {


//
                ActivitiesModel activitiesModel = null;
                try {
                    activitiesModel = (ActivitiesModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (activitiesModel != null) {
              ;
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getTrainerFilterModel().serviceType.clear();
                    LocalCache.getCache().getTrainerFilterModel().price.clear();
                    LocalCache.getCache().getTrainerFilterModel().experience.clear();
                    LocalCache.getCache().getTrainerFilterModel().activities.clear();
                    LocalCache.getCache().getTrainerFilterModel().rating = null;
                    LocalCache.getCache().getTrainerFilterModel().availability.clear();

                    FilterModel filterModel = new FilterModel();
                    filterModel.id = activitiesModel.id;
                    filterModel.title = activitiesModel.title;
                    filterModel.isSelected = true;
                    LocalCache.getCache().getTrainerFilterModel().activities.add(filterModel);
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_FILTER_ACTIVITY_FILTER);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }


            // Your code logic goes here.
            return true;
        }
    });

    private void backpress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.ACTION_UP) {

                    goback();
                    return true;
                }
                return false;
            }
        });
    }

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

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



}