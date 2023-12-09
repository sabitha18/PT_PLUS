package com.pt_plus.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pt_plus.Adapter.ActivityListAdapter;
import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.HomeSliderAdapter;
import com.pt_plus.Adapter.TrainersListAdapter;
;
import com.pt_plus.Model.ActivitiesModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private RecyclerView recyclerViewActivities, recyclerViewTrainers;
    private ActivityListAdapter activityListAdapter;
    private TrainersListAdapter trainersListAdapter;
    private LinearLayout lnrTrianerSeeAll, lnrActivitiesSeeAll;
    private TrainerService trainerService;
    CommonCardCategoryListAdapter commonCardCategoryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getFragmentActivity().setScreenDir(getFragmentActivity().isEnglish());;
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);

        return view;
    }

    private void initView(View v) {
        getFragmentActivity().isDefaultAppBarShow(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        getFragmentActivity().setDrawerLock(false);
        getFragmentActivity().isCartShow(false);
        lnrTrianerSeeAll = v.findViewById(R.id.lnr_trainer_see_all);
        lnrTrianerSeeAll.setOnClickListener(_click);
        lnrActivitiesSeeAll = v.findViewById(R.id.lnr_activites_seeall);
        lnrActivitiesSeeAll.setOnClickListener(_click);

        recyclerViewActivities = v.findViewById(R.id.rcy_activities);
        activityListAdapter = new ActivityListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActivities.setAdapter(activityListAdapter);


        recyclerViewTrainers = v.findViewById(R.id.rcy_trainers);
        trainersListAdapter = new TrainersListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTrainers.setAdapter(trainersListAdapter);

        initSlider(v);


        RecyclerView recyclerViewCenterList = v.findViewById(R.id.rcy_center_list);
        commonCardCategoryListAdapter = new CommonCardCategoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(commonCardCategoryListAdapter);

        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getActivities(ServiceNames.REQUEST_ID_GET_ACTIVITIES);
        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);
        trainerService.getTranerShortList(ServiceNames.REQUEST_ID_GET_TRANIER_SHORT_LIST);
        trainerService.getBanner(ServiceNames.REQUEST_ID_GET_BANNER);
        getFragmentActivity().showProgress(true);

//        handleBack();

    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ACTIVITIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        activityListAdapter.updateData(processActivities(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        commonCardCategoryListAdapter.updateData(processMainCategory(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_TRANIER_SHORT_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        trainersListAdapter.updateData(processTrainer(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_BANNER) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        processBanner(jsonArray);
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CATEGORIES) {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;

                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CommonCardCategoryModel", selectedCateModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentMainCategory fragment2 = new FragmentMainCategory();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {

                        LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                        LocalCache.getCache().getTrainerFilterModel().availability.clear();
                        LocalCache.getCache().getTrainerFilterModel().serviceType.clear();
                        LocalCache.getCache().getTrainerFilterModel().price.clear();
                        LocalCache.getCache().getTrainerFilterModel().experience.clear();
                        LocalCache.getCache().getTrainerFilterModel().activities.clear();
                        LocalCache.getCache().getTrainerFilterModel().rating = null;


                        Bundle bundle = new Bundle();
                        bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_FILTER);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentTrainerList fragment2 = new FragmentTrainerList();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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


                    trainersModelList.add(trainersModel);

                }

            }
            return trainersModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainersModelList;
    }

    private List<CommonCardCategoryModel> processMainCategory(JSONArray jsonArray) {
        List<CommonCardCategoryModel> commonCardCategoryModels = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CommonCardCategoryModel commonCardCategoryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    commonCardCategoryModel = new CommonCardCategoryModel();
                    commonCardCategoryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    commonCardCategoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    commonCardCategoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");


                    commonCardCategoryModels.add(commonCardCategoryModel);

                }

            }
            return commonCardCategoryModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonCardCategoryModels;
    }

    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {
            if(getActivity() == null)
                return;
            getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (viewPager.getCurrentItem() < dotscount - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else
                            viewPager.setCurrentItem(0);
                    } catch (Exception e) {

                    }

                }
            });
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lnr_trainer_see_all: {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;

                    the_slide_timer.cancel();
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getTrainerFilterModel().serviceType.clear();
                    LocalCache.getCache().getTrainerFilterModel().price.clear();
                    LocalCache.getCache().getTrainerFilterModel().experience.clear();
                    LocalCache.getCache().getTrainerFilterModel().activities.clear();
                    LocalCache.getCache().getTrainerFilterModel().rating = null;
                    LocalCache.getCache().getTrainerFilterModel().availability.clear();
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_ALL);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_activites_seeall: {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;
                    the_slide_timer.cancel();

                    Bundle bundle = new Bundle();

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentActivitiesList fragment2 = new FragmentActivitiesList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }


            }
        }
    };
    private CommonCardCategoryModel selectedCateModel;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {
                the_slide_timer.cancel();

//
                CommonCardCategoryModel CommonCardCategoryModel = null;
                try {
                    CommonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CommonCardCategoryModel != null) {
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    FilterModel filterModel = new FilterModel();
                    filterModel.id = CommonCardCategoryModel.id;
                    filterModel.title = CommonCardCategoryModel.name;
                    LocalCache.getCache().getTrainerFilterModel().genter.add(filterModel);

                    selectedCateModel = CommonCardCategoryModel;
                    trainerService.getTrainerCateogriesById(ServiceNames.REQUEST_ID_GET_CATEGORIES, selectedCateModel.id);

                    getFragmentActivity().showProgress(true);
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 12) {


//
                TrainersModel trainersModel = null;
                try {
                    trainersModel = (TrainersModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (trainersModel != null) {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trainersModel", trainersModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerDetails fragment2 = new FragmentTrainerDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 8) {
                the_slide_timer.cancel();

//
                ActivitiesModel activitiesModel = null;
                try {
                    activitiesModel = (ActivitiesModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (activitiesModel != null) {
                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = false;

                    the_slide_timer.cancel();

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
    The_slide_timer the_slide_timer;
    HomeSliderAdapter viewPagerAdapter;

    private void initSlider(View v) {

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);
        viewPagerAdapter = new HomeSliderAdapter(getFragmentActivity(), null);
        viewPager.setAdapter(viewPagerAdapter);


    }

    private List<HomeSliderModel> processBanner(JSONArray jsonArray) {
        List<HomeSliderModel> homeSliderModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                HomeSliderModel homeSliderModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                    homeSliderModel = new HomeSliderModel();
                    homeSliderModel.btnText = "SHOP NOW";
                    homeSliderModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    homeSliderModel.description = AppUtils.getStringValueFromJson(jsonObject, "description");
                    homeSliderModel.imgUrl = AppUtils.getStringValueFromJson(jsonObject, "image");
                    homeSliderModel.related_product_id = AppUtils.getStringValueFromJson(jsonObject, "related_center_id");
//
                    homeSliderModelList.add(homeSliderModel);
                }
                viewPagerAdapter.updateData(homeSliderModelList);
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];
                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(getFragmentActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.active_dot));
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.non_active_dot));
                        }
                        dots[position].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.active_dot));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                the_slide_timer = new The_slide_timer();
                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(the_slide_timer, 2000, 3000);

            }
            return homeSliderModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeSliderModelList;
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

}