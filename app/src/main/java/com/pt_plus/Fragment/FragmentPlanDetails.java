package com.pt_plus.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.PlanDetailsSliderAdapter;
import com.pt_plus.Adapter.ProductListAdapter;
import com.pt_plus.Adapter.ReviewsListAdapter;
import com.pt_plus.Adapter.TrainerDetailsReviewProfileAdapter;
import com.pt_plus.Adapter.TranerDetailsGalleryListAdapter;
import com.pt_plus.Adapter.TranerDetailsSelectPlanListAdapter;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.PlanModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.ReviewModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import kr.co.prnd.readmore.ReadMoreTextView;


public class FragmentPlanDetails extends SuperFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private TextView txtPlanTitle;
    private RecyclerView viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_plan_details, container, false);
        getFragmentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getFragmentActivity().runOnUiThread(new Runnable() {
            public void run() {
                // execute your hiding/presenting the bar here
                try {
                    initView(view, savedInstanceState);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        return view;
    }

    private View appbar;
    private LinearLayout lnrMOreSpecial, linLayout, lnrPlanSellAll;
    MapView mMapView;
    private GoogleMap googleMap;
    private RecyclerView recyclerViewGallery, recyclerViewSelectPlan, recyclerViewFavProducts, recyclerViewreviews;
    private TranerDetailsGalleryListAdapter tranerDetailsGalleryListAdapter;
    private TranerDetailsSelectPlanListAdapter tranerDetailsSelectPlanListAdapter;
    private ProductListAdapter tranerDetailsFavProductListAdapter;
    private ImageView imgBack, imgLocation;
    private CoordinatorLayout layoutMain;
    private ImageView imgMoreSp, imgTriner;
    private TextView txtMoreSp;
    private TrainerService trainerService;
    private StoreService storeService;
    private TextView txtTrainerName, txtTrinerCategory, txtTrinerExperiance, txtTrinerRating, txtTrinerStatus, txtAddress;
    private JSONArray trainerSpecialities;
    private ReadMoreTextView readMoreTextViewDiscription;
    private PlanModel selectedPlanModel;

    private TextView txtPlanPrice, txtPlanSubTitle;
    private ImageView imgAddTowishList;
    private CardView carviewBookNow;
    private CenterService centerService;
    private LinearLayout lnrReviewMain;

    private void initView(View view, Bundle savedInstanceState) throws JSONException {

        lnrReviewMain = view.findViewById(R.id.lnr_review_main);
        carviewBookNow = view.findViewById(R.id.carview_book_now);
        carviewBookNow.setOnClickListener(_click);

        trainerService = new TrainerService(getFragmentActivity(), callBack);
        storeService = new StoreService(getFragmentActivity(), callBack);
        centerService = new CenterService(getFragmentActivity(), callBack);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedPlanModel = (PlanModel) bundle.getSerializable("planModel");
            AppLogger.log("rewrwerwerwerwe        "+selectedPlanModel.id);
            if (selectedPlanModel != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", selectedPlanModel.id);
                if(LocalCache.getCache().getSelectedBookingModel().isFromCenter){
                    centerService.getCenterPlanDetails(ServiceNames.REQUEST_GET_CENTER_PLAN_DETAILS, selectedPlanModel.plan_id);
                }else {
                    trainerService.getPlanDetails(ServiceNames.REQUEST_ID_GET_TRANING_PLAN_DETAILS, selectedPlanModel.id);
                }

                getFragmentActivity().showProgress(true);
            }
            // handle your code here.
        }
        txtPlanPrice = view.findViewById(R.id.plan_price);
        txtPlanTitle = view.findViewById(R.id.plan_title);
        txtPlanSubTitle = view.findViewById(R.id.txt_plan_subtitle);

        readMoreTextViewDiscription = view.findViewById(R.id.txt_Discription);
        txtTrainerName = view.findViewById(R.id.txt_trainerName);
        txtTrinerCategory = view.findViewById(R.id.txt_trainer_category);
        txtTrinerExperiance = view.findViewById(R.id.txt_triner_expiriance);
        imgTriner = view.findViewById(R.id.img_triner_img);
        txtTrinerRating = view.findViewById(R.id.txt_triner_rating);
        txtTrinerStatus = view.findViewById(R.id.txt_triner_status);
        txtAddress = view.findViewById(R.id.txt_address);

        layoutMain = view.findViewById(R.id.layout_main);
        imgMoreSp = view.findViewById(R.id.img_more_sp);
        txtMoreSp = view.findViewById(R.id.txt_more_sp);
//        setMargins(layoutMain,0,0,0,getFragmentActivity().getNavBarHeight(getFragmentActivity()));

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        imgAddTowishList = view.findViewById(R.id.img_add_to_wish_list);
        imgAddTowishList.setOnClickListener(_click);


        recyclerViewreviews = view.findViewById(R.id.rcy_reviews);
        reviewsListAdapter = new ReviewsListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewreviews.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewreviews.setAdapter(reviewsListAdapter);


        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);


        viewPager = (RecyclerView) view.findViewById(R.id.viewPager22);


        trainerDetailsReviewProfileAdapter = new TrainerDetailsReviewProfileAdapter(getFragmentActivity(), null, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentActivity());
        layoutManager.setReverseLayout(true);
        viewPager.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, true));
        viewPager.setAdapter(trainerDetailsReviewProfileAdapter);
        viewPager.addItemDecoration(new OverlapDecoration());
        viewPager.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));


        backpress(view);

        recyclerViewSelectPlan = view.findViewById(R.id.rcy_select_plan);
        tranerDetailsSelectPlanListAdapter = new TranerDetailsSelectPlanListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewSelectPlan.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSelectPlan.setAdapter(tranerDetailsSelectPlanListAdapter);

        initSlider(view);

    }

    ViewPager viewPagerSlider;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private PlanDetailsSliderAdapter homeSliderAdapter;
    TrainerDetailsReviewProfileAdapter trainerDetailsReviewProfileAdapter;
    ReviewsListAdapter reviewsListAdapter;

    private void initSlider(View v) {

        viewPagerSlider = (ViewPager) v.findViewById(R.id.viewPagerSlider);
        sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);
        homeSliderAdapter = new PlanDetailsSliderAdapter(getFragmentActivity(), null);
        viewPagerSlider.setAdapter(homeSliderAdapter);


    }

    private int lengthf;

    private void processSpecial(int length) {
        try {
            lengthf = length;
            LayoutInflater ltInflater = getLayoutInflater();

            linLayout.removeAllViews();
            for (int i = 0; i < length; i++) {

                if (trainerSpecialities != null && trainerSpecialities.length() > 0) {
                    if ((i + 1) <= trainerSpecialities.length()) {
                        View item = ltInflater.inflate(R.layout.layout_specialities, linLayout, false);
//            TextView tvName = (TextView) item.findViewById(R.id.txt_product_name);
                        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                        TextView txt_title = item.findViewById(R.id.txt_title);
                        txt_title.setText(trainerSpecialities.getString(i));
                        item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                        linLayout.addView(item);
                    }

                }


            }
            if (trainerSpecialities != null && trainerSpecialities.length() > 0) {
                if (lengthf == 5) {
                    imgMoreSp.setRotation(0);
                    txtMoreSp.setText(trainerSpecialities.length() + " More Specialities");
                } else {
                    txtMoreSp.setText("Less Specialities");
                    imgMoreSp.setRotation(270);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lnt_more_spcial: {
                    if (lengthf == 5) {
                        if (trainerSpecialities != null) {
                            processSpecial(trainerSpecialities.length());
                        }

                    } else {
                        processSpecial(5);
                    }
                    break;
                }
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.img_add_to_wish_list: {
                    try {

                        if (getFragmentActivity().isUserLoged()) {
                            if(!isWhislisted){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("plan_id", selectedPlanModel.id);
                                trainerService.planAddToWishList(ServiceNames.REQUEST_ID_GET_PLAN_ADD_TO_WISHLIST, jsonObject);
                            }else {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("plan_id", selectedPlanModel.id);
                                trainerService.planRemoveWishList(ServiceNames.REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST, jsonObject);
                            }

                        } else {
                            Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                            startActivityForResult(intent, 111);
                            getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.carview_book_now: {
                    processBookNow();
                    break;
                }


            }
        }
    };

    private void processBookNow() {
        try {
            if(LocalCache.getCache().getSelectedBookingModel().isFromCenter  ){
                Bundle bundle = new Bundle();
                bundle.putString("FROM", "Center");
                FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentAppoinment fragment2 = new FragmentAppoinment();
                fragment2.setArguments(bundle);
                fragmentTransaction.replace(R.id.lnr_content, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else {
                LocalCache.getCache().getSelectedBookingModel().planModel = selectedPlanModel;
                if (LocalCache.getCache().getSelectedBookingModel().planModel != null
                        && LocalCache.getCache().getSelectedBookingModel().trainersModel != null
                ) {
                    Bundle bundle = new Bundle();
                    bundle.putString("FROM", "PLAN");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentAppoinment fragment2 = new FragmentAppoinment();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 2) {

                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productModel", productModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductDetails fragment2 = new FragmentProductDetails();
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
        timer.cancel();
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {

                if (serviceId == ServiceNames.REQUEST_ID_GET_TRANING_PLAN_DETAILS || serviceId == ServiceNames.REQUEST_GET_CENTER_PLAN_DETAILS) {

                    JSONArray banner = jsonObject.has("banner") ? jsonObject.getJSONArray("banner") : null;
                    if (banner != null && banner.length() > 0) {
                        processSLider(banner);
                    }


                    proccessPlanDetails(jsonObject);
                    JSONArray sub_plans = jsonObject.has("sub_plans") ? jsonObject.getJSONArray("sub_plans") : null;
                    if (sub_plans != null && sub_plans.length() > 0) {
                        tranerDetailsSelectPlanListAdapter.updateData(processPlanModel(sub_plans));
                    }
                    JSONArray review = jsonObject.has("review") ? jsonObject.getJSONArray("review") : null;
                    if (review != null && review.length() > 0) {
                        List<ReviewModel> reviewModelList = proccessReview(review);
                        trainerDetailsReviewProfileAdapter.updateData(reviewModelList);
                        reviewModelList.remove(reviewModelList.size() - 1);
                        reviewsListAdapter.updateData(reviewModelList);
                    }else {
                        lnrReviewMain.setVisibility(View.GONE);
                    }


                } else if (serviceId == ServiceNames.REQUEST_ID_GET_PLAN_ADD_TO_WISHLIST) {
                    try {
                        JSONObject newjson = new JSONObject();
                        newjson.put("id", selectedPlanModel.id);
                        trainerService.getPlanDetails(ServiceNames.REQUEST_ID_GET_TRANING_PLAN_DETAILS, selectedPlanModel.id);
                        getFragmentActivity().showProgress(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                }else if (serviceId == ServiceNames.REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST) {
                    try {
                        JSONObject newjson = new JSONObject();
                        newjson.put("id", selectedPlanModel.id);
                        trainerService.getPlanDetails(ServiceNames.REQUEST_ID_GET_TRANING_PLAN_DETAILS, selectedPlanModel.id);
                        getFragmentActivity().showProgress(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
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

    private List<ReviewModel> proccessReview(JSONArray jsonArray) {
        List<ReviewModel> reviewModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ReviewModel reviewModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    reviewModel = new ReviewModel();
                    reviewModel.user_dp = AppUtils.getStringValueFromJson(jsonObject, "user_dp");
                    reviewModel.user_name = AppUtils.getStringValueFromJson(jsonObject, "user_name");
                    reviewModel.review = AppUtils.getStringValueFromJson(jsonObject, "review");
                    reviewModel.totalCount = 0;

                    reviewModelList.add(reviewModel);

                }
                reviewModel = new ReviewModel();

                reviewModel.totalCount = jsonArray.length();

                reviewModelList.add(reviewModel);

            }
            return reviewModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviewModelList;
    }
boolean isWhislisted = false;
    private void proccessPlanDetails(JSONObject jsonObject) {
        try {

            selectedPlanModel.plan_id = selectedPlanModel.id;
            selectedPlanModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");

            selectedPlanModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
            if(AppUtils.getStringValueFromJson(jsonObject, "title") != null && !AppUtils.getStringValueFromJson(jsonObject, "title").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "title").equalsIgnoreCase("null")){
                txtPlanTitle.setText(AppUtils.getStringValueFromJson(jsonObject, "title"));
            }
            if(AppUtils.getStringValueFromJson(jsonObject, "description") != null && !AppUtils.getStringValueFromJson(jsonObject, "description").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "description").equalsIgnoreCase("null")){
                readMoreTextViewDiscription.setText(AppUtils.getStringValueFromJson(jsonObject, "description"));
            }
            if(AppUtils.getStringValueFromJson(jsonObject, "sub_title") != null && !AppUtils.getStringValueFromJson(jsonObject, "sub_title").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "sub_title").equalsIgnoreCase("null")){
                txtPlanSubTitle.setText(AppUtils.getStringValueFromJson(jsonObject, "sub_title"));
            }


            if(AppUtils.getStringValueFromJson(jsonObject, "price") != null && !AppUtils.getStringValueFromJson(jsonObject, "price").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "price").equalsIgnoreCase("null")){
                txtPlanPrice.setText(AppUtils.getStringValueFromJson(jsonObject, "price") + " " + AppUtils.getStringValueFromJson(jsonObject, "currency"));

            }


                    if(AppUtils.getBooleanValueFromJson(jsonObject, "wishlisted")){
                        int tintColor = ContextCompat.getColor(getFragmentActivity(), R.color.red); // Replace with your desired tint color resource
                        imgAddTowishList.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
                        isWhislisted = true;
                    }else {
                        int tintColor = ContextCompat.getColor(getFragmentActivity(), R.color.white); // Replace with your desired tint color resource
                        imgAddTowishList.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
                        isWhislisted = false;
                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<HomeSliderModel> processSLider(JSONArray jsonArray) {
        List<HomeSliderModel> homeSliderModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                HomeSliderModel homeSliderModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    homeSliderModel = new HomeSliderModel();
                    homeSliderModel.imgUrl = jsonArray.getString(i);
                    homeSliderModelList.add(homeSliderModel);

                }


                homeSliderAdapter.updateData(homeSliderModelList);
                dotscount = homeSliderAdapter.getCount();
                dots = new ImageView[dotscount];
                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(getFragmentActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                if (dots.length > 0) {
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getFragmentActivity(), R.drawable.active_dot));
                }

                viewPagerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private List<PlanModel> processPlanModel(JSONArray jsonArray) {
        List<PlanModel> planModelArrayList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                PlanModel planModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    planModel = new PlanModel();
                    planModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    planModel.title = AppUtils.getStringValueFromJson(jsonObject, "title");
                    planModel.sub_title = AppUtils.getStringValueFromJson(jsonObject, "sub_title");
                    planModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    planModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    planModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    planModelArrayList.add(planModel);

                }

            }
            return planModelArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planModelArrayList;
    }

    private class The_slide_timer extends TimerTask {
        @Override
        public void run() {
            try {
                if(getActivity() == null)
                    return;
                getFragmentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (viewPagerSlider.getCurrentItem() < dotscount - 1) {
                            viewPagerSlider.setCurrentItem(viewPagerSlider.getCurrentItem() + 1);
                        } else
                            viewPagerSlider.setCurrentItem(0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
