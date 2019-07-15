package com.example.lookface.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lookface.R;
import com.example.lookface.model.PicBean;
import com.example.lookface.other.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private List<PicBean> picList;

    private Context mContext;

    public PicAdapter(List<PicBean> picList) {
        this.picList = picList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pic_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PicBean picBean = picList.get(i);
        PicBean.FacesBean.AttributesBean face = picBean.getFaces().get(0).getAttributes();
        //定性别与颜值评分
        if (face.getGender().getValue().equals("Male")) {       // TODO: 2019/7/14 to remember equals
            viewHolder.imgSex.setImageDrawable(mContext.getDrawable(R.drawable.male));
            viewHolder.beautyText.setText(face.getBeauty().getMale_score() + "分");
        } else {
            viewHolder.imgSex.setImageDrawable(mContext.getDrawable(R.drawable.female));
            viewHolder.beautyText.setText(face.getBeauty().getFemale_score() + "分");
        }
        Glide.with(mContext).load(picBean.getPath()).into(viewHolder.imgShow);
        viewHolder.emotionText.setText(getEmotion(face));   //情绪
        switch (face.getEthnicity().getValue()) {   //人种
            case Value.WHITE:
                viewHolder.ethnicityText.setText("白种人");
                break;
            case Value.ASIAN:
                viewHolder.ethnicityText.setText("黄种人");
                break;
            case Value.BLACK:
                viewHolder.ethnicityText.setText("黑种人");
                break;
            default:
                viewHolder.ethnicityText.setText("黄种人");
                break;
        }
        viewHolder.ageText.setText(Integer.toString(face.getAge().getValue()));
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSex;

        ImageView imgShow;

        TextView beautyText;

        TextView ageText;

        TextView ethnicityText;

        TextView emotionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSex = itemView.findViewById(R.id.choose_sex);
            imgShow = itemView.findViewById(R.id.img_show);
            beautyText = itemView.findViewById(R.id.beauty_text);
            ageText = itemView.findViewById(R.id.age_text);
            ethnicityText = itemView.findViewById(R.id.ethnicity_text);
            emotionText = itemView.findViewById(R.id.emotion_text);
        }
    }

    private String getEmotion(PicBean.FacesBean.AttributesBean face) {
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
                return (o1.getValue()).toString().compareTo(o2.getValue().toString());
            }
        });
        return sortList.get(sortList.size() - 1).getKey();
    }
}
