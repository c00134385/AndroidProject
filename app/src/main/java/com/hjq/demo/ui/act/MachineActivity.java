package com.hjq.demo.ui.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.widget.ListView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.model.BasicModel;
import com.hjq.demo.model.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MachineActivity extends MyActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private List<BasicModel> basicModels;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_machine;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        basicModels = new ArrayList<>();
        basicModels.add(new BasicModel(getString(R.string.model), MachineManager.getInstance().getModel()));
        basicModels.add(new BasicModel(getString(R.string.sn), MachineManager.getInstance().getSn()));
        basicModels.add(new BasicModel(getString(R.string.version_code), MachineManager.getInstance().getVersionName()));
        basicModels.add(new BasicModel(getString(R.string.ip), NetworkManager.getInstance().getIpAddress()));
        basicModels.add(new BasicModel(getString(R.string.wlan_mac), NetworkManager.getInstance().getWifiMac()));
        basicModels.add(new BasicModel(getString(R.string.ba), NetworkManager.getInstance().getBluetoothMac()));
        basicModels.add(new BasicModel(getString(R.string.imei), NetworkManager.getInstance().getImei()));


        //从某一时间开始计时
        long startTime = SystemClock.elapsedRealtime();
        long timer = (startTime)/1000;//得到从开始计时到现在的时间,单位:s
        String sTime = String.format("%02d:%02d:%02d", timer/3600,timer%3600/60,timer%60);//转为标准格式
        basicModels.add(new BasicModel(getString(R.string.worktime), sTime));

        basicModels.add(new BasicModel(getString(R.string.extra_info), ""));

        MyAdapter myAdapter = new MyAdapter(this, basicModels);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelRcvr, batteryLevelFilter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(batteryLevelRcvr);
    }

    private BroadcastReceiver batteryLevelRcvr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder();

            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            int level = -1; // percentage, or -1 for unknown
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }

            StringBuilder batteryStatus = new StringBuilder();
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    batteryStatus.append("正在充电");
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    batteryStatus.append("电量满");
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                default:
                    batteryStatus.append("未在充电");
                    break;
            }
            batteryStatus.append(" " + level + "%");

            basicModels.add(new BasicModel(getString(R.string.battery), batteryStatus.toString()));
        }
    };
}
