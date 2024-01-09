package com.pt_plus.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.FragmentManager;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.ReviewsListAdapter;
import com.pt_plus.Adapter.TrainerDetailsReviewProfileAdapter;
import com.pt_plus.Adapter.ProductListAdapter;
import com.pt_plus.Adapter.TranerDetailsGalleryListAdapter;
import com.pt_plus.Adapter.TranerDetailsSelectPlanListAdapter;
import com.pt_plus.Dialog.GalleryDialogFragment;
import com.pt_plus.Model.GalleryModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.PlanModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.ReviewModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.prnd.readmore.ReadMoreTextView;


public class FragmentTrainerDetails extends SuperFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RecyclerView viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trainer_details, container, false);
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
    private LinearLayout lnrMOreSpecial, linLayout, lnrPlanSellAll, lnrMessage, lnrReviews;
    MapView mMapView;
    private GoogleMap googleMap;
    private JSONObject jsonObjectNew;
    private RecyclerView recyclerViewGallery, recyclerViewSelectPlan, recyclerViewFavProducts, recyclerViewreviews;
    private TranerDetailsGalleryListAdapter tranerDetailsGalleryListAdapter;
    private TranerDetailsSelectPlanListAdapter tranerDetailsSelectPlanListAdapter;
    private ProductListAdapter tranerDetailsFavProductListAdapter;
    private ImageView imgBack, imgLocation, addToWishLIst;
    private CoordinatorLayout layoutMain;
    private ImageView imgMoreSp, imgTriner;
    private TextView txtMoreSp;
    private TrainerService trainerService;
    private StoreService storeService;
    private TextView txtTrainerName, txtTrinerCategory, txtTrinerExperiance, txtTrinerRating, txtTrinerStatus, txtAddress;
    private JSONArray trainerSpecialities;
    private ReadMoreTextView readMoreTextViewDiscription;
    private TrainersModel selectedTrainersModal;
    private CardView cardViewBookNow;
    private LinearLayout lnrProductSeeALl, lnrGallerySeeALl, lnrSpelaties, lnrGallery, lnrSelectPlan, lnrFavProducts, lnrDiescriptions;
    ShimmerFrameLayout shimmerFrameLayout;

    private void initView(View view, Bundle savedInstanceState) throws JSONException {

        lnrSpelaties = view.findViewById(R.id.lnr_spealities);
        lnrSpelaties.setVisibility(View.GONE);

        lnrFavProducts = view.findViewById(R.id.lnr_fav_products);
        lnrFavProducts.setVisibility(View.GONE);

        lnrGallery = view.findViewById(R.id.lnr_gallery);
        lnrGallery.setVisibility(View.GONE);
        lnrSelectPlan = view.findViewById(R.id.lnr_select_plan);
        lnrSelectPlan.setVisibility(View.GONE);

        lnrDiescriptions = view.findViewById(R.id.lnr_discription);
        lnrDiescriptions.setVisibility(View.GONE);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        lnrReviews = view.findViewById(R.id.lnr_reviews);
        cardViewBookNow = view.findViewById(R.id.carview_book_now);
        cardViewBookNow.setOnClickListener(_click);
        lnrMessage = view.findViewById(R.id.lnr_mesg);
        lnrMessage.setOnClickListener(_click);

        lnrGallerySeeALl = view.findViewById(R.id.lnr_gallery_see_all);
        lnrGallerySeeALl.setOnClickListener(_click);

        trainerService = new TrainerService(getFragmentActivity(), callBack);
        storeService = new StoreService(getFragmentActivity(), callBack);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedTrainersModal = (TrainersModel) bundle.getSerializable("trainersModel");
            if (selectedTrainersModal != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", selectedTrainersModal.id);
                trainerService.getTrainerDetails(ServiceNames.REQUEST_ID_GET_TRANIER_DETAIL, jsonObject);
                getFragmentActivity().showProgress(true);
            }
            // handle your code here.
        }
        lnrProductSeeALl = view.findViewById(R.id.lnr_product_see_all);
        lnrProductSeeALl.setOnClickListener(_click);

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
        imgLocation = view.findViewById(R.id.img_location);
        imgLocation.setOnClickListener(_click);
        addToWishLIst = view.findViewById(R.id.img_add_to_wish_list);
        addToWishLIst.setOnClickListener(_click);

        lnrPlanSellAll = view.findViewById(R.id.lnr_see_all_plan);
        lnrPlanSellAll.setOnClickListener(_click);

        recyclerViewGallery = view.findViewById(R.id.rcy_gellery);
        tranerDetailsGalleryListAdapter = new TranerDetailsGalleryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGallery.setAdapter(tranerDetailsGalleryListAdapter);


        recyclerViewSelectPlan = view.findViewById(R.id.rcy_select_plan);
        tranerDetailsSelectPlanListAdapter = new TranerDetailsSelectPlanListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewSelectPlan.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSelectPlan.setAdapter(tranerDetailsSelectPlanListAdapter);


        recyclerViewFavProducts = view.findViewById(R.id.rcy_fav_products);
        tranerDetailsFavProductListAdapter = new ProductListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewFavProducts.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavProducts.setAdapter(tranerDetailsFavProductListAdapter);


        recyclerViewreviews = view.findViewById(R.id.rcy_reviews);
        reviewsListAdapter = new ReviewsListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewreviews.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewreviews.setAdapter(reviewsListAdapter);

 // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        lnrMOreSpecial = view.findViewById(R.id.lnt_more_spcial);
        lnrMOreSpecial.setOnClickListener(_click);
        linLayout = (LinearLayout) view.findViewById(R.id.lnr_Specialities);


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


