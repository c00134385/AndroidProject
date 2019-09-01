package com.hjq.demo.ui.act.frag;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.demo.R;
import com.hjq.demo.adapter.MyRecyclerViewDivider;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.WifiAdmin;
import com.hjq.dialog.InputDialog;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class WifiTestFragment extends MyLazyFragment {

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
        return R.layout.fragment_test_wifi;
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
        listView.addItemDecoration(new MyRecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL,2, ContextCompat.getColor(getContext(),R.color.colorAccent)));
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

        if(WifiManager.WIFI_STATE_ENABLED == WifiAdmin.getInstance().getState()) {
            btSwitch.setChecked(true);
            btState.setText("WIFI已打开");
        } else {
            btSwitch.setChecked(false);
            btState.setText("WIFI已关闭");
        }
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


    class WifiRecyclerAdapter extends RecyclerView.Adapter<WifiRecyclerAdapter.WifiViewHolder>{

        private Context context;
        private List<ScanResult> list;

        public WifiRecyclerAdapter(Context context, List<ScanResult> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public WifiRecyclerAdapter.WifiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item_wifi, viewGroup, false);
            WifiRecyclerAdapter.WifiViewHolder holder = new WifiRecyclerAdapter.WifiViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull WifiRecyclerAdapter.WifiViewHolder wifiViewHolder, int i) {
            final ScanResult scanResult = list.get(i);
            View view = wifiViewHolder.itemView;
            TextView wifiNo = view.findViewById(R.id.wifi_no);
            TextView wifiName = view.findViewById(R.id.wifi_name);
            TextView wifiState = view.findViewById(R.id.wifi_state);
            TextView wifiLevel = view.findViewById(R.id.wifi_level);
            wifiNo.setText(""+i);
            wifiName.setText(scanResult.SSID);
            Timber.d(scanResult.BSSID);
            if(scanResult.BSSID.equals(WifiAdmin.getInstance().getWifiInfo().getBSSID())) {
                wifiState.setText("已连接");
            }
//        wifiState.setText(scanResult.capabilities);
            wifiLevel.setText(scanResult.level +"");

            wifiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("");
                    startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
//                    if(scanResult.BSSID.equals(WifiAdmin.getInstance().getWifiInfo().getBSSID())) {
//                        ToastUtils.show("已连接");
//                    } else {
//                        new InputDialog.Builder(getActivity())
//                                .setTitle(scanResult.SSID) // 标题可以不用填写
////                                .setContent("密码")
//                                .setHint("请输入密码")
//                                .setConfirm("确定")
//                                .setCancel("取消") // 设置 null 表示不显示取消按钮
//                                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
//                                .setListener(new InputDialog.OnListener() {
//
//                                    @Override
//                                    public void onConfirm(Dialog dialog, String content) {
////                                        toast("确定了：" + content);
//                                        if(TextUtils.isEmpty(content) || content.trim().length() < 8) {
//                                            Toast.makeText(getContext(), "密码至少8位", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//                                        WifiAdmin.getInstance().addNetwork(WifiAdmin.getInstance().CreateWifiInfo(scanResult.SSID, content.trim(), 3));
//                                    }
//
//                                    @Override
//                                    public void onCancel(Dialog dialog) {
////                                        toast("取消了");
//                                    }
//                                })
//                                .show();
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(null == list) {
                return 0;
            }
            return list.size();
        }

        final class WifiViewHolder extends RecyclerView.ViewHolder {

            public WifiViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
