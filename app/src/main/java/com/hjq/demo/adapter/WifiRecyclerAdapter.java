package com.hjq.demo.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.mananger.WifiAdmin;

import java.util.List;

import timber.log.Timber;

public class WifiRecyclerAdapter extends RecyclerView.Adapter<WifiRecyclerAdapter.WifiViewHolder>{

    private Context context;
    private List<ScanResult> list;

    public WifiRecyclerAdapter(Context context, List<ScanResult> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_wifi, viewGroup, false);
        WifiViewHolder holder = new WifiViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder wifiViewHolder, int i) {
        ScanResult scanResult = list.get(i);
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
