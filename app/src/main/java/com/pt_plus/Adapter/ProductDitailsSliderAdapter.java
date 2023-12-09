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


public class ProductDitailsSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.ic_baseline_arrow_forward_ios_24,R.drawable.ic_launcher_background,R.drawable.logo};
    private List<HomeSliderModel> homeSliderModel;

    public ProductDitailsSliderAdapter(Context context, List<HomeSliderModel> homeSliderModel) {
        this.context = context;
        this.homeSliderModel = homeSliderModel;
    }

    @Override
    public int getCount() {
//        return images.length;
        return homeSliderModel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.product_details_slider_layout, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(context).load(homeSliderModel.get(position).imgUrl).into(imageView);
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
}