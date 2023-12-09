package com.pt_plus.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FragmentPaymentWebView extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_web_view, container, false);
        initView(view);
        return view;
    }

    WebView webView;
    private View appbar;
    private TextView txtappBarTitle;
    private ImageView imgBack;
    private String fromPage, orderId,bookingId;

    private void initView(View view) {

        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText("");
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String userAgent = System.getProperty( "http.agent" );
        webView.getSettings().setUserAgentString(userAgent);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        String permissionsPolicy = "accelerometer=(), ambient-light-sensor=(), autoplay=(), battery=(), camera=(), " +
                "display-capture=(), document-domain=(), encrypted-media=(), fullscreen=(), geolocation=(), " +
                "gyroscope=(), layout-animations=(), legacy-image-formats=(), magnetometer=(), " +
                "microphone=(), midi=(), oversized-images=(), payment=(), publickey-credentials-get=(), " +
                "screen-wake-lock=(), speaker-selection=(), sync-xhr=(), unsized-media=(), usb=(), vr=(),interest-cohort=(), payment=(), serial=(), browsing-topics=(), hid=()";


        Map<String, String> headers = new HashMap<>();

        // Set the "Permissions-Policy" header with the desired permissions

        headers.put("Permissions-Policy", permissionsPolicy);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("paymentUrl") != null) {
                webView.loadUrl(bundle.getString("paymentUrl"),headers);
            }
            fromPage = bundle.getString("from");
            orderId = bundle.getString("orderId");
            bookingId = bundle.getString("bookingId");
        }
        AppLogger.log("sdhadjhjahjkadhfkjhda       "+bundle.getString("bookingId"));


// Set the Permissions-Policy header
        webView.setWebChromeClient(new WebChromeClient(){


        });

        webView.setWebViewClient(new WebViewClient() {

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // Handle URL loading within the WebView
//
//                view.loadUrl(url,headers);
//                return true;
//            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    AppLogger.log("url   sdas   " + url);
                    if (fromPage.equalsIgnoreCase("booking")) {
                        if (url.contains("https://app.ptplus-co.com/api/trainer/payment/success")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "success");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            bundle.putString("bookingId", bookingId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentBookingTrainerResult fragment2 = new FragmentBookingTrainerResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }else if (url.contains("https://app.ptplus-co.com/api/trainer/payment/error")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "failed");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            bundle.putString("bookingId", bookingId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentBookingTrainerResult fragment2 = new FragmentBookingTrainerResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }
                    } else if (fromPage.equalsIgnoreCase("Center booking")) {
                        if (url.contains("https://app.ptplus-co.com/api/center/payment/success")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "success");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            bundle.putString("bookingId", bookingId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentBookingCenterResult fragment2 = new FragmentBookingCenterResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }else if (url.contains("https://app.ptplus-co.com/api/center/payment/error")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "failed");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            bundle.putString("bookingId", bookingId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentBookingCenterResult fragment2 = new FragmentBookingCenterResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        }
                    }

                    else {
                        if (url.contains("https://app.ptplus-co.com/api/store/payment/success")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "success");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentCheckoutResult fragment2 = new FragmentCheckoutResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else if(url.contains("https://app.ptplus-co.com/api/store/payment/error")){
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "failed");
                            bundle.putString("from", fromPage);
                            bundle.putString("orderId", orderId);
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentCheckoutResult fragment2 = new FragmentCheckoutResult();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // Handle progress updates, JavaScript alerts, etc.
        });


    }


    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.img_back: {
                    goback();
                    break;
                }


            }
        }
    };

    private void goback() {
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

}