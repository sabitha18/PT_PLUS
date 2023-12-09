package com.pt_plus.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
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
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentAppoinment extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private TrainerService trainerService;
    private TrainerServiceNew trainerServiceOne;
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

        Calendar calendar = Calendar.getInstance();
//        calendarView = view.findViewById(R.id.calendarView);
//        calendarView.setMinimumDate(calendar);
       // calendarView.setDisabledDays(getDisabledDays());

//        calendarView.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) {
//                AppLogger.log("dafadf        " + eventDay.getCalendar());
//                loadTimeSlotes(eventDay.getCalendar());
//            }
//        });

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
                    JSONArray disabledDates = jsonObject.getJSONArray("result");
                    setDisabledDaysNew(disabledDates);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<Calendar> getDisabledDays(List<String> disabledWeekDays) {
        System.out.println(" check ----------    " + disabledWeekDays);

        List<Calendar> disabledCalendars = new ArrayList<>();

        // Get the current day of the week
        Calendar today = Calendar.getInstance();
        int currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

        // Iterate through each day in the disabled week days list
        for (String disabledWeekDay : disabledWeekDays) {
            // Calculate the difference between the current day and the disabled week day
            int dayDifference = getDayOfWeek(disabledWeekDay) - currentDayOfWeek;

            // Adjust the difference to get the correct day in the future
            if (dayDifference <= 0) {
                dayDifference += 7;
            }

            // Create a Calendar instance representing the disabled day
            Calendar disabledCalendar = Calendar.getInstance();
            disabledCalendar.add(Calendar.DAY_OF_MONTH, dayDifference);


            // Limit the loop to add a reasonable number of future occurrences
            int futureOccurrencesLimit = 10;
            int futureOccurrencesCount = 0;

            // Continue adding days until reaching a future month or reaching the limit
            while (disabledCalendar.after(today) && futureOccurrencesCount < futureOccurrencesLimit) {
                disabledCalendars.add((Calendar) disabledCalendar.clone());
                disabledCalendar.add(Calendar.DAY_OF_MONTH, 7); // Move to the next occurrence
                futureOccurrencesCount++;
                System.out.println("Disabled Calendar: work" + disabledCalendars.get(0));
            }
        }
        return disabledCalendars;
    }


    private void setDisabledDaysNew(JSONArray availableDays) {
        try {
            // unavailableDays = new ArrayList<>();
            List<Calendar> disabledCalendars = new ArrayList<>();
            // New list to store unavailable days

            // Create a list of all days of the week
            String[] allDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "friday", "Saturday"};

            // Iterate through all days and check if they are present in the availableDays JSON
            for (String day : allDays) {
                boolean isDayAvailable = false;

                for (int i = 0; i < availableDays.length(); i++) {
                    JSONObject dayObject = availableDays.getJSONObject(i);
                    String availableDay = dayObject.getString("day");

                    if (day.equalsIgnoreCase(availableDay)) {
                        isDayAvailable = true;
                        break;
                    }
                }

                if (!isDayAvailable) {
                    System.out.println("Not available date: " + day);
                    unavailableDays.add(day);


                }
            }
            List<Calendar> disabledCalendarsNew = getDisabledDays(unavailableDays);


            Calendar calendar = Calendar.getInstance();
            calendarView = view.findViewById(R.id.calendarView);
            calendarView.setMinimumDate(calendar);

           // calendarView.setDisabledDays(getDisabledDays());
            if (unavailableDays.size() > 0) {
                calendarView.setDisabledDays(disabledCalendarsNew);
            }
            calendarView.setOnDayClickListener(new OnDayClickListener() {
                @Override
                public void onDayClick(EventDay eventDay) {
                    AppLogger.log("dafadf        " + eventDay.getCalendar());
                    loadTimeSlotes(eventDay.getCalendar());
                }
            });
            System.out.println("Unavailable days: " + Arrays.toString(unavailableDays.toArray()));
            System.out.println("Disabled dates: " + Arrays.toString(disabledCalendars.toArray()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<Calendar> getDisabledDays() {
        Calendar firstDisabled = DateUtils.getCalendar();
        firstDisabled.add(Calendar.DAY_OF_MONTH, 2);

        Calendar secondDisabled = DateUtils.getCalendar();
        secondDisabled.add(Calendar.DAY_OF_MONTH, 1);

        Calendar thirdDisabled = DateUtils.getCalendar();
        thirdDisabled.add(Calendar.DAY_OF_MONTH, 18);

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(firstDisabled);
        calendars.add(secondDisabled);
        calendars.add(thirdDisabled);
        return calendars;
    }

    private int getDayOfWeek(String day) {
        switch (day.toLowerCase()) {
            case "sunday":
                return Calendar.SUNDAY;
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            default:
                return -1; // Invalid day
        }
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

    private CalendarView calendarView;
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
                                JSONArray disabledDates = jsonObject.getJSONArray("result");
                                setDisabledDaysNew(disabledDates);
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
              /*  case R.id.lnr_trainer_see_all: {

                    break;
                }*/
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

}