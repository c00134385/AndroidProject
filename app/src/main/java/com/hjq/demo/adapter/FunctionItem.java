package com.hjq.demo.adapter;

/**
 * Created by duhang on 2019/9/21 0021 09:54.
 * com.hjq.demo.adapter
 */
public class FunctionItem {

    private String name;
    private int imgResId;
    private boolean visiable;

    public FunctionItem(String name, int imgResId, boolean visiable) {
        this.name = name;
        this.imgResId = imgResId;
        this.visiable = visiable;
    }

    public FunctionItem(String name, int imgResId) {
        this(name, imgResId, true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    @Override
    public String toString() {
        return "FunctionItem{" +
                "name='" + name + '\'' +
                ", imgResId=" + imgResId +
                '}';
    }
}
