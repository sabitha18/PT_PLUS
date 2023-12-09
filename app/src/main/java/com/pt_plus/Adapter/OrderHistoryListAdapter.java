package com.pt_plus.Adapter;

import android.annotation.SuppressLint;
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
import com.pt_plus.Model.OrderModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;

import java.util.List;


public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder> {
    private List<OrderModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public OrderHistoryListAdapter(SuperActivity activity, List<OrderModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<OrderModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_order_history_list, parent, false);
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
            OrderModel item = _groupList.get(position);
//
            Glide.with(_activity)
                    .load(item.productModelList.get(0).thumbnail_img)
                    .into(holder.imgProduct);
            holder.txtProductName.setText(item.productModelList.get(0).name);
            holder.txtQty.setText(_activity.getString(R.string.qty)+item.productModelList.get(0).qty);
            holder.txtAmount.setText(item.currency +" "+item.amount);

            holder.txtDate.setText(_activity.getString(R.string.order_on)+ AppDateUtils.getInstance().formateDate(item.ordered_on));
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
                    sendMessageThroughHandler(1, obj);
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

    public List<OrderModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
      private LinearLayout lnrMain;
private TextView txtTitle,txtProductName,txtQty,txtExtraInformation,txtAmount,txtDate;
private ImageView imgProduct;

        private ViewHolder(View view) {
            super(view);


            imgProduct = view.findViewById(R.id.img_product);
            txtProductName = view.findViewById(R.id.txt_product_name);
            txtAmount = view.findViewById(R.id.txt_amount);
            txtExtraInformation = view.findViewById(R.id.txt_extra_information);
            txtDate = view.findViewById(R.id.txt_date);
            txtQty = view.findViewById(R.id.txt_qty);
            txtTitle = view.findViewById(R.id.txt_title);
            lnrMain = view.findViewById(R.id.lnr_main);

        }
    }


}
