package com.pt_plus.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.HomeSliderAdapter;
import com.pt_plus.Adapter.ProductListAdapter;
import com.pt_plus.Adapter.TrainersListAdapter;
import com.pt_plus.Adapter.TranerDetailsGalleryListAdapter;
import com.pt_plus.Adapter.TranerDetailsSelectPlanListAdapter;
import com.pt_plus.Dialog.GalleryDialogFragment;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.Model.GalleryModel;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.PlanModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import kr.co.prnd.readmore.ReadMoreTextView;


public class FragmentCenterDetails extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_details, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private TextView txtAppbarTittle;
    private ImageView imgBack, imgSendMessage;
    private View appbar;
    private LinearLayout lnrTrianerSeeAll;
    MapView mMapView;
    private GoogleMap googleMap;
    private TextView txtAddress;
    private ReadMoreTextView readMoreTextViewDiscription;
    private ImageView imgFav;
    private String centerName, thumbline;
    private LinearLayout lnrSeeALlProducts,lnrSpView,lnrPlanSeeAll,lnrGallerySeeAll,lnrProductView;

    private void initView(View view, Bundle savedInstanceState) {

        lnrTrianerSeeAll = view.findViewById(R.id.lnr_trainer_see_all);
        lnrTrianerSeeAll.setOnClickListener(_click); lnrGallerySeeAll = view.findViewById(R.id.lnr_gallery_see_all);
        lnrGallerySeeAll.setOnClickListener(_click);
        lnrProductView = view.findViewById(R.id.lnr_product_view);
        lnrProductView.setVisibility(View.GONE);
        lnrPlanSeeAll = view.findViewById(R.id.lnr_plan_see_all);
        lnrPlanSeeAll.setOnClickListener(_click);
        imgFav = view.findViewById(R.id.img_fav);
        imgFav.setOnClickListener(_click);
        lnrSpView = view.findViewById(R.id.lnr_sp_view);
        lnrSpView.setVisibility(View.GONE);
        txtAddress = view.findViewById(R.id.txt_address);
        txtAppbarTittle = view.findViewById(R.id.appbar_title);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        lnrSeeALlProducts = view.findViewById(R.id.lnr_seeall_products);
        lnrSeeALlProducts.setOnClickListener(_click);

        imgSendMessage = view.findViewById(R.id.send_message);
        imgSendMessage.setOnClickListener(_click);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);

        getFragmentActivity().isDefaultAppBarShow(false);

        readMoreTextViewDiscription = view.findViewById(R.id.txt_Discription);

        lnrMOreSpecial = view.findViewById(R.id.lnt_more_spcial);
        lnrMOreSpecial.setOnClickListener(_click);
        layoutMain = view.findViewById(R.id.layout_main);
        imgMoreSp = view.findViewById(R.id.img_more_sp);
        txtMoreSp = view.findViewById(R.id.txt_more_sp);
        linLayout = (LinearLayout) view.findViewById(R.id.lnr_Specialities);



        RecyclerView recyclerViewTrainers = view.findViewById(R.id.rcy_trainers);
        trainersListAdapter = new TrainersListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTrainers.setAdapter(trainersListAdapter);


        RecyclerView recyclerViewSelectPlan = view.findViewById(R.id.rcy_select_plan);
        tranerDetailsSelectPlanListAdapter = new TranerDetailsSelectPlanListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewSelectPlan.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSelectPlan.setAdapter(tranerDetailsSelectPlanListAdapter);


        RecyclerView recyclerViewFavProducts = view.findViewById(R.id.rcy_fav_products);
        tranerDetailsFavProductListAdapter = new ProductListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewFavProducts.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavProducts.setAdapter(tranerDetailsFavProductListAdapter);


        RecyclerView recyclerViewGallery = view.findViewById(R.id.rcy_gellery);
        tranerDetailsGalleryListAdapter = new TranerDetailsGalleryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGallery.setAdapter(tranerDetailsGalleryListAdapter);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        viewPagerAdapter = new HomeSliderAdapter(getFragmentActivity(), null);
        viewPager.setAdapter(viewPagerAdapter);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (ActivityCompat.checkSelfPermission(getFragmentActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getFragmentActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(25.3548, 51.1839);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        centerService = new CenterService(getFragmentActivity(), callBack);
        JSONObject jsonObject = new JSONObject();

        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                centerId = bundle.getString("id");
                if (centerId != null) {
                    jsonObject.put("id", centerId);
                    getFragmentActivity().showProgress(true);
                    centerService.centerDetails(ServiceNames.REQUEST_ID_GET_CENTER_DETAILS, jsonObject);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        storeService = new StoreService(getFragmentActivity(), callBack);
    }

    private String centerId;
    private CenterService centerService;
    TranerDetailsGalleryListAdapter tranerDetailsGalleryListAdapter;
    TranerDetailsSelectPlanListAdapter tranerDetailsSelectPlanListAdapter;
    ProductListAdapter tranerDetailsFavProductListAdapter;
    TrainersListAdapter trainersListAdapter;
    private StoreService storeService;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_CENTER_DETAILS) {
//
                    getFragmentActivity().showProgress(false);
                    proccessDetails(jsonObject);
                } else if (serviceId == ServiceNames.REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                } else if (serviceId == ServiceNames.REQUEST_ID_CENTER_ADD_FAVROTES) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void proccessDetails(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {

                JSONArray gallery = jsonObject.has("gallery") ? jsonObject.getJSONArray("gallery") : null;
                if (gallery != null && gallery.length() > 0) {
                    tranerDetailsGalleryListAdapter.updateData(processGallery(gallery));
                }

                JSONArray plans = jsonObject.has("plans") ? jsonObject.getJSONArray("plans") : null;
                if (plans != null && plans.length() > 0) {
                    tranerDetailsSelectPlanListAdapter.updateData(processPlanModel(plans));
                }
                JSONArray products = jsonObject.has("products") ? jsonObject.getJSONArray("products") : null;
                if (products != null && products.length() > 0) {
                    tranerDetailsFavProductListAdapter.updateData(proccessProducts(products));
                    lnrProductView.setVisibility(View.VISIBLE);
                }else {
                    lnrProductView.setVisibility(View.GONE);
                }

                JSONArray trainers = jsonObject.has("trainers") ? jsonObject.getJSONArray("trainers") : null;
                if (trainers != null && trainers.length() > 0) {
                    trainersListAdapter.updateData(processTrainer(trainers));
                }

                JSONArray banner = jsonObject.has("banner") ? jsonObject.getJSONArray("banner") : null;
                if (banner != null && banner.length() > 0) {
                    thumbline = banner.getString(0);
                    viewPagerAdapter.updateData(processCenterBanner(banner));
                    sliderIndicator();
                }
                double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                double longitude = Double.parseDouble(jsonObject.getString("longitude"));
                if (longitude > 0 && latitude > 0) {
                    LatLng sydney = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                }
                txtAddress.setText(AppUtils.getStringValueFromJson(jsonObject, "address"));
                readMoreTextViewDiscription.setText(AppUtils.getStringValueFromJson(jsonObject, "about"));
                trainerSpecialities = jsonObject.has("specialities") ? jsonObject.getJSONArray("specialities") : null;
              if(trainerSpecialities != null && trainerSpecialities.length() >0){
                  processSpecial(5);
                  lnrSpView.setVisibility(View.VISIBLE);
              }else {
                  lnrSpView.setVisibility(View.GONE);
              }
                centerName = AppUtils.getStringValueFromJson(jsonObject, "name");
                txtAppbarTittle.setText(centerName);

                CenterModel centerModel = new CenterModel();
                centerModel.center_id = centerId;
                centerModel.id = centerId;
                centerModel.name = centerName;

                List<HomeSliderModel> homeSliderModels = processCenterBanner(banner);
                if (homeSliderModels != null && homeSliderModels.size() > 0) {
                    centerModel.thumbnail_img = homeSliderModels.get(0).imgUrl;
                }
                LocalCache.getCache().getSelectedBookingModel().centerModel = centerModel;
                LocalCache.getCache().getSelectedBookingModel().isFromCenter = true;
              ;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray trainerSpecialities;

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

    private List<HomeSliderModel> processCenterBanner(JSONArray jsonArray) {
        List<HomeSliderModel> homeSliderModels = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                HomeSliderModel homeSliderModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {

                    homeSliderModel = new HomeSliderModel();
                    ;
                    homeSliderModel.imgUrl = jsonArray.getString(i);


                    homeSliderModels.add(homeSliderModel);

                }

            }
            return homeSliderModels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeSliderModels;
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
                    planModelArrayList.add(planModel);

                }

            }
            return planModelArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planModelArrayList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 10) {


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


            } else if (message.what == 2) {

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


            } else if (message.what == 12) {

//
////
//                TrainersModel trainersModel = null;
//                try {
//                    trainersModel = (TrainersModel) message.obj;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (trainersModel != null) {
//
//                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = true;
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("trainersModel", trainersModel);
//                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTrainerDetails fragment2 = new FragmentTrainerDetails();
//                    fragment2.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else {
////                    toast(AppCons.RECORD_CAN_NOT_OPEN);
//                }
            } else if (message.what == 13) {

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
                case R.id.send_message: {
                    if (getFragmentActivity().isUserLoged()) {

                        Bundle bundle = new Bundle();
                        bundle.putString("id", centerId);
                        bundle.putString("name", centerName);
                        bundle.putString("thumbnail_img", thumbline);
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
                case R.id.lnr_trainer_see_all: {
                    LocalCache.getCache().getTrainerFilterModel().genter.clear();
                    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.clear();
                    LocalCache.getCache().getTrainerFilterModel().serviceType.clear();
                    LocalCache.getCache().getTrainerFilterModel().price.clear();
                    LocalCache.getCache().getTrainerFilterModel().experience.clear();
                    LocalCache.getCache().getTrainerFilterModel().activities.clear();
                    LocalCache.getCache().getTrainerFilterModel().rating = null;

                    LocalCache.getCache().getSelectedBookingModel().isFromCenter = true;

                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_CENTER_TRAINERS);
                    bundle.putString("center_id", centerId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerList fragment2 = new FragmentTrainerList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.lnr_seeall_products: {
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.PRODUCT_LIST_TYPE_CENTETR_PRODUCTS);
                    bundle.putString("center_id", centerId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentProductLIst fragment2 = new FragmentProductLIst();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                } case R.id.lnr_plan_see_all: {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", AppCons.PLAN_LIST_TYPE_CENTER);
                    bundle.putString("center_id", centerId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainingPlanList fragment2 = new FragmentTrainingPlanList();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }case R.id.lnr_gallery_see_all: {
                    Bundle bundle = new Bundle();
                    bundle.putLong("type", AppCons.GALLERY_LIST_TYPE_CENTER);
                    bundle.putString("center_id", centerId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentGallery fragment2 = new FragmentGallery();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }
                case R.id.img_fav: {
                    try {
                        if (getFragmentActivity().isUserLoged()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("center_id", centerId);
                            centerService.centerAddTofaverits(ServiceNames.REQUEST_ID_CENTER_ADD_FAVROTES, jsonObject);
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


            }
        }
    };
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    HomeSliderAdapter viewPagerAdapter;

    private void sliderIndicator() {


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
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);

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

                        if (viewPager.getCurrentItem() < dotscount - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else
                            viewPager.setCurrentItem(0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private int lengthf;
    private LinearLayout lnrMOreSpecial, linLayout, layoutMain;
    private ImageView imgMoreSp;
    private TextView txtMoreSp;

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

}