package com.pt_plus.Service;

import android.content.Context;

import com.pt_plus.Service.Base.ApiClient;
import com.pt_plus.Service.Base.ApiInterface;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    Context context;
    ApiInterface service;
    CallBack _callBack;

    public AuthService(Context context, CallBack callBack) {
        this.context = context;
        this._callBack = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }


    public void registerUser(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.registerUser(body);
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

    public void loginUser(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.loginUser(body);
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


    public void updateProfilePIc(int ServiceId, File file) {
        try {
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file );
            RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);
            Call<Object> call = service.updateProfilePic(multipartBody);
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


    public void getProfile(int ServiceId) {
        try {


            Call<Object> call = service.getProfile();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        PrefUtils.saveToPrefs(context, PrefKeys.PREF_PROFILE, jsArray.toString());
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

    public void updateProfile(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.updateProfile(body);
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

    public void getForgottPasswrodOtp(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getForgottPasswrodOtp(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        System.out.println("check ..........");
                        JSONObject jsArray = new JSONObject((Map) response.body());
                        _callBack.onSuccess(ServiceId, jsArray, response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("check 11..........");
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    _callBack.onError(ServiceId, t.getMessage(), 0);
                    System.out.println("check 22..........");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("check 33..........");
        }
    }
    public void registerVerifyOtp(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.registerVerifyOtp(body);
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
    public void forgottPasswordVerifyOtp(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.forgottPasswordVerifyOtp(body);
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

    public void forgottSetPassword(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.forgottSetPassword(body);
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

    public void logout(int ServiceId) {
        try {


            Call<Object> call = service.logout();
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
