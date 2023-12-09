package com.pt_plus.Service.Base;

public interface ServiceNames {

    String URL_GET_ALL_CATEGORIES = "api/store/categories";
    int REQUEST_ID_GET_ALL_CATEGORIES = 100;

    String URL_GET_ALL_NEW_ARRIVALS = "api/store/new-arrivals";
    int REQUEST_ID_GET_ALL_NEW_ARRIVALS = 101;

    String URL_GET_ALL_BEST_SELLERS = "api/store/best-sellers";
    int REQUEST_ID_GET_ALL_BEST_SELLERS = 102;

    String URL_GET_SUB_CATEGORY = "api/store/subcategories";
    int REQUEST_ID_GET_SUB_CATEGORY = 103;

    String URL_GET_PRODUCT_BY_CATEGORY = "api/store/products-by-category";
    int REQUEST_ID_GET_PRODUCT_BY_CATEGORY = 104;

    String URL_GET_PRODUCT_DETAILS = "api/store/product-detail";
    int REQUEST_ID_GET_PRODUCT_DETAILS = 105;

    String URL_GET_GUEST_ADD_TO_CART = "api/store/cart/guest/add-item";
    int REQUEST_ID_GET_GUEST_ADD_TO_CART = 106;


    String URL_GET_STORE_BANNER = "api/store/banners";
    int REQUEST_ID_GET_STORE_BANNER = 107;

    String URL_REGISTER_USER = "api/user/register";
    int REQUEST_ID_REGISTER_USER = 108;

    String URL_LOGIN_USER = "api/user/login";
    int REQUEST_ID_LOGIN_USER = 109;

    String URL_GET_ADD_TO_CART = "api/store/cart/add-item";
    int REQUEST_ID_GET_ADD_TO_CART = 110;

    String URL_GET_GUST_CART = "api/store/cart/guest/get";
    int REQUEST_ID_GET_GUST_CART = 111;

    String URL_GET_GUST_CART_UPDATE = "api/store/cart/guest/update";
    int REQUEST_ID_GET_GUST_CART_UPDATE = 112;

    String URL_GET_CART = "api/store/cart/get";
    int REQUEST_ID_GET_CART = 113;

    String URL_GET_CART_UPDATE = "api/store/cart/update";
    int REQUEST_ID_GET_CART_UPDATE = 114;

    String URL_GET_PRODUCT_ADD_TO_WISH_LIST = "api/store/wishlist/add";
    int REQUEST_ID_PRODUCT_ADD_TO_WISH_LIST = 115;


    String URL_GET_STORE_WISH_LIST = "api/store/wishlist/get";
    int REQUEST_ID_STORE_WISH_LIST = 116;

    String URL_DELETE_STORE_WISH_LIST = "api/store/wishlist/delete";
    int REQUEST_ID_DELETE_STORE_WISH_LIST = 117;


    String URL_UPDATE_PROFILE_PIC = "api/user/profile/dp/update";
    int REQUEST_ID_UPDATE_PROFILE_PIC = 118;

    String URL_GET_COUNTRIES = "api/countries";
    int REQUEST_ID_GET_COUNTRIES = 119;

    String URL_GET_CITY = "api/cities";
    int REQUEST_ID_GET_CITY = 120;

    String URL_GET_BLOCK = "api/blocks";
    int REQUEST_ID_GET_BLOCK = 121;

    String URL_GET_GUST_SINGLE_PRODUCT_DELIVERY_CHARGE = "api/store/checkout/guest/get-deliverycharge/single-product";
    int REQUEST_ID_GET_SINGLE_PRODUCT_DELIVERY_CHARGE = 122;

    String URL_GET_GUST_SINGLE_PRODUCT_CHECKOUT = "api/store/order/guest/make/single-product";
    int REQUEST_ID_GET_GUST_SINGLE_PRODUCT_CHECKOUT = 123;


    String URL_GET_GUST__DELIVERY_CHARGE = "api/store/checkout/guest/get-deliverycharge/cart";
    int REQUEST_ID_GET_GUST_DELIVERY_CHARGE = 124;

    String URL_GET_GUST_CHECKOUT = "api/store/order/guest/make";
    int REQUEST_ID_GET_CHECKOUT = 125;

    String URL_GET_ADD_ADDRESS = "api/user/address/add";
    int REQUEST_ID_GET_ADD_ADDRESS = 126;


