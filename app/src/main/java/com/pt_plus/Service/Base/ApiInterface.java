package com.pt_plus.Service.Base;


import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.pt_plus.Model.StoreCategory.StoreCategory;
import com.pt_plus.Model.StoreCategory.StoreCategoryItem;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //sending mobile num to get otp
//    @FormUrlEncoded
//    @GET("api/store/categories")
    @GET(ServiceNames.URL_GET_ALL_CATEGORIES)
    Call<Object> GetCategories();

    @GET(ServiceNames.URL_GET_ALL_NEW_ARRIVALS)
    Call<Object> newArrivals();

    @GET(ServiceNames.URL_GET_ALL_BEST_SELLERS)
    Call<Object> bestSellers();

    @GET(ServiceNames.URL_GET_SUB_CATEGORY + "/{id}")
    Call<Object> getSubCategorys(@Path("id") String id);

    @GET(ServiceNames.URL_GET_PRODUCT_BY_CATEGORY + "/{id}")
    Call<Object> getProductByCateGory(@Path("id") String id);

    @GET(ServiceNames.URL_GET_PRODUCT_DETAILS + "/{id}")
    Call<Object> getProductDetails(@Path("id") String id);

    @POST(ServiceNames.URL_GET_GUEST_ADD_TO_CART)
    Call<Object> guestAddToCart(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_STORE_BANNER)
    Call<Object> getStoreBanner();

    @POST(ServiceNames.URL_REGISTER_USER)
    Call<Object> registerUser(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_LOGIN_USER)
    Call<Object> loginUser(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_ADD_TO_CART)
    Call<Object> addToCart(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_GUST_CART)
    Call<Object> getGustCart(@Query("guest_id") String guest_id);

    @POST(ServiceNames.URL_GET_GUST_CART_UPDATE)
    Call<Object> gustCartUpdate(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_CART)
    Call<Object> getCart();

    @POST(ServiceNames.URL_GET_CART_UPDATE)
    Call<Object> cartUpdate(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_PRODUCT_ADD_TO_WISH_LIST)
    Call<Object> productAddTowishList(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_STORE_WISH_LIST)
    Call<Object> getStoreWishList();

    @POST(ServiceNames.URL_DELETE_STORE_WISH_LIST)
    Call<Object> deleteStoreWishListProduct(@Body RequestBody jsonObject);

    @Multipart
    @POST(ServiceNames.URL_UPDATE_PROFILE_PIC)
    Call<Object> updateProfilePic(@Part MultipartBody.Part file);

    @GET(ServiceNames.URL_GET_COUNTRIES)
    Call<Object> getCountries();

    @GET(ServiceNames.URL_GET_CITY)
    Call<Object> getCity(@Query("country_id") long city_id);

    @GET(ServiceNames.URL_GET_BLOCK)
    Call<Object> getBlock(@Query("city_id") long city_id);

    @POST(ServiceNames.URL_GET_GUST_SINGLE_PRODUCT_DELIVERY_CHARGE)
    Call<Object> gustSingleProductDeliveryCharge(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_GUST_SINGLE_PRODUCT_CHECKOUT)
    Call<Object> gustSingleProductCheckOUt(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_GUST__DELIVERY_CHARGE)
    Call<Object> gustDeliveryCharge(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_GUST_CHECKOUT)
    Call<Object> gustCheckout(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_ADD_ADDRESS)
    Call<Object> addAddress(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_ADDRESS)
    Call<Object> getAddress();


    @POST(ServiceNames.URL_GET_DELIVERY_CHARGE_SINGLE_PRODUCT)
    Call<Object> getDeliveryChargeSingleProduct(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_SINGLE_PRODUCT_CHECKOUT)
    Call<Object> singleProductCheckout(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_DELIVERY_CHARGE)
    Call<Object> getDeliveryCharge(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_STORE_CHECKOUT)
    Call<Object> storeCheckOut(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_ORDERS)
    Call<Object> getOrders();


    @POST(ServiceNames.URL_GET_ORDER_DETAILS)
    Call<Object> getOrderDetails(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_PROFILE)
    Call<Object> getProfile();

    @GET(ServiceNames.URL_GET_ACTIVITIES)
    Call<Object> getActivities();


    @GET(ServiceNames.URL_GET_MAIN_CATEGORIES)
    Call<Object> getMainCateGories();

    @GET(ServiceNames.URL_GET_GET_CATEGORIES)
    Call<Object> getTrainerCateogries();

    @GET(ServiceNames.URL_GET_TRANIER_SUB_CATEGORIES + "/{id}")
    Call<Object> getTranierSubCategories(@Path("id") String id);


    @GET(ServiceNames.URL_GET_GET_CATEGORIES + "/{id}")
    Call<Object> getTrainerCateogriesById(@Path("id") String id);

    @GET(ServiceNames.URL_GET_TRANIER_SHORT_LIST)
    Call<Object> getTranerShortList();


    @GET(ServiceNames.URL_GET_TRANIER_LIST_ALL)
    Call<Object> getTranerListAll();

    @POST(ServiceNames.URL_GET_TRANIER_DETAIL)
    Call<Object> getTrainerDetails(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_TRANING_PLAN_LIST)
    Call<Object> getTrainingPlanLIst(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_TRANING_PLAN_DETAILS + "/{id}")
    Call<Object> getTrainerPlanDetails(@Path("id") String id);

    @POST(ServiceNames.URL_GET_TRAINER_ADD_TO_WISHLIST)
    Call<Object> trainerAddTowishList(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_PLAN_ADD_TO_WISHLIST)
    Call<Object> planAddToWishList(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_TRAINER_WISH_LIST)
    Call<Object> getTrainerWishLIst();

    @POST(ServiceNames.URL_GET_TRAINER_REMOVE_WISH_LIST)
    Call<Object> trainerRemoveWishList(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_PLAN_WISH_LIST)
    Call<Object> getPlanWishList();

    @POST(ServiceNames.URL_GET_PLAN_REMOVE_WISH_LIST)
    Call<Object> planRemoveWishList(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_CENTER_BANNER)
    Call<Object> getCenterBanner();

    @GET(ServiceNames.URL_GET_CENTER_CATEGORIES + "/{id}")
    Call<Object> getCenterCategories(@Path("id") String id);

    @GET(ServiceNames.URL_GET_CENTER_SUB_CATEGORIES + "/{id}")
    Call<Object> getCenterSubCategories(@Path("id") String id);

    @POST(ServiceNames.URL_GET_CENTER_LIST)
    Call<Object> getCenterList(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_DETAILS)
    Call<Object> centerDetails(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_BANNER)
    Call<Object> getBanner();

    @POST(ServiceNames.URL_GET_CENTER_ADD_FAVROTES)
    Call<Object> centerAddTofaverits(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_CENTER_FAVROTES_LIST)
    Call<Object> getCenterFaveroteList();


    @POST(ServiceNames.URL_GET_CENTER_REMOVE_FAVEROTE)
    Call<Object> removeCenterFav(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_TRINER_FILTER_SERVICE_TYPE)
    Call<Object> getTrinerFilterServiceType();

    @GET(ServiceNames.URL_GET_TRINER_FILTER_CATEGORIES)
    Call<Object> getTrinerFilterCategories();

    @POST(ServiceNames.GET_ALL_TRAINER_TIMESLOT)
    Call<Object> getTrianerAllTimeSlotes(@Body RequestBody jsonObject);
    @POST(ServiceNames.URL_GET_TRINER_TIME_SLOTES)
    Call<Object> getTrianerTimeSlotes(@Body RequestBody jsonObject);
    @POST(ServiceNames.URL_BOOK_TRINER)
    Call<Object> bookTrainer(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_MY_BOOKINGS)
    Call<Object> getMyBookings();

    @POST(ServiceNames.URL_TRINNER_FILTER)
    Call<Object> trainerFilter(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_TRINNER_CATEGORIES + "/{id}")
    Call<Object> getTrainerCateories(@Path("id") String id);

    @GET(ServiceNames.URL_GET_ACTIVITIES_ALL)
    Call<Object> getActivitiesAll();

    @POST(ServiceNames.URL_GET_TRAINER_FAV_PRODUCTS)
    Call<Object> getTrainerFavrateProducts(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_TRAINER_BOOKING_DETAILS)
    Call<Object> getBookingDetails(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CANCEL_BOOKING)
    Call<Object> cancelBooking(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_RELATED_PRODUCTS)
    Call<Object> getReatedProduct(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_EXPLORE)
    Call<Object> getExploreSearch(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_PRIVACY_POLICY)
    Call<Object> getPrivacyPolicy();

    @POST(ServiceNames.URL_GET_CANCEL_ORDER)
    Call<Object> cancelOrder(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_GALLERY)
    Call<Object> getGallery();

    @POST(ServiceNames.URL_GET_TRAINER_GALLERY)
    Call<Object> getTRainerGallery(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CHAT_HISTORY)
    Call<Object> getChatHistory();

    @POST(ServiceNames.URL_GET_ORDER_TRANSACTION_DETAILS)
    Call<Object> getOrderTransactionDeatils(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_CONDACT_DEATILS)
    Call<Object> getCondactDetails();

    @GET(ServiceNames.URL_GET_CHAT_EXICUTIVE)
    Call<Object> getChatExicutive();


    @POST(ServiceNames.URL_GET_CHAT_VIEW)
    Call<Object> getChatVIew(@Body RequestBody jsonObject);


    @POST(ServiceNames.URL_GET_CHAT_SENT)
    Call<Object> sentMessage(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_UPDATE_PROFILE)
    Call<Object> updateProfile(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_TERMS_CONDITIONS)
    Call<Object> getTermsANdConditions();

    @POST(ServiceNames.URL_GET_CENTER_PRODUCT_LIST)
    Call<Object> getCenterProductList(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_TRAINER_LIST)
    Call<Object> getCenterTrainerList(@Body RequestBody jsonObject);


    @GET(ServiceNames.URL_GET_CENTER_GROUP_TYPES)
    Call<Object> getCenterByGroupType();


    @GET(ServiceNames.URL_GET_ABOUT_US)
    Call<Object> getAboutUs();

    @GET(ServiceNames.URL_GET_CENTER_PLAN_DETAILS + "/{id}")
    Call<Object> getCenterPlanDetails(@Path("id") String id);

    @POST(ServiceNames.URL_GET_CENTER_TIME_SLOTES)
    Call<Object> getCenterTimeSLotes(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_BOOKING)
    Call<Object> centerBooking(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_BOOKING_DETAILS)
    Call<Object> centerBookingDetails(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_CENTER_BOOKING_HISTORY)
    Call<Object> getCenterBookingHistory();

    @POST(ServiceNames.URL_GET_CENTER_BOOKING_CANCEL)
    Call<Object> centerBookingCancel(@Body RequestBody jsonObject);

    @GET(ServiceNames.URL_GET_STORE_FILTER_CATEGORIES)
    Call<Object> getStoreFilterCategories();

    @POST(ServiceNames.URL_GET_STORE_PRODUCT_FILTER)
    Call<Object> getProductByFilter(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_PLAN_LIST)
    Call<Object> getCenterPlanList(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_CENTER_GALLERY)
    Call<Object> getCenterGallery(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_GET_FORGOT_PASSWORD_OTP)
    Call<Object> getForgottPasswrodOtp(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_REGISTER_VERIFY_OTP)
    Call<Object> registerVerifyOtp(@Body RequestBody jsonObject);
    @POST(ServiceNames.URL_FORGOT_VERIFY_OTP)
    Call<Object> forgottPasswordVerifyOtp(@Body RequestBody jsonObject);

    @POST(ServiceNames.URL_FORGOT_SET_PASSWORD)
    Call<Object> forgottSetPassword(@Body RequestBody jsonObject);
    @POST(ServiceNames.URL_LOGOUT)
    Call<Object> logout();

    @GET(ServiceNames.URL_GET_TRAINER_FILTER_LOCATIONS)
    Call<Object> getTrainerFilterLocation();

    @GET(ServiceNames.URL_GET_NOTIFICATIONS)
    Call<Object> getNotifications();

    @POST(ServiceNames.URL_GET_NOTIFICATION_TOGGLE_STATUS)
    Call<Object> changeNotificationToggleStatus(@Body RequestBody jsonObject);
}



