package com.hjq.demo.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hjq.demo.R;

public class Paint2View extends ConstraintLayout {


    private Point currPoint;
    private TextView point_pos;
    Paint p;

    public Paint2View(Context context) {
        this(context, null, 0);
    }

    public Paint2View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Paint2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(attrs);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_view_paint2, this);
        point_pos = v.findViewById(R.id.pos_info);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Point类记录当前的X和Y坐标
                Point p = new Point((int)event.getX(),(int)event.getY());
                if(event.getAction() == MotionEvent.ACTION_DOWN) {  //判断抬起
                    currPoint = p;
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    currPoint = null;
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
//                        postInvalidate();  //重绘
                    currPoint = p;
                }
                if(null != currPoint) {
                    point_pos.setText("X: " + event.getX() +  "\nY: " + event.getY()+ "");
                }
                postInvalidate();  //重绘
                return true;
            }
        });

        p = new Paint();
        p.setColor(Color.RED);   //设置颜色
    }

    private void initStyle(AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(null != currPoint) {
            canvas.drawLine(currPoint.x,0,currPoint.x,this.getHeight(), p);
            canvas.drawLine(0, currPoint.y, this.getWidth(), currPoint.y, p);
        }

        super.onDraw(canvas);
    }
}
