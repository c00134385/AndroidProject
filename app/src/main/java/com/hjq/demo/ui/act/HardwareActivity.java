package com.hjq.demo.ui.act;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjq.demo.R;
import com.hjq.demo.adapter.ListAdapter;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.HardwareManager;
import com.hjq.demo.model.BasicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HardwareActivity extends MyActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BasicModel> basicModels;
    private ListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hardware;
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
        basicModels.add(new BasicModel(getString(R.string.screen_resolve), HardwareManager.getInstance().getScreenResolve()));
        basicModels.add(new BasicModel(getString(R.string.screen_size), HardwareManager.getInstance().getScreenSize()));
        basicModels.add(new BasicModel(getString(R.string.camera_front), HardwareManager.getInstance().getCameraFront()));
        basicModels.add(new BasicModel(getString(R.string.camera_back), HardwareManager.getInstance().getCameraBack()));
        basicModels.add(new BasicModel(getString(R.string.memory), HardwareManager.getInstance().getMemory()));
        basicModels.add(new BasicModel(getString(R.string.flash), HardwareManager.getInstance().getFlash()));
        basicModels.add(new BasicModel(getString(R.string.cpu), HardwareManager.getInstance().getCpu()));
        basicModels.add(new BasicModel(getString(R.string.cpu_hz), HardwareManager.getInstance().getCpuHz()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        listAdapter = new ListAdapter(basicModels);
        recyclerView.setAdapter(listAdapter);
    }
}
