package com.pt_plus.Dialog;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pt_plus.Model.GalleryModel;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.MyVideoView;

public class GalleryDialogFragment extends androidx.fragment.app.DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.gallery_dialog_fragment, container, false);
        initView(view);
        return view;


    }
private GalleryModel galleryModel;
    private void initView(View view) {
        Bundle mArgs = getArguments();
        if(mArgs != null){
            galleryModel = (GalleryModel) mArgs.getSerializable("galleryModel");
         }
      MyVideoView videoView = view.findViewById(R.id.idVideoView);

        AppLogger.log("dfdfsad55                "+galleryModel.video);


        // Uri object to refer the
        // resource from the videoUrl
        Uri uri = Uri.parse(galleryModel.video);

        // sets the resource from the
        // videoUrl to the videoView
        videoView.setVideoURI(uri);

        // creating object of
        // media controller class
        MediaController mediaController = new MediaController(getContext());

        // sets the anchor view
        // anchor view for the videoView
        mediaController.setAnchorView(videoView);

        // sets the media player to the videoView
        mediaController.setMediaPlayer(videoView);

        // sets the media controller to the videoView
        videoView.setMediaController(mediaController);

        // starts the video
        videoView.start();


    }
}