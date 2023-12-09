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
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.Utils.UiUtils;

public class CountryBoottomSheetDilaog extends BottomSheetDialogFragment {
    private SuperActivity context;

    public CountryBoottomSheetDilaog(SuperActivity fragmentActivity) {
        this.context = fragmentActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_country_change, container, false);
        View backgroundView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap blurredBitmap = UiUtils.getInstance().createBlurredBitmap(backgroundView, getContext());

        Drawable drawable = new BitmapDrawable(getResources(), blurredBitmap);
        getDialog().getWindow().setBackgroundDrawable(drawable);

        initView(view);
        return view;
    }


private CardView cardViewSubmit;
    private RadioButton radioEn,radioAr;
    private void initView(View view) {


        cardViewSubmit = view.findViewById(R.id.carview_submit);
        cardViewSubmit.setOnClickListener(_click);



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



                    Intent intent = new Intent(context, ActivitySpashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dismiss();
                    context.finish();

                    break;
                }



            }
        }
    };




}
