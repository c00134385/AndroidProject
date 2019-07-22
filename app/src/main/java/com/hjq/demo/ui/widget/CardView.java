package com.hjq.demo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.utils.DisplayUtil;

public class CardView extends ConstraintLayout {

    private Context context;
    private TextView tvTitle;
    private TextView tvStatus;

    private int state = 0;

    public CardView(Context context) {
        super(context);
        init(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttr(attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttr(attrs);
    }

    private void init(Context context) {
        this.context = context;

        tvTitle = new TextView(context);
        tvTitle.setTextSize(getResources().getDimension(R.dimen.font_size_76px));  // 方法2
        addView(tvTitle);
        LayoutParams layoutParams = (LayoutParams)tvTitle.getLayoutParams();
        layoutParams.leftToLeft = ConstraintSet.PARENT_ID;
        layoutParams.rightToRight = ConstraintSet.PARENT_ID;
        layoutParams.topToTop = ConstraintSet.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        layoutParams.horizontalBias = 0.3f;
        tvTitle.setLayoutParams(layoutParams);
//
        tvStatus = new TextView(context);
        tvStatus.setTextSize(getResources().getDimension(R.dimen.font_size_40px));
        addView(tvStatus);

        layoutParams = (LayoutParams)tvStatus.getLayoutParams();
        layoutParams.rightToRight = ConstraintSet.PARENT_ID;
        layoutParams.topToTop = ConstraintSet.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        layoutParams.rightMargin = DisplayUtil.dip2px(context, getResources().getDimension(R.dimen.space_40));
        tvStatus.setLayoutParams(layoutParams);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CardView, 0, 0);
        String tvTitleStr = typedArray.getString(R.styleable.CardView_title);
        tvTitle.setText(tvTitleStr);
        String tvStateStr = typedArray.getString(R.styleable.CardView_status);
        tvStatus.setText(tvStateStr);
        state = typedArray.getInt(R.styleable.CardView_state, 0);
        typedArray.recycle();
    }

    public void updateState() {
        setState(state + 1);
    }

    public void setState(int state) {
        if(state >= 3) {
            state = 0;
        }

        this.state = state;
        final int finalState = state;
        post(new Runnable() {
            @Override
            public void run() {
                switch (finalState) {
                    case 0:
                        setBackgroundResource(R.drawable.content_bg);
                        break;
                    case 1:
                        setBackgroundResource(R.drawable.content_bg1);
                        break;
                    case 2:
                        setBackgroundResource(R.drawable.content_bg2);
                        break;
                }
            }
        });
    }
}
