package com.pt_plus.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.TimeSloteListAdapter;
import com.pt_plus.Model.SelectBoxModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.CallBackNew;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.TrainerService;
import com.pt_plus.Service.TrainerServiceNew;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.CustomCalender;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class FragmentAppoinment extends SuperFragment implements CustomCalender.OnRightButtonClickListener,CustomCalender.OnLeftButtonClickListener {


    private AttributeSet AttributeSetNew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private JSONArray disabledDates;
    private TrainerService trainerService;
    private TrainerServiceNew trainerServiceOne;
    private CustomCalender customCalendar;
    HashMap<Integer, Object> dateHashmap = new HashMap<>();
    View view;
    CommonCardCategoryListAdapter commonCardCategoryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getFragmentActivity().getApplicationContext().getResources().updateConfiguration(config, null);

        view = inflater.inflate(R.layout.fragment_appoinment, container, false);
        initView(view);
        return view;
    }


    private View appbar, imgBack;
    private TextView txtappBarTitle, text_morning, text_evng;
    private ImageView imgTrainer, imgPlan;
    private CardView cardViewBookNow;
    private CenterService centerService;

    private List<String> unavailableDays = new ArrayList<>();
    private RecyclerView recyclerViewActivities, recyclerEvaningLSotes;


    private void initView(View v) {
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        getFragmentActivity().setDrawerLock(false);
        getFragmentActivity().isCartShow(false);

        appbar = v.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = v.findViewById(R.id.appbar_title);
        imgBack = v.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        cardViewBookNow = v.findViewById(R.id.carview_book_now);
        cardViewBookNow.setOnClickListener(_click);
        text_morning = v.findViewById(R.id.text_morning);
        text_evng = v.findViewById(R.id.text_evng);

        txtappBarTitle.setText(R.string.appoinment);


        imgTrainer = v.findViewById(R.id.img_trainer);
        txtTrainerName = v.findViewById(R.id.txt_name);
        txtRating = v.findViewById(R.id.txt_rating);
        txtSpec = v.findViewById(R.id.txt_spacl);
        txtExp = v.findViewById(R.id.txt_des);
        imgPlan = v.findViewById(R.id.img_plan);
        txtPlanName = v.findViewById(R.id.txt_plan_name);
        txtAMount = v.findViewById(R.id.txt_amount);

        customCalendar = view.findViewById(R.id.custom_calendar);
        customCalendar.setOnLeftButtonClickListener(this);
        customCalendar.setOnRightButtonClickListener(this); // Assuming your Fragment implements the interface

        ProccessView();

        recyclerViewActivities = v.findViewById(R.id.rcy_activities);
        morningTimeSloteListAdapter = new TimeSloteListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActivities.setAdapter(morningTimeSloteListAdapter);

        recyclerEvaningLSotes = v.findViewById(R.id.rcy_evaningTIme_slotes);
        evaningTimeSloteListAdapter = new TimeSloteListAdapter(getFragmentActivity(), null, _handler);
        recyclerEvaningLSotes.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerEvaningLSotes.setAdapter(evaningTimeSloteListAdapter);

        trainerServiceOne = new TrainerServiceNew(getFragmentActivity(), _callBackNew);
        trainerService = new TrainerService(getFragmentActivity(), _callBack);
        centerService = new CenterService(getFragmentActivity(), _callBack);
        Calendar calendar = Calendar.getInstance();
        loadTimeSlotes(calendar);


    }

    private final CallBackNew _callBackNew = new CallBackNew() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                System.out.println("sabi 000   ---------    " + jsonObject);
                if (jsonObject.has("result")) {
                    disabledDates = jsonObject.getJSONArray("result");
                    setDisabledDaysNew(disabledDates, customCalendar.getSelectedDate(),dateHashmap);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };


    private void setDisabledDaysNew(JSONArray availableDays, Calendar selectedDate, HashMap<Integer, Object> dateHashmap) {
        try {

            List<String> availableWeeks = new ArrayList<>();
            String[] allDays = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
            for (String day : allDays) {
                boolean isDayAvailable = false;


            }
            for (int i = 0; i < availableDays.length(); i++) {
                JSONObject dayObject = availableDays.getJSONObject(i);
                String availableDay = dayObject.getString("day");
                availableWeeks.add(availableDay);
            }
            System.out.println("check == " + availableWeeks);
            List<String> disabledDates = getDisabledDates(availableWeeks);
            System.out.println("check == 333 " + disabledDates);


            HashMap<Object, Property> descHashMap = new HashMap<>();
            Property defaultProperty = new Property();
            defaultProperty.layoutResource = R.layout.default_view;
            defaultProperty.dateTextViewResource = R.id.text_view;
            descHashMap.put("default", defaultProperty);

            Property currentProperty = new Property();
            currentProperty.layoutResource = R.layout.current_view;
            currentProperty.dateTextViewResource = R.id.text_view;
            descHashMap.put("current", currentProperty);

            Property presentProperty = new Property();
            presentProperty.layoutResource = R.layout.present_view;
            presentProperty.dateTextViewResource = R.id.text_view;
            descHashMap.put("previous", presentProperty);

            // For absent
            Property absentProperty = new Property();
            absentProperty.layoutResource = R.layout.absent_view;
            absentProperty.dateTextViewResource = R.id.text_view;
            descHashMap.put("absent", absentProperty);

            // set desc hashmap on custom calendar
            customCalendar.setMapDescToProp(descHashMap);

            // initialize calendar to set current month
            Calendar calendar = selectedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);



             // checking month previous or not to set disable
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            if (calendar.get(Calendar.MONTH) == currentMonth) {
                System.out.println("check == 222222   "+calendar.get(Calendar.DAY_OF_MONTH)+"  33  "+calendar.get(Calendar.MONTH));
                dateHashmap.clear();
                Calendar calendar_new = Calendar.getInstance();

                for (int i = 1; i < calendar_new.get(Calendar.DAY_OF_MONTH); i++) {
                    dateHashmap.put(i, "previous");
                }
                dateHashmap.put(calendar_new.get(Calendar.DAY_OF_MONTH), "current");
                loadTimeSlotesNew(calendar_new);
            }
            System.out.println("disabled dates **   " + disabledDates);
            for (String disabledDate : disabledDates) {
                Calendar disabledCalendar = selectedDate;
                disabledCalendar.setTime(dateFormat.parse(disabledDate));

                // Get the day of the month for the disabled date
                int dayOfMonth = disabledCalendar.get(Calendar.DAY_OF_MONTH);

                if ("previous".equals(dateHashmap.get(dayOfMonth))) {

                } else {
                    dateHashmap.put(dayOfMonth, "absent");
                }

            }
            for (Map.Entry<Integer, Object> entry : dateHashmap.entrySet()) {
                int dayOfMonth = entry.getKey();
                Object value = entry.getValue();

                System.out.println("check ==    "+"Day: " + dayOfMonth + ", Value: " + value);
            }
            // set date
            customCalendar.setDate(calendar, dateHashmap);

            customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                    int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

                    if ("absent".equals(dateHashmap.get(dayOfMonth))) {
                        // Handle the case where an "absent" date is clicked (no action in this example)
                        return;
                    }
                    if ("previous".equals(dateHashmap.get(dayOfMonth))) {
                        // Handle the case where an "absent" date is clicked (no action in this example)
                        return;
                    } else {
                        for (Integer day : dateHashmap.keySet()) {
                            System.out.println(" selected date -----   " + selectedDate);
                            if ("current".equals(dateHashmap.get(day))) {
                                loadTimeSlotesNew(selectedDate);
                                dateHashmap.put(day, "default");
                                break;  // Assuming only one "current" selection is allowed
                            }
                        }
                    }


                    // Update the dateHashmap to set the selected date as "current"
                    dateHashmap.put(dayOfMonth, "current");

                    // Update the CustomCalendar with the modified dateHashmap
                    customCalendar.setDate(selectedDate, dateHashmap);

                    // Print the selected date
                    String sDate = dayOfMonth + "/" + (selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.YEAR);
                    System.out.println("date -------   " + sDate);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getDisabledDates(List<String> availableWeeks) {
        List<String> disabledDates = new ArrayList<>();

        // Set the start and end date range for your calendar
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(customCalendar.getSelectedDate().getTime());
        startDate.set(Calendar.DAY_OF_MONTH, 1); // Set your desired start date

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(customCalendar.getSelectedDate().getTime());
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        // Iterate through the date range
        while (startDate.before(endDate) || startDate.equals(endDate)) {
            Date currentDate = startDate.getTime();
            String dayOfWeek = getDayOfWeek(currentDate);

            // Check if the day is not in the available weeks list
            if (!availableWeeks.contains(dayOfWeek)) {
                disabledDates.add(dateFormat.format(currentDate));
            }

            // Move to the next day
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return disabledDates;
    }


    private static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Convert day of week to a string representation (e.g., "Monday")
        String[] daysOfWeek = {"", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        return daysOfWeek[dayOfWeek];
    }


    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                System.out.println("sabi ---------    " + jsonObject);
                if (statusCode == 201) {
                    text_evng.setVisibility(View.GONE);
                    text_morning.setVisibility(View.GONE);
                    recyclerViewActivities.setVisibility(View.GONE);
                    recyclerEvaningLSotes.setVisibility(View.GONE);
                }
                getFragmentActivity().showProgress(false);
                if (serviceId == ServiceNames.REQUEST_ID_TRINER_TIME_SLOTES || serviceId == ServiceNames.REQUEST_GET_CENTER_TIME_SLOTES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        boolean morningPresent = false;
                        boolean eveningPresent = false;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject slotJson = jsonArray.getJSONObject(i);
                            String type = AppUtils.getStringValueFromJson(slotJson, "part");
                            JSONArray slotArray = slotJson.has("slots") ? slotJson.getJSONArray("slots") : null;

                            if (type.equals("Morning")) {
                                morningPresent = true;
                                morningTimeSloteListAdapter.updateData(proceesTimeSlote(slotArray, "morning"));
                            } else if (type.equals("Evening")) {
                                eveningPresent = true;
                                evaningTimeSloteListAdapter.updateData(proceesTimeSlote(slotArray, "evening"));
                            }
                        }

                        // Set visibility based on the presence of both morning and evening parts
                        if (morningPresent && eveningPresent) {
                            recyclerEvaningLSotes.setVisibility(View.VISIBLE);
                            recyclerViewActivities.setVisibility(View.VISIBLE);
                            text_morning.setVisibility(View.VISIBLE);
                            text_evng.setVisibility(View.VISIBLE);
                        } else {
                            recyclerEvaningLSotes.setVisibility(eveningPresent ? View.VISIBLE : View.GONE);
                            recyclerViewActivities.setVisibility(morningPresent ? View.VISIBLE : View.GONE);
                            text_morning.setVisibility(morningPresent ? View.VISIBLE : View.INVISIBLE);
                            text_evng.setVisibility(eveningPresent ? View.VISIBLE : View.INVISIBLE);
                        }
                    }

                } else if (serviceId == ServiceNames.REQUEST_ID_BOOK_TRINER) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    JSONObject paymentUrl = jsonObject.has("payment_url") ? jsonObject.getJSONObject("payment_url") : null;
                    if (paymentUrl != null) {
                        JSONObject data = jsonObject.has("data") ? jsonObject.getJSONObject("data") : null;
                        Bundle bundle = new Bundle();

//                        bundle.putString("paymentUrl", AppUtils.getStringValueFromJson(paymentUrl, "paymentURL"));
                        JSONObject paymentUrlData = paymentUrl.has("data") ? paymentUrl.getJSONObject("data") : null;
                        if (paymentUrlData != null) {
                            bundle.putString("paymentUrl", AppUtils.getStringValueFromJson(paymentUrlData, "link"));
                        }

                        bundle.putString("from", "booking");
                        bundle.putString("orderId", AppUtils.getStringValueFromJson(data, "order_id"));
                        bundle.putString("bookingId", AppUtils.getStringValueFromJson(data, "id"));
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentPaymentWebView fragment2 = new FragmentPaymentWebView();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else if (serviceId == ServiceNames.REQUEST_GET_CENTER_BOOKINGS) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    JSONObject paymentUrl = jsonObject.has("payment_url") ? jsonObject.getJSONObject("payment_url") : null;
                    if (paymentUrl != null) {
                        JSONObject data = jsonObject.has("data") ? jsonObject.getJSONObject("data") : null;
                        Bundle bundle = new Bundle();
                        bundle.putString("paymentUrl", AppUtils.getStringValueFromJson(paymentUrl, "paymentURL"));
                        bundle.putString("from", "Center booking");
                        bundle.putString("orderId", AppUtils.getStringValueFromJson(data, "order_id"));
                        bundle.putString("bookingId", AppUtils.getStringValueFromJson(data, "id"));
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentPaymentWebView fragment2 = new FragmentPaymentWebView();
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

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<SelectBoxModel> proceesTimeSlote(JSONArray jsonArray, String type) {
        List<SelectBoxModel> selectBoxModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                SelectBoxModel selectBoxModel = new SelectBoxModel();
                selectBoxModel.isSelected = false;
                selectBoxModel.type = type;
                selectBoxModel.title = jsonArray.getString(i);
                selectBoxModelList.add(selectBoxModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectBoxModelList;
    }


    private String selectedDate;

    private void loadTimeSlotes(Calendar calendar) {
        try {

            Date date = calendar.getTime();

            selectedDate = AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd");
            if (LocalCache.getCache().getSelectedBookingModel().isFromCenter) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("center_id", LocalCache.getCache().getSelectedBookingModel().centerModel.center_id);
                jsonObject.put("date", selectedDate);
                centerService.getCenterTimeSLotes(ServiceNames.REQUEST_ID_TRINER_TIME_SLOTES, jsonObject);
            } else {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("trainer_id", LocalCache.getCache().getSelectedBookingModel().trainersModel.trainer_id);
                jsonObject.put("date", selectedDate);
                trainerService.getTrianerTimeSlotes(ServiceNames.REQUEST_ID_TRINER_TIME_SLOTES, jsonObject);
                trainerServiceOne.getTrianerAllTimeSlotes(ServiceNames.REQUEST_GET_ALL_TRAINER_TIMESLOT, jsonObject, new CallBackNew() {
                    @Override
                    public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
                        try {
                            System.out.println("sabi 000   ---------    " + jsonObject);
                            if (jsonObject.has("result")) {
                                disabledDates = jsonObject.getJSONArray("result");
                                setDisabledDaysNew(disabledDates, customCalendar.getSelectedDate(), dateHashmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int serviceId, String errorMessage, int statusCode) {
                        // Handle error
                    }
                });
            }

            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTimeSlotesNew(Calendar calendar) {
        try {

            Date date = calendar.getTime();

            selectedDate = AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd");
            if (LocalCache.getCache().getSelectedBookingModel().isFromCenter) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("center_id", LocalCache.getCache().getSelectedBookingModel().centerModel.center_id);
                jsonObject.put("date", selectedDate);
                centerService.getCenterTimeSLotes(ServiceNames.REQUEST_ID_TRINER_TIME_SLOTES, jsonObject);
            } else {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("trainer_id", LocalCache.getCache().getSelectedBookingModel().trainersModel.trainer_id);
                jsonObject.put("date", selectedDate);
                trainerService.getTrianerTimeSlotes(ServiceNames.REQUEST_ID_TRINER_TIME_SLOTES, jsonObject);
            }

            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TimeSloteListAdapter morningTimeSloteListAdapter, evaningTimeSloteListAdapter;
    private TextView txtTrainerName, txtRating, txtSpec, txtExp, txtPlanName, txtAMount;

    private void ProccessView() {
        try {

            txtRating.setVisibility(View.GONE);
            txtSpec.setVisibility(View.GONE);
            txtExp.setVisibility(View.GONE);
            if (LocalCache.getCache().getSelectedBookingModel().isFromCenter == false) {
                Glide.with(getFragmentActivity()).load(LocalCache.getCache().getSelectedBookingModel().trainersModel.profile_picture).placeholder(R.drawable.no_image).into(imgTrainer);

                txtTrainerName.setText(LocalCache.getCache().getSelectedBookingModel().trainersModel.name);
                txtRating.setText(LocalCache.getCache().getSelectedBookingModel().trainersModel.rating + "");
                txtSpec.setText(LocalCache.getCache().getSelectedBookingModel().trainersModel.speclization + "");
                txtExp.setText(LocalCache.getCache().getSelectedBookingModel().trainersModel.experiance + "");

                txtRating.setVisibility(View.VISIBLE);
                txtSpec.setVisibility(View.VISIBLE);
                txtExp.setVisibility(View.VISIBLE);

            } else {
                Glide.with(getFragmentActivity()).load(LocalCache.getCache().getSelectedBookingModel().centerModel.thumbnail_img).placeholder(R.drawable.no_image).into(imgTrainer);
                txtTrainerName.setText(LocalCache.getCache().getSelectedBookingModel().centerModel.name);
            }


            Glide.with(getFragmentActivity()).load(LocalCache.getCache().getSelectedBookingModel().planModel.thumbnail_img).placeholder(R.drawable.no_image).into(imgPlan);

            txtPlanName.setText(LocalCache.getCache().getSelectedBookingModel().planModel.title);
            txtAMount.setText(LocalCache.getCache().getSelectedBookingModel().planModel.currency + " " + LocalCache.getCache().getSelectedBookingModel().planModel.price);


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
                case R.id.carview_book_now: {

                    if (selectTimeSLote != null && selectTimeSLote.title != null) {
                        if (getFragmentActivity().isUserLoged()) {
                            if (LocalCache.getCache().getSelectedBookingModel().isFromCenter) {
                                bookCenter();
                            } else {
                                if (selectTimeSLote != null && selectTimeSLote.title != null) {
                                    bookTrainer();
                                } else {
                                    toastInFragment(getFragmentActivity().getResources().getString(R.string.please_choose_a_slot));
                                }

                            }

                        } else {
                            Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                            startActivityForResult(intent, 111);
                            getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        }

                    } else {
                        toastInFragment(getFragmentActivity().getResources().getString(R.string.please_choose_a_slot));
                    }


                    break;
                }

            }
        }
    };

    private void bookCenter() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("center_id", LocalCache.getCache().getSelectedBookingModel().centerModel.center_id);
            jsonObject.put("plan_id", LocalCache.getCache().getSelectedBookingModel().planModel.plan_id);
            jsonObject.put("appointment_date", selectedDate);
            jsonObject.put("appointment_time", selectTimeSLote.title);
            getFragmentActivity().showProgress(true);
            centerService.centerBooking(ServiceNames.REQUEST_GET_CENTER_BOOKINGS, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookTrainer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trainer_id", LocalCache.getCache().getSelectedBookingModel().trainersModel.trainer_id);
            jsonObject.put("plan_id", LocalCache.getCache().getSelectedBookingModel().planModel.plan_id);
            jsonObject.put("appointment_date", selectedDate);

            jsonObject.put("appointment_time", selectTimeSLote.title);
            getFragmentActivity().showProgress(true);
            trainerService.bookTrainer(ServiceNames.REQUEST_ID_BOOK_TRINER, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SelectBoxModel selectTimeSLote;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                SelectBoxModel selectBoxModel = null;
                try {
                    selectBoxModel = (SelectBoxModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (selectBoxModel != null) {
                    selectTimeSLote = selectBoxModel;
                    if (selectBoxModel.type.equalsIgnoreCase("morning")) {
                        List<SelectBoxModel> selectBoxModelList = morningTimeSloteListAdapter.getAllItems();
                        if (selectBoxModelList != null && selectBoxModelList.size() > 0) {
                            for (SelectBoxModel item : selectBoxModelList) {
                                if (item.title.equalsIgnoreCase(selectBoxModel.title)) {
                                    item.isSelected = true;
                                } else {
                                    item.isSelected = false;
                                }
                            }
                        }
                        morningTimeSloteListAdapter.notifyDataSetChanged();

                        List<SelectBoxModel> list = evaningTimeSloteListAdapter.getAllItems();
                        if (list != null && list.size() > 0) {
                            for (SelectBoxModel item : list) {
                                item.isSelected = false;
                            }
                        }
                        evaningTimeSloteListAdapter.notifyDataSetChanged();

                    } else {
                        List<SelectBoxModel> selectBoxModelList = evaningTimeSloteListAdapter.getAllItems();
                        if (selectBoxModelList != null && selectBoxModelList.size() > 0) {
                            for (SelectBoxModel item : selectBoxModelList) {
                                if (item.title.equalsIgnoreCase(selectBoxModel.title)) {
                                    item.isSelected = true;
                                } else {
                                    item.isSelected = false;
                                }
                            }
                        }
                        evaningTimeSloteListAdapter.notifyDataSetChanged();

                        List<SelectBoxModel> list = morningTimeSloteListAdapter.getAllItems();
                        if (list != null && list.size() > 0) {
                            for (SelectBoxModel item : list) {
                                item.isSelected = false;
                            }
                        }
                        morningTimeSloteListAdapter.notifyDataSetChanged();
                    }
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

    @Override
    public void onRightButtonClicked() {
        Calendar selectedMonthCalendar = customCalendar.getSelectedDate();
        System.out.println("right clicked -----------------" + disabledDates);
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        setDisabledDaysNew(disabledDates, selectedMonthCalendar, dateHashmap);
    }

    public void onLeftButtonClicked() {
        Calendar selectedMonthCalendar = customCalendar.getSelectedDate();
        System.out.println("left clicked -----------------" + disabledDates);
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        setDisabledDaysNew(disabledDates, selectedMonthCalendar, dateHashmap);
    }
}