package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ChatDetailsModel;
import com.pt_plus.R;

import java.util.List;


public class ChatDetailsAdapter extends RecyclerView.Adapter<ChatDetailsAdapter.ViewHolder> {
    private List<ChatDetailsModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public ChatDetailsAdapter(SuperActivity activity, List<ChatDetailsModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<ChatDetailsModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_details_list, parent, false);
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
            ChatDetailsModel item = _groupList.get(position);
//

            holder.lntTxtChatIncoming.setVisibility(View.GONE);
            holder.lntTxtChatOutgoing.setVisibility(View.GONE);
holder.txtIncoming.setText(item.message);
holder.txtOutGoing.setText(item.message);
            if (!item.isSelf) {
                holder.lntTxtChatIncoming.setVisibility(View.VISIBLE);
            } else {
                holder.lntTxtChatOutgoing.setVisibility(View.VISIBLE);
            }

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

    public List<ChatDetailsModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain;
        private TextView txtTitle,txtIncoming,txtOutGoing;
        private LinearLayout lntTxtChatIncoming, lntTxtChatOutgoing;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);
            lntTxtChatIncoming = view.findViewById(R.id.lnt_text_incoming);
            lntTxtChatOutgoing = view.findViewById(R.id.lnt_text_outgoing);
            txtIncoming = view.findViewById(R.id.txt_incoming);
            txtOutGoing = view.findViewById(R.id.txt_outgoing);

        }
    }


}
