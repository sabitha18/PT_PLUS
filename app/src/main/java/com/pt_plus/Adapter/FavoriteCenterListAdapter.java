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
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.CenterModel;
import com.pt_plus.R;

import java.util.List;


public class FavoriteCenterListAdapter extends RecyclerView.Adapter<FavoriteCenterListAdapter.ViewHolder> {
    private List<CenterModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FavoriteCenterListAdapter(SuperActivity activity, List<CenterModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<CenterModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_favorate_center_list, parent, false);
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

            Glide.with(_activity).load(item.thumbnail_img).into(holder.imageView);
            holder.txt_name.setText(item.name);
            holder.txt_spacl.setText(item.about);
            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);     holder.txt_remove.setTag(item);
            holder.txt_remove.setOnClickListener(_click);
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
                    sendMessageThroughHandler(10, obj);
                    break;
                }    case R.id.lnr_main: {
                    sendMessageThroughHandler(11, obj);
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

    public List<CenterModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
      private LinearLayout lnrMain;
private TextView txtTitle,txt_name,txt_spacl,txt_remove;
private ImageView imageView;
        private ViewHolder(View view) {
            super(view);


            txt_remove = view.findViewById(R.id.txt_remove);
            txt_name = view.findViewById(R.id.txt_name);
            imageView = view.findViewById(R.id.imageView);
            txt_spacl = view.findViewById(R.id.txt_spacl);
            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);

        }
    }


}
