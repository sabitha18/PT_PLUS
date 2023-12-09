package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.LocationModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<LocationModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public LocationListAdapter(SuperActivity activity, List<LocationModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<LocationModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_location_list, parent, false);
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
            LocationModel item = _groupList.get(position);
            holder.radioButton.setText(item.name);
            AppLogger.log("453454   ddadsafadsfadsfdsf    "+item.isSelected);
            holder.radioButton.setSelected(false);
            holder.radioButton.setChecked(false);
            if (item.isSelected) {
                holder.radioButton.setSelected(true);
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setSelected(false);
                holder.radioButton.setChecked(false);
            }

            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
            holder.radioButton.setTag(item);
            holder.radioButton.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.lnr_main: {
                    sendMessageThroughHandler(123, obj);
                    break;
                }case R.id.radio_btn: {
                    sendMessageThroughHandler(123, obj);
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

    public List<LocationModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain;
        private RadioButton radioButton;

        private ViewHolder(View view) {
            super(view);


            lnrMain = view.findViewById(R.id.lnr_main);
            radioButton = view.findViewById(R.id.radio_btn);

        }
    }


}
