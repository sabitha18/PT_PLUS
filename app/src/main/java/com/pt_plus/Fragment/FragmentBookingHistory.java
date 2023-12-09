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

import com.pt_plus.Adapter.BookingHistoryListAdapter;
import com.pt_plus.Adapter.CenterBookingHistoryListAdapter;
import com.pt_plus.Adapter.HorizondalTypeListAdapter;
import com.pt_plus.Model.ActivitiesModel;
import com.pt_plus.Model.BookingModel;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.Model.FilterModel;
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
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentBookingHistory extends SuperFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private TrainerService trainerService;
    private RadioButton radioCenter,radioTrianer;

    private void initView(View view) {
        radioCenter = view.findViewById(R.id.radio_center);
        radioCenter.setOnClickListener(_click);
        radioTrianer = view.findViewById(R.id.radio_trainer);
        radioTrianer.setOnClickListener(_click);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.booking_history);

         recyclerViewActivities = view.findViewById(R.id.rcy_booking);
        bookingHistoryListAdapter = new BookingHistoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(bookingHistoryListAdapter);


        trainerService = new TrainerService(getFragmentActivity(), _callBack);
        trainerService.getMyBookings(ServiceNames.REQUEST_ID_GET_MY_BOOKINGS);
        getFragmentActivity().showProgress(true);


         recyclerViewCenter = view.findViewById(R.id.rcy_center);
        centerBookingHistoryListAdapter = new CenterBookingHistoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenter.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenter.setAdapter(centerBookingHistoryListAdapter);


        centerService = new CenterService(getFragmentActivity(), _callBack);
        centerService.getCenterBookingHistory(ServiceNames.REQUEST_GET_CENTER_BOOKING_HISTORY);
        getFragmentActivity().showProgress(true);

        showView(recyclerViewActivities);
    }
    private CenterService centerService;
    RecyclerView recyclerViewCenter;
    CenterBookingHistoryListAdapter centerBookingHistoryListAdapter;
    RecyclerView recyclerViewActivities;
    BookingHistoryListAdapter bookingHistoryListAdapter;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                getFragmentActivity().showProgress(false);
                if(serviceId == ServiceNames.REQUEST_GET_CENTER_BOOKING_CANCEL){
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    centerService.getCenterBookingHistory(ServiceNames.REQUEST_GET_CENTER_BOOKING_HISTORY);
                    getFragmentActivity().showProgress(true);
                }else if(serviceId == ServiceNames.REQUEST_GET_CENTER_BOOKING_HISTORY){
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        proccessCenterBookingModel(jsonArray);
                        centerBookingHistoryListAdapter.updateData(proccessCenterBookingModel(jsonArray));
                    }
                }  else  if(serviceId == ServiceNames.REQUEST_ID_CANCEL_BOOKING){
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    trainerService.getMyBookings(ServiceNames.REQUEST_ID_GET_MY_BOOKINGS);
                    getFragmentActivity().showProgress(true);
                }else {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        proccessBookingModel(jsonArray);
                        bookingHistoryListAdapter.updateData(proccessBookingModel(jsonArray));
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

    private List<BookingModel> proccessBookingModel(JSONArray jsonArray) {
        List<BookingModel> bookingModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BookingModel bookingModel = new BookingModel();
                bookingModel.planModel = new PlanModel();
                bookingModel.trainersModel = new TrainersModel();
                bookingModel.bookingId = AppUtils.getStringValueFromJson(jsonObject, "id");
                bookingModel.bookingStatus = AppUtils.getStringValueFromJson(jsonObject, "booking_status");
                bookingModel.trainersModel.name = AppUtils.getStringValueFromJson(jsonObject, "trainer_name");
                bookingModel.trainersModel.profile_picture = AppUtils.getStringValueFromJson(jsonObject, "trainer_image");
                bookingModel.trainersModel.speclization = AppUtils.getStringValueFromJson(jsonObject, "trainer_category");
                if(AppUtils.getStringValueFromJson(jsonObject, "trainer_rating") != null && !AppUtils.getStringValueFromJson(jsonObject, "trainer_rating").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "trainer_rating").equalsIgnoreCase("null")){
                    bookingModel.trainersModel.rating = AppUtils.getStringValueFromJson(jsonObject, "trainer_rating");
                }else {
                    bookingModel.trainersModel.rating = "0";
                }

                bookingModel.trainersModel.experiance = AppUtils.getStringValueFromJson(jsonObject, "trainer_experience");
                if(AppUtils.getStringValueFromJson(jsonObject, "plan_title") != null &&!AppUtils.getStringValueFromJson(jsonObject, "plan_title").isEmpty() &&!AppUtils.getStringValueFromJson(jsonObject, "plan_title").equalsIgnoreCase("null")){
                    bookingModel.planModel.title = AppUtils.getStringValueFromJson(jsonObject, "plan_title");
                }else {
                    bookingModel.planModel.title ="";
                }

                bookingModel.planModel.sub_title = AppUtils.getStringValueFromJson(jsonObject, "plan_sub_title");
                bookingModel.planModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "plan_image");
                bookingModel.planModel.price = AppUtils.getStringValueFromJson(jsonObject, "plan_price");
                bookingModel.planModel.currency = AppUtils.getStringValueFromJson(jsonObject, "plan_currency");
                bookingModel.booked_on = AppUtils.getStringValueFromJson(jsonObject, "booked_on");
                bookingModelList.add(bookingModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingModelList;
    }

    private List<BookingModel> proccessCenterBookingModel(JSONArray jsonArray) {
        List<BookingModel> bookingModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BookingModel bookingModel = new BookingModel();
                bookingModel.planModel = new PlanModel();
                bookingModel.trainersModel = new TrainersModel();
                bookingModel.centerModel = new CenterModel();

                bookingModel.bookingId = AppUtils.getStringValueFromJson(jsonObject, "id");
                bookingModel.bookingStatus = AppUtils.getStringValueFromJson(jsonObject, "booking_status");
                bookingModel.centerModel.name = AppUtils.getStringValueFromJson(jsonObject, "center_name");
                bookingModel.centerModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "center_image");
