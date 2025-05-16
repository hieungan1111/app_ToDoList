package com.example.todolist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView extends View {
    private Paint barPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private float[] values = {7.5f, 8.0f, 9.5f, 6.5f, 8.5f, 2.5f, 0.0f};
    private float maxValue = 10.0f; // Maximum value for the chart

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.parseColor("#3F51B5")); // Primary color
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        gridPaint.setAntiAlias(true);
    }

    public void setValues(float[] values) {
        this.values = values;

        // Find the maximum value
        maxValue = 0;
        for (float value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        // Round up to the nearest whole number and add a little padding
        maxValue = (float) Math.ceil(maxValue) + 1;

        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (values == null || values.length == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        // Draw horizontal grid lines
        int numGridLines = 5;
        float gridSpacing = height * 0.8f / numGridLines;
        float topMargin = height * 0.1f;

        for (int i = 0; i <= numGridLines; i++) {
            float y = height - topMargin - (i * gridSpacing);
            canvas.drawLine(width * 0.1f, y, width * 0.9f, y, gridPaint);

            // Draw grid line labels (hours)
            float value = (i * maxValue) / numGridLines;
            String label = String.format("%.0f", value);
            canvas.drawText(label, width * 0.05f, y + 10, textPaint);
        }

        // Calculate bar width and spacing
        float barWidth = (width * 0.8f) / values.length;
        float spacing = barWidth * 0.2f;
        float usableBarWidth = barWidth - spacing;

        // Draw bars
        for (int i = 0; i < values.length; i++) {
            float value = values[i];
            float barHeight = (value / maxValue) * (height * 0.8f);

            float left = width * 0.1f + (i * barWidth) + (spacing / 2);
            float top = height - topMargin - barHeight;
            float right = left + usableBarWidth;
            float bottom = height - topMargin;

            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRect(rect, barPaint);

            // Draw value on top of the bar
            if (value > 0) {
                canvas.drawText(String.format("%.1f", value), left + usableBarWidth / 2, top - 10, textPaint);
            }
        }
    }
}
