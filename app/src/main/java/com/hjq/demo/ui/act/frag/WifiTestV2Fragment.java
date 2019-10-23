package com.hjq.demo.ui.act.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.mananger.WifiAdmin;
import com.hjq.demo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class WifiTestV2Fragment extends MyLazyFragment {

    @BindView(R.id.btn_openwifi)
    Button btnOpenWifi;

    @BindView(R.id.container_wifi)
    View containerWifi;

    @BindView(R.id.tv_wifiinfo)
    TextView tvWifiInfo;

    @BindView(R.id.btn_openhtml)
    Button btnOpenHtml;

    @BindView(R.id.web_view)
    WebView webView;

    private final String url = "http://m.baidu.com/";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateWifiInfo();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_wifi_v2;
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

    }

    @OnClick({R.id.btn_openwifi, R.id.btn_openhtml})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_openwifi:
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                break;

            case R.id.btn_openhtml:
                loadUrl();
                break;
        }
    }

    private void updateWifiInfo() {
        WifiInfo wifiInfo = WifiAdmin.getInstance().getWifiInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("BSSID: " + wifiInfo.getBSSID());
        sb.append("\nSSID: " + wifiInfo.getSSID());
        sb.append("\nIPAddress: " + CommonUtils.int2ip(wifiInfo.getIpAddress()));
        sb.append("\nMacAddress: " + NetworkManager.getInstance().getWifiMac());
        sb.append("\nNetworkId: " + wifiInfo.getNetworkId());
        sb.append("\nLinkSpeed: " + wifiInfo.getLinkSpeed());
        sb.append("\nRssi: " + wifiInfo.getRssi());
        tvWifiInfo.setText(sb.toString());

        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void loadUrl() {
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            settings.setDomStorageEnabled(true);
        }
        //解决一些图片加载问题
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
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

        if(WifiManager.WIFI_STATE_ENABLED == WifiAdmin.getInstance().getState() && null != WifiAdmin.getInstance().getWifiInfo()
        && WifiAdmin.getInstance().getWifiInfo().getSupplicantState() == SupplicantState.COMPLETED) {
            btnOpenWifi.setVisibility(View.GONE);
            containerWifi.setVisibility(View.VISIBLE);
        } else {
            btnOpenWifi.setVisibility(View.VISIBLE);
            containerWifi.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mReceiver);
        mHandler.removeCallbacksAndMessages(null);

        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.setTag(null);
            webView.clearHistory();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d("action:%s", intent.getAction());
            switch (intent.getAction()) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                            WifiManager.WIFI_STATE_DISABLED);
                    Timber.d("wifiState:%s", wifiState);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_DISABLING:
                            tvWifiInfo.setText("WIFI关闭中");
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            tvWifiInfo.setText("WIFI已关闭");
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            tvWifiInfo.setText("WIFI打开中");
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            tvWifiInfo.setText("WIFI已打开");
                            mHandler.sendEmptyMessage(0);
                            break;
                        default:
                            tvWifiInfo.setText("WIFI未知状态");
                            break;
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    Timber.d("extras:%s", intent.getExtras().toString());
                    break;
            }
        }
    };
}
