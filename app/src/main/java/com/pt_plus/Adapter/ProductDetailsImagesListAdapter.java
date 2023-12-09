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
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.R;

import java.util.List;


public class ProductDetailsImagesListAdapter extends RecyclerView.Adapter<ProductDetailsImagesListAdapter.ViewHolder> {
    private List<HomeSliderModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;
private int currentPostion;

    public ProductDetailsImagesListAdapter(SuperActivity activity, List<HomeSliderModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }
public void currentPostion(int pos){
        this.currentPostion =pos;
}
    public void updateData(List<HomeSliderModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_details_image_list, parent, false);
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
            HomeSliderModel item = _groupList.get(position);

//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);
            Glide.with(_activity).load(item.imgUrl).into(holder.cardViewGroupBlock);
//            holder.txtTitle.setText(item.title);
            holder.cardViewGroupBlock.setTag(item);
            if(currentPostion == position){
                holder.lnrMain.setBackground(_activity.getDrawable(R.drawable.all_side_grey_border));
            }else {
                holder.lnrMain.setBackground(null);
            }
            holder.lnrMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageThroughHandler(1, position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                /*case R.id.card_view_category: {
                    sendMessageThroughHandler(4, obj);
                    break;
                }*/
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

    public List<HomeSliderModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
        private TextView txtTitle;
private LinearLayout lnrMain;
        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.imageView);
            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);

        }
    }


}
