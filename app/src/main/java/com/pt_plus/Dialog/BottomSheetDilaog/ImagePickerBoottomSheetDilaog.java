package com.pt_plus.Dialog.BottomSheetDilaog;


import android.content.Intent;
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
import com.pt_plus.Activitys.ActivitySpashScreen;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.R;
import com.pt_plus.Utils.Callback.PopUpWithValueCallBack;
import com.pt_plus.Utils.UiUtils;

public class ImagePickerBoottomSheetDilaog extends BottomSheetDialogFragment {
    private SuperActivity context;
    PopUpWithValueCallBack _popUpWithValueCallBack;

    public ImagePickerBoottomSheetDilaog(SuperActivity fragmentActivity, PopUpWithValueCallBack popUpWithValueCallBack) {
        this.context = fragmentActivity;
        _popUpWithValueCallBack = popUpWithValueCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_image_picker, container, false);
        View backgroundView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap blurredBitmap = UiUtils.getInstance().createBlurredBitmap(backgroundView, getContext());

        Drawable drawable = new BitmapDrawable(getResources(), blurredBitmap);
        getDialog().getWindow().setBackgroundDrawable(drawable);

        initView(view);
        return view;
    }


    private CardView cardViewSubmit;
    private RadioButton radioCamer, radioGallery;

    private void initView(View view) {


        cardViewSubmit = view.findViewById(R.id.carview_submit);
        cardViewSubmit.setOnClickListener(_click);
        radioCamer = view.findViewById(R.id.radio_camera);
        radioCamer.setOnClickListener(_click);
        radioGallery = view.findViewById(R.id.radio_gallery);
        radioGallery.setOnClickListener(_click);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up any views or listeners here
    }

    int clickedValue = 0;
    private final OnClickListener _click = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.carview_submit: {

                    _popUpWithValueCallBack.onOK(clickedValue);
                    dismiss();
                    break;
                }
                case R.id.radio_camera: {
                    clickedValue = 0;


                    break;
                }
                case R.id.radio_gallery: {

                    clickedValue = 1;

                    break;
                }


            }
        }
    };


}
