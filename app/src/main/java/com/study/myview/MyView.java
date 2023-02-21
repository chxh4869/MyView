package com.study.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

public class MyView extends View {


    float iconSize;
    String lineText = "";
    float linetextSize;
    Rect textRect;
    int lineTextGravity;
    float lineTextMargin;
    int lintTextColor = Color.BLACK;
    int lineTextStyle = 0;
    float lineTextStrokeSize = 0;
    int linePrestenVisable = 0;


    int lineColor = Color.BLACK;
    float lineLength = 0;
    float lineHight;

    int iconId;
    float origanalXPoin = 0;
    float monveXPoint = 0;
    float downXPoint = 0;

    class LineTextGravity {
        public static final int TOP = 1;
        public static final int BOTTOM = 2;
    }

    Bitmap iconBitmap;
    Paint paint;
    TextPaint textPaint;

    public int getPrecent() {
        return precent;
    }

    int precent;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
            lineHight = typedArray.getDimension(R.styleable.MyView_lineSize, 10f);
            lineText = typedArray.getNonResourceString(R.styleable.MyView_lineText);
            linetextSize = typedArray.getDimension(R.styleable.MyView_linetextSize, 20f);
            textRect = measureText(lineText + ":0%", linetextSize, 4);
            lineColor = typedArray.getColor(R.styleable.MyView_lineColor, Color.GREEN);
            lineLength = typedArray.getDimension(R.styleable.MyView_lineLength, 60f);
            iconId = typedArray.getResourceId(R.styleable.MyView_lineicon, R.drawable.thumb_green);
            iconSize = typedArray.getDimension(R.styleable.MyView_iconSize, lineHight * 2);
            lineTextGravity = typedArray.getInt(R.styleable.MyView_lineTextGravity, LineTextGravity.TOP);
            lineTextMargin = typedArray.getDimension(R.styleable.MyView_lineTextMargin, 0f);
            lintTextColor = typedArray.getColor(R.styleable.MyView_lineTextColor,Color.BLACK);
            lineTextStyle = typedArray.getInt(R.styleable.MyView_lineTextStyle,0);
            lineTextStrokeSize = typedArray.getDimension(R.styleable.MyView_lineTextStrokeSize,1);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intiPaint();
    }

    private void intiPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineHight);
        paint.setColor(lineColor);

        textPaint = new TextPaint();
        switch (lineTextStyle){
            case 0:
            default:
                textPaint.setStyle(Paint.Style.FILL);
                break;
            case 1:
                textPaint.setStyle(Paint.Style.STROKE);
                textPaint.setStrokeWidth(lineTextStrokeSize);
                break;
            case 2:
                textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                textPaint.setStrokeWidth(lineTextStrokeSize);
                break;
        }

        textPaint.setTextSize(linetextSize);
        textPaint.setColor(lintTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int model = MeasureSpec.getMode(widthMeasureSpec);
        int sp = MeasureSpec.getSize(widthMeasureSpec);
        if (model == MeasureSpec.UNSPECIFIED) {

        } else if (model == MeasureSpec.EXACTLY) {

        } else if (model == MeasureSpec.AT_MOST && getParent() instanceof View && ((View) getParent()).getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int) lineLength * 2 + (int) iconSize
                    , MeasureSpec.AT_MOST);
            if (MeasureSpec.getSize(widthMeasureSpec) > sp) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(sp, MeasureSpec.EXACTLY);
                lineLength = (sp - (iconSize)) / 2;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (textRect.height() * 2 + iconSize + lineTextMargin + 40), MeasureSpec.AT_MOST);
        } else if (model == MeasureSpec.AT_MOST && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int) lineLength * 2 + (int) iconSize
                    , MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (textRect.height() * 2 + iconSize + lineTextMargin +40), MeasureSpec.AT_MOST);
        }
        origanalXPoin = getMeasuredWidth() / 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (iconBitmap == null) {
            Drawable drawable = getResources().getDrawable(iconId, null);
            if (drawable != null) {
                iconBitmap = Bitmap.createBitmap((int) iconSize, (int) iconSize, Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(iconBitmap);
                drawable.setBounds(new Rect(0, 0, (int) iconSize, (int) iconSize));
                drawable.draw(canvas1);
            }
        }
        canvas.drawLine(getMeasuredWidth() / 2 - lineLength, getMeasuredHeight() / 2, getMeasuredWidth() / 2 + lineLength, getMeasuredHeight() / 2, paint);

        canvas.drawBitmap(iconBitmap, getMeasuredWidth() / 2 - iconBitmap.getWidth() / 2 - precent * lineLength / 100, getMeasuredHeight() / 2 - iconBitmap.getHeight() / 2, paint);

        String drawText = lineText + ":" + precent + "%";
        //textRect = measureText(drawText, textSize, 2);
        //paint.setTextSize(textSize);

        if (lineTextGravity == LineTextGravity.TOP) {
            canvas.drawText(drawText, getMeasuredWidth() / 2 - (textRect.left + textRect.right) / 2, getMeasuredHeight() / 2 - iconBitmap.getHeight() / 2 - textRect.bottom - lineTextMargin, textPaint);
        } else {
            canvas.drawText(drawText, getMeasuredWidth() / 2 - (textRect.left + textRect.right) / 2, getMeasuredHeight() / 2 + iconBitmap.getHeight() / 2 - textRect.top + lineTextMargin, textPaint);
        }
    }

    public static Rect measureText(String text, float textSize, int StrokeWidth) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(StrokeWidth);
        Rect textRect = new Rect();
        paint.setTextSize(textSize);

        paint.getTextBounds(text, 0, text.length(), textRect);
        paint.reset();
        return textRect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION", String.format("Down x:%s  rawX:%s  y:%s  rawY:%s",
                        event.getX(), event.getRawX(), event.getY(), event.getRawY()));
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d("ACTION", String.format("MOVE x:%s  rawX:%s  y:%s  rawY:%s",
                        event.getX(), event.getRawX(), event.getY(), event.getRawY()));
                downXPoint = event.getX();
                monveXPoint = origanalXPoin - downXPoint;

                if (origanalXPoin - monveXPoint >= origanalXPoin + lineLength) {
                    monveXPoint = 0 - lineLength;
                } else if (origanalXPoin - monveXPoint <= origanalXPoin - lineLength) {
                    monveXPoint = lineLength;
                }

                double p = (monveXPoint / lineLength) * 100;

                BigDecimal d = new BigDecimal(p).setScale(0, BigDecimal.ROUND_HALF_UP);
                precent = d.intValue();
                Log.d("ppp", d.toPlainString() + "%");

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("ACTION", String.format("UP x:%s  rawX:%s  y:%s  rawY:%s",
                        event.getX(), event.getRawX(), event.getY(), event.getRawY()));
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("ACTION", String.format("CANCEL x:%s  rawX:%s  y:%s  rawY:%s",
                        event.getX(), event.getRawX(), event.getY(), event.getRawY()));
                break;
        }
        return super.onTouchEvent(event);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        MyViewSave myViewSave = new MyViewSave(parcelable);
        myViewSave.present = precent;
        return myViewSave;
    }

    @Nullable
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            MyViewSave ss = (MyViewSave) state;
            super.onRestoreInstanceState(state);
            precent = ss.present;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
