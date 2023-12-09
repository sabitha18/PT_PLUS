package com.pt_plus.Service;

import android.content.Context;
import android.widget.Toast;

import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBackNew;
import com.pt_plus.Utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerServiceNew {
    Context context;
    ApiInterface service;
    CallBackNew _callBackNew;

    public TrainerServiceNew(Context context, CallBackNew callBack) {
        this.context = context;
        this._callBackNew = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }


    public void getTrianerAllTimeSlotes(int ServiceId, JSONObject jsonObject, CallBackNew _callBackNew) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrianerAllTimeSlotes(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        if (response.code() == 200) {
                            JSONArray jsArray = new JSONArray((ArrayList) response.body());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("result", jsArray);
                            System.out.println(" result ---   " + jsArray);
                            _callBackNew.onSuccess(ServiceId, jsonObject, response.code());
                        } else {
                            JSONObject obj = new JSONObject((Map) response.body());
                            System.out.println(" result --new-   " + obj);
                            if (obj.has("status") && !obj.getBoolean("status")) {
                                Toast.makeText(context, AppUtils.getStringValueFromJson(obj, "message"), Toast.LENGTH_SHORT).show();
                                _callBackNew.onSuccess(ServiceId, obj, response.code());
                            } else {
                                JSONArray jsArray = new JSONArray((ArrayList) response.body());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("result", jsArray);
                                _callBackNew.onSuccess(ServiceId, jsonObject, response.code());
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBackNew.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
