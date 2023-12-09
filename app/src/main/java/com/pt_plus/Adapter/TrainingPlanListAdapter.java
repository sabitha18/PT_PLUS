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
import com.pt_plus.Model.PlanModel;
import com.pt_plus.R;

import java.util.List;


public class TrainingPlanListAdapter extends RecyclerView.Adapter<TrainingPlanListAdapter.ViewHolder> {
    private List<PlanModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;


    public TrainingPlanListAdapter(SuperActivity activity, List<PlanModel> groupList, Handler handler) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<PlanModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_training_plan_list, parent, false);
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
            PlanModel item = _groupList.get(position);

//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);
            Glide.with(_activity)
                    .load(item.thumbnail_img)
                    .into( holder.cardViewGroupBlock);
            if(item.title != null && !item.title.isEmpty() && !item.title.equalsIgnoreCase("null")){
                holder.txtTitle.setText(item.title);
            }
            if(item.sub_title != null && !item.sub_title.isEmpty() && !item.sub_title.equalsIgnoreCase("null")){
                holder.txtDiscription.setText(item.sub_title);
            }
            if(item.num_of_trainers != null && !item.num_of_trainers.isEmpty() && !item.num_of_trainers.equalsIgnoreCase("null")){
                holder.txtTrainerCount.setText(item.num_of_trainers+" "+_activity.getString(R.string.trainers));
            }


            holder.cardViewGroupBlock.setTag(item);
            holder.cardViewGroupBlock.setOnClickListener(_click);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.img_bg: {

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

    public List<PlanModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
        private TextView txtTitle,txtDiscription,txtTrainerCount;

        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.img_bg);
            txtTitle = view.findViewById(R.id.txt_title);
            txtDiscription = view.findViewById(R.id.txt_discription);
            txtTrainerCount = view.findViewById(R.id.txt_trainer_count);

        }
    }


}
