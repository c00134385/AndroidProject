package com.hjq.demo.ui.act.frag;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.SerialManager;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SerialTestFragment extends MyLazyFragment {


    @BindView(R.id.device)
    Spinner device;

    @BindView(R.id.baud_rate)
    Spinner baudRate;

    @BindView(R.id.btn_open)
    Button btnOpen;

    @BindView(R.id.state)
    TextView tvState;

    @BindView(R.id.btn_send)
    Button btnSend;


    @BindView(R.id.EditTextReception)
    EditText recvEdit;

    String mDevice;
    String mBaudrate;

    private SerialPort mSerialPort = null;

    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_serial;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {

        String[] entries = SerialManager.getInstance().mSerialPortFinder.getAllDevices();
        if(null != entries) {
            for(int i = 0; i < entries.length; i++) {
                Timber.d("entries[%d]:%s", i, entries[i]);
            }

        }

        final String[] entryValues = SerialManager.getInstance().mSerialPortFinder.getAllDevicesPath();
        if(null != entryValues) {
            for(int i = 0; i < entryValues.length; i++) {
                Timber.d("entryValues[%d]:%s", i, entryValues[i]);
            }
        }

//        / 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, entries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        device.setAdapter(adapter);

        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Timber.d("");
                mDevice = entryValues[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mDevice = null;
            }
        });


        baudRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String[] baud = getResources().getStringArray(R.array.baudrates);
                Timber.d("baudrate:%s", baud[pos]);
                mBaudrate = baud[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        baudRate.setSelection(2);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null == mDevice) {
                    ToastUtils.show("请选择设备");
                    return;
                }

                if(null == mBaudrate) {
                    ToastUtils.show("请选择波特率");
                    return;
                }

                /* Open the serial port */
                try {
                    mSerialPort = new SerialPort(new File(mDevice), Integer.valueOf(mBaudrate), 0);
                    tvState.setText("打开设备成功");
                    mOutputStream = mSerialPort.getOutputStream();
                    mInputStream = mSerialPort.getInputStream();

                    mReadThread = new ReadThread();
                    mReadThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    tvState.setText("打开设备失败");
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        System.out.println("当前时间：" + sdf.format(date));
                String str = "time:" + sdf.format(date);

                Observable.just(str)
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                if(null != mOutputStream) {
                                    mOutputStream.write(s.getBytes());
                                }
                                return s;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribe();

            }
        });

    }

    @Override
    protected void initData() {

    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
//                        onDataReceived(buffer, size);
                        final String recv = new String(buffer, 0, size);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recvEdit.append(recv);
                            }
                        });

                        Timber.d("%s", recv);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class SendingThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    if (mOutputStream != null) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        System.out.println("当前时间：" + sdf.format(date));
                        mOutputStream.write(("time:" + sdf.format(date)).getBytes());
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null != mReadThread) {
            mReadThread.interrupt();
        }
    }
}
