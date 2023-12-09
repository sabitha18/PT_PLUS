package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.R;

import java.util.List;


public class FilterInnerCategoryListAdapter extends RecyclerView.Adapter<FilterInnerCategoryListAdapter.ViewHolder> {
    private List<CommonCardCategoryModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FilterInnerCategoryListAdapter(SuperActivity activity, List<CommonCardCategoryModel> groupList, Handler handler) {
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
                .inflate(R.layout.layout_filter_inner_category_list, parent, false);
        return new ViewHolder(itemView);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private String tempStr = null;
    FilterInnerCategoryListAdapter filterCategoryListAdapter;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            CommonCardCategoryModel item = _groupList.get(position);

//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);

            holder.txtTitle.setText(item.name);

            if (item.isSelected) {
                holder.txtTitle.setChecked(true);
            } else {
                holder.txtTitle.setChecked(false);
            }


            holder.cardViewGroupBlock.setTag(item);
            holder.cardViewGroupBlock.setOnClickListener(_click);
            holder.txtTitle.setTag(item);
            holder.txtTitle.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.txt_title:
                case R.id.base_cardview: {
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

    public List<CommonCardCategoryModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardViewGroupBlock;
        private RadioButton txtTitle;
        LinearLayout hiddenView;
        CardView cardView;
        RecyclerView recyclerViewCenterList;

        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.base_cardview);
            txtTitle = view.findViewById(R.id.txt_title);

        }
    }


}
