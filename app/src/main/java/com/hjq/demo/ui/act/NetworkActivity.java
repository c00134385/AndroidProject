package com.hjq.demo.ui.act;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ListView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.model.BasicModel;
import com.hjq.demo.model.MyAdapter;
import com.hjq.demo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NetworkActivity extends MyActivity {

    @BindView(R.id.wifi_list_view)
    ListView wifiListView;

    @BindView(R.id.eth_list_view)
    ListView ethListView;

    private List<BasicModel> wifiModels;
    private List<BasicModel> ethModels;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_network;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        WifiManager wifiManager =(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        wifiModels = new ArrayList<>();
        wifiModels.add(new BasicModel(getString(R.string.wifi_statue), NetworkManager.getInstance().isWifiConnected()?"已连接":"未连接"));
        wifiModels.add(new BasicModel(getString(R.string.wifi_ssid), wifiInfo.getSSID()));
        wifiModels.add(new BasicModel(getString(R.string.wifi_mac), NetworkManager.getInstance().getWifiMac()));
        wifiModels.add(new BasicModel(getString(R.string.wifi_ip), CommonUtils.int2ip(wifiInfo.getIpAddress())));
        wifiModels.add(new BasicModel(getString(R.string.wifi_gateway), CommonUtils.int2ip(dhcpInfo.gateway)));
        wifiModels.add(new BasicModel(getString(R.string.wifi_dns1), CommonUtils.int2ip(dhcpInfo.dns1)));
        wifiModels.add(new BasicModel(getString(R.string.wifi_level), wifiInfo.getRssi() + "db"));
        MyAdapter wifiAdapter = new MyAdapter(this, wifiModels);
        wifiListView.setAdapter(wifiAdapter);

//        dhcpInfo = NetworkManager.getInstance().getEthernetDhcpInfo();
        ethModels = new ArrayList<>();
        ethModels.add(new BasicModel(getString(R.string.eth_statue), NetworkManager.getInstance().isEthernetConnected()?"已连接":"未连接"));
        ethModels.add(new BasicModel(getString(R.string.eth_mac), NetworkManager.getInstance().getEthernetMac()));
        ethModels.add(new BasicModel(getString(R.string.eth_ip), NetworkManager.getInstance().getEhernetIp()));
        ethModels.add(new BasicModel(getString(R.string.eth_gateway), ""));
        ethModels.add(new BasicModel(getString(R.string.eth_dns1), ""));
//        ethModels.add(new BasicModel(getString(R.string.eth_level), "Rssi:" + wifiInfo.getRssi()));

//        EthernetManager mEthManager = getSystemService("ethernet");

        MyAdapter ethAdapter = new MyAdapter(this, ethModels);
        ethListView.setAdapter(ethAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        final BaseDialog dialog = new WaitDialog.Builder(this)
//                .setMessage("加载中...") // 消息文本可以不用填写
//                .show();
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 5000);
    }
}
