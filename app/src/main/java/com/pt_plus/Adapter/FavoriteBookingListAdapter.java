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
import com.bumptech.glide.request.RequestOptions;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.TrainersModel;
import com.pt_plus.R;

import java.util.List;


public class FavoriteBookingListAdapter extends RecyclerView.Adapter<FavoriteBookingListAdapter.ViewHolder> {
    private List<TrainersModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FavoriteBookingListAdapter(SuperActivity activity, List<TrainersModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<TrainersModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_favorate_booking_list, parent, false);
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
            Glide.with(_activity)
                    .load(item.profile_picture).apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
            holder.txtTitle.setText(item.name);
            holder.txt_spacl.setText(item.category);
            holder.txt_exp.setText(item.experiance);
            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
            holder.txt_remove.setTag(item);
            holder.txt_remove.setOnClickListener(_click);holder.lnr_book.setTag(item);
            holder.lnr_book.setOnClickListener(_click);
            holder.txtRating.setVisibility(View.GONE);
            if(item.rating != null && !item.rating.isEmpty()){
                holder.txtRating.setText(item.rating);
                holder.txtRating.setVisibility(View.VISIBLE);
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
                    sendMessageThroughHandler(4, obj);
                    break;
                }
                case R.id.txt_remove: {
                    sendMessageThroughHandler(5, obj);
                    break;
                } case R.id.lnr_book: {
                    sendMessageThroughHandler(6, obj);
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

    public List<TrainersModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain, lnr_book;
        private TextView txtTitle, txt_spacl, txt_exp, txt_remove,txtRating;
        private ImageView imageView;

        private ViewHolder(View view) {
            super(view);


            lnr_book = view.findViewById(R.id.lnr_book);
            txt_remove = view.findViewById(R.id.txt_remove);
            txt_exp = view.findViewById(R.id.txt_exp);
            txt_spacl = view.findViewById(R.id.txt_spacl);
            imageView = view.findViewById(R.id.imageView);
            txtTitle = view.findViewById(R.id.txt_name);
            lnrMain = view.findViewById(R.id.lnr_main);
            txtRating = view.findViewById(R.id.txt_rating);

        }
    }


}
