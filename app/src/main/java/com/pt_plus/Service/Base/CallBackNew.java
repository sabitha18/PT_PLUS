package com.pt_plus.Service.Base;

import org.json.JSONObject;



public interface CallBackNew {
    void onSuccess(int serviceId, JSONObject jsonObject , int statusCode);

    void onError(int serviceId, String errorMessage ,int statusCode);
}
