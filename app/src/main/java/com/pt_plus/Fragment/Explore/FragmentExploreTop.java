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
import android.widget.TextView;

import com.pt_plus.Adapter.ExploreTopListAdapter;
import com.pt_plus.Adapter.TrainersFilterListAdapter;
import com.pt_plus.Fragment.FragmentTrainerDetails;
import com.pt_plus.Fragment.SuperFragment;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.cons.LocalCache;

import java.util.List;


public class FragmentExploreTop extends SuperFragment {

    List<TrainersModel> trainersModelList ;
    public FragmentExploreTop(List<TrainersModel> model) {
        trainersModelList = model;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_top, container, false);
        initView(view);
        return view;
    }
    private TextView txtNodata;
    private ExploreTopListAdapter  exploreTopListAdapter;
    private RecyclerView recyclerViewActivities, recyclerViewTrainerList;
    private void  initView(View view){
        txtNodata = view.findViewById(R.id.txt_no_data);
        recyclerViewTrainerList = view.findViewById(R.id.rcy_trainer_list);
        exploreTopListAdapter = new ExploreTopListAdapter(getFragmentActivity(), null, _handler);
        recyclerViewTrainerList.setLayoutManager(new LinearLayoutManager(getFragmentActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTrainerList.setAdapter(exploreTopListAdapter);
        AppLogger.log("42342345234545245  "+ trainersModelList.size());
        if(trainersModelList != null && trainersModelList.size() > 0){

            exploreTopListAdapter.updateData(trainersModelList);
            txtNodata.setVisibility(View.GONE);
        }else {

        }
    }
    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if (message.what == 2) {


//
                TrainersModel trainersModel = null;
                try {
                    trainersModel = (TrainersModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (trainersModel != null) {
                    LocalCache.getCache().getSelectedBookingModel().trainersModel = trainersModel;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trainersModel", trainersModel);
                    FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentTrainerDetails fragment2 = new FragmentTrainerDetails();
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