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

public class StoreService {
    Context context;
    ApiInterface service;
    CallBack _callBack;

    public StoreService(Context context, CallBack callBack) {
        this.context = context;
        this._callBack = callBack;
        service = ApiClient.getClient(context).create(ApiInterface.class);
    }

    public void getCategories(int ServiceId) {
        try {

            Call<Object> call = service.GetCategories();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {

                        JSONArray jsArray = new JSONArray((ArrayList) response.body());
                        AppLogger.log("reasdsa     " + jsArray.get(0));
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

    public void getAllNewArrrivals(int ServiceId) {
        try {

            Call<Object> call = service.newArrivals();
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

    public void getAllBestSellerse(int ServiceId) {
        try {

            Call<Object> call = service.bestSellers();
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

    public void getSubCategorys(int ServiceId, String cateGoryId) {
        try {

            Call<Object> call = service.getSubCategorys(cateGoryId);
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

    public void getProductByCateGory(int ServiceId, String cateGoryId) {
        try {

            Call<Object> call = service.getProductByCateGory(cateGoryId);
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

    public void getProductDetails(int ServiceId, String productId) {
        try {

            Call<Object> call = service.getProductDetails(productId);
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

    public void guestAddToCard(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.guestAddToCart(body);
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


    public void getStoreBanner(int ServiceId) {
        try {

            Call<Object> call = service.getStoreBanner();
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

    public void addToCard(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.addToCart(body);
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

    public void getGustCart(int ServiceId, String str) {
        try {


            Call<Object> call = service.getGustCart(str);
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

    public void gustCartUpdate(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.gustCartUpdate(body);
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

    public void getCart(int ServiceId) {
        try {


            Call<Object> call = service.getCart();
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

    public void cartUpdate(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.cartUpdate(body);
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

    public void productAddToWishList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.productAddTowishList(body);
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

    public void getStoreWishList(int ServiceId) {
        try {


            Call<Object> call = service.getStoreWishList();
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


    public void deleteStoreWishListProduct(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.deleteStoreWishListProduct(body);
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

    public void gustSingleProductDeliveryCharge(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.gustSingleProductDeliveryCharge(body);
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

    public void gustSingleProductCheckOUt(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.gustSingleProductCheckOUt(body);
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

    public void gustDeliveryCharge(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.gustDeliveryCharge(body);
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

    public void gustCheckout(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.gustCheckout(body);
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

    public void getDeliveryChargeSingleProduct(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getDeliveryChargeSingleProduct(body);
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

    public void singleProductCheckout(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.singleProductCheckout(body);
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

    public void getDeliveryCharge(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getDeliveryCharge(body);
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

    public void storeCheckOut(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.storeCheckOut(body);
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

    public void getOrders(int ServiceId) {
        try {

            Call<Object> call = service.getOrders();
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

    public void getOrderDetails(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getOrderDetails(body);
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

    public void getTrainerFavrateProducts(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getTrainerFavrateProducts(body);
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

    public void getReatedProduct(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getReatedProduct(body);
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

    public void getCenterProductList(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getCenterProductList(body);
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

    public void cancelOrder(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.cancelOrder(body);
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

    public void getOrderTransactionDeatils(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getOrderTransactionDeatils(body);
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
    public void getStoreFilterCategories(int ServiceId) {
        try {


            Call<Object> call = service.getStoreFilterCategories();
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

    public void getProductByFilter(int ServiceId, JSONObject jsonObject) {
        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            Call<Object> call = service.getProductByFilter(body);
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

}
