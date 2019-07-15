package com.example.lookface.analyse;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.lookface.model.DataRepo;
import com.example.lookface.model.PicBean;
import com.example.lookface.model.Picture;
import com.example.lookface.other.Utility;

import java.util.HashMap;

public class AnalysePresenter<T extends Activity & AnalyseContract.View> implements AnalyseContract.Presenter {

    private T view;

    private DataRepo repo;

    private PicBean picBean;

    private Picture picture;

    public AnalysePresenter(T view) {
        this.view = view;
        this.repo = DataRepo.getInstance();
    }

    @Override
    public void getAnalysis() {
        postRequest();
    }

    @Override
    public void showPicture() {
        view.getPic();
    }

    public Uri getImgUri() {
        return FileProvider.getUriForFile(view, view.getPackageName() + ".fileprovider", repo.createUriFile());
    }

    @Override
    public void savePicBean() {
        repo.getBeanList().add(picBean);
        repo.getSaveList().add(picture);
        picture.save();
    }

    public void postRequest() {
        byte[] buff = repo.getBytesFromBitmap(view.getBitmap());
        final String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";    //网址
        HashMap<String, String> map = new HashMap<>();
        final HashMap<String, byte[]> byteMap = new HashMap<>();
        byteMap.put("image_file", buff);
        map.put("api_key", "0nnZm2FfsWmLOfreVoAeFKs3FWI150Ev");
        map.put("api_secret", "0D1RBzseY6obBf02HDOyoKYrR_mcE7As");
        map.put("return_landmark", "0");    //告诉它不返回那些点
        map.put("return_attributes", "emotion,gender,age,smiling,facequality,ethnicity,beauty");    //希望返回的数据
        //test
        //map.put("image_url", "https://cdn.faceplusplus.com.cn/mc-official/scripts/demoScript/images/demo-pic1.jpg");

        final HashMap<String, String> finalMap = new HashMap<>(map);
        byteMap.put("image_file", buff);    //传相册文件
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bacd = repo.postAndGetResponseBytes(url, finalMap, byteMap);
                        String str = new String(bacd);
                        picBean = Utility.handlePicJSONResponse(str);
                        if (picBean != null) {
                            if (picBean.getFaces().size() == 0) {
                                view.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(view, "您的图片不包含脸", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            picture = new Picture();
                            String path = view.getImgPath();
                            picture.setPicturePath(path);
                            picture.setJSONString(str);
                            picBean.setPath(path);
                            //picBean.setGSONString(str);
                        }
                        view.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.setPicAnalyse(picBean, repo.getEmotion(picBean.getFaces().get(0).getAttributes()));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
