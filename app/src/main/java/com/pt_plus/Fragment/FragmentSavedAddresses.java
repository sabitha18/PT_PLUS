package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pt_plus.Adapter.OrderHistoryListAdapter;
import com.pt_plus.Adapter.SavedAddressListAdapter;
import com.pt_plus.Model.AddressModel;
import com.pt_plus.Model.HorizondalTypeListModal;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentSavedAddresses extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_addresses, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private String fromPage;
    private ImageView imgAddAddress;
    private GeneralService generalService;
    private SavedAddressListAdapter savedAddressListAdapter;

    private void initView(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromPage = bundle.getString("FROM");
        }

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        imgAddAddress = view.findViewById(R.id.img_add_address);
        imgAddAddress.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.main_appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.my_addresses);


        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_booking);
         savedAddressListAdapter = new SavedAddressListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(savedAddressListAdapter);


        generalService = new GeneralService(getFragmentActivity(), _callBack);
        generalService.getAddress(ServiceNames.REQUEST_ID_GET_ADDRESS);
        getFragmentActivity().showProgress(true);
    }

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ADDRESS) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null) {
                        savedAddressListAdapter.updateData( processAddressList(jsonArray));
                    }
                }
                getFragmentActivity().showProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private List<AddressModel> processAddressList(JSONArray jsonArray) {
        List<AddressModel> addressModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i =0 ;i<jsonArray.length() ;i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AddressModel addressModel = new AddressModel();
                    addressModel.id = AppUtils.getStringValueFromJson(jsonObject,"id");
                    addressModel.titile = AppUtils.getStringValueFromJson(jsonObject,"title");
                    addressModel.fullName = AppUtils.getStringValueFromJson(jsonObject,"name");
                    addressModel.email = AppUtils.getStringValueFromJson(jsonObject,"email");
                    addressModel.mobilePrefix = AppUtils.getStringValueFromJson(jsonObject,"phone_code");
                    addressModel.mobile = AppUtils.getStringValueFromJson(jsonObject,"phone");
                    addressModel.country = AppUtils.getStringValueFromJson(jsonObject,"country");
                    addressModel.Area = AppUtils.getStringValueFromJson(jsonObject,"state");
                    addressModel.zipCOde = AppUtils.getStringValueFromJson(jsonObject,"pincode");
                    addressModel.streetName = AppUtils.getStringValueFromJson(jsonObject,"street");
                    addressModel.block = AppUtils.getStringValueFromJson(jsonObject,"block");
                    addressModel.building = AppUtils.getStringValueFromJson(jsonObject,"building");
                    addressModel.gender = AppUtils.getStringValueFromJson(jsonObject,"gender");
                    addressModel.AddressType = AppUtils.getStringValueFromJson(jsonObject,"type");
                    addressModelList.add(addressModel);
                }
                return  addressModelList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressModelList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


                AddressModel addressModel = null;
                try {
                    addressModel = (AddressModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (addressModel != null) {
                    OrderModel orderModel= LocalCache.getCache().getSelectedOrder();
                    if(orderModel != null){
                        LocalCache.getCache().getSelectedOrder().addressModel=addressModel;
                    }

                    if( fromPage != null &&fromPage.equalsIgnoreCase("CHECKOUT")){
                        goback();
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("addressModel", addressModel);
                        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentAddAddress fragment2 = new FragmentAddAddress();
                        fragment2.setArguments(bundle);
                        fragmentTransaction.replace(R.id.lnr_content, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.img_add_address: {
                    Bundle bundle = new Bundle();
                    bundle.putString("FROM", "MYADDRESS");
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentAddAddress fragment2 = new FragmentAddAddress();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                }


            }
        }
    };

    private void goback() {
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

}