package com.hjq.demo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.demo.R;
import com.hjq.demo.model.BasicModel;

import java.util.List;

/**
 * Created by duhang on 2019/9/21 0021 09:52.
 * com.hjq.demo.adapter
 */
public class ListAdapter extends BaseQuickAdapter<BasicModel, BaseViewHolder> {

    public ListAdapter(@Nullable List<BasicModel> data) {
        super(R.layout.layout_list_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BasicModel item) {
        helper.setText(R.id.item_text_title, item.getKey() + "\b:");
        helper.setText(R.id.item_text, item.getValue());
    }
}
