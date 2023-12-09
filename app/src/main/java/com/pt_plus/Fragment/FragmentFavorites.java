package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pt_plus.Adapter.FavoriteBookingListAdapter;
import com.pt_plus.Adapter.FavoriteCenterListAdapter;
import com.pt_plus.Adapter.FavoritePlanListAdapter;
import com.pt_plus.Adapter.FavoriteStoreListAdapter;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.Model.PlanModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentFavorites extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private RadioButton radioBooking, radioCenters, radioStore, radioPlan;
    private RecyclerView recyclerViewBooking, recyclerViewCenters, recyclerViewStore, recyclerViewPlans;
    private StoreService storeService;
    private TrainerService trainerService;
    private CenterService centerService;
    private FavoriteStoreListAdapter favoriteStoreListAdapter;
    private FavoritePlanListAdapter favoritePlanListAdapter;
    private FavoriteBookingListAdapter favoriteBookingListAdapter;
    FavoriteCenterListAdapter favoriteCenterListAdapter;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.favorites);

        recyclerViewBooking = view.findViewById(R.id.rcy_booking);
        recyclerViewCenters = view.findViewById(R.id.rcy_centers);
        recyclerViewStore = view.findViewById(R.id.rcy_store);
        recyclerViewPlans = view.findViewById(R.id.rcy_plan);


        radioBooking = view.findViewById(R.id.radio_booking);
        radioBooking.setOnClickListener(_click);
        radioCenters = view.findViewById(R.id.radio_centers);
        radioCenters.setOnClickListener(_click);
        radioPlan = view.findViewById(R.id.radio_plan);
        radioPlan.setOnClickListener(_click);
        radioStore = view.findViewById(R.id.radio_store);
        radioStore.setOnClickListener(_click);


        favoriteBookingListAdapter = new FavoriteBookingListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewBooking.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewBooking.setAdapter(favoriteBookingListAdapter);

        favoriteCenterListAdapter = new FavoriteCenterListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenters.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenters.setAdapter(favoriteCenterListAdapter);

        favoriteStoreListAdapter = new FavoriteStoreListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewStore.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewStore.setAdapter(favoriteStoreListAdapter);


        recyclerViewCenters.setVisibility(View.GONE);
        recyclerViewStore.setVisibility(View.GONE);
        recyclerViewPlans.setVisibility(View.GONE);

        storeService = new StoreService(getFragmentActivity(), callBack);
        storeService.getStoreWishList(ServiceNames.REQUEST_ID_STORE_WISH_LIST);

        trainerService = new TrainerService(getFragmentActivity(), callBack);
        trainerService.getTrainerWishLIst(ServiceNames.REQUEST_ID_GET_TRAINER_WISH_LIST);
        trainerService.getPlanWishList(ServiceNames.REQUEST_ID_GET_PLAN_WISH_LIST);

        centerService = new CenterService(getFragmentActivity(), callBack);
        centerService.getCenterFaveroteList(ServiceNames.REQUEST_ID_CENTER_FAVROTES_LIST);

        favoritePlanListAdapter = new FavoritePlanListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPlans.setAdapter(favoritePlanListAdapter);
    }

    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_STORE_WISH_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        favoriteStoreListAdapter.updateData(proccessProducts(jsonArray));
                    } else {
                        favoriteStoreListAdapter.getAllItems().clear();
                        favoriteStoreListAdapter.notifyDataSetChanged();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_CENTER_FAVROTES_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        favoriteCenterListAdapter.updateData(proccessCenterLIst(jsonArray));
                    } else {
                        favoriteCenterListAdapter.getAllItems().clear();
                        favoriteCenterListAdapter.notifyDataSetChanged();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_TRAINER_WISH_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        favoriteBookingListAdapter.updateData(proceesTrainer(jsonArray));
                    } else {
                        favoriteBookingListAdapter.getAllItems().clear();
                        favoriteBookingListAdapter.notifyDataSetChanged();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_PLAN_WISH_LIST) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        favoritePlanListAdapter.updateData(proccessPlanAdapter(jsonArray));
                    } else {
                        favoritePlanListAdapter.getAllItems().clear();
                        favoritePlanListAdapter.notifyDataSetChanged();
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_GUEST_ADD_TO_CART || serviceId == ServiceNames.REQUEST_ID_GET_ADD_TO_CART) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));

                } else if (serviceId == ServiceNames.REQUEST_ID_DELETE_STORE_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    storeService.getStoreWishList(ServiceNames.REQUEST_ID_STORE_WISH_LIST);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_TRAINER_REMOVE_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    trainerService.getTrainerWishLIst(ServiceNames.REQUEST_ID_GET_TRAINER_WISH_LIST);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    trainerService.getPlanWishList(ServiceNames.REQUEST_ID_GET_PLAN_WISH_LIST);
                } else if (serviceId == ServiceNames.REQUEST_ID_CENTER_REMOVE_FAVEROTE) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    centerService.getCenterFaveroteList(ServiceNames.REQUEST_ID_CENTER_FAVROTES_LIST);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<CenterModel> proccessCenterLIst(JSONArray jsonArray) {
        List<CenterModel> productModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                CenterModel centerModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    centerModel = new CenterModel();
                    centerModel.center_id = AppUtils.getStringValueFromJson(jsonObject, "center_id");
                    centerModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    centerModel.about = AppUtils.getStringValueFromJson(jsonObject, "about");

                    centerModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    centerModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    productModelList.add(centerModel);
                }

            }
            return productModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productModelList;
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
                    productModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    productModel.currency = AppUtils.getStringValueFromJson(jsonObject, "currency");
                    productModel.price = AppUtils.getStringValueFromJson(jsonObject, "price");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject, "product_name");
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

    private List<TrainersModel> proceesTrainer(JSONArray jsonArray) {
        List<TrainersModel> trainersModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                TrainersModel trainersModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    trainersModel = new TrainersModel();
                    trainersModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    trainersModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                    trainersModel.trainer_id = AppUtils.getStringValueFromJson(jsonObject, "trainer_id");
                    trainersModel.profile_picture = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    trainersModel.rating = AppUtils.getStringValueFromJson(jsonObject, "rating");

                    trainersModel.experiance = AppUtils.getStringValueFromJson(jsonObject, "experience");
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

    private List<PlanModel> proccessPlanAdapter(JSONArray jsonArray) {
        List<PlanModel> planModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                PlanModel planModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    planModel = new PlanModel();
                    planModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    planModel.plan_id = AppUtils.getStringValueFromJson(jsonObject, "plan_id");
                    planModel.title = AppUtils.getStringValueFromJson(jsonObject, "name");
                    planModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");


                    planModelList.add(planModel);
                }

            }
            return planModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planModelList;
    }

    private void showView(View view) {
        try {
            recyclerViewBooking.setVisibility(View.GONE);
            recyclerViewCenters.setVisibility(View.GONE);
            recyclerViewStore.setVisibility(View.GONE);
            recyclerViewPlans.setVisibility(View.GONE);

            view.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            try {
                if (message.what == 1) {

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


                } else if (message.what == 2) {
                    ProductModel productModel = null;
                    try {
                        productModel = (ProductModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (productModel != null) {
                        validateAndSubmitAddtoCart(productModel);
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } else if (message.what == 3) {
                    ProductModel productModel = null;
                    try {
                        productModel = (ProductModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (productModel != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", productModel.id);
                        storeService.deleteStoreWishListProduct(ServiceNames.REQUEST_ID_DELETE_STORE_WISH_LIST, jsonObject);
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } else if (message.what == 4) {
                    TrainersModel trainersModel = null;
                    try {
                        trainersModel = (TrainersModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trainersModel != null) {
                        LocalCache.getCache().getSelectedBookingModel().trainersModel = trainersModel;
//                        trainersModel.id = trainersModel.trainer_id;
                        trainersModel.id =  LocalCache.getCache().getSelectedBookingModel().trainersModel.trainer_id;
                        trainersModel.trainer_id = trainersModel.trainer_id;
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
                } else if (message.what == 5) {
                    TrainersModel trainersModel = null;
                    try {
                        trainersModel = (TrainersModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trainersModel != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", trainersModel.id);
                        trainerService.trainerRemoveWishList(ServiceNames.REQUEST_ID_GET_TRAINER_REMOVE_WISH_LIST, jsonObject);
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } else if (message.what == 6) {
                    TrainersModel trainersModel = null;
                    try {
                        trainersModel = (TrainersModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trainersModel != null) {
                        trainersModel.id = trainersModel.trainer_id;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trainersModel", trainersModel);
                        LocalCache.getCache().getSelectedBookingModel().trainersModel = trainersModel;
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        FragmentTrainingPlanList fragment2 = new FragmentTrainingPlanList();
                        FragmentTrainerDetails fragment2 = new FragmentTrainerDetails();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } else if (message.what == 7) {
                    PlanModel planModel = null;
                    try {
                        planModel = (PlanModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (planModel != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", planModel.id);
//                        trainerService.trainerRemoveWishList(ServiceNames.REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST, jsonObject);
                        trainerService.planRemoveWishList(ServiceNames.REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST, jsonObject);
                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                } else if (message.what == 8) {
                    PlanModel planModel = null;
                    try {
                        planModel = (PlanModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (planModel != null) {
                        LocalCache.getCache().getSelectedBookingModel().planModel = planModel;
                        planModel.id = planModel.id = planModel.plan_id;
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
                } else if (message.what == 9) {
                    PlanModel planModel = null;
                    try {
                        planModel = (PlanModel) message.obj;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (planModel != null) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("type", AppCons.TRAINER_LIST_TYPE_ALL);
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
                } else if (message.what == 10) {
                    CenterModel centerModel = null;
                    try {
                        centerModel = (CenterModel) message.obj;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", centerModel.id);
                        centerService.removeCenterFav(ServiceNames.REQUEST_ID_CENTER_REMOVE_FAVEROTE, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (centerModel != null) {


                    } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                    }
                }else if (message.what == 11) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Your code logic goes here.
            return true;
        }
    });

    private void validateAndSubmitAddtoCart(ProductModel productIModel) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", productIModel.productId);

            int qty = 1;

            if (qty > 0) {
                jsonObject.put("quantity", qty);
                String access_token = (String) PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_SESSION, "");

                if (access_token != null && !access_token.isEmpty()) {
                    storeService.addToCard(ServiceNames.REQUEST_ID_GET_ADD_TO_CART, jsonObject);
                }

            } else {
                toastInFragment("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.radio_booking: {
                    showView(recyclerViewBooking);
                    break;
                }
                case R.id.radio_centers: {
                    showView(recyclerViewCenters);
                    break;
                }
                case R.id.radio_store: {
                    showView(recyclerViewStore);
                    break;
                }
                case R.id.radio_plan: {
                    showView(recyclerViewPlans);
                    break;
                }


            }
        }
    };

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

}