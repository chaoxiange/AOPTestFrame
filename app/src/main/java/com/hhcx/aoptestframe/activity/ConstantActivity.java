
package com.hhcx.aoptestframe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhcx.aoptestframe.R;
import com.hhcx.aoptestframe.activity.adapter.ListViewAdapter;
import com.hhcx.aoptestframe.helper.DBHelper;
import com.hhcx.frame.base.ActionCallback;
import com.hhcx.frame.common.Enums.StateType;

import java.util.Map;

public abstract class ConstantActivity extends Activity {

    protected static final String TAG = "MainActivity";
    public static Handler handler;
    public static ActionCallback callback;
    protected ListViewAdapter adapter;

    protected Map<String, Object> param;

    // 控件
    protected LinearLayout layoutResult;
    public TextView txtResult;
    protected ListView lvwMain;
    protected LinearLayout layoutListView;
    protected TextView txtRight;
    protected TextView txtWrong;
    protected TextView txtNone;
    protected ImageView imgChargingIndicator;

    protected Context context;

    protected boolean isMain = true;

    public static int terminalType;
    /**
     * ListView当前移动到的位置
     */
    public int firstVisiblePosition;
    
    protected DBHelper dbHelper;

    protected long firstTime = 0;
    protected long secondTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        txtRight = (TextView) findViewById(R.id.txt_right);
        txtWrong = (TextView) findViewById(R.id.txt_wrong);
        txtNone = (TextView) findViewById(R.id.txt_none);
        layoutResult = (LinearLayout) findViewById(R.id.layout_result);
        txtResult = (TextView) findViewById(R.id.txt_result); // 定义txtResult
        txtResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        lvwMain = (ListView) findViewById(R.id.lvw_main);
        layoutListView = (LinearLayout) findViewById(R.id.layout_lvw);
    }

    public static void writeLog(String obj) {
        Message msg = new Message();
        msg.what = StateType.LOG;
        msg.obj = obj + "\n";
        handler.sendMessage(msg);
    }

    public static void writeSuccessLog(String obj) {
        Message msg = new Message();
        msg.what = StateType.LOG_SUCCESS;
        msg.obj = "\t\t" + obj + "\n";
        handler.sendMessage(msg);
    }

    public static void writeFailedLog(String obj) {
        Message msg = new Message();
        msg.what = StateType.LOG_FAILED;
        msg.obj = "\t\t" + obj + "\n";
        handler.sendMessage(msg);
    }

}
