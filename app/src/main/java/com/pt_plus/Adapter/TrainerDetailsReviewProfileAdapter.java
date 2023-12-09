package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ReviewModel;
import com.pt_plus.R;

import java.util.List;


public class TrainerDetailsReviewProfileAdapter extends RecyclerView.Adapter<TrainerDetailsReviewProfileAdapter.ViewHolder> {
    private List<ReviewModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public TrainerDetailsReviewProfileAdapter(SuperActivity activity, List<ReviewModel>  groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<ReviewModel>  categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trainer_details_profile_image_layout, parent, false);
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
            ReviewModel item = _groupList.get(position);
//            holder.txtGroupName.setText(AppUtils.getStringValueFromJson(item, "group_title"));
if(item.totalCount > 0){
    holder.lnrMian.setVisibility(View.GONE);
    holder.lnrCount.setVisibility(View.VISIBLE);
    holder.txtCOunt.setText(item.totalCount+"");
}else {
    holder.lnrMian.setVisibility(View.VISIBLE);
    holder.lnrCount.setVisibility(View.GONE);
    Glide.with(_activity).load(item.user_dp).placeholder(R.drawable.no_image_triner).into( holder.cardViewGroupBlock);
}

//            setMargins(holder.cardViewGroupBlock, -5, 0, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
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

    public List<ReviewModel>  getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
private TextView txtCOunt,txtSpc;
private LinearLayout lnrCount;

private FrameLayout  lnrMian;

        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.imageView);
            lnrMian = view.findViewById(R.id.lnr_main);
            txtCOunt = view.findViewById(R.id.txt_count);
            lnrCount = view.findViewById(R.id.lnr_count);


        }
    }


}
