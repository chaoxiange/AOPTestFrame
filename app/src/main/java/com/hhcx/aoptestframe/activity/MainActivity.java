
package com.hhcx.aoptestframe.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hhcx.aoptestframe.R;
import com.hhcx.aoptestframe.activity.adapter.ListViewAdapter;
import com.hhcx.aoptestframe.bean.MainItem;
import com.hhcx.aoptestframe.bean.SubItem;
import com.hhcx.aoptestframe.helper.DBHelper;
import com.hhcx.aoptestframe.util.BackManager;
import com.hhcx.frame.base.ActionManager;
import com.hhcx.frame.common.Enums.StateType;
import com.hhcx.frame.common.LogHelper;
import com.hhcx.frame.impl.ActionCallbackImpl;

import java.util.HashMap;

public class MainActivity extends ConstantActivity {

    private boolean showResultTv = false;
    MainItem mainItem;
    private int positionPressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initUI();

        BackManager.waitForBack();
    }

    private void initParams() {
        context = this;
        handler = new Handler(new HandlerCallback());
        callback = new ActionCallbackImpl(handler);
        param = new HashMap<>();
        dbHelper = DBHelper.getInstance();
    }

    private void initUI() {
        if (!showResultTv) {
            layoutResult.setVisibility(View.GONE);
        }
        dbHelper.initDatabase();
        adapter = new ListViewAdapter(context, AppApplication.testItems);
        lvwMain.setAdapter(adapter);
        lvwMain.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String command = view.findViewById(R.id.tv_name).getTag().toString();
                onItemPressed(command, position);
            }
        });
        lvwMain.setOnScrollListener(new MyOnScrollListener());
        setItemCounts();
    }

    @Override
    protected void onDestroy() {
        dbHelper.closeDatabase();
        BackManager.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMain) {
                secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 800) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序...", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                } else {
                    System.exit(0);
                }
            } else {
                onKeyBack();
            }
        } else {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    /**
     * 在子页面时 当点击返回键时, 或发生返回事件时
     */
    private void onKeyBack() {
        refreshListView(ListViewAdapter.INDEX_NONE);
        Log.d(TAG, "firstVisiblePosition = " + firstVisiblePosition);
        lvwMain.setSelection(firstVisiblePosition);

        isMain = true;
        closeAllDriver();
        if (!showResultTv) {
            layoutResult.setVisibility(View.GONE);
        } else {
            writeLog("测试结束");
        }
        setItemCounts();
    }

    /**
     * 响应点击列表项
     * 
     * @param command Item的命令
     * @param position Item的位置
     */
    private void onItemPressed(String command, int position) {
        Log.e(TAG, command);
        if (isMain) {
            isMain = false;
            positionPressed = position;
            mainItem = AppApplication.testItems.get(position);
            writeLog(mainItem.getCommand());

            if (mainItem.isActivity()) {
                ComponentName cn = new ComponentName(context, mainItem.getPackageName());
                Intent intent = new Intent();
                intent.setComponent(cn);
                startActivityForResult(intent, position);
            } else {
                refreshListView(position);
                if (!showResultTv) {
                    layoutResult.setVisibility(View.VISIBLE);
                    txtResult.setText("");
                }
                for (SubItem subItem : mainItem.getSubItems()) {
                    if (subItem.getCommand().equals("autoTest")) {
                        param.clear();
                        param.put("mainItem", mainItem.getCommand());
                        param.put("subItem", "autoTest");
                        Log.e(TAG, "itemPressed : " + mainItem.getCommand() + "/autoTest");
                        ActionManager.doSubmit(mainItem.getCommand() + "/autoTest", context, param,
                                callback);
                    }
                }
            }

        } else {
            param.clear();
            param.put("mainItem", mainItem.getCommand());
            param.put("subItem", command);
            Log.e(TAG, "itemPressed : " + mainItem.getCommand() + "/" + command);
            ActionManager.doSubmit(mainItem.getCommand() + "/" + command, context, param,
                    callback);
        }
    }

    private void closeAllDriver() {
        ActionManager.doSubmit(AppApplication.testItems.get(positionPressed).getCommand() + "/" + "close", context, null, null);
    }


    /**
     * 刷新列表项
     */
    private void refreshListView(int position) {
        adapter.refreshView(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "+onActivityResult");
        Log.e(TAG, "position = " + positionPressed);
        View view = lvwMain.getChildAt(positionPressed - lvwMain.getFirstVisiblePosition());
        lvwMain.getAdapter().getView(positionPressed, view, lvwMain);
        setItemCounts();
    }

    private class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                // position records the position of the visible top line
                if (isMain) {
                    firstVisiblePosition = lvwMain.getFirstVisiblePosition();
                    Log.d(TAG, "firstVisiblePosition = " + firstVisiblePosition);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
        }
    }


    private class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StateType.LOG:
                    LogHelper.infoAppendMsg((String) msg.obj, txtResult);
                    break;
                case StateType.LOG_SUCCESS:
                    LogHelper.infoAppendMsgForSuccess((String) msg.obj, txtResult);
                    break;
                case StateType.LOG_FAILED:
                    LogHelper.infoAppendMsgForFailed((String) msg.obj, txtResult);
                    break;
                case StateType.KEY_BACK:
                    onKeyBack();
                    break;
                case StateType.ALERT:
                    break;
                case StateType.LOG_ALERT:
                    LogHelper.infoAppendForAlert((String) msg.obj, txtResult);
                    break;
                case StateType.HANDLER_WHAT_07:
                    layoutResult.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, 0, 1.5f));
                    imgChargingIndicator.setVisibility(View.VISIBLE);
                    break;
                case StateType.HANDLER_WHAT_08:
                    layoutResult.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, 0, 1.0f));
                    imgChargingIndicator.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void cleanStatus() {
        dbHelper.clear();
        adapter.notifyDataSetChanged();
        setItemCounts();
    }

    /**
     * 设置成功\失败\未测试项目的数量
     */
    private void setItemCounts() {
        txtRight.setText(dbHelper.queryRightItemCount());
        txtWrong.setText(dbHelper.queryWrongItemCount());
        txtNone.setText(dbHelper.queryNoneItemCount());
    }

}
