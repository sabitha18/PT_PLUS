package com.pt_plus.Adapter;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<ProductModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public ProductListAdapter(SuperActivity activity, List<ProductModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<ProductModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_list, parent, false);
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
            ProductModel item = _groupList.get(position);
//            holder.txtGroupName.setText(AppUtils.getStringValueFromJson(item, "group_title"));
//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);

            Glide.with(_activity)
                    .load(item.thumbnail_img)
                    .into(holder.cardViewGroupBlock);
            holder.txtTitle.setText(item.name);
            holder.txtPrice.setText(item.currency+" "+item.price);
            holder.cardViewGroupBlock.setTag(item);
            holder.cardViewGroupBlock.setOnClickListener(_click);
            holder.imgAddToWishList.setTag(item);
            holder.imgAddToWishList.setOnClickListener(_click);
            holder.txtBuyNow.setTag(item);
            holder.txtBuyNow.setOnClickListener(_click);

            if(item.wishlisted){
                int tintColor = ContextCompat.getColor(_activity, R.color.red); // Replace with your desired tint color resource
                holder.imgAddToWishList.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

            }else {
                int tintColor = ContextCompat.getColor(_activity, R.color.white); // Replace with your desired tint color resource
                holder.imgAddToWishList.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
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
                case R.id.imageView: {
                    sendMessageThroughHandler(2, obj);
                    break;
                }case R.id.add_to_wish_list: {
                    sendMessageThroughHandler(4, obj);
                    break;
                }case R.id.txt_buy_now: {
                    sendMessageThroughHandler(5, obj);
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

    public List<ProductModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock,imgAddToWishList;
private TextView txtTitle,txtPrice,txtBuyNow;

        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.imageView);
            txtTitle = view.findViewById(R.id.txt_title);
            txtPrice = view.findViewById(R.id.txt_price);
            imgAddToWishList = view.findViewById(R.id.add_to_wish_list);
            txtBuyNow = view.findViewById(R.id.txt_buy_now);


        }
    }


}
