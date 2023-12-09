package com.pt_plus.Fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Adapter.ChatHistoryListAdapter;
import com.pt_plus.Adapter.SavedAddressListAdapter;
import com.pt_plus.Model.ChatHistoryModel;
import com.pt_plus.Model.HorizondalTypeListModal;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentChatHistory extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_history, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;

    GeneralService generalService;

    private void initView(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.chat_history);


        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_booking);
         chatHistoryListAdapter = new ChatHistoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(chatHistoryListAdapter);



        generalService = new GeneralService(getFragmentActivity(), _callBack);
        generalService.getChatHistory(ServiceNames.REQUEST_GET_CHAT_HISTORY);
        getFragmentActivity().showProgress(true);
    }
    ChatHistoryListAdapter chatHistoryListAdapter;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_CHAT_HISTORY) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        chatHistoryListAdapter.updateData(  proccessChatHistory(jsonArray));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<ChatHistoryModel> proccessChatHistory(JSONArray jsonArray) {
        List<ChatHistoryModel> chatHistoryModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ChatHistoryModel chatHistoryModel = new ChatHistoryModel();
                chatHistoryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                chatHistoryModel.name = AppUtils.getStringValueFromJson(jsonObject, "name");
                chatHistoryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                chatHistoryModel.last_message = AppUtils.getStringValueFromJson(jsonObject, "last_message");
                chatHistoryModel.last_message_time = AppUtils.getStringValueFromJson(jsonObject, "last_message_time");
                chatHistoryModel.chat_with_id = AppUtils.getStringValueFromJson(jsonObject, "chat_with_id");
                chatHistoryModelList.add(chatHistoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatHistoryModelList;
    }


    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


                ChatHistoryModel chatHistoryModel = null;
                try {
                    chatHistoryModel = (ChatHistoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (chatHistoryModel != null) {
                    if (getFragmentActivity().isUserLoged()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",chatHistoryModel.chat_with_id);
                        bundle.putString("name",chatHistoryModel.name);
                        bundle.putString("thumbnail_img",chatHistoryModel.thumbnail_img);
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

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}