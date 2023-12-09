package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.List;


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private List<ProductModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public CartListAdapter(SuperActivity activity, List<ProductModel>  groupList, Handler handler) {
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
                .inflate(R.layout.layout_cart_list, parent, false);
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
            holder.txtQty.setText(item.qty+"");
            holder.txtPrice.setText(item.currency+" "+item.price);
            holder.cardViewGroupBlock.setTag(item);
            holder.imgIncriment.setTag(item);
            holder.imgDecriment.setTag(item);
            holder.txtRemoveCart.setTag(item);
            holder.txtRemoveCart.setOnClickListener(_click);
            holder.imgIncriment.setOnClickListener(_click);
            holder.imgDecriment.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.img_increment: {
                    sendMessageThroughHandler(1, obj);
                    break;
                }case R.id.img_decriment: {
                    sendMessageThroughHandler(2, obj);
                    break;
                }case R.id.txt_remove_cart: {
                    sendMessageThroughHandler(3, obj);
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
        private ImageView cardViewGroupBlock,imgIncriment,imgDecriment;
private TextView txtTitle,txtPrice,txtQty,txtRemoveCart;


        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.imageView);
            txtTitle = view.findViewById(R.id.txt_title);
            txtPrice = view.findViewById(R.id.txt_price);
            txtQty = view.findViewById(R.id.txt_qty);
            imgIncriment = view.findViewById(R.id.img_increment);
            imgDecriment = view.findViewById(R.id.img_decriment);
            txtRemoveCart = view.findViewById(R.id.txt_remove_cart);


        }
    }


}
