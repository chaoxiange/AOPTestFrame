
package com.hhcx.aoptestframe.action;

import android.util.Log;

import com.hhcx.aoptestframe.helper.DBHelper;
import com.hhcx.aoptestframe.helper.SqlConstants;
import com.hhcx.aoptestframe.util.BackManager;
import com.hhcx.frame.base.AbstractAction;
import com.hhcx.frame.base.ActionCallback;
import com.hhcx.frame.impl.ActionCallbackImpl;

import java.util.Map;

public abstract class ConstantAction extends AbstractAction {
    public static String TAG = null;
    protected ActionCallbackImpl mCallback;
    public static boolean isOpened = false;
    protected static final int EVENT_ID_CANCEL = -1;
    protected boolean isRun = false;
    protected String operationResult = "0";
    protected String mainItem;
    protected String subItem;
    public static final int TEST_QRC_FOR_SECONDARY_DISPLAY = 2;
    public static final int TEST_QRC_FOR_DEFAULT = 3;
    public static final int TEST_QRC_FOR_ZOOM = 4;
    public static final int TEST_QRC_FOR_FIXED_FOCUS = 5;
    
    /**
     * 有调用者时启用
     */
    protected static final int TIME_WAITING = 10000;

    protected void setParameter(Map<String, Object> param, ActionCallbackImpl callback) {
        this.mCallback = callback;
        mainItem = param.get("mainItem").toString();
        subItem = param.get("subItem").toString();
        Log.e("123", "mainItem = " + mainItem + " subItem = " + subItem);
    }

    protected void notifyBack() {
        BackManager.notifyBack();
    }

    protected void saveToDatabase() {
        DBHelper dbHelper = DBHelper.getInstance();
        Log.e(TAG, "mainItem = " + mainItem + " subItem = " + subItem + " operationResult = "
                + operationResult);
        dbHelper.saveChangedItem(mainItem, subItem, operationResult);
    }

    protected void saveToDatabase(String subItem) {
        DBHelper dbHelper = DBHelper.getInstance();
        Log.e(TAG, "mainItem = " + mainItem + " subItem = " + subItem + " operationResult = "
                + operationResult);
        dbHelper.saveChangedItem(mainItem, subItem, operationResult);
    }

    protected void setSuccessResult() {
        this.operationResult = SqlConstants.RESULT_SUCCESS;
        saveToDatabase();
    }

    protected void setFailedResult() {
        this.operationResult = SqlConstants.RESULT_FAILED;
        saveToDatabase();
    }

    protected String getResourceString(int id) {
        return mContext.getResources().getString(id);
    }

    protected String getMethodName() {
        StackTraceElement[] eles = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : eles) {
            Log.e("stackTraceElement", stackTraceElement.getMethodName());
        }
        return eles[0].getMethodName();
    }

    @Override
    protected void doBefore(Map<String, Object> param, ActionCallback callback) {
        TAG = this.getClass().getName()
                .substring(this.getClass().getName().indexOf("action.") + "action.".length());
        // Log.e("TAG", "TAG = " + TAG);
    }

    public abstract void autoTest(Map<String, Object> param, ActionCallbackImpl callback);

    public abstract void close(Map<String, Object> param, ActionCallbackImpl callback);

}
