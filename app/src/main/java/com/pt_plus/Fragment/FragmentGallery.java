package com.pt_plus.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pt_plus.Adapter.GalleryGridAdapter;
import com.pt_plus.Dialog.GalleryDialogFragment;
import com.pt_plus.Model.GalleryModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.CenterService;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.cons.AppCons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentGallery extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private ImageView imgBack;
    private View appbar;
    private TextView txtappBarTitle;
    private RadioButton radioVideo,radioPhoto;
    private   GridView gridViewVideo,gridViewPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        initView(view);
        return view;
    }
    private GeneralService generalService;
    private CenterService centerService;
    private void  initView(View view){
        generalService = new GeneralService(getFragmentActivity(),_callBack);
        centerService = new CenterService(getFragmentActivity(),_callBack);

        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Long type = bundle.getLong("type");
                if (type == AppCons.GALLERY_LIST_TYPE_TARINER) {
                    generalService.getTRainerGallery(ServiceNames.REQUEST_GET_TRAINER_GALLERY, new JSONObject().put("trainer_id", bundle.getString("trainer_id")));
                } else if (type == AppCons.GALLERY_LIST_TYPE_CENTER) {
                    centerService.getCenterGallery(ServiceNames.REQUEST_GET_CENTER_GALLERY,new JSONObject().put("center_id",bundle.getString("center_id")));
                } else {
                    generalService.getGallery(ServiceNames.REQUEST_ID_GET_GALLERY);
                }
                getFragmentActivity().showProgress(true);
                // handle your code here.
            }else {
                generalService.getGallery(ServiceNames.REQUEST_ID_GET_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        getFragmentActivity().showProgress(true);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);

        radioVideo = view.findViewById(R.id.radio_video);
        radioVideo.setOnClickListener(_click);
        radioPhoto = view.findViewById(R.id.radio_photo);
        radioPhoto.setOnClickListener(_click);


        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.gallery_q);

        gridViewVideo  = view.findViewById(R.id.gridview_video);
        gridViewPhoto  = view.findViewById(R.id.gridview_photo);
        videoGridAdaper = new GalleryGridAdapter(getContext(), _handler);
        videoGridAdaper.setLayout(R.layout.layout_gallery_video_grid_list);
        gridViewVideo.setNumColumns(2);
        gridViewVideo.setAdapter(videoGridAdaper);


        gridViewPhoto  = view.findViewById(R.id.gridview_photo);
        imgaeGridAdapter= new GalleryGridAdapter(getContext(), _handler);
        imgaeGridAdapter.setLayout(R.layout.layout_gallery_photo_grid_list);
        gridViewPhoto.setNumColumns(2);
        gridViewPhoto.setAdapter(imgaeGridAdapter);



        showView(gridViewVideo);

    }
    GalleryGridAdapter videoGridAdaper,imgaeGridAdapter;
    private final CallBack _callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if(serviceId == ServiceNames.REQUEST_ID_GET_GALLERY || serviceId == ServiceNames.REQUEST_GET_TRAINER_GALLERY || serviceId == ServiceNames.REQUEST_GET_CENTER_GALLERY){
                    AppLogger.log("dfadsfdsdsf         "+jsonObject.getJSONArray("gallery_video"));
                    JSONArray gallery_video =jsonObject.has("gallery_video")?jsonObject.getJSONArray("gallery_video"):null;
                    JSONArray gallery_photo =jsonObject.has("gallery_photo")?jsonObject.getJSONArray("gallery_photo"):null;
                    videoGridAdaper.updateData(processGallery(gallery_video,true));
                    imgaeGridAdapter.updateData(processGalleryImage(gallery_photo,false));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            getFragmentActivity().showProgress(false);
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };
    private List<GalleryModel> processGallery(JSONArray jsonArray,boolean isVideo) {
        List<GalleryModel> galleryModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                GalleryModel galleryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    galleryModel = new GalleryModel();
                    galleryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    galleryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    galleryModel.video = AppUtils.getStringValueFromJson(jsonObject, "video");
                    galleryModel.type = AppUtils.getStringValueFromJson(jsonObject, "type");
                    galleryModel.isVideo = isVideo;



                    galleryModelList.add(galleryModel);

                }

            }
            return galleryModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return galleryModelList;
    }
    private List<GalleryModel> processGalleryImage(JSONArray jsonArray,boolean isVideo) {
        List<GalleryModel> galleryModelList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {

                GalleryModel galleryModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    galleryModel = new GalleryModel();
                    galleryModel.id = AppUtils.getStringValueFromJson(jsonObject, "id");
                    galleryModel.thumbnail_img = AppUtils.getStringValueFromJson(jsonObject, "thumbnail_img");
                    galleryModel.video = AppUtils.getStringValueFromJson(jsonObject, "image");
//                    galleryModel.type = AppUtils.getStringValueFromJson(jsonObject, "type");
                    galleryModel.isVideo = isVideo;



                    galleryModelList.add(galleryModel);

                }

            }
            return galleryModelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return galleryModelList;
    }

    Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

             if (message.what == 1) {


                GalleryModel galleryModel = null;
                try {
                    galleryModel = (GalleryModel) message.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (galleryModel != null) {
                    if(galleryModel.isVideo){
                        Bundle args = new Bundle();
                        args.putSerializable("galleryModel",galleryModel);
                        GalleryDialogFragment dialogFragment=new GalleryDialogFragment();
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentActivity().getSupportFragmentManager(),"My  Fragment");
                    }


                }


            }
            // Your code logic goes here.
            return true;
        }
    });
    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }case R.id.radio_video: {
                    showView(gridViewVideo);
                    break;
                }case R.id.radio_photo: {
                    showView(gridViewPhoto);

                    break;
                }



            }
        }
    };
    private  void showView(View view){
        if(view != null){
            gridViewVideo.setVisibility(View.GONE);
            gridViewPhoto.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);

        }
    }

    private void goback() {
        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}