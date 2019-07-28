package com.hjq.demo.ui.act.frag;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.utils.NetworkUtils;
import com.hjq.widget.SwitchButton;

import java.lang.reflect.Method;

import butterknife.BindView;
import timber.log.Timber;

public class ForgTestFragment extends MyLazyFragment {

    @BindView(R.id.switch_btn)
    SwitchButton switchButton;

    @BindView(R.id.tv_state)
    TextView tvState;

    TelephonyManager teleManager;
    ConnectivityManager connectivityManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_4g;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean isChecked) {
                if(isChecked) {
                    //
                } else {
                    //
                }
            }
        });
    }

    @Override
    protected void initData() {
        teleManager = (TelephonyManager) getSystemService(getContext().TELEPHONY_SERVICE);
        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo networkInfo:networkInfos) {
            Timber.d("type:%d name:%s", networkInfo.getType(), networkInfo.getTypeName());
        }

        Timber.d("Sim:%b", NetworkUtils.hasSimCard(getContext()));

        if(NetworkUtils.hasSimCard(getContext())) {
            tvState.setText("有SIM卡");
        } else {
            tvState.setText("无SIM卡");
        }
    }


//    public static void setDataEnabled(int slotIdx, boolean enable, Context context) throws Exception
//    {
//        try {
//            int subid = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(slotIdx).getSubscriptionId();
//            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            Method setDataEnabled = telephonyService.getClass().getDeclaredMethod("setDataEnabled", int.class, boolean.class);
//            if (null != setDataEnabled) {
//                setDataEnabled.invoke(telephonyService, subid, enable);
////                LogUtil.LOGD(TAG,"setDataEnabled suc",false);
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
////            LogUtil.LOGD(TAG,"setDataEnabled exception",false);
//        }
//    }
//
//    public static boolean getDataEnabled(int slotIdx,Context context) throws Exception {
//        boolean enabled = false;
//        try {
//            int subid = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(slotIdx).getSubscriptionId();
//            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            Method getDataEnabled = telephonyService.getClass().getDeclaredMethod("getDataEnabled", int.class);
//            if (null != getDataEnabled) {
//                enabled = (Boolean) getDataEnabled.invoke(telephonyService, subid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return enabled;
//    }


}
