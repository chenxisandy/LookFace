package com.example.lookface.other;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lookface.broadcast.NetBroadcastReceiver;
import com.example.lookface.model.DataRepo;

public class BaseActivity extends AppCompatActivity {

    protected NetBroadcastReceiver netBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            netBroadcastReceiver=new NetBroadcastReceiver();
            registerReceiver(netBroadcastReceiver, filter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netBroadcastReceiver);   //最后取消注册
        DataRepo.getInstance().saveListToLitePal();
    }
}
