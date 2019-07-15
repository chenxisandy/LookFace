package com.example.lookface.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dingmouren.layoutmanagergroup.echelon.EchelonLayoutManager;
import com.example.lookface.R;
import com.example.lookface.analyse.AnalyseActivity;
import com.example.lookface.merge.MergeActivity;
import com.example.lookface.model.DataRepo;
import com.example.lookface.model.PicBean;
import com.example.lookface.other.BaseActivity;
import com.example.lookface.other.Value;
import com.github.clans.fab.FloatingActionButton;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, MainContract.View {

    private MainContract.Presenter presenter;

    private PicAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //每次一开始就弄出数据库
        LitePal.getDatabase();
        initView();
    }

    private void initView() {
        //set presenter
        presenter = new MainPresenter(this);
        //fab
        FloatingActionButton pickPicFab = findViewById(R.id.fab_pick_pic);
        FloatingActionButton cameraFab = findViewById(R.id.fab_camera);
        FloatingActionButton mergeFab = findViewById(R.id.fab_merge);
        pickPicFab.setOnClickListener(this);
        cameraFab.setOnClickListener(this);
        mergeFab.setOnClickListener(this);
        //recyclerView & data
        presenter.setData();
    }

    /**
     * 检验权限是否完全申请成功
     */

    private boolean checkPermissionsOK() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();

        // 判断权限是否已经授予，没有就把该权限添加到列表中
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), Value.REQUEST_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Value.REQUEST_PERMISSION:
                // 请求码对应的是申请多个权限
                if (grantResults.length > 0) {
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(MainActivity.this, permissions[i] + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_pick_pic:
                if (checkPermissionsOK()) {
                    intentTOPickPic();
                }
                break;
            case R.id.fab_camera:
                if (checkPermissionsOK()) {
                    intentToCamera();
                }
                break;
            case R.id.fab_merge:
                if (checkPermissionsOK()) {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void intentTOPickPic() {
        Intent intent = new Intent(MainActivity.this, AnalyseActivity.class);
        intent.putExtra(Value.ACTION_TYPE, Value.PICK_PIC);
        startActivity(intent);
    }

    @Override
    public void intentToCamera() {
//        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(takeIntent, Value.PICK_PIC);
        Intent intent = new Intent(MainActivity.this, AnalyseActivity.class);
        intent.putExtra(Value.ACTION_TYPE, Value.TAKE_CAMERA);
        startActivity(intent);

    }

    @Override
    public void intentToMerge() {
        Intent intent = new Intent(MainActivity.this, MergeActivity.class);
        startActivity(intent);
    }


    @Override
    public void setRecyclerView(List<PicBean> list) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        EchelonLayoutManager layoutManager = new EchelonLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PicAdapter(list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
