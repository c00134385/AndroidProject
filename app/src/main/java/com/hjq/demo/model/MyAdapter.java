package com.hjq.demo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hjq.demo.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<BasicModel> basicModelList;

    public MyAdapter(Context context, List<BasicModel> basicModelList) {
        this.context = context;
        this.basicModelList = basicModelList;
    }

    @Override
    public int getCount() {
        if(null != basicModelList) {
            return basicModelList.size();
        }
        return 0;
    }

    @Override
    public BasicModel getItem(int i) {
        return basicModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView==null) {
            //因为getView()返回的对象，adapter会自动赋给ListView
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_list_item, null);;
            holder.key = convertView.findViewById(R.id.item_key);
            holder.value = convertView.findViewById(R.id.item_value);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.key.setText(basicModelList.get(i).getKey());
        holder.value.setText(basicModelList.get(i).getValue());
        return convertView;
    }

    private final class ViewHolder{
        private TextView key;
        private TextView value;
    }
}
