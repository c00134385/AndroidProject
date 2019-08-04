package com.hjq.demo.ui.act.frag;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.Prints;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.USBPrinting;
import com.lvrenyang.io.base.IOCallBack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class PrinterTestFragment extends MyLazyFragment implements IOCallBack {

    public static int nPrintWidth = 384;
    public static boolean bCutter = false;
    public static boolean bDrawer = false;
    public static boolean bBeeper = true;
    public static int nPrintCount = 1;
    public static int nCompressMethod = 0;
    public static boolean bAutoPrint = false;
    public static int nPrintContent = 1;

//    private ScanGun mScanGun = null;

//    @BindView(R.id.btn_connect)
//    Button btnConnect;

    @BindView(R.id.buttonDisconnect)
    Button btnDisconnect;

    @BindView(R.id.buttonPrint)
    Button btnPrint;

    @BindView(R.id.linearlayoutdevices)
    LinearLayout linearlayoutdevices;

//    Button btnDisconnect,btnPrint;

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    USBPrinting mUsb = new USBPrinting();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_printer;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        btnDisconnect.performClick();
    }

    @OnClick({R.id.buttonDisconnect, R.id.buttonPrint})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDisconnect:
                es.submit(new TaskClose(mUsb));
                break;

            case R.id.buttonPrint:
                btnPrint.setEnabled(false);
                es.submit(new TaskPrint(mPos));
                break;
        }
    }

    @Override
    protected void initData() {
        btnDisconnect.setEnabled(false);
        btnPrint.setEnabled(false);

        mPos.Set(mUsb);
        mUsb.SetCallBack(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            probe();
        } else {
            finish();
        }
    }

    private void conntect() {
        Timber.d("conntect");

    }

    private void probe() {
        linearlayoutdevices.removeAllViews();
        final UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceList.size() > 0) {
            // 初始化选择对话框布局，并添加按钮和事件

            while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
                final UsbDevice device = deviceIterator.next();
                //Toast.makeText( this, device.toString(), Toast.LENGTH_SHORT).show();

                Button btDevice = new Button(
                        linearlayoutdevices.getContext());
                btDevice.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btDevice.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                btDevice.setText(String.format(" VID:%04X PID:%04X",
                        device.getVendorId(), device.getProductId()));
                btDevice.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        PendingIntent mPermissionIntent = PendingIntent
                                .getBroadcast(
                                        getContext(),
                                        0,
                                        new Intent(
                                                getContext()
                                                        .getApplicationInfo().packageName),
                                        0);

                        if (!mUsbManager.hasPermission(device)) {
                            mUsbManager.requestPermission(device,
                                    mPermissionIntent);
                            Toast.makeText(getContext(),
                                    "没有权限", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(mActivity, "Connecting...", Toast.LENGTH_SHORT).show();
                            linearlayoutdevices.setEnabled(false);
                            for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                            {
                                Button btn = (Button)linearlayoutdevices.getChildAt(i);
                                btn.setEnabled(false);
                            }
                            btnDisconnect.setEnabled(false);
                            btnPrint.setEnabled(false);
                            es.submit(new TaskOpen(mUsb,mUsbManager,device,mActivity));
                            //es.submit(new TaskTest(mPos,mUsb,mUsbManager,device,mActivity));
                        }
                    }
                });
                linearlayoutdevices.addView(btDevice);
            }
        }
    }

    public class TaskTest implements Runnable
    {
        Pos pos = null;
        USBPrinting usb = null;
        UsbManager usbManager = null;
        UsbDevice usbDevice = null;
        Context context = null;

        public TaskTest(Pos pos, USBPrinting usb, UsbManager usbManager, UsbDevice usbDevice, Context context)
        {
            this.pos = pos;
            this.usb = usb;
            this.usbManager = usbManager;
            this.usbDevice = usbDevice;
            this.context = context;
            pos.Set(usb);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            for(int i = 0; i < 1000; ++i)
            {
                long beginTime = System.currentTimeMillis();
                if(usb.Open(usbManager,usbDevice,context))
                {
                    long endTime = System.currentTimeMillis();
                    pos.POS_S_Align(0);
                    pos.POS_S_TextOut(i+ "\t" + "Open\tUsedTime:" + (endTime - beginTime) + "\r\n", 0, 0, 0, 0, 0);
                    beginTime = System.currentTimeMillis();
                    int ticketResult = pos.POS_TicketSucceed(i, 30000);
                    endTime = System.currentTimeMillis();
                    pos.POS_S_TextOut(i+ "\t" + "Ticket\tUsedTime:" + (endTime - beginTime) + "\t" + (ticketResult == 0 ? "Succeed" : "Failed") +  "\r\n", 0, 0, 0, 0, 0);
                    pos.POS_CutPaper();
                    usb.Close();
                }
            }
        }
    }

    public class TaskOpen implements Runnable
    {
        USBPrinting usb = null;
        UsbManager usbManager = null;
        UsbDevice usbDevice = null;
        Context context = null;

        public TaskOpen(USBPrinting usb, UsbManager usbManager, UsbDevice usbDevice, Context context)
        {
            this.usb = usb;
            this.usbManager = usbManager;
            this.usbDevice = usbDevice;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Open(usbManager,usbDevice,context);
        }
    }

    static int dwWriteIndex = 1;
    public class TaskPrint implements Runnable
    {
        Pos pos = null;

        public TaskPrint(Pos pos)
        {
            this.pos = pos;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final int bPrintResult = Prints.PrintTicket(getContext(), pos, nPrintWidth, bCutter, bDrawer, bBeeper, nPrintCount, nPrintContent, nCompressMethod);
            final boolean bIsOpened = pos.GetIO().IsOpened();

            mActivity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
                    btnPrint.setEnabled(bIsOpened);
                }
            });

        }

    }

    public class TaskClose implements Runnable
    {
        USBPrinting usb = null;

        public TaskClose(USBPrinting usb)
        {
            this.usb = usb;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Close();
        }
    }

    @Override
    public void OnOpen() {
// TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnDisconnect.setEnabled(true);
                btnPrint.setEnabled(true);
                linearlayoutdevices.setEnabled(false);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(false);
                }
                Toast.makeText(mActivity, "Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
// TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                linearlayoutdevices.setEnabled(true);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
                Toast.makeText(mActivity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
// TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable(){

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                linearlayoutdevices.setEnabled(true);
                for(int i = 0; i < linearlayoutdevices.getChildCount(); ++i)
                {
                    Button btn = (Button)linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
                probe(); // 如果因为打印机关机导致Close。那么这里需要重新枚举一下。
            }
        });
    }

}
