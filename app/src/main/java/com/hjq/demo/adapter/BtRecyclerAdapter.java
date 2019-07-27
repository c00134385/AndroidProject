package com.hjq.demo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyRecyclerViewAdapter;

import java.util.List;

public class BtRecyclerAdapter extends RecyclerView.Adapter<BtRecyclerAdapter.BtViewHolder> {

    Context context;
    List<BluetoothDevice> list;


    public BtRecyclerAdapter(Context context, List<BluetoothDevice> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public BtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_bt, viewGroup, false);
        BtViewHolder holder = new BtViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BtViewHolder btViewHolder, int i) {
        BluetoothDevice bluetoothDevice = list.get(i);
        View view = btViewHolder.itemView;
        TextView deviceNo = view.findViewById(R.id.device_no);
        TextView deviceName = view.findViewById(R.id.device_name);
        TextView deviceAddr = view.findViewById(R.id.device_addr);
        deviceNo.setText(i+"");
        if(TextUtils.isEmpty(bluetoothDevice.getName())) {
            deviceName.setText("null");
        } else {
            deviceName.setText(bluetoothDevice.getName());
        }
        deviceAddr.setText(bluetoothDevice.getAddress());
    }

    @Override
    public int getItemCount() {
        if(null == list) {
            return 0;
        }
        return list.size();
    }

    final class BtViewHolder extends RecyclerView.ViewHolder {

        public BtViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
