
package com.pt_plus.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.FilterModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.ArrayList;
import java.util.List;

public class FilterGridAdapter extends ArrayAdapter<ProductModel> {

    List<FilterModel> items_list = new ArrayList<>();
    int custom_layout_id;
    private SuperActivity _activity;
    private Context _context;
    private Handler _handler;

    public FilterGridAdapter(Context context, Handler handler) {
        super(context, 0);

//        super(context);
//        custom_layout_id = resource;
        this._context = context;
        this._handler = handler;
    }

    public void updateData(List<FilterModel> list) {
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
        FilterModel item = items_list.get(position);


        RadioButton radioButton = v.findViewById(R.id.radio_button);
        radioButton.setText(item.title);
        if (item.isSelected) {
            radioButton.setChecked(true);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_24, 0);
        } else {
            radioButton.setChecked(false);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }
        radioButton.setTag(item);
        radioButton.setOnClickListener(_Click);


        return v;
    }

    public List<FilterModel> getAllItems() {
        return this.items_list;
    }

    private final View.OnClickListener _Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {

                case R.id.radio_button: {
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