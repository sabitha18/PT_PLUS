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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.HorizondalTypeListAdapter;
import com.pt_plus.Adapter.TrainersFilterListAdapter;
import com.pt_plus.Adapter.TrainingPlanListAdapter;
import com.pt_plus.Model.HorizondalTypeListModal;
import com.pt_plus.Model.PlanModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentTrainingPlanList extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_training_plan_list, container, false);
        try {
            initView(view);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private RecyclerView recyclerViewActivities, recyclerViewTrainerList;
    private TextView txtappBarTitle;

    private View appbar;
    private ImageView imgBack;
    RadioGroup radioGroup;
    private TrainingPlanListAdapter trainingPlanListAdapter;
    private TrainerService trainerService;
    private CenterService centerService;

    private void initView(View view) throws JSONException {
        trainerService = new TrainerService(getFragmentActivity(), callBack);
        centerService = new CenterService(getFragmentActivity(), callBack);

        getFragmentActivity().showProgress(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int type = bundle.getInt("type");

            if (type == AppCons.PLAN_LIST_TYPE_TRAINER) {
                TrainersModel selectedTrainersModal = (TrainersModel) bundle.getSerializable("trainersModel");
                if (selectedTrainersModal != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trainer_id", selectedTrainersModal.id);
                    trainerService.getTrainingPlanLIst(ServiceNames.REQUEST_ID_GET_TRANING_PLAN_LIST, jsonObject);
                }
            } else if (type == AppCons.PLAN_LIST_TYPE_CENTER) {
                centerService.getCenterPlanList(ServiceNames.REQUEST_GET_CENTER_PLAN_LIST,new JSONObject().put("center_id",bundle.getString("center_id")));
            }

            // handle your code here.
        }


        getFragmentActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 110);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.select_training_plan);

        RecyclerView recyclerViewCenterList = view.findViewById(R.id.rcy_trainer_list);
        trainingPlanListAdapter = new TrainingPlanListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(trainingPlanListAdapter);


//        backpress(view);
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_TRANING_PLAN_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        trainingPlanListAdapter.updateData(processPlanModel(jsonArray));
                    }
                }else  if (serviceId == ServiceNames.REQUEST_GET_CENTER_PLAN_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        trainingPlanListAdapter.updateData(processPlanModel(jsonArray));
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


    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                PlanModel planModel = null;
                try {
                    planModel = (PlanModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (planModel != null) {
                    LocalCache.getCache().getSelectedBookingModel().planModel = planModel;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("planModel", planModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentPlanDetails fragment2 = new FragmentPlanDetails();
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
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
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

    private List<PlanModel> processPlanModel(JSONArray jsonArray) {
        List<PlanModel> planModelArrayList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                PlanModel planModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    planModel = new PlanModel();
                    planModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    planModel.plan_id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    planModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    planModel.sub_title = AppUtils.getStringValueFromJson(jsonObject, "sub_title");
                    planModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    planModel.num_of_trainers = AppUtils.getStringValueFromJson(jsonObject, "num_of_trainers");
                    planModelArrayList.add(planModel);

                }

            }
            return planModelArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planModelArrayList;
    }

}