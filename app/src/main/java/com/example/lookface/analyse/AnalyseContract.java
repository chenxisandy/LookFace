package com.example.lookface.analyse;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.lookface.model.PicBean;

public interface AnalyseContract {
    interface View {
        int getRequestCodeFromIntent();

        void getPic();

        void setPicAnalyse(PicBean picBean, String emotion);

        Bitmap getBitmap();

        String getImgPath();
    }

    interface Presenter {
        void getAnalysis();

        void showPicture();

        Uri getImgUri();

        void savePicBean();
    }
}