    String URL_GET_ADDRESS = "api/user/address/get";
    int REQUEST_ID_GET_ADDRESS = 127;

    String URL_GET_DELIVERY_CHARGE_SINGLE_PRODUCT = "api/store/checkout/get-deliverycharge/single-product";
    int REQUEST_ID_GET_DELIVERY_CHARGE_SINGLE_PRODUCT = 128;

    String URL_GET_SINGLE_PRODUCT_CHECKOUT = "api/store/order/make/single-product";
    int REQUEST_ID_GET_SINGLE_PRODUCT_CHECKOUT = 129;


    String URL_GET_DELIVERY_CHARGE = "api/store/checkout/get-deliverycharge/cart";
    int REQUEST_ID_GET_DELIVERY_CHARGE = 130;

    String URL_GET_STORE_CHECKOUT = "api/store/order/make";
    int REQUEST_ID_GET_STORE_CHECKOUT = 131;

    String URL_GET_ORDERS = "api/store/order/my-orders";
    int REQUEST_ID_GET_ORDERS = 132;

    String URL_GET_ORDER_DETAILS = "api/store/order/detail";
    int REQUEST_ID_GET_ORDER_DETAILS = 133;

    String URL_GET_PROFILE = "api/user/profile/get";
    int REQUEST_ID_GET_PROFILE = 134;


    String URL_GET_ACTIVITIES = "api/activities";
    int REQUEST_ID_GET_ACTIVITIES = 135;

    String URL_GET_MAIN_CATEGORIES = "api/main-categories";
    int REQUEST_ID_GET_MAIN_CATEGORIES = 136;
    String URL_GET_GET_CATEGORIES = "api/trainer/categories";
    int REQUEST_ID_GET_CATEGORIES = 137;
    String URL_GET_TRANIER_SUB_CATEGORIES = "api/trainer/subcategories";
    int REQUEST_ID_GET_TRANIER_SUB_CATEGORIES = 138;


    String URL_GET_TRANIER_SHORT_LIST = "api/trainer/list";
    int REQUEST_ID_GET_TRANIER_SHORT_LIST = 139;

    String URL_GET_TRANIER_LIST_ALL = "api/trainer/list/all";
    int REQUEST_ID_GET_TRANIER_LIST_ALL = 140;

    String URL_GET_TRANIER_DETAIL = "api/trainer/detail";
    int REQUEST_ID_GET_TRANIER_DETAIL = 141;


    String URL_GET_TRANING_PLAN_LIST = "api/trainer/plans";
    int REQUEST_ID_GET_TRANING_PLAN_LIST = 142;

    String URL_GET_TRANING_PLAN_DETAILS = "api/trainer/plan-detail";
    int REQUEST_ID_GET_TRANING_PLAN_DETAILS = 143;

    String URL_GET_TRAINER_ADD_TO_WISHLIST = "api/trainer/wishlist/add";
    int REQUEST_ID_GET_TRAINER_ADD_TO_WISHLIST = 144;

    String URL_GET_PLAN_ADD_TO_WISHLIST = "api/trainer/plan/wishlist/add";
    int REQUEST_ID_GET_PLAN_ADD_TO_WISHLIST = 145;

    String URL_GET_TRAINER_WISH_LIST = "api/trainer/wishlist/get";
    int REQUEST_ID_GET_TRAINER_WISH_LIST = 146;
    String URL_GET_TRAINER_REMOVE_WISH_LIST = "api/trainer/wishlist/delete";
    int REQUEST_ID_GET_TRAINER_REMOVE_WISH_LIST = 147;

    String URL_GET_PLAN_WISH_LIST = "api/trainer/plan/wishlist/get";
    int REQUEST_ID_GET_PLAN_WISH_LIST = 148;

    String URL_GET_PLAN_REMOVE_WISH_LIST = "api/trainer/plan/wishlist/delete";
    int REQUEST_ID_GET_PLAN_REMOVE_WISH_LIST = 149;


    String URL_GET_CENTER_BANNER = "api/center/banners";
    int REQUEST_ID_GET_CENTER_BANNER = 150;

    String URL_GET_CENTER_CATEGORIES = "api/center/categories";
    int REQUEST_ID_GET_CENTER_CATEGORIES = 151;

