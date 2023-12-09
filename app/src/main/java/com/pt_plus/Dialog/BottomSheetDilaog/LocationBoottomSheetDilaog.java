package com.pt_plus.Dialog.BottomSheetDilaog;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pt_plus.Activitys.ActivitySpashScreen;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Adapter.LocationListAdapter;
import com.pt_plus.Adapter.TrainersFilterListAdapter;
import com.pt_plus.Fragment.FragmentTrainerDetails;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.LocationModel;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationBoottomSheetDilaog extends BottomSheetDialogFragment {
    private SuperActivity context;
PopUpCallBack popUpCallBack;
    public LocationBoottomSheetDilaog(SuperActivity fragmentActivity, PopUpCallBack callBack) {
        this.context = fragmentActivity;
        this.popUpCallBack = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_location, container, false);
        View backgroundView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap blurredBitmap = UiUtils.getInstance().createBlurredBitmap(backgroundView, getContext());

        Drawable drawable = new BitmapDrawable(getResources(), blurredBitmap);
        getDialog().getWindow().setBackgroundDrawable(drawable);

        initView(view);
        return view;
    }


    RecyclerView recyclerViewLocation;
    private CardView cardViewSubmit;
    private void initView(View view) {
        cardViewSubmit = view.findViewById(R.id.carview_submit);
        cardViewSubmit.setOnClickListener(_click);

        recyclerViewLocation = view.findViewById(R.id.rcy_location);
        locationListAdapter = new LocationListAdapter(context, null, _handler);
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewLocation.setAdapter(locationListAdapter);

        trainerService = new TrainerService(context, _callBack);
        trainerService.getTrainerFilterLocation(ServiceNames.REQUEST_GET_TRAINER_FILTER_LOCATIONS);
        context.showProgress(true);
    }

    TrainerService trainerService;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            context.showProgress(false);
            try {
                if (serviceId == ServiceNames.REQUEST_GET_TRAINER_FILTER_LOCATIONS) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        processLoations(jsonArray);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void processLoations(JSONArray jsonArray) {
        try {
            List<LocationModel> locationModelList = new ArrayList<>();
            LocationModel locationModel = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                locationModel = new LocationModel();
                locationModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                locationModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");

                if (LocalCache.getCache().getTrainerFilterModel().locationModel != null) {
                    if (LocalCache.getCache().getTrainerFilterModel().locationModel.name.equalsIgnoreCase(locationModel.name)) {
                        locationModel.isSelected = true;
                    } else {
                        locationModel.isSelected = false;
                    }
                } else {
                    locationModel.isSelected = false;
                }
                locationModelList.add(locationModel);
            }
            locationListAdapter.updateData(locationModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationListAdapter locationListAdapter;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 123) {


                LocationModel locationModel = null;
                try {
                    locationModel = (LocationModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (locationModel != null) {
                    List<LocationModel> locationModelList = locationListAdapter.getAllItems();

                        for (LocationModel item :locationModelList){

                            if(item.name.equalsIgnoreCase(locationModel.name)){

                                item.isSelected = true;
                            }else {
                                item.isSelected = false;
                            }
                        }


                    locationListAdapter.notifyDataSetChanged();
                    LocalCache.getCache().getTrainerFilterModel().locationModel = locationModel;

//                    popUpCallBack.onOK();
//                    dismiss();
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }

            // Your code logic goes here.
            return true;
        }
    });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up any views or listeners here
    }

    private final OnClickListener _click = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.carview_submit: {

                    popUpCallBack.onOK();
                    dismiss();

                    break;
                }


            }
        }
    };


}
