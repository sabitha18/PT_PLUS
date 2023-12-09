package com.pt_plus.Service;

import android.content.Context;

import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Utils.AppLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralService {
    Context context;
    ApiInterface service;
    CallBack _callBack;

    public GeneralService(Context context, CallBack callBack) {
        this.context = context;
        this._callBack = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }


    public void getCountries(int ServiceId) {
        try {


            Call<Object> call = service.getCountries();
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

    public void getCity(int ServiceId, long cityId) {
        try {


            Call<Object> call = service.getCity(cityId);
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

    public void getBlock(int ServiceId, long cityId) {
        try {


            Call<Object> call = service.getBlock(cityId);
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

    public void addAddress(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.addAddress(body);
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

    public void getAddress(int ServiceId) {
        try {


            Call<Object> call = service.getAddress();
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

    public void getExploreSearch(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getExploreSearch(body);
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

    public void getPrivacyPolicy(int ServiceId) {
        try {


            Call<Object> call = service.getPrivacyPolicy();
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
    }public void getTermsANdConditions(int ServiceId) {
        try {


            Call<Object> call = service.getTermsANdConditions();
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

    public void getGallery(int ServiceId) {
        try {


            Call<Object> call = service.getGallery();
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
    public void getTRainerGallery(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTRainerGallery(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray,response.code());
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

    public void getChatHistory(int ServiceId) {
        try {


            Call<Object> call = service.getChatHistory();
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
    public void getCondactDetails(int ServiceId) {
        try {


            Call<Object> call = service.getCondactDetails();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray,response.code());

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
    public void getChatExicutive(int ServiceId) {
        try {


            Call<Object> call = service.getChatExicutive();
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

    public void getChatVIew(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getChatVIew(body);
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
    public void sentMessage(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.sentMessage(body);
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

    public void getAboutUs(int ServiceId) {
        try {


            Call<Object> call = service.getAboutUs();
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

    public void getNotifications(int ServiceId) {
        try {


            Call<Object> call = service.getNotifications();
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

    public void changeNotificationToggleStatus(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.changeNotificationToggleStatus(body);
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
