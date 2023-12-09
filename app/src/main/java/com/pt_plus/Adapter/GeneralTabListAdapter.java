package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.R;

import java.util.List;


public class GeneralTabListAdapter extends RecyclerView.Adapter<GeneralTabListAdapter.ViewHolder> {
    private List<FilterModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public GeneralTabListAdapter(SuperActivity activity, List<FilterModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<FilterModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_general_tab_list, parent, false);
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
//            holder.txtGroupName.setText(AppUtils.getStringValueFromJson(item, "group_title"));
//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);
            holder.txtTitle.setText(item.title);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(item.isSelected){
//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    holder.changeBg.setBackgroundDrawable(ContextCompat.getDrawable(_activity, R.drawable.white_border_curverd_bg) );
//
//                } else {
//                    holder.changeBg.setBackground(ContextCompat.getDrawable(_activity, R.drawable.white_border_curverd_bg));
//                }
                holder.txtTitle.setBackground(ContextCompat.getDrawable(_activity,R.drawable.selected_general_tab_shape));
            }else {
                holder.txtTitle.setBackgroundColor(ContextCompat.getColor(_activity,R.color.secondary_color));
//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    holder.changeBg.setBackgroundDrawable(null );
//                } else {
//                    holder.changeBg.setBackground(null);
//                }
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
                    sendMessageThroughHandler(124, obj);
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

    public List<FilterModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
      private LinearLayout lnrMain;
private TextView txtTitle;
private RadioButton radioButton;
private MaterialCardView changeBg;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);



        }
    }


}
