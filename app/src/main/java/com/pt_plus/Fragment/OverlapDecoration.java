package com.pt_plus.Fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class OverlapDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
//        if (itemPosition == 0) {
//            return;
//        }
//        outRect.set(0, 0, 0, -150);//<-- bottom
//        outRect.set(0, 20, -35, 0);//<-- bottom
        outRect.set(0, 20, -35, 0);//<-- bottom
    }

}
