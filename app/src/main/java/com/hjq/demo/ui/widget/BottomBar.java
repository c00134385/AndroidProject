package com.hjq.demo.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.hjq.demo.R;

public class BottomBar extends FrameLayout implements View.OnClickListener {

    private Button btnLeft;
    private Button btnMiddle;
    private Button btnRight;
    private OnClickListener clickListener;

    public BottomBar(@androidx.annotation.NonNull Context context) {
        this(context, null);
    }

    public BottomBar(@androidx.annotation.NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(@androidx.annotation.NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(@androidx.annotation.NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initStyle(attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_bar, this);
        btnLeft = findViewById(R.id.btn_left);
        btnMiddle = findViewById(R.id.btn_middle);
        btnRight = findViewById(R.id.btn_right);
    }

    private void initStyle(AttributeSet attrs) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 设置监听
        btnLeft.setOnClickListener(this);
        btnMiddle.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        // 移除监听
        btnLeft.setOnClickListener(null);
        btnMiddle.setOnClickListener(null);
        btnRight.setOnClickListener(null);
        super.onDetachedFromWindow();
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if (getClickListener() == null) return;

        final int id = v.getId();
        if (id == R.id.btn_left) {
            getClickListener().onLeftClick(v);
        }else if (id == R.id.btn_middle) {
            getClickListener().onMiddleClick(v);
        }else if (id == R.id.btn_right) {
            getClickListener().onRightClick(v);
        }
    }

    public interface OnClickListener {

        /**
         * 左项被点击
         *
         * @param v     被点击的左项View
         */
        void onLeftClick(View v);

        /**
         * 标题被点击
         *
         * @param v     被点击的标题View
         */
        void onMiddleClick(View v);

        /**
         * 右项被点击
         *
         * @param v     被点击的右项View
         */
        void onRightClick(View v);
    }
}
