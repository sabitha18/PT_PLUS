
package com.pt_plus.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.CategoryModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryGridAdapter extends ArrayAdapter<ProductModel> {

    List<CategoryModel> items_list = new ArrayList<>();
    int custom_layout_id;
    private SuperActivity _activity;
    private Context _context;
    private Handler _handler;

    public SubCategoryGridAdapter(Context context, Handler handler) {
        super(context, 0);

//        super(context);
//        custom_layout_id = resource;
        this._context = context;
        this._handler = handler;
    }

    public void updateData(List<CategoryModel> list) {
        this.items_list = list;
        notifyDataSetChanged();
    }

    public void setLayout(int resource) {
        custom_layout_id = resource;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(custom_layout_id, null);
        }
        CategoryModel item = items_list.get(position);


        ImageView imgProduct = v.findViewById(R.id.imageView);
        TextView txtTitle = v.findViewById(R.id.txt_title);
//        imgProduct.setBackgroundResource(item.drwableId);

        txtTitle.setText(item.name);
        Glide.with(_context)
                .load(item.thumbnail_img)
                .into(imgProduct);
//

        CardView cardView = v.findViewById(R.id.card_group_block);
        cardView.setTag(item);
        cardView.setOnClickListener(_Click);


        return v;
    }

    private final View.OnClickListener _Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {

                case R.id.card_group_block: {
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

}