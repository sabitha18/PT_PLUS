package com.pt_plus.Service.Base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;


import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    public TokenInterceptor(Context context) {
        this.context = context;
    }

    Context context;
    private boolean isCalledAtLeastOnce = false;

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String lang = "en";
        if ((Boolean) PrefUtils.getFromPrefs(context.getApplicationContext(), PrefKeys.PREF_LANG, true)) {
            lang = "en";
        } else {
            lang = "ar";
        }
        request = request.newBuilder()
                .addHeader("Authorization", "Bearer " + PrefUtils.getFromPrefs(context.getApplicationContext(), PrefKeys.PREF_SESSION, ""))
                .addHeader("Accept", "application/json") //new header added
                .addHeader("x-locale", lang) //new header added
                .build();

        //alternative
//        Headers moreHeaders = request.headers().newBuilder()
//                .add("headerKey1", "HeaderVal1")
//                .add("headerKey2", "HeaderVal2")
//                .set("headerKey2", "HeaderVal2--UpdatedHere") // existing header UPDATED if available, else added.
//                .add("headerKey3", "HeaderKey3")
//                .add("headerLine4 : headerLine4Val") //line with `:`, spaces doesn't matter.
//                .removeAll("headerKey3") //Oops, remove this.
//                .build();
//
//        request = request.newBuilder().headers(moreHeaders).build();

        /* ##### List of headers ##### */
        // headerKey0: HeaderVal0
        // headerKey0: HeaderVal0--NotReplaced/NorUpdated
        // headerKey1: HeaderVal1
        // headerKey2: HeaderVal2--UpdatedHere
        // headerLine4: headerLine4Val

        Response response = chain.proceed(request);
        AppLogger.log("Code   ==>   " + response.code() + "");
        AppLogger.log("Header ==>      " + response.request().headers() + "");

        if (response.code() == 401) {
            WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(context, ActivityLogin.class);
            context.startActivity(intent);

        }
        return response;
    }
}
