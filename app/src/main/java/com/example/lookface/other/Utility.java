package com.example.lookface.other;

import com.example.lookface.model.PicBean;
import com.google.gson.Gson;

public class Utility {
    public static PicBean handlePicJSONResponse(String response) {
        try {
            Gson gson = new Gson();
            PicBean picBean;
            picBean = gson.fromJson(response, PicBean.class);
            return picBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
