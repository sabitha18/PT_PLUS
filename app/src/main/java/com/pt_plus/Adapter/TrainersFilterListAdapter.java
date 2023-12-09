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
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;

import java.util.List;


public class TrainersFilterListAdapter extends RecyclerView.Adapter<TrainersFilterListAdapter.ViewHolder> {
    private List<TrainersModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public TrainersFilterListAdapter(SuperActivity activity, List<TrainersModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<TrainersModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter_trainer_list, parent, false);
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
            TrainersModel item = _groupList.get(position);
//            holder.txtGroupName.setText(AppUtils.getStringValueFromJson(item, "group_title"));
//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Adjust caching strategy if needed
                    .error(R.drawable.no_image_triner);
            Glide.with(_activity)
                    .load(item.profile_picture)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image_triner).apply(requestOptions)
                    .into(holder.cardViewGroupBlock);
            holder.txtName.setText(item.name);
            holder.txtSpc.setText(item.category);
            holder.txtDes.setText(item.experiance);
            holder.txtRating.setVisibility(View.GONE);
            if(item.rating != null && !item.rating.isEmpty()){
                holder.txtRating.setText(item.rating);
                holder.txtRating.setVisibility(View.VISIBLE);
            }

            holder.lnrMian.setTag(item);
            holder.lnrMian.setOnClickListener(_click);
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
                    sendMessageThroughHandler(2, obj);
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

    public List<TrainersModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
private TextView txtName,txtSpc,txtDes,txtRating;
private LinearLayout lnrMian;
        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.imageView);
            txtName = view.findViewById(R.id.txt_name);
            txtSpc = view.findViewById(R.id.txt_spacl);
            txtDes = view.findViewById(R.id.txt_des);
            txtRating = view.findViewById(R.id.txt_rating);
            lnrMian = view.findViewById(R.id.lnr_main);

        }
    }


}