    String URL_GET_CENTER_SUB_CATEGORIES = "api/center/subcategories";
    int REQUEST_ID_GET_CENTER_SUB_CATEGORIES = 152;

    String URL_GET_CENTER_LIST = "api/center/list";
    int REQUEST_ID_GET_CENTER_LIST = 153;

    String URL_GET_CENTER_DETAILS = "api/center/detail";
    int REQUEST_ID_GET_CENTER_DETAILS = 154;
    String URL_GET_BANNER = "api/banners";
    int REQUEST_ID_GET_BANNER = 155;

    String URL_GET_CENTER_ADD_FAVROTES = "api/center/wishlist/add";
    int REQUEST_ID_CENTER_ADD_FAVROTES = 156;

    String URL_GET_CENTER_FAVROTES_LIST = "api/center/wishlist/get";
    int REQUEST_ID_CENTER_FAVROTES_LIST = 157;

    String URL_GET_CENTER_REMOVE_FAVEROTE = "api/center/wishlist/delete";
    int REQUEST_ID_CENTER_REMOVE_FAVEROTE = 158;

    String URL_GET_TRINER_FILTER_SERVICE_TYPE = "api/trainer/filter-service-types";
    int REQUEST_ID_TRINER_FILTER_SERVICE_TYPE = 159;

    String URL_GET_TRINER_FILTER_CATEGORIES = "api/trainer/filter-categories";
    int REQUEST_ID_TRINER_FILTER_CATEGORIES = 160;


    String URL_GET_TRINER_TIME_SLOTES = "api/trainer/time-slots";
    int REQUEST_ID_TRINER_TIME_SLOTES = 161;


    String URL_BOOK_TRINER = "api/trainer/booking/make";
    int REQUEST_ID_BOOK_TRINER = 162;

    String URL_GET_MY_BOOKINGS = "api/trainer/booking/my-bookings";
    int REQUEST_ID_GET_MY_BOOKINGS = 163;

    String URL_TRINNER_FILTER = "api/trainer/list/byfilters";
    int REQUEST_ID_TRINNER_FILTER = 164;


    String URL_TRINNER_CATEGORIES = "api/trainer/categories/";
    int REQUEST_ID_TRINNER_CATEGORIES = 165;

    String URL_GET_ACTIVITIES_ALL = "api/activities/all";
    int REQUEST_ID_ACTIVITIES_ALL = 166;

    String URL_GET_TRAINER_FAV_PRODUCTS = "api/trainer/fav-products";
    int REQUEST_ID_TRAINER_FAV_PRODUCTS = 167;

    String URL_GET_TRAINER_BOOKING_DETAILS = "api/trainer/booking/detail";
    int REQUEST_ID_TRAINER_BOOKING_DETAILS = 168;
    String URL_GET_CANCEL_BOOKING = "api/trainer/booking/cancel";
    int REQUEST_ID_CANCEL_BOOKING = 169;

    String URL_GET_RELATED_PRODUCTS = "api/store/related-products";
    int REQUEST_ID_RELATED_PRODUCTS = 170;

    String URL_GET_EXPLORE = "api/search";
    int REQUEST_ID_EXPLORE = 171;

    String URL_GET_PRIVACY_POLICY = "api/privacy-policy";
    int REQUEST_ID_PRIVACY_POLICY = 172;

    String URL_GET_CANCEL_ORDER = "api/store/order/cancel";
    int REQUEST_ID_CANCEL_ORDER = 173;

    String URL_GET_GALLERY = "api/user/gallery/get";
    int REQUEST_ID_GET_GALLERY = 174;

    String URL_GET_TRAINER_GALLERY = "api/trainer/gallery";
    int REQUEST_GET_TRAINER_GALLERY = 175;


    String URL_GET_CHAT_HISTORY = "api/chat/history";
    int REQUEST_GET_CHAT_HISTORY = 176;
    String URL_GET_ORDER_TRANSACTION_DETAILS = "api/store/order/transactions/detail";
    int REQUEST_GET_ORDER_TRANSACTION_DETAILS = 177;


    String URL_GET_CONDACT_DEATILS = "api/contact-details";
    int REQUEST_GET_CONDACT_DEATILS = 178;


    String URL_GET_CHAT_EXICUTIVE = "api/chat/support-executives";
    int REQUEST_GET_CHAT_EXICUTIVE = 179;