//        backpress(view);


        if (LocalCache.getCache().getSelectedBookingModel().isFromCenter) {
            lnrMessage.setVisibility(View.GONE);
        }

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        imgTriner.setVisibility(View.GONE);

    }

    private TrainerDetailsReviewProfileAdapter trainerDetailsReviewProfileAdapter;
    private ReviewsListAdapter reviewsListAdapter;
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
                    txtMoreSp.setText(trainerSpecialities.length() - lengthf + " More Specialities");
                    if (trainerSpecialities.length() - lengthf <= 0) {
                        imgMoreSp.setVisibility(View.GONE);
                        txtMoreSp.setVisibility(View.GONE);
                    }
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
                case R.id.lnr_mesg: {
                    if (getFragmentActivity().isUserLoged()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", selectedTrainersModal.trainer_id);
                        bundle.putString("name", selectedTrainersModal.name);
                        bundle.putString("thumbnail_img", selectedTrainersModal.profile_picture);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentChatDetails fragment2 = new FragmentChatDetails();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {
                        Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                        startActivityForResult(intent, 111);
                        getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    }


                    break;
                }
                case R.id.img_location: {
                    String url = AppUtils.getStringValueFromJson(jsonObjectNew, "map");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    break;
                }
                case R.id.lnr_see_all_plan: {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", AppCons.PLAN_LIST_TYPE_TRAINER);

                    bundle.putSerializable("trainersModel", selectedTrainersModal);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainingPlanList fragment2 = new FragmentTrainingPlanList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.img_add_to_wish_list: {

                    try {
                        if (getFragmentActivity().isUserLoged()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("trainer_id", selectedTrainersModal.id);
                            trainerService.trainerAddTowishList(ServiceNames.REQUEST_ID_GET_TRAINER_ADD_TO_WISHLIST, jsonObject);
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

                    ProccessBokkNow();
                    break;
                }
                case R.id.lnr_product_see_all: {

                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_TARINER_FAV_PRODUCTS);
                    bundle.putString("trainer_id", selectedTrainersModal.trainer_id);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_gallery_see_all: {

                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.GALLERY_LIST_TYPE_TARINER);
                    bundle.putString("trainer_id", selectedTrainersModal.trainer_id);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentGallery fragment2 = new FragmentGallery();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }


            }
        }
    };

    private void ProccessBokkNow() {
        try {

            LocalCache.getCache().getSelectedBookingModel().trainersModel = selectedTrainersModal;

            Bundle bundle = new Bundle();
            bundle.putInt("type", AppCons.PLAN_LIST_TYPE_TRAINER);
            bundle.putSerializable("trainersModel", selectedTrainersModal);
            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentTrainingPlanList fragment2 = new FragmentTrainingPlanList();
            fragment2.setArguments(bundle);
            fragmentTransaction.replace(R.id.lnr_content, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

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


            } else if (message.what == 4) {
                try {
                    ProductModel productModel = null;
                    try {
                        productModel = (ProductModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (productModel != null) {
                        String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");
                        if (access_token != null && !access_token.isEmpty()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("product_id", productModel.productId);
                            storeService.productAddToWishList(ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST, jsonObject);
                        } else {
                            Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                            startActivityForResult(intent, 111);
                            getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        }
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (message.what == 5) {


                ProductModel productModel = null;
                try {
                    productModel = (ProductModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productModel != null) {
                    LocalCache.getCache().clearSelectedOrder();
                    Bundle bundle = new Bundle();
                    OrderModel orderModel = new OrderModel();
                    List<ProductModel> productModelList = new ArrayList<>();
                    productModel.qty = 1;
                    productModelList.add(productModel);
                    orderModel.isSingleProductOrder = true;
                    orderModel.productModelList = productModelList;
                    LocalCache.getCache().setSelectedOrder(orderModel);
                    bundle.putString("type", "buyNow");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCheckOut fragment2 = new FragmentCheckOut();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


            } else if (message.what == 10) {


                GalleryModel galleryModel = null;
                try {
                    galleryModel = (GalleryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (galleryModel != null) {
                    Bundle args = new Bundle();
                    args.putSerializable("galleryModel", galleryModel);
                    GalleryDialogFragment dialogFragment = new GalleryDialogFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");

                }


            } else if (message.what == 13) {

                PlanModel planModel = null;
                try {
                    planModel = (PlanModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (planModel != null) {
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
                AppLogger.log("5555555555555555555555           " + jsonObject);
                if (serviceId == ServiceNames.REQUEST_ID_GET_TRANIER_DETAIL) {
                    AppLogger.log("4444444              " + jsonObject);
                    if (jsonObject != null) {
                        AppLogger.log("44444444444444444444   ");
                        jsonObjectNew = jsonObject;
                        proccessTrainerDetails(jsonObject);
                    }

                } else if (serviceId == ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_TRAINER_ADD_TO_WISHLIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                }
                shimmerFrameLayout.setVisibility(View.GONE);
                imgTriner.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
            } catch (Exception e) {
                e.printStackTrace();
            }

            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void proccessTrainerDetails(JSONObject jsonObject) {
        try {
            Glide.with(getFragmentActivity())
                    .load(AppUtils.getStringValueFromJson(jsonObject, "profile_picture"))
                    .into(imgTriner);
            AppLogger.log("proccessing ********     "+AppUtils.getStringValueFromJson(jsonObject, "map"));
            txtTrainerName.setText(AppUtils.getStringValueFromJson(jsonObject, "name"));
            JSONArray main_categories = jsonObject.has("main_categories") ? jsonObject.getJSONArray("main_categories") : null;
            String mainCategory = "";
            if (main_categories != null && main_categories.length() > 0) {
                for (int i = 0; i < main_categories.length(); i++) {
                    mainCategory = mainCategory + main_categories.get(i);
                    if ((i + 1) != main_categories.length()) {
                        mainCategory = mainCategory + " / ";
                    }
                }
            }
            selectedTrainersModal.trainer_id = selectedTrainersModal.id;

            txtTrinerCategory.setText(getString(R.string.for_new) + mainCategory);
            txtTrinerExperiance.setText(getString(R.string.experience) + AppUtils.getStringValueFromJson(jsonObject, "experience"));
            if (AppUtils.getStringValueFromJson(jsonObject, "rating") != null && !AppUtils.getStringValueFromJson(jsonObject, "rating").equalsIgnoreCase("null")) {
                txtTrinerRating.setText(AppUtils.getStringValueFromJson(jsonObject, "rating"));
            } else {
                txtTrinerRating.setText("0");
            }

            txtTrinerStatus.setText(AppUtils.getStringValueFromJson(jsonObject, "status"));
            if (AppUtils.getStringValueFromJson(jsonObject, "about") != null && !AppUtils.getStringValueFromJson(jsonObject, "about").equalsIgnoreCase("null")) {
                readMoreTextViewDiscription.setText(AppUtils.getStringValueFromJson(jsonObject, "about"));
                lnrDiescriptions.setVisibility(View.VISIBLE);
            } else {
                lnrDiescriptions.setVisibility(View.GONE);
                readMoreTextViewDiscription.setVisibility(View.GONE);
            }

            if (AppUtils.getStringValueFromJson(jsonObject, "address") != null && !AppUtils.getStringValueFromJson(jsonObject, "address").equalsIgnoreCase("null")) {
                txtAddress.setText(AppUtils.getStringValueFromJson(jsonObject, "address"));
            } else {
                txtAddress.setVisibility(View.GONE);
            }


            JSONArray gallery = jsonObject.has("gallery") ? jsonObject.getJSONArray("gallery") : null;
            if (gallery != null && gallery.length() > 0) {
                lnrGallery.setVisibility(View.VISIBLE);
                tranerDetailsGalleryListAdapter.updateData(processGallery(gallery));
            } else {
                lnrGallery.setVisibility(View.GONE);
            }

            JSONArray plans = jsonObject.has("plans") ? jsonObject.getJSONArray("plans") : null;
            if (plans != null && plans.length() > 0) {
                lnrSelectPlan.setVisibility(View.VISIBLE);
                tranerDetailsSelectPlanListAdapter.updateData(processPlanModel(plans));
            } else {
                lnrSelectPlan.setVisibility(View.GONE);
            }

            JSONArray favourite_products = jsonObject.has("favourite_products") ? jsonObject.getJSONArray("favourite_products") : null;
            if (favourite_products != null && favourite_products.length() > 0) {
                lnrFavProducts.setVisibility(View.VISIBLE);
                tranerDetailsFavProductListAdapter.updateData(proccessProducts(favourite_products));
            } else {
                lnrFavProducts.setVisibility(View.GONE);
            }
//            client recyrment to hide
            lnrFavProducts.setVisibility(View.GONE);

            JSONArray review = jsonObject.has("review") ? jsonObject.getJSONArray("review") : null;
            if (review != null && review.length() > 0) {
                List<ReviewModel> reviewModelList = proccessReview(review);
                trainerDetailsReviewProfileAdapter.updateData(reviewModelList);
                reviewModelList.remove(reviewModelList.size() - 1);
                reviewsListAdapter.updateData(reviewModelList);
                lnrReviews.setVisibility(View.VISIBLE);
            } else {
                lnrReviews.setVisibility(View.GONE);
            }

            trainerSpecialities = jsonObject.has("specialities") ? jsonObject.getJSONArray("specialities") : null;
            if (trainerSpecialities != null && trainerSpecialities.length() > 0) {
                lnrSpelaties.setVisibility(View.VISIBLE);
                String temp = null;
                for (int i = 0; i < trainerSpecialities.length(); i++) {
                    if (temp == null) {
                        temp = trainerSpecialities.getString(i);
                    } else {
//                temp = temp +" , "+trainerSpecialities.getString(i);
                    }
                }
                selectedTrainersModal.speclization = temp;
            } else {
                lnrSpelaties.setVisibility(View.GONE);
            }


            selectedTrainersModal.experiance = AppUtils.getStringValueFromJson(jsonObject, "experience");
            processSpecial(5);
            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
            double longitude = Double.parseDouble(jsonObject.getString("longitude"));
            if (longitude > 0 && latitude > 0) {
                LatLng sydney = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private List<ProductModel> proccessProducts(JSONArray jsonArray) {
        List<ProductModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                ProductModel productModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject, "product_id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModelList.add(productModel);

                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
    }

    private List<GalleryModel> processGallery(JSONArray jsonArray) {
        List<GalleryModel> galleryModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                GalleryModel galleryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    galleryModel = new GalleryModel();
                    galleryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    galleryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    galleryModel.video = AppUtils.getStringValueFromJson(jsonObject, "video");
                    galleryModel.type = AppUtils.getStringValueFromJson(jsonObject, "type");


                    galleryModelList.add(galleryModel);

                }

            }
            return galleryModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return galleryModelList;
    }

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
