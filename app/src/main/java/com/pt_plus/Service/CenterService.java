package com.pt_plus.Service;

import android.content.Context;

import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Utils.AppLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterService {
    Context context;
    ApiInterface service;
    CallBack _callBack;

    public CenterService(Context context, CallBack callBack) {
        this.context = context;
        this._callBack = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }

    public void getCenterBanner(int ServiceId) {
        try {


            Call<Object> call = service.getCenterBanner();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    AppLogger.log("reasdsa    eeee " + t.getMessage());
                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterCategories(int ServiceId, String id) {
        try {


            Call<Object> call = service.getCenterCategories(id);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterSubCategories(int ServiceId, String str) {
        try {


            Call<Object> call = service.getCenterSubCategories(str);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterList(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void centerDetails(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.centerDetails(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {

                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void centerAddTofaverits(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.centerAddTofaverits(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {

                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterFaveroteList(int ServiceId) {
        try {


            Call<Object> call = service.getCenterFaveroteList();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCenterFav(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.removeCenterFav(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {

                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterByGroupType(int ServiceId) {
        try {
            Call<Object> call = service.getCenterByGroupType();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterPlanDetails(int ServiceId, String id) {
        try {


            Call<Object> call = service.getCenterPlanDetails(id);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    AppLogger.log("reasdsa    eeee " + t.getMessage());
                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterTimeSLotes(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterTimeSLotes(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCenterPlanList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterPlanList(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCenterGallery(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterGallery(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void centerBooking(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.centerBooking(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void centerBookingDetails(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.centerBookingDetails(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCenterBookingHistory(int ServiceId) {
        try {


            Call<Object> call = service.getCenterBookingHistory();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result", jsArray);
                        _callBack.onSuccess(ServiceId, jsonObject, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void centerBookingCancel(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.centerBookingCancel(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
