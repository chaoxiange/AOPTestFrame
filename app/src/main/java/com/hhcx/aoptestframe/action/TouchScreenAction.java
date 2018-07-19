
package com.hhcx.aoptestframe.action;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hhcx.aoptestframe.helper.DBHelper;
import com.hhcx.aoptestframe.helper.SqlConstants;

import java.util.ArrayList;

public class TouchScreenAction extends Activity {
    private static final String TAG = "TouchScreenAction";
    ArrayList<EdgePoint> mArrayList;
    String resultString = "Failed";
    int mHightPix = 0, mWidthPix = 0, mRadius = 20, mStep = 40;
    float w = 0, h = 0;
    Context mContext;

    boolean isReCheck = false;
    boolean testResult = false;
    private String mainItem;
    DBHelper dbHelper;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this);

        initParameter();
        initUI();
        // // It must be common divisor of width and hight
        // mStep = getStep(mWidthPix, mHightPix);
        // mRadius = mStep / 2;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new Panel(this));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onResume();
    }


    @SuppressLint("NewApi")
    private void initParameter() {
        mContext = TouchScreenAction.this;
        // get panel size
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        mHightPix = point.y;
        mWidthPix = point.x;
        Log.e(TAG, "mHightPix = " + mHightPix + " mWidthPix = " + mWidthPix);
        mStep = 40;
        mRadius = 20;
        mainItem = TouchScreenAction.class.getSimpleName();
        dbHelper = DBHelper.getInstance();
    }

    @SuppressLint("InlinedApi")
    private void initUI() {
//             非android 15, 全屏通过沉浸模式实现, 新W1上可以使用
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


    }

    public class EdgePoint {

        int x;
        int y;
        boolean isChecked = false;

        public EdgePoint(int x, int y, boolean isCheck) {

            this.x = x;
            this.y = y;
            this.isChecked = isCheck;
        }

    }

    /**
     * 获取屏幕边缘的测试点坐标
     */
    public ArrayList<EdgePoint> getTestPoint() {

        ArrayList<EdgePoint> list = new ArrayList<EdgePoint>();

        for (int x = mRadius; x < mWidthPix + mRadius; x += mStep) {
            for (int y = mRadius; y < mHightPix + mRadius; y += mStep) {
                if (x > mRadius && x < mWidthPix - mRadius && y > mRadius
                        && y < mHightPix - mRadius)
                    continue;
                Log.e(TAG, "(x, y)" + "(" + x + ", " + y + ")");
                list.add(new EdgePoint(x, y, false));
            }
        }

        return list;

    }

    public ArrayList<EdgePoint> getTestPoint2() {

        ArrayList<EdgePoint> list = new ArrayList<EdgePoint>();
        int step = getStep2(mWidthPix, mHightPix);
        for (int i = 0; i < step; i++) {
            EdgePoint point = getPoint(i);
            if (hasPoint) {
                list.add(point);
            }
        }
        for (int i = 0; i < step; i++) {
            EdgePoint point = getPoint2(i);
            if (hasPoint) {
                list.add(point);
            }
        }
        return list;

    }

    private int getStep2(int width, int height) {
        int step = 0;
        double middle = Math.sqrt(width * width + height * height) / (mRadius * 2);
        step = (int) middle;
        if (middle > step) {
            step = step + 1;
        }
        return step;
    }

    private boolean hasPoint = true;

    /**
     * 获取从左上角到右下角的对角线
     */
    private EdgePoint getPoint(int i) {
        hasPoint = true;
        double scale = mHightPix * 1.0 / mWidthPix;
        double x = Math.sqrt(((i * mRadius * 2 + mRadius) * (i * mRadius * 2 + mRadius))
                / (scale * scale + 1));
        double y = x * scale;
        if (x < 0 || x > mWidthPix) {
            hasPoint = false;
        }
        EdgePoint point = new EdgePoint((int) x, (int) y, false);
        return point;
    }

    /**
     * 获取从右上角到左下角的对角线
     */
    private EdgePoint getPoint2(int i) {
        hasPoint = true;
        double scale = mHightPix * 1.0 / mWidthPix;
        double x = Math.sqrt(((i * mRadius * 2 + mRadius) * (i * mRadius * 2 + mRadius))
                / (scale * scale + 1));
        double y = x * scale;
        if (x < 0 || x > mWidthPix) {
            hasPoint = false;
        }
        EdgePoint point = new EdgePoint((int) (mWidthPix - x), (int) y, false);
        return point;
    }

    private void init(Context context) {

        mContext = context;
        resultString = "Failed";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    class Panel extends View {

        public static final int TOUCH_TRACE_NUM = 30;
        public static final int PRESSURE = 500;
        private TouchData[] mTouchData = new TouchData[TOUCH_TRACE_NUM];
        private int traceCounter = 0;
        private Paint mPaint = new Paint();

        public class TouchData {

            public float x;
            public float y;
            public float r;
        }

        public Panel(Context context) {

            super(context);
            mArrayList = getTestPoint();
            mPaint.setARGB(100, 100, 100, 100);
            for (int i = 0; i < TOUCH_TRACE_NUM; i++) {
                mTouchData[i] = new TouchData();
            }

        }

        private int getNext(int c) {

            int temp = c + 1;
            return temp < TOUCH_TRACE_NUM ? temp : 0;
        }

        public void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            mPaint.setColor(Color.LTGRAY);
            mPaint.setTextSize(20);
            canvas.drawText("W: " + w, mWidthPix / 2 - 20, mHightPix / 2 - 10, mPaint);
            canvas.drawText("H: " + h, mWidthPix / 2 - 20, mHightPix / 2 + 10, mPaint);

            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(mRadius);
            mPaint.setAntiAlias(true);
            for (int i = 0; i < mArrayList.size(); i++) {
                EdgePoint point = mArrayList.get(i);
                mPaint.setColor(Color.RED);
                canvas.drawCircle(point.x, point.y, mPaint.getStrokeWidth(), mPaint);

            }

            for (int i = 0; i < TOUCH_TRACE_NUM; i++) {
                TouchData td = mTouchData[i];
                mPaint.setColor(Color.BLUE);
                if (td.r > 0) {
                    canvas.drawCircle(td.x, td.y, 2, mPaint);
                }

            }

            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            final int eventAction = event.getAction();

            w = event.getRawX();
            h = event.getRawY();
            if ((eventAction == MotionEvent.ACTION_MOVE) || (eventAction == MotionEvent.ACTION_UP)) {
                for (int i = 0; i < mArrayList.size(); i++) {
                    EdgePoint point = mArrayList.get(i);
                    if (!point.isChecked
                            && ((w >= (point.x - mRadius)) && (w <= (point.x + mRadius)))
                            && ((h >= (point.y - mRadius)) && (h <= (point.y + mRadius)))) {
                        mArrayList.remove(i);
                        break;
                    }

                }

                if (mArrayList.isEmpty()) {
                    if (isReCheck) {
                        testResult = true;
                        finish();
                    } else {
                        mArrayList = getTestPoint2();
                        isReCheck = true;
                    }
                }

                TouchData tData = mTouchData[traceCounter];
                tData.x = event.getX();
                tData.y = event.getY();
                tData.r = event.getPressure() * PRESSURE;
                traceCounter = getNext(traceCounter);
                invalidate();

            }
            return true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (testResult) {
            setSuccessResult();
        } else {
            setFailedResult();
        }
    }

    protected void setSuccessResult() {
        dbHelper.saveChangedItem(mainItem, mainItem, SqlConstants.RESULT_SUCCESS);
    }

    protected void setFailedResult() {
        dbHelper.saveChangedItem(mainItem, mainItem, SqlConstants.RESULT_FAILED);
    }

}
