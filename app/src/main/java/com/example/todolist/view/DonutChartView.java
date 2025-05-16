package com.example.todolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DonutChartView extends View {
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private RectF rectF;
    private float percentage = 71f; // Default percentage

    public DonutChartView(Context context) {
        super(context);
        init();
    }

    public DonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonutChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(30f);
        backgroundPaint.setColor(Color.parseColor("#EEEEEE"));

        foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(30f);
        foregroundPaint.setColor(Color.parseColor("#FF4444"));

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = (int) (foregroundPaint.getStrokeWidth() / 2);
        rectF.set(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background circle
        canvas.drawArc(rectF, 0, 360, false, backgroundPaint);

        // Draw progress arc
        float sweepAngle = 360 * percentage / 100;
        canvas.drawArc(rectF, -90, sweepAngle, false, foregroundPaint);
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        invalidate();
    }
}
