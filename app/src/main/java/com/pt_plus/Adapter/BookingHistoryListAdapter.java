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
import com.pt_plus.Model.BookingModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;

import java.util.List;


public class BookingHistoryListAdapter extends RecyclerView.Adapter<BookingHistoryListAdapter.ViewHolder> {
    private List<BookingModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public BookingHistoryListAdapter(SuperActivity activity, List<BookingModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<BookingModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_booking_history_list, parent, false);
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

            BookingModel item = _groupList.get(position);

            Glide.with(_activity).load(item.trainersModel.profile_picture).placeholder(R.drawable.no_image).into(holder.imgTrainer);
            Glide.with(_activity).load(item.planModel.thumbnail_img).placeholder(R.drawable.no_image).into(holder.imgPlan);
            holder.txtTitle.setText(item.trainersModel.name);
            holder.txtSpl.setText(item.trainersModel.speclization);
            holder.txtExp.setText(item.trainersModel.experiance);
            holder.txtPlanTItile.setText(item.planModel.title);
            holder.txtPlanPrice.setText(item.planModel.price + " " + item.planModel.currency);
            holder.txtPriceTotal.setText(item.planModel.price + " " + item.planModel.currency);
            holder.txtRating.setText(item.trainersModel.rating + "");
            holder.txtDate.setText(_activity.getString(R.string.book_on) + AppDateUtils.getInstance().formateDate(item.booked_on));
            holder.lnrMain.setTag(item);
            holder.lnrMain.setOnClickListener(_click);
            holder.lnrCancelBooking.setTag(item);
            holder.lnrCancelBooking.setOnClickListener(_click);
            if(item.bookingStatus.equalsIgnoreCase("cancelled")){
                holder.lnrCancelBooking.setVisibility(View.GONE);
            }else {
                holder.lnrCancelBooking.setVisibility(View.VISIBLE);
            }
            if(!_activity.isEnglish()){
                holder.txtExp.setTextDirection(View.TEXT_DIRECTION_RTL);
                holder.txtPlanPrice.setTextDirection(View.TEXT_DIRECTION_RTL);
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
                case R.id.lnr_main: {
                    sendMessageThroughHandler(1, obj);
                    break;
                } case R.id.lnr_cancel_booking: {
                    sendMessageThroughHandler(2, obj);
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

    public List<BookingModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnrMain,lnrCancelBooking;
        private ImageView imgTrainer, imgPlan;
        private TextView txtTitle, txtSpl, txtExp, txtRating, txtPlanTItile, txtPlanPrice, txtDate, txtPriceTotal;

        private ViewHolder(View view) {
            super(view);


            txtTitle = view.findViewById(R.id.txtTrainerName);
            lnrMain = view.findViewById(R.id.lnr_main);
            imgTrainer = view.findViewById(R.id.imageView);
            txtSpl = view.findViewById(R.id.txt_spacl);
            txtExp = view.findViewById(R.id.txt_exsperiance);
            txtRating = view.findViewById(R.id.txt_rating);
            imgPlan = view.findViewById(R.id.img_plan);
            txtPlanTItile = view.findViewById(R.id.txt_paln_title);
            txtPlanPrice = view.findViewById(R.id.txt_plan_price);
            txtDate = view.findViewById(R.id.txt_date);
            txtPriceTotal = view.findViewById(R.id.txt_price_total);
            lnrCancelBooking = view.findViewById(R.id.lnr_cancel_booking);

        }
    }


}
