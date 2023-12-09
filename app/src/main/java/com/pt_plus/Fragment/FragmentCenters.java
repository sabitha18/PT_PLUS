package com.pt_plus.Fragment;

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

import com.pt_plus.Adapter.CenterGroupTypeListAdapter;
import com.pt_plus.Adapter.CenterListAdapter;
import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.StoreSliderAdapter;
import com.pt_plus.Adapter.TrainersListAdapter;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.ProductModel;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;


public class FragmentCenters extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_centers, container, false);
        initView(view);
        return view;
    }

    private LinearLayout lnrCLubSeeAll, lnrAcadmySeeALl, lnrGymSeeAll;
    CommonCardCategoryListAdapter commonCardCategoryListAdapter;
    private CenterService centerService;
    private TrainerService trainerService;

    private void initView(View view) {
        getFragmentActivity().initAppbar();
        getFragmentActivity().isDefaultAppBarShow(true);
        getFragmentActivity().isCartShow(false);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        getFragmentActivity().setDrawerLock(false);
        initSlider(view);



        RecyclerView recyclerViewCenterCategory = view.findViewById(R.id.rcy_center_categoty);
        commonCardCategoryListAdapter = new CommonCardCategoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterCategory.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterCategory.setAdapter(commonCardCategoryListAdapter);


        centerService = new CenterService(getFragmentActivity(), callBack);
        trainerService = new TrainerService(getFragmentActivity(), callBack);
        centerService.getCenterByGroupType(ServiceNames.REQUEST_GET_CENTER_GROUP_TYPES);
        centerService.getCenterBanner(ServiceNames.REQUEST_ID_GET_CENTER_BANNER);

        trainerService.getMainCateGories(ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES);


        RecyclerView recyclerViewCenterType = view.findViewById(R.id.rcy_center_group_type);
        centerGroupTypeListAdapter = new CenterGroupTypeListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterType.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterType.setAdapter(centerGroupTypeListAdapter);

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
private CenterGroupTypeListAdapter centerGroupTypeListAdapter;
    private CenterListAdapter clubCenterListAdapter, AcdamyCenteListAdapter, gymCenterListAdapter;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_BANNER) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        processBanner(jsonArray);
                    }

                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CommonCardCategoryModel", selectedCommonCardCategoryModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentCenterCategory fragment2 = new FragmentCenterCategory();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putLong("type", AppCons.CENTER_LIST_TYPE_CATEGORY);
                        bundle.putSerializable("CommonCardCategoryModel", selectedCommonCardCategoryModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentCenterList fragment2 = new FragmentCenterList();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_MAIN_CATEGORIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        commonCardCategoryListAdapter.updateData(processMainCategory(jsonArray));
                    }
                } else if (serviceId == ServiceNames.REQUEST_GET_CENTER_GROUP_TYPES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        List<CommonCardCategoryModel> commonCardCategoryModelList = new ArrayList<>();
                        CommonCardCategoryModel commonCardCategoryModel = null;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject centersJsonOng = jsonArray.getJSONObject(i);
                            commonCardCategoryModel = new CommonCardCategoryModel();
                            JSONArray centerArray = centersJsonOng.has("centers") ? centersJsonOng.getJSONArray("centers") : null;
                            commonCardCategoryModel.id = AppUtils.getStringValueFromJson(centersJsonOng,"type_id");
                            commonCardCategoryModel.name = AppUtils.getStringValueFromJson(centersJsonOng,"type");
                            commonCardCategoryModel.centerModelList= processCenter(centerArray,  commonCardCategoryModel.name);
                            commonCardCategoryModelList.add(commonCardCategoryModel);




                        }
                        centerGroupTypeListAdapter.updateData(commonCardCategoryModelList);
                    }

                }
                getFragmentActivity().showProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    FilterModel clubFilterModel = new FilterModel();
    FilterModel acdaymFilterModel = new FilterModel();
    FilterModel gymFilterModel = new FilterModel();

    private List<CenterModel> processCenter(JSONArray jsonArray, String type) {
        List<CenterModel> centerModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CenterModel centerModel = new CenterModel();
                centerModel.center_id = AppUtils.getStringValueFromJson(jsonObject, "id");
                centerModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                centerModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                centerModel.type = type;
                centerModelList.add(centerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return centerModelList;
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

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {





            }
        }
    };
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    StoreSliderAdapter viewPagerAdapter;

    private void initSlider(View v) {

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);
        viewPagerAdapter = new StoreSliderAdapter(getFragmentActivity(), null, _handler);
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
                timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);

            }
            return homeSliderModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeSliderModelList;
    }

    java.util.Timer timer;

    private class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            if (getActivity() == null)
                return;

            getFragmentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() < dotscount - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }

    private CommonCardCategoryModel selectedCommonCardCategoryModel;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {
                timer.cancel();

//
                CommonCardCategoryModel CommonCardCategoryModel = null;
                try {
                    CommonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (CommonCardCategoryModel != null) {

                    LocalCache.getCache().getCenterFilterModel().category = null;
                    LocalCache.getCache().getCenterFilterModel().main_category = null;
                    LocalCache.getCache().getCenterFilterModel().type = null;

                    FilterModel filterModel = new FilterModel();
                    filterModel.id = CommonCardCategoryModel.id;
                    filterModel.title = CommonCardCategoryModel.name;
                    LocalCache.getCache().getCenterFilterModel().main_category = filterModel;
                    selectedCommonCardCategoryModel = CommonCardCategoryModel;
                    centerService.getCenterCategories(ServiceNames.REQUEST_ID_GET_CENTER_CATEGORIES, CommonCardCategoryModel.id);


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 12) {
                timer.cancel();

//
                CenterModel centerModel = null;
                try {
                    centerModel = (CenterModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (centerModel != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id", centerModel.center_id);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCenterDetails fragment2 = new FragmentCenterDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            } else if (message.what == 3) {

                HomeSliderModel homeSliderModel = null;
                try {
                    homeSliderModel = (HomeSliderModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (homeSliderModel != null) {
                    if (homeSliderModel.related_product_id != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("id", homeSliderModel.related_product_id);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentCenterDetails fragment2 = new FragmentCenterDetails();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }


            } else if (message.what == 18) {

                CommonCardCategoryModel commonCardCategoryModel = null;
                try {
                    commonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (commonCardCategoryModel != null) {

                    FilterModel filterModel = new FilterModel();
                    filterModel.title = commonCardCategoryModel.name;
                    filterModel.id = commonCardCategoryModel.id;

                    LocalCache.getCache().getCenterFilterModel().category = null;
                    LocalCache.getCache().getCenterFilterModel().main_category = null;
                    LocalCache.getCache().getCenterFilterModel().type = filterModel;

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.lnr_content, new FragmentCenterList());
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


}