package com.pt_plus.Fragment;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.pt_plus.Adapter.GeneralTabListAdapter;
import com.pt_plus.Adapter.HorizondalTypeListAdapter;
import com.pt_plus.Adapter.TrainersFilterListAdapter;
import com.pt_plus.Dialog.BottomSheetDilaog.LangugeChageBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.LocationBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.TrinerSortBoottomSheetDilaog;
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
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentTrainerList extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trainer_list, container, false);
        initView(view);
        return view;
    }

    private RecyclerView recyclerViewActivities, recyclerViewTrainerList;
    private TextView txtappBarTitle, txtNodata;
    private HorizondalTypeListAdapter horizondalTypeListAdapter;
    private TrainersFilterListAdapter trainersFilterListAdapter;
    private View appbar;
    private ImageView imgBack;
    RadioGroup radioGroup;
    private LinearLayout lnrFilter, lnrFIlterRating,lnrFilterLayout,lnrLocation;
    private TrainerService trainerService;


    private void initView(View view) {
        RecyclerView rcy_genter = view.findViewById(R.id.rcy_genter);
        txtNodata = view.findViewById(R.id.txt_no_data);
        lnrFilterLayout = view.findViewById(R.id.lnr_filter_layout);
        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getTrainerCateogries(ServiceNames.REQUEST_ID_GET_CATEGORIES);
        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);
        lnrFilter = view.findViewById(R.id.lnr_filter);
        recyclerViewActivities = view.findViewById(R.id.rcy_activities);
        getFragmentActivity().showProgress(true);
        Bundle bundle = this.getArguments();
        try {
            if (bundle != null) {
                Long type = bundle.getLong("type");
                if (type == AppCons.TRAINER_LIST_TYPE_ALL) {
                    trainerService.getTranerListAll(ServiceNames.REQUEST_ID_GET_TRANIER_LIST_ALL);
                }else  if (type == AppCons.TRAINER_LIST_TYPE_FILTER_ACTIVITY_FILTER) {
                    rcy_genter.setVisibility(View.GONE);
                    tranerFilter();
                }else  if (type == AppCons.TRAINER_LIST_TYPE_CENTER_TRAINERS) {
                    rcy_genter.setVisibility(View.GONE);
                    lnrFilterLayout.setVisibility(View.GONE);
                    recyclerViewActivities.setVisibility(View.GONE);
                    trainerService.getCenterTrainerList(ServiceNames.REQUEST_GET_CENTER_TRAINER_LIST, new JSONObject().put("center_id", bundle.getString("center_id")));
                }
                else {
                    tranerFilter();
                }
                // handle your code here.
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);


        getFragmentActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 110);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        lnrFilter = view.findViewById(R.id.lnr_filter);
        lnrFilter.setOnClickListener(_click);lnrLocation = view.findViewById(R.id.lnr_filter_loaction);
        lnrLocation.setOnClickListener(_click);
        lnrFIlterRating = view.findViewById(R.id.lnt_filter_rating);
        lnrFIlterRating.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.trainers);

        horizondalTypeListAdapter = new HorizondalTypeListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActivities.setAdapter(horizondalTypeListAdapter);


        recyclerViewTrainerList = view.findViewById(R.id.rcy_trainer_list);
        trainersFilterListAdapter = new TrainersFilterListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewTrainerList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTrainerList.setAdapter(trainersFilterListAdapter);



        generalTabListAdapter = new GeneralTabListAdapter(getFragmentActivity(), null, _handler);
        rcy_genter.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcy_genter.setAdapter(generalTabListAdapter);


//        requireActivity().getSupportFragmentManager().setFragmentResultListener("request Key", getViewLifecycleOwner(), new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//
//            }
//        });


//        requireActivity().getSupportFragmentManager().setFragmentResult("request_Key", new Bundle());


