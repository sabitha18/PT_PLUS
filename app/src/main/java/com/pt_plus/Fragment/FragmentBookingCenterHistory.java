package com.pt_plus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Adapter.BookingHistoryListAdapter;
import com.pt_plus.Adapter.CenterBookingHistoryListAdapter;
import com.pt_plus.Model.BookingModel;
import com.pt_plus.Model.CenterModel;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentBookingCenterHistory extends SuperFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_center_history, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private CenterService centerService;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.booking_history);

        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_booking);
        centerBookingHistoryListAdapter = new CenterBookingHistoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(centerBookingHistoryListAdapter);


        centerService = new CenterService(getFragmentActivity(), _callBack);
        centerService.getCenterBookingHistory(ServiceNames.REQUEST_GET_CENTER_BOOKING_HISTORY);
        getFragmentActivity().showProgress(true);
    }

    CenterBookingHistoryListAdapter centerBookingHistoryListAdapter;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                getFragmentActivity().showProgress(false);
                if(serviceId == ServiceNames.REQUEST_GET_CENTER_BOOKING_CANCEL){
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    centerService.getCenterBookingHistory(ServiceNames.REQUEST_GET_CENTER_BOOKING_HISTORY);
                    getFragmentActivity().showProgress(true);
                }else {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        proccessBookingModel(jsonArray);
                        centerBookingHistoryListAdapter.updateData(proccessBookingModel(jsonArray));
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