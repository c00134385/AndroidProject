package com.hjq.demo.ui.act.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.adapter.WifiRecyclerAdapter;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.WifiAdmin;
import com.hjq.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class ComTestFragment extends MyLazyFragment {

    @BindView(R.id.switch_btn)
    SwitchButton btSwitch;

    @BindView(R.id.list_view)
    RecyclerView listView;

    @BindView(R.id.tv_state)
    TextView btState;


    List<ScanResult> list = new ArrayList<>();
    WifiRecyclerAdapter wifiRecyclerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_com;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext() );
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
//设置布局管理器
        listView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//设置Adapter
        wifiRecyclerAdapter = new WifiRecyclerAdapter(getContext(), list);
        listView.setAdapter(wifiRecyclerAdapter);
        //设置分隔线
//        btList.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        listView.setItemAnimator( new DefaultItemAnimator());
        if(WifiManager.WIFI_STATE_ENABLED == WifiAdmin.getInstance().getState()) {
            btSwitch.setChecked(true);
        } else {
            btSwitch.setChecked(false);
        }
        btSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean isChecked) {
                Timber.d("isChecked:%b", isChecked);
                if(isChecked) {
                    WifiAdmin.getInstance().openWifi(getContext());
                } else {
                    WifiAdmin.getInstance().closeWifi(getContext());
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();//注册广播接收信号
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//蓝牙状态改变的广播
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//蓝牙状态改变的广播
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);//蓝牙状态改变的广播
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//蓝牙状态改变的广播
        getContext().registerReceiver(mReceiver, intentFilter);//用BroadcastReceiver 来取得结果
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d("action:%s", intent.getAction());
            list.clear();
            list.addAll(WifiAdmin.getInstance().getWifiList());
            wifiRecyclerAdapter.notifyDataSetChanged();
            switch (intent.getAction()) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                            WifiManager.WIFI_STATE_DISABLED);
                    Timber.d("wifiState:%s", wifiState);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_DISABLING:
                            btState.setText("WIFI关闭中");
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            btState.setText("WIFI已关闭");
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            btState.setText("WIFI打开中");
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            btState.setText("WIFI已打开");
                            break;
                        default:
                            btState.setText("WIFI未知状态");
                            break;
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
//                    Timber.d("extras:%s", intent.getExtras().toString());
                    break;
            }
        }
    };
}
