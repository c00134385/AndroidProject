package com.hjq.demo.ui.act;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.adapter.ListAdapter;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.mananger.RkManager;
import com.hjq.demo.model.BasicModel;
import com.hjq.demo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NetworkActivity extends MyActivity {

    @BindView(R.id.recyclerView_wifi)
    RecyclerView recyclerViewWifi;
    @BindView(R.id.recyclerView_eth)
    RecyclerView recyclerViewEth;

    private List<BasicModel> wifiModels;
    private List<BasicModel> ethModels;

//    private MyManager manager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_network;
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
//        manager = MyManager.getInstance(this);
//        manager.bindAIDLService(this);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewWifi.setLayoutManager(linearLayoutManager);

        ListAdapter wifiAdapter = new ListAdapter(wifiModels);
        recyclerViewWifi.setAdapter(wifiAdapter);

//        dhcpInfo = NetworkManager.getInstance().getEthernetDhcpInfo();
        ethModels = new ArrayList<>();
//        ethModels.add(new BasicModel(getString(R.string.eth_statue), NetworkManager.getInstance().isEthernetConnected()?"已连接":"未连接"));
//        ethModels.add(new BasicModel(getString(R.string.eth_mac), NetworkManager.getInstance().getEthernetMac()));
//        ethModels.add(new BasicModel(getString(R.string.eth_ip), NetworkManager.getInstance().getEhernetIp()));
//        ethModels.add(new BasicModel(getString(R.string.eth_gateway), ""));
//        ethModels.add(new BasicModel(getString(R.string.eth_dns1), ""));

        ethModels.add(new BasicModel(getString(R.string.eth_statue), NetworkManager.getInstance().isEthernetConnected()?"已连接":"未连接"));
        ethModels.add(new BasicModel(getString(R.string.eth_mac), RkManager.getInstance().getMac()));
        ethModels.add(new BasicModel(getString(R.string.eth_ip), RkManager.getInstance().getIp()));
//        ethModels.add(new BasicModel(getString(R.string.eth_gateway), EthernetManager.getInstance().getGateway()));
//        ethModels.add(new BasicModel(getString(R.string.eth_dns1), EthernetManager.getInstance().getDns1()));
//        ethModels.add(new BasicModel(getString(R.string.eth_level), "Rssi:" + wifiInfo.getRssi()));

//        EthernetManager mEthManager = getSystemService("ethernet");

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewEth.setLayoutManager(linearLayoutManager2);

        ListAdapter ethAdapter = new ListAdapter(ethModels);
        recyclerViewEth.setAdapter(ethAdapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        manager.unBindAIDLService(this);
    }
}
