package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.R;

import java.util.List;


public class FilterRecliclerviewListAdapter extends RecyclerView.Adapter<FilterRecliclerviewListAdapter.ViewHolder> {
    private List<FilterModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FilterRecliclerviewListAdapter(SuperActivity activity, List<FilterModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<FilterModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter_grid_list, parent, false);
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
            FilterModel item = _groupList.get(position);

//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);


            holder.radioButton.setText(item.title);
            if (item.isSelected) {
                holder.radioButton.setChecked(true);
                holder.radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_24, 0);
            } else {
                holder.radioButton.setChecked(false);
                holder.radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }
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
                case R.id.radio_button: {
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

    public List<FilterModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
        private TextView txtTitle;
        RadioButton radioButton;
        private ViewHolder(View view) {
            super(view);
             radioButton = view.findViewById(R.id.radio_button);
            cardViewGroupBlock = view.findViewById(R.id.img_bg);
            txtTitle = view.findViewById(R.id.txt_title);

        }
    }


}