//                bookingModel.trainersModel.speclization = AppUtils.getStringValueFromJson(jsonObject, "trainer_category");
//                bookingModel.trainersModel.rating = Double.parseDouble(AppUtils.getStringValueFromJson(jsonObject, "trainer_rating"));
//                bookingModel.trainersModel.experiance = AppUtils.getStringValueFromJson(jsonObject, "trainer_experience");
                bookingModel.planModel.title = AppUtils.getStringValueFromJson(jsonObject, "plan_title");
                bookingModel.planModel.sub_title = AppUtils.getStringValueFromJson(jsonObject, "plan_sub_title");
                bookingModel.planModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "plan_image");
                bookingModel.planModel.price = AppUtils.getStringValueFromJson(jsonObject, "plan_price");
                bookingModel.planModel.currency = AppUtils.getStringValueFromJson(jsonObject, "plan_currency");
                bookingModel.booked_on = AppUtils.getStringValueFromJson(jsonObject, "booked_on");
                bookingModelList.add(bookingModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingModelList;
    }


    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {


            if (message.what == 1) {

                BookingModel bookingModel = null;
                try {
                    bookingModel = (BookingModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bookingModel != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id", bookingModel.bookingId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentBookingHistoryDetails fragment2 = new FragmentBookingHistoryDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }else if (message.what == 2) {

                BookingModel bookingModel = null;
                try {
                    bookingModel = (BookingModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bookingModel != null) {

                    BookingModel finalBookingModel = bookingModel;
                    UiUtils.getInstance().showConfirm(11, "Are you sure you want to Cancel this Booking ?", new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            try {
                                trainerService.cancelBooking(ServiceNames.REQUEST_ID_CANCEL_BOOKING, new JSONObject().put("id", finalBookingModel.bookingId));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancel() {

                        }
                    }, getFragmentActivity());


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }else if (message.what == 3) {

                BookingModel bookingModel = null;
                try {
                    bookingModel = (BookingModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bookingModel != null) {

                    Bundle bundle = new Bundle();
                    AppLogger.log("iddsadsa                     "+ bookingModel.bookingId);
                    bundle.putString("id", bookingModel.bookingId);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCenterBookingHistoryDetails fragment2 = new FragmentCenterBookingHistoryDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }else if (message.what == 4) {

                BookingModel bookingModel = null;
                try {
                    bookingModel = (BookingModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bookingModel != null) {

                    BookingModel finalBookingModel = bookingModel;
                    UiUtils.getInstance().showConfirm(11, "Are you sure you want to Cancel this Booking ?", new PopUpCallBack() {
                        @Override
                        public void onOK() {
                            try {
                                centerService.centerBookingCancel(ServiceNames.REQUEST_GET_CENTER_BOOKING_CANCEL, new JSONObject().put("id", finalBookingModel.bookingId));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancel() {

                        }
                    }, getFragmentActivity());


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }case R.id.radio_center: {
                    showView(recyclerViewCenter);
                    break;
                }case R.id.radio_trainer: {
                    showView(recyclerViewActivities);
                    break;
                }


            }
        }
    };
    private  void showView(View view){
        if(view != null){
            recyclerViewActivities.setVisibility(View.GONE);
            recyclerViewCenter.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);

        }
    }

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}