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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ChatHistoryModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.cons.AppCons;

import java.util.List;


public class ChatHistoryListAdapter extends RecyclerView.Adapter<ChatHistoryListAdapter.ViewHolder> {
    private List<ChatHistoryModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public ChatHistoryListAdapter(SuperActivity activity, List<ChatHistoryModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<ChatHistoryModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_history_list, parent, false);
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
            ChatHistoryModel item = _groupList.get(position);
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Adjust caching strategy if needed
                    .error(R.drawable.no_image);
            Glide.with(_activity)
                    .load(item.thumbnail_img)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image).apply(requestOptions)
                    .into(holder.imgUser);
holder.txtName.setText(item.name);
holder.txtMsg.setText(item.last_message);
holder.txtDate.setText(AppDateUtils.getDisplayableTime(AppDateUtils.getInstance().convertStringToDateIst(item.last_message_time, AppCons.YYYY_MMM_DD_HH_MM_FORMAT).getTime()));

            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
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
        return _groupList != null ? _groupList.size() : 0;
    }

    public List<ChatHistoryModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
      private LinearLayout lnrMain;
private TextView txtTitle,txtName,txtMsg,txtDate;
private ImageView imgUser;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);
            imgUser = view.findViewById(R.id.imageView);
            txtName = view.findViewById(R.id.txt_name);
            txtMsg = view.findViewById(R.id.txt_msg);
            txtDate = view.findViewById(R.id.txt_date);

        }
    }


}
