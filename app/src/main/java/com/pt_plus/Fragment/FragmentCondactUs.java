package com.pt_plus.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.pt_plus.Activitys.ActivityLogin;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;


public class FragmentCondactUs extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_condact_us, container, false);
        initView(view);
        return view;
    }

    private View appbar;
    private TextView txtappBarTitle, txtEmail, txtPhone;
    private GeneralService generalService;
    private MaterialCardView cardViewWahtsAPp, cardViewChat;
    private ImageView imgInstagaram, imgYoutube, imgTicktok, imgTwitter, imgSnapChat;

    private void initView(View view) {
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getFragmentActivity().getString(R.string.contact_us));
        txtEmail = view.findViewById(R.id.txt_email);
        txtPhone = view.findViewById(R.id.txt_phone);
        cardViewWahtsAPp = view.findViewById(R.id.carview_whatsApp);
        imgYoutube = view.findViewById(R.id.img_youtube);
        imgInstagaram = view.findViewById(R.id.img_instagram);
        imgTicktok = view.findViewById(R.id.img_tick_tok);
        imgTwitter = view.findViewById(R.id.img_twitter);
        imgSnapChat = view.findViewById(R.id.img_snap_chat);
        cardViewChat = view.findViewById(R.id.carview_chat);
        generalService = new GeneralService(getFragmentActivity(), _callBack);
        generalService.getCondactDetails(ServiceNames.REQUEST_GET_CONDACT_DEATILS);
        getFragmentActivity().showProgress(true);
        cardViewChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                generalService.getChatExicutive(ServiceNames.REQUEST_GET_CHAT_EXICUTIVE);
            }
        });
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        if (!getFragmentActivity().isEnglish()) {
            txtEmail.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
    }

    private ImageView imgBack;
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

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_GET_CONDACT_DEATILS) {
                    setView(jsonObject);
                } else if (serviceId == ServiceNames.REQUEST_GET_CHAT_EXICUTIVE) {
                    if (getFragmentActivity().isUserLoged()) {

                        JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                        if (jsonArray != null && jsonArray.length() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", AppUtils.getStringValueFromJson((JSONObject) jsonArray.get(0), "id"));
                            bundle.putString("name", AppUtils.getStringValueFromJson((JSONObject) jsonArray.get(0), "name"));
                            bundle.putString("thumbnail_img", AppUtils.getStringValueFromJson((JSONObject) jsonArray.get(0), "thumbnail_img"));
                            FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentChatDetails fragment2 = new FragmentChatDetails();
                            fragment2.setArguments(bundle);
                            fragmentTransaction.replace(R.id.lnr_content, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    } else {
                        Intent intent = new Intent(getFragmentActivity(), ActivityLogin.class);
                        startActivityForResult(intent, 111);
                        getFragmentActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void setView(JSONObject jsonObject) {
        try {
            if (jsonObject.has("email") && AppUtils.getStringValueFromJson(jsonObject, "email") != null && !AppUtils.getStringValueFromJson(jsonObject, "email").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "email").equalsIgnoreCase("null")) {
                txtEmail.setText(AppUtils.getStringValueFromJson(jsonObject, "email"));
            }
            if (jsonObject.has("phone") && AppUtils.getStringValueFromJson(jsonObject, "phone") != null && !AppUtils.getStringValueFromJson(jsonObject, "phone").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "phone").equalsIgnoreCase("null")) {
                txtPhone.setText(AppUtils.getStringValueFromJson(jsonObject, "phone"));
            }


            cardViewWahtsAPp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("whatsapp://send?phone=" + AppUtils.getStringValueFromJson(jsonObject, "whatsapp") + "&text=" + URLEncoder.encode("Message\n", "UTF-8")));
                        getFragmentActivity().startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(getFragmentActivity(), "Whatsapp not installed!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            imgInstagaram.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri appUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "instagram_link"));
                    Uri browserUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "instagram_link"));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);

                }
            });

            imgYoutube.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri appUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "youtube_link"));
                    Uri browserUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "youtube_link"));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);
                }
            });
            imgTicktok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri appUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "tiktok_link"));
                    Uri browserUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "tiktok_link"));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);

                }
            });
            imgTwitter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri appUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "twitter_link"));
                    Uri browserUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "twitter_link"));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);

                }
            });
            imgSnapChat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri appUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "snapchat_link"));
                    Uri browserUri = Uri.parse(AppUtils.getStringValueFromJson(jsonObject, "snapchat_link"));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                    startActivity(browserIntent);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}