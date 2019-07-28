package com.hjq.demo.ui.act.frag;

import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.widget.SwitchButton;

import butterknife.BindView;

public class LanTestFragment extends MyLazyFragment {

    @BindView(R.id.switch_btn)
    SwitchButton switchButton;

    @BindView(R.id.tv_state)
    TextView tvState;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_lan;
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

    }
}
