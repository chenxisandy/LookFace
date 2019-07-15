package com.example.lookface.main;

import android.content.Context;
import android.net.Uri;

import com.example.lookface.model.PicBean;

import java.util.List;

public interface MainContract {
    interface View {
//        //set presenter of view
//        void setPresenter(Presenter presenter);

        void intentTOPickPic();

        void intentToCamera();

        void intentToMerge();

        void setRecyclerView(List<PicBean> list);
    }

    interface Presenter {
//        void selectFromGallery();
//
//        void selectFromCamera();
//
//        void beginMerge();

        void setData();

    }
}
