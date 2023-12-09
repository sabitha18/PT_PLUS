package com.pt_plus.Fragment;

import android.app.Service;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pt_plus.Adapter.ChatDetailsAdapter;
import com.pt_plus.Adapter.ChatHistoryListAdapter;
import com.pt_plus.Model.ChatDetailsModel;
import com.pt_plus.Model.HorizondalTypeListModal;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class FragmentChatDetails extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_details, container, false);
        initView(view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return view;
    }

    private ImageView imgBack, imgProfile;
    private View appbar;
    private TextView txtappBarTitle, txtUserName;

    private EditText etDob, etMessage;
    String chatId;
    private LinearLayout lnrSent;
    private CardView carviewSent;
    private GeneralService generalService;

    private void initView(View view) {
        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_booking);
        chatDetailsAdapter = new ChatDetailsAdapter(getFragmentActivity(), null, _handler);
        LinearLayoutManager manager =new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        recyclerViewActivities.setLayoutManager(manager);
        recyclerViewActivities.setAdapter(chatDetailsAdapter);

        lnrSent = view.findViewById(R.id.lnr_sent);
        lnrSent.setOnClickListener(_click);
        carviewSent = view.findViewById(R.id.carview_sent);
        carviewSent.setOnClickListener(_click);
        etMessage = view.findViewById(R.id.et_message);
        txtUserName = view.findViewById(R.id.txt_user_name);
        imgProfile = view.findViewById(R.id.img_profile);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        generalService = new GeneralService(getFragmentActivity(), _callBack);
        Bundle bundle = getArguments();

        try {
            if(bundle.getString("name") != null){
                txtUserName.setText(bundle.getString("name"));
            }
            if(bundle.getString("thumbnail_img") != null){
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Adjust caching strategy if needed
                        .error(R.drawable.no_image);
                Glide.with(getFragmentActivity()).load(bundle.getString("thumbnail_img") ).placeholder(R.drawable.no_image).apply(requestOptions).into(imgProfile);
            }
            chatId = bundle.getString("id");
            generalService.getChatVIew(ServiceNames.REQUEST_GET_CHAT_VIEW, new JSONObject().put("chat_with_id", chatId));
            getFragmentActivity().showProgress(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        callTimer();
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
//        txtappBarTitle = view.findViewById(R.id.appbar_title);
//        txtappBarTitle.setText("Richard Will");


    }

    private Timer timer;

    private void callTimer() {
        try {
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        generalService.getChatVIew(ServiceNames.REQUEST_GET_CHAT_VIEW, new JSONObject().put("chat_with_id", chatId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, 0, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_CHAT_VIEW) {
                    setChatView(jsonObject);
                }else if(serviceId ==  ServiceNames.REQUEST_GET_CHAT_VIEW){
                    generalService.getChatVIew(ServiceNames.REQUEST_GET_CHAT_VIEW, new JSONObject().put("chat_with_id", chatId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {
            timer.cancel();
        }
    };

    private void setChatView(JSONObject jsonObject) {
        try {
            JSONArray chat_person_profile = jsonObject.has("chat_person_profile") ? jsonObject.getJSONArray("chat_person_profile") : null;
            if (chat_person_profile != null && chat_person_profile.length() > 0) {
                txtUserName.setText(AppUtils.getStringValueFromJson((JSONObject) chat_person_profile.get(0), "name"));
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Adjust caching strategy if needed
                        .error(R.drawable.no_image);
                Glide.with(getFragmentActivity()).load(AppUtils.getStringValueFromJson((JSONObject) chat_person_profile.get(0), "thumbnail_img")).placeholder(R.drawable.no_image).apply(requestOptions).into(imgProfile);
            }
            JSONArray messages = jsonObject.has("messages") ? jsonObject.getJSONArray("messages") : null;
            if (messages != null && messages.length() > 0) {
                proccessMessgae(messages);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proccessMessgae(JSONArray jsonArray) {
        try {
            List<ChatDetailsModel> chatDetailsModelList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ChatDetailsModel chatDetailsModel = new ChatDetailsModel();
                if (AppUtils.getStringValueFromJson(jsonObject, "sent_by").equalsIgnoreCase("self")) {
                    chatDetailsModel.isSelf = true;
                } else {
                    chatDetailsModel.isSelf = false;
                }

                chatDetailsModel.message = AppUtils.getStringValueFromJson(jsonObject, "message");
                chatDetailsModelList.add(chatDetailsModel);
            }
            chatDetailsAdapter.updateData(chatDetailsModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ChatDetailsAdapter chatDetailsAdapter;
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


                /*HorizondalTypeListModal HorizondalTypeListModal = null;
                try {
                    HorizondalTypeListModal = (HorizondalTypeListModal) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (HorizondalTypeListModal != null) {
                    List<HorizondalTypeListModal> list = horizondalTypeListAdapter.getAllItems();
                    for (HorizondalTypeListModal item : list) {
                        list.get(list.indexOf(item)).isSelected = false;
                    }
                    list.get(list.indexOf(HorizondalTypeListModal)).isSelected = true;
                    horizondalTypeListAdapter.notifyDataSetChanged();
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }*/
            }
            // Your code logic goes here.
            return true;
        }
    });

    private void sentMessage() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sendto_id", chatId);
            jsonObject.put("message", etMessage.getText().toString());
            generalService.sentMessage(ServiceNames.REQUEST_GET_CHAT_SENT,jsonObject);
            etMessage.setText("");
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
                case R.id.carview_sent:
                case R.id.lnr_sent:
                {
                    sentMessage();

                    break;
                }


            }
        }
    };

    private void goback() {
        timer.cancel();
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}