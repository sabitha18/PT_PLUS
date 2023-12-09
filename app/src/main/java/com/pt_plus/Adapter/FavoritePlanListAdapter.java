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
import com.pt_plus.Model.PlanModel;
import com.pt_plus.R;

import java.util.List;


public class FavoritePlanListAdapter extends RecyclerView.Adapter<FavoritePlanListAdapter.ViewHolder> {
    private List<PlanModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FavoritePlanListAdapter(SuperActivity activity, List<PlanModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<PlanModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_favorate_plan_list, parent, false);
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
            PlanModel item = _groupList.get(position);

            Glide.with(_activity)
                    .load(item.thumbnail_img).apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
            holder.txtTitle.setText(item.title);
            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
            holder.txt_remove.setTag(item);
            holder.txt_remove.setOnClickListener(_click);   holder.lnr_book_now.setTag(item);
            holder.lnr_book_now.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.txt_remove: {
                    sendMessageThroughHandler(7, obj);
                    break;
                }
                case R.id.lnr_main: {
                    sendMessageThroughHandler(8, obj);
                    break;
                } case R.id.lnr_book_now: {
                    sendMessageThroughHandler(9, obj);
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

    public List<PlanModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain, lnr_book_now;
        private TextView txtTitle, txt_remove;
        private ImageView imageView;

        private ViewHolder(View view) {
            super(view);


            imageView = view.findViewById(R.id.imageView);
            txtTitle = view.findViewById(R.id.txt_name);
            lnrMain = view.findViewById(R.id.lnr_main);
            lnr_book_now = view.findViewById(R.id.lnr_book_now);
            txt_remove = view.findViewById(R.id.txt_remove);

        }
    }


}
