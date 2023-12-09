package com.pt_plus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pt_plus.Model.HomeSliderModel;
import com.pt_plus.R;

import java.util.List;


public class PlanDetailsSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.ic_baseline_arrow_forward_ios_24,R.drawable.ic_launcher_background,R.drawable.logo};
    private List<HomeSliderModel> homeSliderModel;

    public PlanDetailsSliderAdapter(Context context, List<HomeSliderModel> homeSliderModel) {
        this.context = context;
        this.homeSliderModel = homeSliderModel;
    }

    public void updateData(List<HomeSliderModel>  categoryList) {
        this.homeSliderModel = categoryList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
//        return images.length;
        return homeSliderModel != null ? homeSliderModel.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.plan_details_slider_layout, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(homeSliderModel.get(position).title);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(context).load(homeSliderModel.get(position).imgUrl).into(imageView);
//        imageView.setImageResource(homeSliderModel.get(0).imgUrl);

        if (homeSliderModel.get(position).title == null) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
        }

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
}