package com.pt_plus.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;

import java.util.List;



public class StoreSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.ic_baseline_arrow_forward_ios_24,R.drawable.ic_launcher_background,R.drawable.logo};
    private List<HomeSliderModel> homeSliderModel;
    private Handler _handler;

    public StoreSliderAdapter(Context context, List<HomeSliderModel> homeSliderModel, Handler handler) {
        this.context = context;
        this.homeSliderModel = homeSliderModel;
      this._handler =  handler;
    }

    @Override
    public int getCount() {
//        return images.length;
        return homeSliderModel != null ?homeSliderModel.size() :0;
    }
    public void updateData(List<HomeSliderModel>  categoryList) {
        this.homeSliderModel = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.store_slider_layout, null);
       TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
       txtTitle.setText(homeSliderModel.get(position).title);
       TextView txtDEs = (TextView) view.findViewById(R.id.txt_des);
        txtDEs.setText(homeSliderModel.get(position).description);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Button btnShop = (Button) view.findViewById(R.id.btn_shop);
        btnShop.setText(homeSliderModel.get(position).btnText);
        btnShop.setTag(homeSliderModel.get(position));

        Glide.with(context).load(homeSliderModel.get(position).imgUrl).into(imageView);

        btnShop.setOnClickListener(_click);

//        imageView.setImageResource(homeSliderModel.get(0).imgUrl);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            switch (v.getId()) {
                case R.id.btn_shop: {
                    if(obj != null){
                        sendMessageThroughHandler(3, obj);
                    }

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