package com.pt_plus.Fragment.Explore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.Model.ExploreModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
//    private static final int NUM_TABS = 4; // Number of tabs
    private static final int NUM_TABS = 2; // Number of tabs
    private ExploreModel exploreModel;
    private SuperActivity _activity;

    public CustomPagerAdapter(FragmentManager fragmentManager, SuperActivity fragmentActivity) {
        super(fragmentManager);
        this._activity = fragmentActivity;
    }

    public void updateData(ExploreModel model) {
        this.exploreModel = model;
        notifyDataSetChanged();
        AppLogger.log("Updated data: " + model.trainersModelList.size());
    }

    @Override
    public Fragment getItem(int position) {

        if (exploreModel != null) {
            switch (position) {
                case 0:
                    return new FragmentExploreTop(exploreModel.topList);
                case 1:
                    return new FragmentExploreTrainer(exploreModel.trainersModelList);
//                case 2:
//                    return new FragmentExploreCenter(exploreModel.centerModelList);
//                case 3:
//                    return new FragmentExploreProducts(exploreModel.productModelList);
            }
        }

        // If data is not available, return a default fragment or null
        return null;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
//                return "Top";
            return  _activity.getString(R.string.Top);
            case 1:
//                return "Trainers";
                return  _activity.getString(R.string.Trainers);
//            case 2:
////                return "Centers";
//            return _activity.getString(R.string.Centers);
//            case 3:
////                return "Products";
//                return _activity.getString(R.string.products);
            default:
                return "Unknown";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Return POSITION_NONE to force recreation of the fragment when data is updated
        return POSITION_NONE;
    }
}