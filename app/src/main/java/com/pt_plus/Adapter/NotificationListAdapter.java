package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private JSONArray _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public NotificationListAdapter(SuperActivity activity, JSONArray groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(JSONArray categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_list, parent, false);
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
            JSONObject jsonObject = _groupList.getJSONObject(position);
            holder.imgNotifications.setVisibility(View.GONE);
            if (AppUtils.getStringValueFromJson(jsonObject, "image") != null && !AppUtils.getStringValueFromJson(jsonObject, "image").isEmpty() && !AppUtils.getStringValueFromJson(jsonObject, "image").equalsIgnoreCase("null")) {
                Glide.with(_activity)
                        .load(AppUtils.getStringValueFromJson(jsonObject, "image"))
                        .into(holder.imgNotifications);
                holder.imgNotifications.setVisibility(View.VISIBLE);
            }

            holder.txtTitle.setText(AppUtils.getStringValueFromJson(jsonObject, "title"));



            holder.txtDate.setText(AppDateUtils.getInstance().formateDate(AppUtils.getStringValueFromJson(jsonObject, "date")));
//            holder.txtDate.setText(AppDateUtils.getDisplayableTime(AppDateUtils.getInstance().convertStringToDateIst(AppDateUtils.getInstance().convertDateToOtherFormat(AppUtils.getStringValueFromJson(jsonObject, "date"), AppCons.DATE_FORAMTE, AppCons.YYYY_MMM_DD_HH_MM_FORMAT), AppCons.YYYY_MMM_DD_HH_MM_FORMAT).getTime()));
            if (!_activity.isEnglish()) {
                holder.txtTitle.setTextDirection(View.TEXT_DIRECTION_RTL);

                holder.txtDate.setTextDirection(View.TEXT_DIRECTION_RTL);
            }
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
                    sendMessageThroughHandler(1, obj);
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
        return _groupList != null ? _groupList.length() : 0;
    }

    public JSONArray getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain;
        private TextView txtTitle, txtDes, txtDate;
        private ImageView imgNotifications;

        private ViewHolder(View view) {
            super(view);


            imgNotifications = view.findViewById(R.id.img_notifications);
            txtTitle = view.findViewById(R.id.txt_title);
            txtDes = view.findViewById(R.id.txt_des);
            txtDate = view.findViewById(R.id.txt_date);


        }
    }


}
