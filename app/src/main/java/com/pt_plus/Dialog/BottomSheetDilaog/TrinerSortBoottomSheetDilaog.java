package com.pt_plus.Dialog.BottomSheetDilaog;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.R;
import com.pt_plus.Utils.Callback.PopUpCallBack;
import com.pt_plus.Utils.UiUtils;
import com.pt_plus.cons.LocalCache;

public class TrinerSortBoottomSheetDilaog extends BottomSheetDialogFragment {
    private SuperActivity context;
    PopUpCallBack popUpCallBack;

    public TrinerSortBoottomSheetDilaog(SuperActivity fragmentActivity, PopUpCallBack callBack) {
        this.context = fragmentActivity;
        this.popUpCallBack = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_triner_sort, container, false);
        View backgroundView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap blurredBitmap = UiUtils.getInstance().createBlurredBitmap(backgroundView, getContext());

        Drawable drawable = new BitmapDrawable(getResources(), blurredBitmap);
        getDialog().getWindow().setBackgroundDrawable(drawable);

        initView(view);
        return view;
    }


    private CardView cardViewSubmit;
    private RadioButton radioFeaturd, radioNewet, radioLowToHigh, radioHightToLow;

    private void initView(View view) {


        cardViewSubmit = view.findViewById(R.id.carview_submit);
        cardViewSubmit.setOnClickListener(_click);



        radioLowToHigh = view.findViewById(R.id.radio_low_to_hign);
        radioLowToHigh.setOnClickListener(_click);


        radioHightToLow = view.findViewById(R.id.radio_high_to_low);
        radioHightToLow.setOnClickListener(_click);



        if(   LocalCache.getCache().getTrainerFilterModel().rating  != null &&  LocalCache.getCache().getTrainerFilterModel().rating .equalsIgnoreCase("asc") ){
            radioLowToHigh.setChecked(true);
        }
        if(   LocalCache.getCache().getTrainerFilterModel().rating  != null &&  LocalCache.getCache().getTrainerFilterModel().rating .equalsIgnoreCase("desc") ){
            radioHightToLow.setChecked(true);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up any views or listeners here
    }

    private final OnClickListener _click = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.carview_submit: {
popUpCallBack.onOK();
dismiss();
                    break;
                }

                case R.id.radio_low_to_hign: {

                    LocalCache.getCache().getTrainerFilterModel().rating  = "asc";

                    break;
                }
                case R.id.radio_high_to_low: {

                    LocalCache.getCache().getTrainerFilterModel().rating = "desc";

                    break;
                }


            }
        }
    };


}
