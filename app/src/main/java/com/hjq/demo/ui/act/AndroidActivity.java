package com.hjq.demo.ui.act;

import android.widget.ListView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.model.BasicModel;
import com.hjq.demo.model.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AndroidActivity extends MyActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private List<BasicModel> basicModels;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_android;
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
        basicModels = new ArrayList<>();
        basicModels.add(new BasicModel(getString(R.string.android_version), MachineManager.getInstance().getAndroidVersion()));
        basicModels.add(new BasicModel(getString(R.string.firmware_version), MachineManager.getInstance().getFirmwareVersion()));
        basicModels.add(new BasicModel(getString(R.string.firmware_info), ""));

        MyAdapter myAdapter = new MyAdapter(this, basicModels);
        listView.setAdapter(myAdapter);
    }
}
