package com.example.moneytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DiagramView extends View {
    private int income;
    private int expense;
    private Paint incomePaint = new Paint();
    private Paint expensePaint = new Paint();

    public void update  (int income, int expense){
        this.income = income;
        this.expense = expense;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPieDiagram(canvas);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }

    private void drawPieDiagram (Canvas canvas){
        if (expense + income == 0){
            return;
        }

        float expenseAngle = 360.f*expense/(expense + income);
        float incomeAngle = 360.f * income/(income + expense);
        int space = 5;
        int size = Math.min(canvas.getWidth(), canvas.getHeight()) - space * 2;
        final int xMargin = (canvas.getWidth() - size)/2, yMargin = (canvas.getHeight() - size)/2;

        canvas.drawArc(xMargin - space, yMargin, canvas.getWidth() - xMargin - space, canvas.getHeight() - yMargin, 180 - expenseAngle/2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, canvas.getWidth() - xMargin + space, canvas.getHeight() - yMargin, 360 - incomeAngle/2, incomeAngle, true, incomePaint);
    }
//TODO Сделать переключатель между диаграммами
    private void drawRectDiagram (Canvas canvas){
        if (expense + income == 0){
            return;
        }

        long max = Math.max(expense, income);
        long expenseHeight = canvas.getHeight()*expense/max;
        long incomeHeight = canvas.getHeight()*income/max;
        int w = getWidth()/4;
        canvas.drawRect(w/2, canvas.getHeight() - expenseHeight, w*3/2, canvas.getHeight(), expensePaint);
        canvas.drawRect(5*w/2, canvas.getHeight() - incomeHeight, w*7/2, canvas.getHeight(), incomePaint);
    }

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        incomePaint.setColor(getResources().getColor(R.color.balance_income));
        expensePaint.setColor(getResources().getColor(R.color.balance_expense));
    }
}
