package com.pt_plus.Fragment.Explore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pt_plus.Adapter.ExploreRecentHsitoryListAdapter;
import com.pt_plus.Adapter.FilterRecliclerviewListAdapter;
import com.pt_plus.Fragment.Explore.CustomPagerAdapter;
import com.pt_plus.Fragment.FragmentProductDetails;
import com.pt_plus.Fragment.SuperFragment;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.ExploreModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentExplore extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(false);
        getFragmentActivity().isCartShow(false);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        initView(view);
        return view;
    }

    private LinearLayout lnrMain;
    private ViewPager viewPager;
    private CustomPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    private GeneralService generalService;
    private EditText etSearch;
    private int lastVIewId = 0;

    private void initView(View view) {
        lnrMain = view.findViewById(R.id.lnr_main);
        getFragmentActivity().setStatusBarHight(lnrMain);

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        pagerAdapter = new CustomPagerAdapter(getFragmentManager(),getFragmentActivity());
        viewPager.setAdapter(pagerAdapter);

        etSearch = view.findViewById(R.id.et_search);

        generalService = new GeneralService(getFragmentActivity(), _callBack);
        search("test ");
//        getFragmentActivity().showProgress(true);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        proccessview();


        RecyclerView galleryGridView = view.findViewById(R.id.rcy_hsitory);
        exploreRecentHsitoryListAdapter = new ExploreRecentHsitoryListAdapter(getFragmentActivity(), null, _handler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getFragmentActivity(), 2);
        galleryGridView.setLayoutManager(mLayoutManager);
        galleryGridView.setAdapter(exploreRecentHsitoryListAdapter);

        setRecenTHistory();
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                       etSearch.setText(null);
                        search(null);
                        return true;
                    }
                }
                return false;
            }
        });
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

    private void setRecenTHistory() {
        try {
            JSONArray recentSearch = new JSONArray(    PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_RECENT_SEARCH,"").toString());
           AppLogger.log("hesadsa      "+recentSearch);
            exploreRecentHsitoryListAdapter.updateData(recentSearch);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ExploreRecentHsitoryListAdapter exploreRecentHsitoryListAdapter;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {
                String str = null;
                try {
                    str = (String) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (str != null) {
                    etSearch.setText(str);
                    search(str);

                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });

    private void proccessview() {




        pagerAdapter.updateData(selectedExploreModel);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter.notifyDataSetChanged();


        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(lastVIewId);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                lastVIewId = position;
            }

            @Override
            public void onPageSelected(int position) {
                lastVIewId = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter.notifyDataSetChanged();


    }

    private void search(String keyword) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keyword", keyword);
            jsonObject.put("latitude", 0);
            jsonObject.put("longitude", 0);
            addToResentSearch(keyword);
            generalService.getExploreSearch(ServiceNames.REQUEST_ID_EXPLORE, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addToResentSearch(String keyword) {
        JSONArray recentSearch = new JSONArray();;
        try {
             recentSearch = new JSONArray(PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_RECENT_SEARCH,"").toString());
             AppLogger.log("ddaafdasf         "+recentSearch);
        }catch (Exception e){
            AppLogger.log("ddaafdasf      5555   "+recentSearch);
            e.printStackTrace();
        }finally {

        }
        try {

         AppLogger.log("search history "+recentSearch);
            if(recentSearch != null && recentSearch.length() > 0){
                boolean isAdd = false;
                for(int i = 0 ; i< recentSearch.length() ;i++){
                    if (!keyword.equalsIgnoreCase(recentSearch.getString(i))){
                        isAdd = true;
                    }
                }
                if(isAdd){
                    recentSearch.put(keyword);
                    PrefUtils.saveToPrefs(getFragmentActivity(), PrefKeys.PREF_RECENT_SEARCH,recentSearch.toString());
                }

            }else {
                recentSearch.put(keyword);
                PrefUtils.saveToPrefs(getFragmentActivity(), PrefKeys.PREF_RECENT_SEARCH,recentSearch.toString());
            }
            setRecenTHistory();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ExploreModel selectedExploreModel = new ExploreModel();
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_EXPLORE) {

                    JSONArray products = jsonObject.has("products") ? jsonObject.getJSONArray("products") : null;
                    JSONArray trainers = jsonObject.has("trainers") ? jsonObject.getJSONArray("trainers") : null;
                    JSONArray centers = jsonObject.has("centers") ? jsonObject.getJSONArray("centers") : null;
                    JSONArray top = jsonObject.has("top") ? jsonObject.getJSONArray("top") : null;
                    selectedExploreModel.productModelList = proccessProducts(products);
                    selectedExploreModel.trainersModelList = processTrainer(trainers);
                    selectedExploreModel.topList = processTrainer(top);
                    selectedExploreModel.centerModelList = processMainCategory(centers);
                    proccessview();
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

    private List<ProductModel> proccessProducts(JSONArray jsonArray) {
        List<ProductModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ProductModel productModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject, "id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModel.wishlisted = AppUtils.getBooleanValueFromJson(jsonObject, "wishlisted");
                    productModelList.add(productModel);

                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
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
                    if(AppUtils.getStringValueFromJson(jsonObject, "rating") != null && !AppUtils.getStringValueFromJson(jsonObject, "rating").isEmpty() && AppUtils.getStringValueFromJson(jsonObject, "rating").equalsIgnoreCase("null") ){
                        trainersModel.rating = AppUtils.getStringValueFromJson(jsonObject, "rating");
                    }else {
                        trainersModel.rating = "0";
                    }

                    if (jsonObject.has("distance")) {
                        trainersModel.distance = AppUtils.getStringValueFromJson(jsonObject, "distance");
                    }
                    trainersModelList.add(trainersModel);

                }

            }
            return trainersModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainersModelList;
    }
}