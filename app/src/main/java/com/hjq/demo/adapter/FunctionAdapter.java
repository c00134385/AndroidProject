package com.hjq.demo.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.common.MyApplication;

import java.util.List;

/**
 * Created by duhang on 2019/9/21 0021 09:52.
 * com.hjq.demo.adapter
 */
public class FunctionAdapter extends BaseQuickAdapter<FunctionItem, BaseViewHolder> {

    public FunctionAdapter(@Nullable List<FunctionItem> data) {
        super(R.layout.item_function, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionItem item) {
        helper.setText(R.id.fun_name, item.getName());
        helper.setImageDrawable(R.id.fun_img, MyApplication.context().getResources().getDrawable(item.getImgResId()));
        helper.addOnClickListener(R.id.fun_item_layout);

        if(item.isVisiable()) {
            helper.itemView.setVisibility(View.VISIBLE);
        } else {
            helper.itemView.setVisibility(View.GONE);
        }
    }
}
