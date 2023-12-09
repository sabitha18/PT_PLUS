package com.pt_plus.Fragment.Explore;

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

import com.pt_plus.Adapter.CommonCardCategoryListAdapter;
import com.pt_plus.Adapter.ExploreCenterListAdapter;
import com.pt_plus.Fragment.FragmentCenterDetails;
import com.pt_plus.Fragment.SuperFragment;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.R;

import java.util.List;

public class FragmentExploreCenter extends SuperFragment {

private List<CommonCardCategoryModel> commonCardCategoryModels;
    public FragmentExploreCenter(List<CommonCardCategoryModel> model) {
        commonCardCategoryModels = model;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_center, container, false);
        initView(view);
        return view;
    }
    ExploreCenterListAdapter commonCardCategoryListAdapter;
    private void initView(View view){
        RecyclerView recyclerViewCenterList = view.findViewById(R.id.rcy_center_list);
        commonCardCategoryListAdapter = new ExploreCenterListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCenterList.setAdapter(commonCardCategoryListAdapter);
        if(commonCardCategoryModels != null && commonCardCategoryModels.size() > 0){
            commonCardCategoryListAdapter.updateData(commonCardCategoryModels);
        }
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 1) {


//
                CommonCardCategoryModel commonCardCategoryModel = null;
                try {
                    commonCardCategoryModel = (CommonCardCategoryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (commonCardCategoryModel != null) {


                    Bundle bundle = new Bundle();
                    bundle.putString("id", commonCardCategoryModel.id);

                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentCenterDetails fragment2 = new FragmentCenterDetails();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.lnr_content, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                }
            }
            // Your code logic goes here.
            return true;
        }
    });
}