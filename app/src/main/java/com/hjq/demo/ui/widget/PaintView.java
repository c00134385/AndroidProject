package com.hjq.demo.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.hjq.demo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaintView extends ConstraintLayout {

    private List<Point> allPoint = new ArrayList<Point>();

    private Button btnClear;
    private Button btnExit;

    Paint p;
    private Path path;

    public PaintView(Context context) {
        this(context, null, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initStyle(attrs);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_view_paint, this);
        btnExit = v.findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allPoint.clear();
                setVisibility(GONE);
            }
        });

        btnClear = v.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                allPoint.clear();
                postInvalidate();
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == btnExit.getId()) {
                    return false;
                } else {
                    //Point类记录当前的X和Y坐标
                    Point p = new Point((int)event.getX(),(int)event.getY());
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {  //判断抬起
                        allPoint = new ArrayList<Point>();  //开始新的记录
                        allPoint.add(p);   //记录坐标点
                        path.moveTo(event.getX(), event.getY());
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        allPoint.add(p);   //记录坐标点
                    } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                        allPoint.add(p);   //记录坐标点
//                        postInvalidate();  //重绘
                        path.lineTo(event.getX(), event.getY());
                    }
                    postInvalidate();  //重绘

                    return true;
                }
            }
        });

        p = new Paint();
        p.setColor(Color.RED);   //设置颜色
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    private void initStyle(AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

       /* if(allPoint.size()>1) {
            Iterator<Point> iter = allPoint.iterator();
            Point first = null;
            Point last = null;
            while(iter.hasNext()) {    //迭代输出
                if(first == null) {
                    first = (Point) iter.next();
                } else {
                    if(last != null) {
                        first = last;       //修改起始点
                    }
                    last = (Point) iter.next();   //结束点
                    canvas.drawLine(first.x,first.y,last.x,last.y,p);
                }
            }
        }*/
       if(!path.isEmpty()) {
           canvas.drawPath(path, p);
       }

        super.onDraw(canvas);
    }
}
