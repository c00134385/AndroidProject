package com.hjq.demo.ui.act.frag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.ICheckListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

import butterknife.BindView;

public class KeyboardTestFragment extends MyLazyFragment {

    private Handler handler = new Handler();

    @BindView(R.id.eventLog)
    EditText eventLog;

    private PayKeyboard keyboard;

    @BindView(R.id.wifi)
    EditText  wifi;

    @BindView(R.id.baudrate)
    EditText baudrate;

    @BindView(R.id.gprs)
    EditText gprs;

    private USBDetector detector;

    @BindView(R.id.layoutList)
    Spinner spinner;

    @BindView(R.id.btn_clear)
    Button btnClear;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_keyboard;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        eventLog.setClickable(false);
        eventLog.setFocusable(false);
        eventLog.setFocusableInTouchMode(false);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventLog.setText("");
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        detector =  PayKeyboard.getDetector(getContext());
        detector.setListener(new ICheckListener() {
            @Override
            public void onAttach() {
                openKeyboard();
            }
        });
        openKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(keyboard!=null){
            // keyboard.release();
            keyboard.release();
            keyboard=null;

        }
        if(detector!=null){
            detector.release();
            detector = null;
        }
    }

    public void updateSignal(){
        int w = 0;
        int g = 0;
        if(wifi.getText().length()>0) w =  Integer.parseInt(wifi.getText().toString());
        if(gprs.getText().length()>0) g = Integer.parseInt(gprs.getText().toString());

        if(keyboard!=null && !keyboard.isReleased()) keyboard.updateSign(w,g);
    }


    private void openKeyboard(){
        if(keyboard==null||keyboard.isReleased()){
            keyboard = PayKeyboard.get(getContext());
            if(keyboard!=null) {
                if(spinner.getSelectedItemPosition()>=0) keyboard.setLayout(spinner.getSelectedItemPosition());
                if(baudrate.getText().length()>0){
                    keyboard.setBaudRate(Integer.parseInt(baudrate.getText().toString()));
                }
                keyboard.setListener(new DefaultKeyboardListener() {
                    @Override
                    public void onRelease() {
                        super.onRelease();
                        keyboard = null;
                        Log.i("KeyboardUI", "Keyboard release!!!!!!");
                    }

                    @Override
                    public void onAvailable() {
                        super.onAvailable();
                        // keyboard.updateSign(PayKeyboard.SIGN_TYPE_W,4);
                        updateSignal();
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.i("KeyboardUI", "usb exception!!!!");
                        keyboard = null;
                        super.onException(e);
                    }

                    @Override
                    public void onPay(final IPayRequest request) {
                        super.onPay(request);
                        request.setResult(true);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                final AlertDialog.Builder normalDialog =
//                                        new AlertDialog.Builder(getContext());
//                                normalDialog.setTitle("支付提示");
//                                normalDialog.setMessage(String.format("请支付 %.2f 元", request.getMoney()));
//                                normalDialog.setPositiveButton("支付成功",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                request.setResult(true);
//                                            }
//                                        });
//                                normalDialog.setNegativeButton("支付失败",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                request.setResult(false);
//                                            }
//                                        });
//                                normalDialog.show();
//                            }
//                        });
                    }

                    @Override
                    public void onKeyDown(final int keyCode, final String keyName) {
                        super.onKeyDown(keyCode, keyName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                eventLog.setMovementMethod(ScrollingMovementMethod.getInstance());
                                eventLog.setSelection(eventLog.getText().length(), eventLog.getText().length());
                                eventLog.getText().append(String.format("key down event code : %s, name: %s \n ", keyCode, keyName));
                            }
                        });


                    }


                    @Override
                    public void onKeyUp(int keyCode, String keyName) {
                        super.onKeyUp(keyCode, keyName);
                    }
                });
                keyboard.open();
            }
        }else{
            Log.i("KeyboardUI","keyboard exists!!!");
        }
    }
}
