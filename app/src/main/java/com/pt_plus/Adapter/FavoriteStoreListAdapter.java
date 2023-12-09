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
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.List;


public class FavoriteStoreListAdapter extends RecyclerView.Adapter<FavoriteStoreListAdapter.ViewHolder> {
    private List<ProductModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public FavoriteStoreListAdapter(SuperActivity activity, List<ProductModel>  groupList, Handler handler) {
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
                .inflate(R.layout.layout_favorate_store_list, parent, false);
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
            Glide.with(_activity)
                    .load(item.thumbnail_img).apply(RequestOptions.circleCropTransform())
                    .into(holder.imgProduct);
holder.txt_name.setText(item.name);
holder.txtPrice.setText(item.price);
            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
            holder.lnrAddTocart.setTag(item);
            holder.lnrAddTocart.setOnClickListener(_click);
            holder.txtRemove.setTag(item);
            holder.txtRemove.setOnClickListener(_click);
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
                    sendMessageThroughHandler(1, obj);
                    break;
                } case R.id.lnr_add_tocart: {
                    sendMessageThroughHandler(2, obj);
                    break;
                } case R.id.txt_remove: {
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
      private LinearLayout lnrMain,lnrAddTocart;
      private ImageView imgProduct;
      private TextView txt_name,txtPrice,txtRemove;
private TextView txtTitle;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);
            imgProduct = view.findViewById(R.id.imageView);
            txt_name = view.findViewById(R.id.txt_name);
            txtPrice = view.findViewById(R.id.txt_price);
            lnrAddTocart = view.findViewById(R.id.lnr_add_tocart);
            txtRemove = view.findViewById(R.id.txt_remove);

        }
    }


}
