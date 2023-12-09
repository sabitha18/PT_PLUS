package com.pt_plus.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.AppCons;
import com.pt_plus.cons.LocalCache;

import org.json.JSONObject;


public class FragmentBookingHistoryDetails extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking_history_details, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack, imgTrainer, imgChat, imgPhone;
    private TextView txtappBarTitle, txtName, txtSplec, txtStatus, txtDaate, txtTime, txtAddress, txtTotalPrice;
    private View appbar;
    private TrainerService trainerService;
    private LinearLayout lnrCancelOrder, lnrMainView;
    private String bookingId, from="";

    private void initView(View view) {
        imgPhone = view.findViewById(R.id.img_phone);
        lnrMainView = view.findViewById(R.id.lnr_main_view);
        lnrMainView.setVisibility(View.GONE);
        lnrCancelOrder = view.findViewById(R.id.lnr_cancel_order);
        lnrCancelOrder.setOnClickListener(_click);
        txtTotalPrice = view.findViewById(R.id.txt_total_price);
        txtAddress = view.findViewById(R.id.txt_adderss);
        txtTime = view.findViewById(R.id.txt_time);
        txtDaate = view.findViewById(R.id.txt_date);
        txtStatus = view.findViewById(R.id.txt_status);
        txtSplec = view.findViewById(R.id.txt_spacl);
        imgTrainer = view.findViewById(R.id.img_trainer);
        txtName = view.findViewById(R.id.txt_name);
        imgBack = view.findViewById(R.id.img_back);
        imgChat = view.findViewById(R.id.img_chat);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText("");

        trainerService = new TrainerService(getFragmentActivity(), _callback);
        try {
            Bundle bundle = this.getArguments();
            bookingId = bundle.getString("id");
            from = bundle.getString("from");

            trainerService.getBookingDetails(ServiceNames.REQUEST_ID_TRAINER_BOOKING_DETAILS, new JSONObject().put("id", bundle.getString("id")));
            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToVview(JSONObject jsonObject) {
        try {
            Glide.with(this).load(AppUtils.getStringValueFromJson(jsonObject, "trainer_image")).placeholder(R.drawable.no_image).into(imgTrainer);
            txtName.setText(AppUtils.getStringValueFromJson(jsonObject, "trainer_name"));
            txtSplec.setText(AppUtils.getStringValueFromJson(jsonObject, "trainer_category"));
            txtStatus.setText(AppUtils.getStringValueFromJson(jsonObject, "booking_status"));
            txtAddress.setText(AppUtils.getStringValueFromJson(jsonObject, "trainer_address"));
            txtTotalPrice.setText("Total Price " + AppUtils.getStringValueFromJson(jsonObject, "plan_currency") + "  " + AppUtils.getStringValueFromJson(jsonObject, "plan_price"));
            txtDaate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "booked_on")));
            txtTime.setText(AppDateUtils.convertDateToOtherFormat(AppUtils.getStringValueFromJson(jsonObject, "booked_on"), AppCons.DATE_FORAMTE, "h:mm a"));

            if (AppUtils.getStringValueFromJson(jsonObject, "booking_status").equalsIgnoreCase("cancelled")) {
                lnrCancelOrder.setVisibility(View.GONE);
            } else {
                lnrCancelOrder.setVisibility(View.VISIBLE);
            }

            imgChat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentActivity().isUserLoged()) {

                        Bundle bundle = new Bundle();
                        bundle.putString("id", AppUtils.getStringValueFromJson(jsonObject, "trainer_id"));
                        bundle.putString("name", AppUtils.getStringValueFromJson(jsonObject, "trainer_name"));
                        bundle.putString("thumbnail_img", AppUtils.getStringValueFromJson(jsonObject, "trainer_image"));
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

                }
            });

            imgPhone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = AppUtils.getStringValueFromJson(jsonObject, "trainer_phone");
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            getFragmentActivity().showProgress(false);
            if (serviceId == ServiceNames.REQUEST_ID_TRAINER_BOOKING_DETAILS) {
                setDataToVview(jsonObject);
                lnrMainView.setVisibility(View.VISIBLE);
            } else {
                toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                goback();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.lnr_cancel_order: {
                    try {
                        UiUtils.getInstance().showConfirm(11, "Are you sure you want to Cancel this Booking ?", new PopUpCallBack() {
                            @Override
                            public void onOK() {
                                try {
                                    trainerService.cancelBooking(ServiceNames.REQUEST_ID_CANCEL_BOOKING, new JSONObject().put("id", bookingId));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancel() {

                            }
                        }, getFragmentActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }


            }
        }
    };

    private void goback() {
        if (from != null&&from.equalsIgnoreCase("booking")) {
            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.lnr_content, new FragmentBookingHistory());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            getFragmentActivity().getSupportFragmentManager().popBackStack();
        }

    }
}