//        backpress(view);
    }


    private List<FilterModel> proccesHorizondalTypeList(JSONArray jsonArray, List<CommonCardCategoryModel> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            FilterModel filterModel = null;
            filterModel = new FilterModel();
            filterModel.id = "";
            filterModel.title = getString(R.string.all);
            filterModel.group = "genter";
            if (selected.size() == 0) {
                filterModel.isSelected = true;
            }
            filterModelList.add(filterModel);

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    filterModel.group = "genter";
                    for (CommonCardCategoryModel str : selected) {
                        CommonCardCategoryModel commonCardCategoryModel = new CommonCardCategoryModel();
                        commonCardCategoryModel.id =  filterModel.id;
                        commonCardCategoryModel.name =    filterModel.title ;
                        if(commonCardCategoryModel.equals( str))
                            filterModel.isSelected = true;
                    }
                    filterModelList.add(filterModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterModelList;
    }
    GeneralTabListAdapter generalTabListAdapter;

    private List<FilterModel> proccesMainCategory(JSONArray jsonArray, List<FilterModel> selected) {
        List<FilterModel> filterModelList = new ArrayList<>();
        try {
            FilterModel filterModel = null;
            filterModel = new FilterModel();
            filterModel.id = "";
            filterModel.title = getString(R.string.all);;
            filterModel.group = "genter";
            if (selected.size() == 0) {
                filterModel.isSelected = true;
            }
            filterModelList.add(filterModel);

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    filterModel = new FilterModel();
                    filterModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    filterModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    filterModel.group = "genter";
                    for (FilterModel str : selected) {
                        if (str.equals(filterModel)) {
                            filterModel.isSelected = true;
                        }
                    }
                    filterModelList.add(filterModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterModelList;
    }

    private void tranerFilter() {
        try {
            JSONObject jsonObject = new JSONObject();
            TrainerFilterModel trainerFilterModel = LocalCache.getCache().getTrainerFilterModel();


            if (trainerFilterModel.genter != null && trainerFilterModel.genter.size() > 0) {
                JSONArray genter = new JSONArray();
                for (FilterModel model : trainerFilterModel.genter) {
                    genter.put(model.id);
                }
                jsonObject.put("main_category", genter);
            }

            JSONArray categories = new JSONArray();
            if (trainerFilterModel.commonCardCategoryModel != null && trainerFilterModel.commonCardCategoryModel.size() > 0) {
                for (CommonCardCategoryModel model : trainerFilterModel.commonCardCategoryModel) {
                    if (model.id != null && !model.id.isEmpty()) {
                        categories.put(model.id);
                    }

                }
                jsonObject.put("categories", categories);
            }


            if (trainerFilterModel.price != null && trainerFilterModel.price.size() > 0) {

                jsonObject.put("price", new JSONArray(trainerFilterModel.price));

            }

            if (trainerFilterModel.experience != null && trainerFilterModel.experience.size() > 0) {

                jsonObject.put("experience", new JSONArray(trainerFilterModel.experience));

            }

            if (trainerFilterModel.activities != null && trainerFilterModel.activities.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (FilterModel model : trainerFilterModel.activities) {
                    jsonArray.put(model.id);
                }
                jsonObject.put("activities", jsonArray);

            }
            if (trainerFilterModel.serviceType != null && trainerFilterModel.serviceType.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (FilterModel model : trainerFilterModel.serviceType) {
                    jsonArray.put(model.id);
                }
                jsonObject.put("service_types", jsonArray);

            }

            if(trainerFilterModel.availability != null && trainerFilterModel.availability.size() >0){
                jsonObject.put("availability", 1);
            }

            if( trainerFilterModel.locationModel != null){
                jsonObject.put("location", trainerFilterModel.locationModel.id);
            }

if(trainerFilterModel.rating != null){
    jsonObject.put("rating", trainerFilterModel.rating);
}

            AppLogger.log("readasc        " + jsonObject);
            trainerService.trainerFilter(ServiceNames.REQUEST_ID_TRINNER_FILTER, jsonObject);
            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if ( serviceId == ServiceNames.REQUEST_GET_CENTER_TRAINER_LIST||serviceId == ServiceNames.REQUEST_ID_GET_TRANIER_LIST_ALL || serviceId == ServiceNames.REQUEST_ID_TRINNER_FILTER) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        txtNodata.setVisibility(View.GONE);

                        trainersFilterListAdapter.updateData(processTrainer(jsonArray));
                    } else {
                        trainersFilterListAdapter.getAllItems().clear();
                        trainersFilterListAdapter.notifyDataSetChanged();
                        txtNodata.setVisibility(View.VISIBLE);
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        horizondalTypeListAdapter.updateData(proccesHorizondalTypeList(jsonArray, LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        generalTabListAdapter.updateData(proccesMainCategory(jsonArray, LocalCache.getCache().getTrainerFilterModel().genter));
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

    private List<TrainersModel> processTrainer(JSONArray jsonArray) {
        List<TrainersModel> trainersModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                TrainersModel trainersModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    trainersModel = new TrainersModel();
                    trainersModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    trainersModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    trainersModel.profile_picture = AppUtils.getStringValueFromJson(jsonObject, "profile_picture");
                    trainersModel.category = AppUtils.getStringValueFromJson(jsonObject, "category");
                    trainersModel.rating = AppUtils.getStringValueFromJson(jsonObject, "rating");
                    trainersModel.experiance = AppUtils.getStringValueFromJson(jsonObject, "experience");
                    trainersModelList.add(trainersModel);

                }

            }
            return trainersModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainersModelList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


                FilterModel filterModel = null;
                try {
                    filterModel = (FilterModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (filterModel != null) {
                    List<FilterModel> list = horizondalTypeListAdapter.getAllItems();
                    CommonCardCategoryModel commonCardCategoryModel = new CommonCardCategoryModel();
                    commonCardCategoryModel.id = filterModel.id;
                    commonCardCategoryModel.name = filterModel.title;
                    for (FilterModel item : list) {
                        LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                        list.get(list.indexOf(item)).isSelected = false;
                    }
                    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.add(commonCardCategoryModel);
                    list.get(list.indexOf(filterModel)).isSelected = true;
                    horizondalTypeListAdapter.notifyDataSetChanged();
                    tranerFilter();
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 124) {


                FilterModel filterModel = null;
                try {
                    filterModel = (FilterModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (filterModel != null) {
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    List<FilterModel> list = generalTabListAdapter.getAllItems();


                    for (FilterModel item : list) {
                        list.get(list.indexOf(item)).isSelected = false;
                    }
                    if (!filterModel.id.isEmpty()) {
                        LocalCache.getCache().getTrainerFilterModel().genter.add(filterModel);
                    }

                    list.get(list.indexOf(filterModel)).isSelected = true;
                    generalTabListAdapter.notifyDataSetChanged();
                    tranerFilter();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 2) {


//
                TrainersModel trainersModel = null;
                try {
                    trainersModel = (TrainersModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (trainersModel != null) {
                    if(!LocalCache.getCache().getSelectedBookingModel().isFromCenter){
                        LocalCache.getCache().getSelectedBookingModel().trainersModel = trainersModel;

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trainersModel", trainersModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentTrainerDetails fragment2 = new FragmentTrainerDetails();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

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
                case R.id.lnt_filter_rating: {
                    TrinerSortBoottomSheetDilaog dialogFragment = new TrinerSortBoottomSheetDilaog(getFragmentActivity(), new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            tranerFilter();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");


                    break;
                }
                case R.id.lnr_filter: {

                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("trainersModel", trainersModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerFilter fragment2 = new FragmentTrainerFilter();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

//
//                    Intent intent = new Intent(getFragmentActivity(), ActivityTrainerFilter.class);
//                    getFragmentActivity().startActivityForResult(intent,11);
                    break;
                } case R.id.lnr_filter_loaction: {

                    LocationBoottomSheetDilaog dialogFragment = new LocationBoottomSheetDilaog(getFragmentActivity(), new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            tranerFilter();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
//                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");
                    break;
                }


            }
        }
    };
    private FragmentResultListener resultListener;

    // Rest of your fragment code

    public void setResultListener(FragmentResultListener listener) {
        this.resultListener = listener;
    }


}