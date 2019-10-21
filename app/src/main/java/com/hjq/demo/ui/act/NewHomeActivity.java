package com.hjq.demo.ui.act;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.adapter.FunctionAdapter;
import com.hjq.demo.adapter.FunctionItem;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.service.DeviceUploadService;
import com.hjq.demo.ui.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class NewHomeActivity extends MyActivity {

    @BindView(R.id.tv_info)
    TextView tvInfo;

    @BindView(R.id.fun_recyclerView)
    RecyclerView funRecyclerView;

    @BindView(R.id.btn_exit)
    Button btnExit;


    private FunctionAdapter functionAdapter;
    private List<FunctionItem> functionItems = new ArrayList<>();

    private long startTouchTime;
    private int validTouchCount;

    int width;
    int height;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_new;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3);
        funRecyclerView.setLayoutManager(glm);
        funRecyclerView.addItemDecoration(new SpaceItemDecoration(5, 5));

        functionAdapter = new FunctionAdapter(functionItems);
        functionAdapter.setOnItemChildClickListener(onItemChildClickListener);
        funRecyclerView.setAdapter(functionAdapter);
        if (null != getTitleBar()) {
            TitleBar titleBar = getTitleBar();
            titleBar.setOnTitleBarListener(new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    Timber.d("onLeftClick v:%s", v.getClass().getSimpleName());
                }

                @Override
                public void onTitleClick(View v) {
                    Timber.d("onTitleClick v:%s", v.getClass().getSimpleName());
                    processHideInfo();
                }

                @Override
                public void onRightClick(View v) {
                    Timber.d("onRightClick v:%s", v.getClass().getSimpleName());
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Timber.d("action:%d x:%f y:%f", event.getAction(), event.getX(), event.getY());
        if (event.getAction() == MotionEvent.ACTION_DOWN && (event.getX() > width / 2) && (event.getY() > height / 2)) {
            processHideInfo();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, DeviceUploadService.class);
        startService(intent);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;

        functionItems.add(new FunctionItem(getString(R.string.machine), R.drawable.ic_fun_machine));
        functionItems.add(new FunctionItem(getString(R.string.network), R.drawable.ic_fun_network));
        functionItems.add(new FunctionItem(getString(R.string.android), R.drawable.ic_fun_android));
        functionItems.add(new FunctionItem(getString(R.string.hardware), R.drawable.ic_fun_hardware));
        functionItems.add(new FunctionItem(getString(R.string.gotest), R.drawable.ic_fun_test));
        functionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void processHideInfo() {

        if (System.currentTimeMillis() - startTouchTime > TimeUnit.SECONDS.toMillis(2)) {
            validTouchCount = 0;
        }

        if (validTouchCount == 0) {
            startTouchTime = System.currentTimeMillis();
        }
        validTouchCount++;

        if (validTouchCount >= 5 && System.currentTimeMillis() - startTouchTime < TimeUnit.SECONDS.toMillis(2)) {
//            if(findViewById(R.id.cv_test).getVisibility() != View.VISIBLE) {
//                findViewById(R.id.cv_test).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.cv_test).setVisibility(View.INVISIBLE);
//            }
            validTouchCount = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        validTouchCount = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick({R.id.btn_exit})
    public void UiClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exit:
                finish();
                break;
        }
    }

    private BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position){
                case 0:
                    startActivity(MachineActivity.class);
                    break;
                case 1:
                    startActivity(NetworkActivity.class);
                    break;
                case 2:
                    startActivity(AndroidActivity.class);
                    break;
                case 3:
                    startActivity(HardwareActivity.class);
                    break;
                case 4:
                    startActivity(NewTestHomeActivity.class);
                    break;
            }
        }
    };
}
