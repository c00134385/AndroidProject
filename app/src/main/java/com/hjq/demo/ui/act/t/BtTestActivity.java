package com.hjq.demo.ui.act.t;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.adapter.BtRecyclerAdapter;
import com.hjq.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class BtTestActivity extends BaseTestActivity {

    @BindView(R.id.bt_switch)
    SwitchButton btSwitch;

    @BindView(R.id.bt_list)
    RecyclerView btList;

    @BindView(R.id.bt_state)
    TextView btState;

    BluetoothAdapter blueadapter;

    List<BluetoothDevice> list = new ArrayList<>();
    BtRecyclerAdapter btRecyclerAdapter;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_bt;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();//注册广播接收信号
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        registerReceiver(bluetoothReceiver, intentFilter);//用BroadcastReceiver 来取得结果
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    protected void initData() {
        btRecyclerAdapter = new BtRecyclerAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
//设置布局管理器
        btList.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//设置Adapter
        btRecyclerAdapter = new BtRecyclerAdapter(this, list);
        btList.setAdapter(btRecyclerAdapter);
        //设置分隔线
//        btList.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        btList.setItemAnimator( new DefaultItemAnimator());

        btSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean isChecked) {
                Timber.d("isChecked:%b", isChecked);
                if(isChecked) {
                    openBt();
                } else {
                    closeBt();
                }
            }
        });
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        if(blueadapter==null) {
            //表示手机不支持蓝牙
            btState.setText("设备不支持蓝牙");
            btSwitch.setEnabled(false);
        }

        if(blueadapter.isEnabled()) {
            btSwitch.setChecked(true);
            doDiscovry();
        } else {
            btSwitch.setChecked(false);
        }
    }

    private void openBt() {
        if (!blueadapter.isEnabled())
        //判断本机蓝牙是否打开
        {//如果没打开，则打开蓝牙
            blueadapter.enable();
        }
    }

    private void closeBt() {
        if(blueadapter.isDiscovering()) {
            blueadapter.cancelDiscovery();
        }
        if (blueadapter.isEnabled())
        //判断本机蓝牙是否打开
        {//如果没打开，则打开蓝牙
            blueadapter.disable();
        }

        list.clear();
        btRecyclerAdapter.notifyDataSetChanged();
    }

    private void doDiscovry() {
        if (blueadapter.isDiscovering()) {
            //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
            return;
        } else {
            blueadapter.startDiscovery();
        }

//        btState.setText("开始搜索蓝牙设备");
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Timber.d("bluetoothReceiver.....action:%s", action);
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                Timber.d("bluetoothReceiver.....state:%d", state);
                String msg = null;
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        btState.setText("蓝牙已打开");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btState.setText("蓝牙已打开");
                        doDiscovry();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        btState.setText("蓝牙已打开");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        btState.setText("蓝牙已关闭");
                        break;
                    default:
                        Timber.d("bluetoothReceiver.....state:%d", state);
                        break;
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Timber.d("设备名："+device.getName()+"\n" +"设备地址："+device.getAddress() + "\n");
                list.add(device);
                btRecyclerAdapter.notifyDataSetChanged();
//                deviceName.add("设备名："+device.getName()+"\n" +"设备地址："+device.getAddress() + "\n");//将搜索到的蓝牙名称和地址添加到列表。
//                arrayList.add( device.getAddress());//将搜索到的蓝牙地址添加到列表。
//                adapter.notifyDataSetChanged();//更新
            }
        }
    };

}