    String URL_GET_CHAT_VIEW = "api/chat/view";
    int REQUEST_GET_CHAT_VIEW = 180;

    String URL_GET_CHAT_SENT = "api/chat/send";
    int REQUEST_GET_CHAT_SENT = 181;

    String URL_UPDATE_PROFILE = "api/user/profile/update";
    int REQUEST_UPDATE_PROFIle = 182;

    String URL_GET_TERMS_CONDITIONS = "api/terms-conditions";
    int REQUEST_GET_TERMS_CONDITIONS = 183;

    String URL_GET_CENTER_PRODUCT_LIST = "api/center/products/list";
    int REQUEST_GET_CENTER_PRODUCT_LIST = 184;

    String URL_GET_CENTER_TRAINER_LIST = "api/center/trainers/list";
    int REQUEST_GET_CENTER_TRAINER_LIST = 185;

    String URL_GET_CENTER_GROUP_TYPES = "api/center/list/groupbytypes";
    int REQUEST_GET_CENTER_GROUP_TYPES = 186;

    String URL_GET_ABOUT_US = "api/app-description";
    int REQUEST_GET_GET_ABOUT_US = 187;

    String URL_GET_CENTER_PLAN_DETAILS = "api/center/plan-detail";
    int REQUEST_GET_CENTER_PLAN_DETAILS = 188;

    String URL_GET_CENTER_TIME_SLOTES = "api/center/time-slots";
    int REQUEST_GET_CENTER_TIME_SLOTES = 189;
    String URL_GET_CENTER_BOOKING = "api/center/booking/make";
    int REQUEST_GET_CENTER_BOOKINGS = 190;

    String URL_GET_CENTER_BOOKING_DETAILS = "api/center/booking/detail";
    int REQUEST_GET_CENTER_BOOKING_DETAILS = 191;

    String URL_GET_CENTER_BOOKING_HISTORY = "api/center/booking/my-bookings";
    int REQUEST_GET_CENTER_BOOKING_HISTORY = 192;

    String URL_GET_CENTER_BOOKING_CANCEL = "api/center/booking/cancel";
    int REQUEST_GET_CENTER_BOOKING_CANCEL = 193;

    String URL_GET_STORE_FILTER_CATEGORIES = "api/store/filter-categories";
    int REQUEST_GET_STORE_FILTER_CATEGORIES = 194;



    String URL_GET_STORE_PRODUCT_FILTER = "api/store/products-by-filter";
    int REQUEST_GET_STORE_PRODUCT_FILTER = 195;


    String URL_GET_CENTER_PLAN_LIST = "api/center/plans/list";
    int REQUEST_GET_CENTER_PLAN_LIST = 196;

    String URL_GET_CENTER_GALLERY = "api/center/gallery/list";
    int REQUEST_GET_CENTER_GALLERY = 197;

    String URL_GET_FORGOT_PASSWORD_OTP = "api/user/forgot-password/get-otp";
    int REQUEST_GET_FORGOT_PASSWORD_OTP= 198;

    String URL_FORGOT_VERIFY_OTP = "api/user/forgot-password/verify-otp";
    String URL_REGISTER_VERIFY_OTP = "api/user/login/by-otp";
    int REQUEST_FORGOT_VERIFY_OTP= 199;


    String URL_FORGOT_SET_PASSWORD = "api/user/forgot-password/set-password";
    int REQUEST_FORGOT_SET_PASSWORD= 200;

    String URL_GET_TRAINER_FILTER_LOCATIONS = "api/trainer/filter-locations";
    int REQUEST_GET_TRAINER_FILTER_LOCATIONS= 201;

    String URL_LOGOUT = "api/user/logout";
    int REQUEST_ID_LOGOUT= 202;

    String URL_GET_NOTIFICATIONS = "api/user/notifications";
    int REQUEST_ID_GET_NOTIFICATIONS= 203;

    String URL_GET_NOTIFICATION_TOGGLE_STATUS = "api/user/notifications/toggle-status";
    int REQUEST_ID_GET_NOTIFICATION_TOGGLE_STATUS= 204;

    String GET_ALL_TRAINER_TIMESLOT = "api/trainer/time-slots-all";
    int REQUEST_GET_ALL_TRAINER_TIMESLOT= 205;
}
