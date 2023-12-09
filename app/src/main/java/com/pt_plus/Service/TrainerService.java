package com.pt_plus.Service;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.CallBackNew;
import com.pt_plus.Utils.AppLogger;
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

public class TrainerService {
    Context context;
    ApiInterface service;
    CallBack _callBack;
    CallBackNew _callBackNew;

    public TrainerService(Context context, CallBack callBack) {
        this.context = context;
        this._callBack = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }

    public void getActivities(int ServiceId) {
        try {


            Call<Object> call = service.getActivities();
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

    public void getMainCateGories(int ServiceId) {
        try {


            Call<Object> call = service.getMainCateGories();
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

    public void getTrainerCateogries(int ServiceId) {
        try {


            Call<Object> call = service.getTrainerCateogries();
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
    public void getTrainerCateogriesById(int ServiceId,String id) {
        try {


            Call<Object> call = service.getTrainerCateogriesById(id);
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

    public void getTranierSubCategories(int ServiceId, String str) {
        try {


            Call<Object> call = service.getTranierSubCategories(str);
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

    public void getTranerShortList(int ServiceId) {
        try {


            Call<Object> call = service.getTranerShortList();
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

    public void getTranerListAll(int ServiceId) {
        try {


            Call<Object> call = service.getTranerListAll();
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

    public void getTrainerDetails(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrainerDetails(body);
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

    public void getTrainingPlanLIst(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrainingPlanLIst(body);
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

    public void getPlanDetails(int ServiceId, String id) {
        try {


            Call<Object> call = service.getTrainerPlanDetails(id);
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

    public void trainerAddTowishList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.trainerAddTowishList(body);
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

    public void planAddToWishList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.planAddToWishList(body);
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

    public void getTrainerWishLIst(int ServiceId) {
        try {


            Call<Object> call = service.getTrainerWishLIst();
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

    public void trainerRemoveWishList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.trainerRemoveWishList(body);
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

    public void getPlanWishList(int ServiceId) {
        try {


            Call<Object> call = service.getPlanWishList();
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

    public void planRemoveWishList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.planRemoveWishList(body);
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

    public void getBanner(int ServiceId) {
        try {


            Call<Object> call = service.getBanner();
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

    public void getTrinerFilterServiceType(int ServiceId) {
        try {


            Call<Object> call = service.getTrinerFilterServiceType();
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

    public void getTrinerFilterCategories(int ServiceId) {
        try {


            Call<Object> call = service.getTrinerFilterCategories();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                     AppLogger.log(response.body().toString());
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

    public void getTrianerAllTimeSlotes(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrianerAllTimeSlotes(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        if(response.code() == 200){
                            JSONArray jsArray = new JSONArray((ArrayList) response.body());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("result", jsArray);
                            System.out.println(" result ---   "+jsArray);
                            _callBackNew.onSuccess(ServiceId, jsonObject, response.code());
                        }else {
                            JSONObject obj = new JSONObject((Map) response.body());
                            System.out.println(" result --new-   "+obj);
                            if(obj.has("status") &&! obj.getBoolean("status")){
                                Toast.makeText(context, AppUtils.getStringValueFromJson(obj,"message"),Toast.LENGTH_SHORT).show();
                                _callBackNew.onSuccess(ServiceId, obj, response.code());
                            }else {
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

                    _callBack.onError(ServiceId, t.getMessage(), 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getTrianerTimeSlotes(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrianerTimeSlotes(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        if(response.code() == 200){
                            JSONArray jsArray = new JSONArray((ArrayList) response.body());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("result", jsArray);
                            System.out.println(" result ---   "+jsArray);
                            _callBack.onSuccess(ServiceId, jsonObject, response.code());
                        }else {
                            JSONObject obj = new JSONObject((Map) response.body());
                            System.out.println(" result --new-   "+obj);
                            if(obj.has("status") &&! obj.getBoolean("status")){
                                Toast.makeText(context, AppUtils.getStringValueFromJson(obj,"message"),Toast.LENGTH_SHORT).show();
                                _callBack.onSuccess(ServiceId, obj, response.code());
                            }else {
                                JSONArray jsArray = new JSONArray((ArrayList) response.body());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("result", jsArray);
                                _callBack.onSuccess(ServiceId, jsonObject, response.code());
                            }
                        }


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

    public void bookTrainer(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.bookTrainer(body);
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

    public void getBookingDetails(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getBookingDetails(body);
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
    public void cancelBooking(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.cancelBooking(body);
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

    public void getMyBookings(int ServiceId) {
        try {


            Call<Object> call = service.getMyBookings();
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

    public void trainerFilter(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.trainerFilter(body);
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

    } public void getCenterTrainerList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterTrainerList(body);
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

    public void getTrainerCateories(int ServiceId, String id) {
        try {


            Call<Object> call = service.getTrainerCateories(id);
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

    public void getActivitiesAll(int ServiceId) {
        try {
            Call<Object> call = service.getActivitiesAll();
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

    public void getTrainerFilterLocation(int ServiceId) {
        try {


            Call<Object> call = service.getTrainerFilterLocation();
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
}
