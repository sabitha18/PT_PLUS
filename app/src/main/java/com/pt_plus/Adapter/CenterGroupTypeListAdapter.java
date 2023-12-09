package com.pt_plus.Adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Fragment.FragmentCenterDetails;
import com.pt_plus.Model.BookingModel;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.cons.LocalCache;

import java.util.List;


public class CenterGroupTypeListAdapter extends RecyclerView.Adapter<CenterGroupTypeListAdapter.ViewHolder> {
    private List<CommonCardCategoryModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public CenterGroupTypeListAdapter(SuperActivity activity, List<CommonCardCategoryModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<CommonCardCategoryModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_center_group_list, parent, false);
        return new ViewHolder(itemView);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private String tempStr = null;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            CommonCardCategoryModel item = _groupList.get(position);
           holder.txtGroupType.setText(item.name);


            CenterListAdapter  gymCenterListAdapter = new CenterListAdapter(_activity, null, new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {

                    if (message.what == 12) {
                        CenterModel centerModel = null;
                        try {
                            centerModel = (CenterModel) message.obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (centerModel != null) {
                            Object obj = centerModel;
                            sendMessageThroughHandler(12, obj);

                        } else {
//                    toast(AppCons.RECORD_CAN_NOT_OPEN);
                        }
                    }
                    // Your code logic goes here.
                    return true;
                }
            }));
          holder.recyclerViewItem.setLayoutManager(new LinearLayoutManager(_activity, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewItem.setAdapter(gymCenterListAdapter);
            gymCenterListAdapter.updateData(item.centerModelList);

            holder.lnrSeeAll.setTag(item);
            holder.lnrSeeAll.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.lnr_see_all: {
                    sendMessageThroughHandler(18, obj);
                    break;
                }
            }
        }
    };

    private void sendMessageThroughHandler(int id, Object obj) {
        Message msg = new Message();
        msg.what = id;
        msg.obj = obj;
        _handler.sendMessage(msg);
    }

    @Override
    public int getItemCount() {
        return _groupList != null ? _groupList.size() : 0;
    }

    public List<CommonCardCategoryModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtGroupType;
        private RecyclerView recyclerViewItem;
        private LinearLayout lnrSeeAll;

        private ViewHolder(View view) {
            super(view);


            txtGroupType = view.findViewById(R.id.txt_group_type);
            recyclerViewItem = view.findViewById(R.id.rcy_item);
            lnrSeeAll = view.findViewById(R.id.lnr_see_all);


        }
    }


}
