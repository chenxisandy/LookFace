package com.example.lookface.analyse;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lookface.R;
import com.example.lookface.model.PicBean;
import com.example.lookface.other.BaseActivity;
import com.example.lookface.other.Value;
import com.race604.flyrefresh.FlyRefreshLayout;

import java.io.FileNotFoundException;

public class AnalyseActivity extends BaseActivity implements AnalyseContract.View, FlyRefreshLayout.OnPullRefreshListener{

    AnalyseContract.Presenter presenter;

    private int actionType;

    private ImageView imgShow;

    private Uri imgUri;

    private Bitmap bitmap;

    private FlyRefreshLayout flyRefreshLayout;

    private String picturePath;

    private PicBean picBean;

    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        presenter = new AnalysePresenter(this);
        initView();
        presenter.showPicture();

    }

    private void initView() {
        imgShow = findViewById(R.id.picture);
        actionType = getRequestCodeFromIntent();    //todo to change
        imgUri = presenter.getImgUri();
        flyRefreshLayout = findViewById(R.id.fly_layout);
        flyRefreshLayout.setOnPullRefreshListener(this);
        actionButton = flyRefreshLayout.getHeaderActionButton();
        if (actionButton != null) {
            actionButton.setImageDrawable(getDrawable(R.drawable.ic_plane));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flyRefreshLayout.startRefresh();
                }
            });
        }
    }

    //implement as view
    @Override
    public int getRequestCodeFromIntent() {
        Intent intent = getIntent();
        actionType = intent.getIntExtra(Value.ACTION_TYPE, 0);
        return actionType;
    }

    @Override
    public void getPic() {
        if (actionType == Value.TAKE_CAMERA) {
            selectFromTake();
        } else if (actionType == Value.PICK_PIC) {
            selectFromGallery();
        }
    }

    @Override
    public void setPicAnalyse(PicBean picBean, String emotion) {
        this.picBean = picBean;
        TextView beautyScore = findViewById(R.id.beauty);
        TextView emotionText = findViewById(R.id.emotion);
        TextView ageText = findViewById(R.id.age_year);
        TextView ethnicityText = findViewById(R.id.ethnicity);
        PicBean.FacesBean.AttributesBean face = picBean.getFaces().get(0).getAttributes();
        if (face.getGender().getValue().equals("Male")) {       // TODO: 2019/7/14 to remember equals
            CardView cardView = findViewById(R.id.card_male);
            cardView.setCardBackgroundColor(getResources().getColor(R.color.blue));
            beautyScore.setText(face.getBeauty().getMale_score() + "分");
        } else {
            CardView cardView = findViewById(R.id.card_female);
            cardView.setCardBackgroundColor(getResources().getColor(R.color.pink));
            beautyScore.setText(face.getBeauty().getFemale_score() + "分");
        }
        emotionText.setText(emotion);   //情绪
        switch (face.getEthnicity().getValue()) {   //人种
            case Value.WHITE:
                ethnicityText.setText("白种人");
                break;
            case Value.ASIAN:
                ethnicityText.setText("黄种人");
                break;
            case Value.BLACK:
                ethnicityText.setText("黑种人");
                break;
                default:
                    ethnicityText.setText("黄种人");
                    break;
        }
        ageText.setText(Integer.toString(face.getAge().getValue()));
        flyRefreshLayout.onRefreshFinish();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public String getImgPath() {
        if (actionType == Value.TAKE_CAMERA) {
            String uriPath = imgUri.getPath();
            String partPath = uriPath.substring(15);
            picturePath = "/storage/emulated/0" + partPath;
        }
        return picturePath;
    }

    //get pic
    private void selectFromTake() {
        imgUri = presenter.getImgUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//将拍取的照片保存到指定URI
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Value.TAKE_CAMERA);
    }

    private void selectFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Value.PICK_PIC);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Value.TAKE_CAMERA:
                    if (data != null && data.hasExtra("data")) {
                        bitmap = data.getParcelableExtra("data");
                        Glide.with(this).load(bitmap).into(imgShow);
                    } else {
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Glide.with(this).load(bitmap).into(imgShow);

                    }
                    break;
                case Value.PICK_PIC:
                    if (data != null) {
                        Uri selectedImg = data.getData();   //获取系统返回得照片得Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        //从系统表中查询指定Uri对应照片
                        Cursor cursor = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                        cursor.moveToFirst();       //cursor = 光标
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex); //得到照片路径
                        cursor.close();
                        Glide.with(this).load(picturePath).into(imgShow);
                    }
            }
        }
    }

    //toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.analyse_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_data:
                break;
        }
        return true;
    }

    //smartRefresh
    @Override
    public void onRefresh(FlyRefreshLayout view) {
        // TODO: 2019/7/14
        Toast.makeText(this, "请耐心等待分析", Toast.LENGTH_SHORT).show();
        actionButton.setImageDrawable(getDrawable(R.drawable.loading));
        if (bitmap == null) {
            bitmap = ((BitmapDrawable) imgShow.getDrawable()).getBitmap();
        }
        presenter.getAnalysis();
    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
        actionButton.setImageDrawable(getDrawable(R.drawable.ic_plane));
        Toast.makeText(this, "分析完毕", Toast.LENGTH_SHORT).show();
    }

    //生命周期


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (picBean != null)
        presenter.savePicBean();
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

}
