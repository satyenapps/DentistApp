package com.appslight.dentistapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.appslight.dentistapp.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satyen on 19/05/2015.
 */
public class DrawingView extends View {

    public MainActivity mainActivity;
    public Paint mPaint;
    //MaskFilter  mEmboss;
    //MaskFilter  mBlur;
    Bitmap mBitmap;
    Canvas mCanvas;
    Path mPath;
    Rect mRect;
    Paint mBitmapPaint;

    public boolean eraserMode = false;
    public boolean touched = false;
    float x, y;
    List<Point> rectPoints = new ArrayList<>();

    public DrawingView(Context context, MainActivity mainActivity) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mainActivity = mainActivity;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        mPath = new Path();
        mRect = new Rect();
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        //canvas.drawColor(Color.WHITE);
        if (touched) {
            canvas.drawRect(x - 50f, y - 25f, x + 100f, y + 50f, mPaint);
        }
        /* working code
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
       // if (!eraserMode) {
            canvas.drawPath(mPath, mPaint);
        //} else {
           // mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        //}*/
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        // kill this so we don't double draw
        mPath.reset();
        // mPath= new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        //touched = true;

        if (touched) {

            //mRect.set(Math.round(x - 50f), Math.round(y - 25f), Math.round(x + 100f), Math.round(y + 50f));
            rectPoints.add(new Point(Math.round(x - 50f), Math.round(y - 25f)));
            //mCanvas.drawRect(mRect, mPaint);
            mainActivity.addView(Math.round(x), Math.round(y));
            for (int p = 0; p < rectPoints.size(); p++) {
                Point point = rectPoints.get(p);
                mCanvas.drawRect(point.x, point.y, x + 100f, y + 50f, mPaint);
            }
            invalidate();
            //mRect.offset(0, 0);
            return true;
        }
        return false;
        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;*/
    }
}
