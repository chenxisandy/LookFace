package com.example.lookface.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.example.lookface.other.Utility;
import com.example.lookface.other.Value;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

public class DataRepo {

    private List<PicBean> picBeanList = new ArrayList<>();

    private List<Picture> saveList = new ArrayList<>();

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;

    private String boundaryString = getBoundary();

    //静态内部类实现单例模式
    private static class RepoHolder {
        private static final DataRepo INSTANCE = new DataRepo();
    }

    private DataRepo() {
    }

    public static final DataRepo getInstance() {
        return RepoHolder.INSTANCE;
    }

    /*
     * return list
     * */
    public List<PicBean> getBeanList() {
        return picBeanList;
    }
    /*
     * get data
     * */


    private String getBoundary() {       //随机取的一个数，边界,一共取32个数
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    private String encode(String value) throws Exception {    //UTF-8编码
        return URLEncoder.encode(value, "UTF-8");
    }

    public byte[] postAndGetResponseBytes(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection httpURLConnection;
        URL url1 = new URL(url);
        httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
        httpURLConnection.setReadTimeout(READ_OUT_TIME);
        httpURLConnection.setRequestProperty("accept", "*/*");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            dataOutputStream.writeBytes("--" + boundaryString + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.writeBytes(value + "\r\n");
        }
        if (fileMap != null && fileMap.size() > 0) {
            Iterator fileIterator = fileMap.entrySet().iterator();
            while (fileIterator.hasNext()) {
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIterator.next();
                dataOutputStream.writeBytes("--" + boundaryString + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.write(fileEntry.getValue());
                dataOutputStream.writeBytes("\r\n");
            }
        }
        dataOutputStream.writeBytes("--" + boundaryString + "--" + "\r\n");
        dataOutputStream.writeBytes("\r\n");
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream ins = null;
        int code = httpURLConnection.getResponseCode();
        try {
            if (code == 200) {
                ins = httpURLConnection.getInputStream();
            } else {
                ins = httpURLConnection.getErrorStream();
            }
        } catch (SSLException e) {
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while ((len = ins.read(buff)) != -1) {
            byteArrayOutputStream.write(buff, 0, len);
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ins.close();
        return bytes;
    }

    public File createUriFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "images");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public String getEmotion(PicBean.FacesBean.AttributesBean face) {
        HashMap<String, Double> map = new HashMap<>();
        map.put(Value.SADNESS, face.getEmotion().getSadness());
        map.put(Value.ANGER, face.getEmotion().getAnger());
        map.put(Value.DISGUST, face.getEmotion().getDisgust());
        map.put(Value.FEAR, face.getEmotion().getFear());
        map.put(Value.HAPPINESS, face.getEmotion().getHappiness());
        map.put(Value.NEUTRAL, face.getEmotion().getNeutral());
        map.put(Value.SURPRISE, face.getEmotion().getSurprise());
        //通过list进行排序
        //先化为list列表
        List<Map.Entry<String, Double>> sortList = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
        //排序
        Collections.sort(sortList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else {
                    return -1;
                }
//                return (o1.getValue()).toString().compareTo(o2.getValue().toString());
            }
        });
        return sortList.get(sortList.size() - 1).getKey();
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] buff = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }

    public void saveListToLitePal() {
        LitePal.saveAll(saveList);
    }

    public void fetchListFromLitePal() {
        //通过老的得到新的
        saveList.clear();
        picBeanList.clear();
        saveList.addAll(LitePal.findAll(Picture.class));
        for (Picture picture :
                saveList) {
            PicBean p = Utility.handlePicJSONResponse(picture.getJSONString());
            if (p != null) {
                p.setPath(picture.getPicturePath());
            }
            picBeanList.add(p);
        }
    }

    public List<PicBean> getPicBeanList() {
        return picBeanList;
    }

    public void setPicBeanList(List<PicBean> picBeanList) {
        this.picBeanList = picBeanList;
    }

    public List<Picture> getSaveList() {
        return saveList;
    }

    public void setSaveList(List<Picture> saveList) {
        this.saveList = saveList;
    }
}
