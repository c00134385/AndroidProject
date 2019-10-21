package com.hjq.demo.ui.act;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.adapter.ListAdapter;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.model.BasicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AndroidActivity extends MyActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BasicModel> basicModels;
    private ListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_android;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        listAdapter = new ListAdapter(basicModels);
        recyclerView.setAdapter(listAdapter);
    }
}
