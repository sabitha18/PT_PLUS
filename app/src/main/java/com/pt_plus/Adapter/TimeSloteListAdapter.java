package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.SelectBoxModel;
import com.pt_plus.R;

import java.util.List;


public class TimeSloteListAdapter extends RecyclerView.Adapter<TimeSloteListAdapter.ViewHolder> {
    private List<SelectBoxModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public TimeSloteListAdapter(SuperActivity activity, List<SelectBoxModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<SelectBoxModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_time_slot_list, parent, false);
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
            SelectBoxModel item = _groupList.get(position);
//
            holder.txtTitle.setText(item.title);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (item.isSelected) {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.lnrMain.setBackgroundDrawable(ContextCompat.getDrawable(_activity, R.drawable.selected_time_slote));
                    holder.txtTitle.setTextColor(_activity.getResources().getColor(R.color.black));
                } else {
                    holder.txtTitle.setTextColor(_activity.getResources().getColor(R.color.black));
                    holder.lnrMain.setBackground(ContextCompat.getDrawable(_activity, R.drawable.selected_time_slote));
                }
            } else {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.lnrMain.setBackgroundDrawable(ContextCompat.getDrawable(_activity, R.drawable.unselected_time_slote));
                    holder.txtTitle.setTextColor(_activity.getResources().getColor(R.color.white));
                } else {
                    holder.txtTitle.setTextColor(_activity.getResources().getColor(R.color.white));
                    holder.lnrMain.setBackground(ContextCompat.getDrawable(_activity, R.drawable.unselected_time_slote));
                }
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

    public List<SelectBoxModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain;
        private TextView txtTitle;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);

        }
    }


}
