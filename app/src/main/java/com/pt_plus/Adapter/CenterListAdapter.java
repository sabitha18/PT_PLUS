package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.R;

import java.util.List;


public class CenterListAdapter extends RecyclerView.Adapter<CenterListAdapter.ViewHolder> {
    private List<CenterModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public CenterListAdapter(SuperActivity activity, List<CenterModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<CenterModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_center_list, parent, false);
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
            CenterModel item = _groupList.get(position);
//            holder.txtGroupName.setText(AppUtils.getStringValueFromJson(item, "group_title"));
//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);

            Glide.with(_activity)

                    .load(item.thumbnail_img)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.cardViewGroupBlock);
            holder.txtName.setText(item.name);
            if (item.name == null) {

                holder.txtSpc.setVisibility(View.GONE);
            }else {

                holder.txtSpc.setVisibility(View.VISIBLE);
            }
            holder.txtSpc.setVisibility(View.GONE);
            holder.txtSpc.setText(item.name);
            holder.cardViewGroupBlock.setTag(item);
            holder.cardViewMain.setTag(item);
            holder.cardViewMain.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.card_group_block: {
                    sendMessageThroughHandler(12, obj);
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

    public List<CenterModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
        private TextView txtName, txtSpc;
        private CardView cardViewMain;

        private ViewHolder(View view) {
            super(view);

            cardViewMain = view.findViewById(R.id.card_group_block);
            cardViewGroupBlock = view.findViewById(R.id.imageView);
            txtName = view.findViewById(R.id.txt_name);
            txtSpc = view.findViewById(R.id.txt_spacl);

        }
    }


}
