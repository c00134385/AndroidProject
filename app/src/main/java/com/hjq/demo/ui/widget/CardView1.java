package com.hjq.demo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;

import timber.log.Timber;

public class CardView1 extends ConstraintLayout{

    private Context context;
    private ImageView icon;
    private TextView title;

    public CardView1(Context context) {
//        super(context);
        this(context, null, 0);
    }

    public CardView1(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public CardView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
        initStyle(attrs);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_card_view1, this);
        icon = v.findViewById(R.id.icon);
        title = v.findViewById(R.id.title);
    }

    private void initStyle(AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CardView1, 0, 0);
        String tvTitleStr = typedArray.getString(R.styleable.CardView1_cv1_title);
        title.setText(tvTitleStr);
        int tvStateStr = typedArray.getResourceId(R.styleable.CardView1_cv1_icon, R.mipmap.ic_launcher);
        icon.setImageDrawable(getResources().getDrawable(tvStateStr));
        typedArray.recycle();
    }
}
