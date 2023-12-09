package com.pt_plus.Service.Base;

import org.json.JSONObject;

/**
 * Created by jal on 07-Nov-18.
 */

public interface CallBack {
    void onSuccess(int serviceId, JSONObject jsonObject , int statusCode);

    void onError(int serviceId, String errorMessage ,int statusCode);
}
