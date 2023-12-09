package com.pt_plus.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.CommonCardCategoryModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.cons.LocalCache;

import java.util.List;


public class FilterCategoryListAdapter extends RecyclerView.Adapter<FilterCategoryListAdapter.ViewHolder> {
    private List<CommonCardCategoryModel> _groupList;
    private SuperActivity _activity;
    private LayoutInflater inflater;
    private Handler _handler;
    private ViewHolder _holder;
private boolean isProductFilter;

    public FilterCategoryListAdapter(SuperActivity activity, List<CommonCardCategoryModel> groupList, Handler handler,boolean isProduct) {
        this._activity = activity;
        this._handler = handler;
        this._groupList = groupList;
        this.isProductFilter = isProduct;
//        inflater = activity.getLayoutInflater();
    }

    public void updateData(List<CommonCardCategoryModel> categoryList) {
        this._groupList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter_category_list, parent, false);
        return new ViewHolder(itemView);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private String tempStr = null;
    FilterInnerCategoryListAdapter filterInnerCategoryListAdapter;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            CommonCardCategoryModel item = _groupList.get(position);

//            holder.cardViewGroupBlock.setBackgroundResource(item.drwableId);

            holder.txtTitle.setText(item.name);


            filterInnerCategoryListAdapter = new FilterInnerCategoryListAdapter(_activity, null, new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    if (message.what == 1) {
                        CommonCardCategoryModel filterModel = null;
                        try {
                            filterModel = (CommonCardCategoryModel) message.obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (filterModel != null) {
                            AppLogger.log("clicked item   " + filterModel.name);
                            List<CommonCardCategoryModel> filterModelList = item.subCat;

                            if (filterModelList != null && filterModelList.size() > 0) {

                                    if (filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected) {

if(isProductFilter){
    LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.remove(filterModel);
}else {
    LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.remove(filterModel);
}


                                        filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = false;
                                    } else {

                                        if(isProductFilter){

                                            LocalCache.getCache().getProductFilterModel().commonCardCategoryModel.add(filterModel);
                                        }else {
                                            LocalCache.getCache().getTrainerFilterModel().commonCardCategoryModel.add(filterModel);

                                        }

                                        filterModelList.get(filterModelList.lastIndexOf(filterModel)).isSelected = true;
                                    }


                            }

                          notifyDataSetChanged();

//                            LocalCache.getCache().getTrainerFilterModel().categories.add(item.id);
                        }
                    }
                    return true;
                }
            }));

            holder.recyclerViewCenterList.setLayoutManager(new LinearLayoutManager(_activity, LinearLayoutManager.VERTICAL, false));
            holder.recyclerViewCenterList.setAdapter(filterInnerCategoryListAdapter);
            filterInnerCategoryListAdapter.updateData(item.subCat);
            if (item.subCat != null && item.subCat.size() > 0) {
                holder.cardView.setOnClickListener(view -> {
                    if (holder.hiddenView.getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                        holder.hiddenView.setVisibility(View.GONE);
                    } else {
                        TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                        holder.hiddenView.setVisibility(View.VISIBLE);
                    }
                });
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
                case R.id.img_bg: {
                    AppLogger.log("cccllldfdsfsadfdfad");
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

    public List<CommonCardCategoryModel> getAllItems() {
        return _groupList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardViewGroupBlock;
        private TextView txtTitle;
        LinearLayout hiddenView;
        CardView cardView;
        RecyclerView recyclerViewCenterList;

        private ViewHolder(View view) {
            super(view);

            cardViewGroupBlock = view.findViewById(R.id.img_bg);
            txtTitle = view.findViewById(R.id.txt_title);
            hiddenView = view.findViewById(R.id.hidden_view);
            cardView = view.findViewById(R.id.base_cardview);
            recyclerViewCenterList = view.findViewById(R.id.rcy_cate);
        }
    }


}
