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

import com.pt_plus.Adapter.BookingHistoryListAdapter;
import com.pt_plus.Adapter.OrderHistoryListAdapter;
import com.pt_plus.Model.HorizondalTypeListModal;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrderHistory extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        initVIew(view);
        return view;
    }

    private ImageView imgBack;
    private TextView txtappBarTitle;
    private View appbar;
    private StoreService storeService;
private OrderHistoryListAdapter orderHistoryListAdapter;
    private void initVIew(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.order_history);

        RecyclerView recyclerViewActivities = view.findViewById(R.id.rcy_booking);
         orderHistoryListAdapter = new OrderHistoryListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewActivities.setAdapter(orderHistoryListAdapter);


        storeService = new StoreService(getFragmentActivity(), _callBack);
        storeService.getOrders(ServiceNames.REQUEST_ID_GET_ORDERS);
        getFragmentActivity().showProgress(true);
    }

    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_ORDERS) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null) {
                        orderHistoryListAdapter.updateData( processOrderHistory(jsonArray));
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
    private List<OrderModel> processOrderHistory(JSONArray jsonArray){
        List<OrderModel> orderModelList = new ArrayList<>();
        try {
            if(jsonArray != null && jsonArray.length() > 0){
                for (int i = 0; i< jsonArray.length() ;i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    OrderModel orderModel = new OrderModel();
                    orderModel.orderId = AppUtils.getStringValueFromJson(jsonObject,"order_id");
                    orderModel.id = AppUtils.getStringValueFromJson(jsonObject,"id");
                    List<ProductModel> productModelList = new ArrayList<>();
                    ProductModel productModel = new ProductModel();
                    productModel.productId = AppUtils.getStringValueFromJson(jsonObject,"product_id");
                    productModel.name = AppUtils.getStringValueFromJson(jsonObject,"product_name");
                    productModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject,"thumbnail_img");
                   productModel.qty = jsonObject.getInt("quantity");
//                   productModel.qty = AppUtils.getLongValueFromJson(jsonObject,"quantity");

                    productModelList.add(productModel);

                    orderModel.productModelList = productModelList;
                    orderModel.amount = AppUtils.getDoubleValueFromJson(jsonObject,"amount");
                    orderModel.currency = AppUtils.getStringValueFromJson(jsonObject,"currency");
                    orderModel.status = AppUtils.getStringValueFromJson(jsonObject,"status");
                    orderModel.ordered_on = AppUtils.getStringValueFromJson(jsonObject,"ordered_on");
                    orderModelList.add(orderModel);
                }
                return  orderModelList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  orderModelList;
    }
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


                OrderModel orderModel = null;
                try {
                    orderModel = (OrderModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (orderModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", orderModel.id);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentOrderDetails fragment2 = new FragmentOrderDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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


            }
        }
    };

    private void goback() {
        //                    if (getParentFragmentManager().getBackStackEntryCount() > 0) {
//                        getParentFragmentManager().popBackStack();
//                    } else {
//                        getFragmentActivity().onBackPressed();
//                    }
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}