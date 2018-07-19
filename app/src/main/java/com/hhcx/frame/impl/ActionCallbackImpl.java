
package com.hhcx.frame.impl;

import android.os.Handler;
import android.os.Message;

import com.hhcx.frame.common.Enums.StateType;
import com.hhcx.aoptestframe.util.SystemProperties;
import com.hhcx.frame.base.ActionCallback;

public class ActionCallbackImpl extends ActionCallback {

    private Handler handler;

    /**
     * 三种写日志的方式:
     * <p>
     * sendResponse(String msgString)写 普通 日志, 类型为Log
     * </p>
     * <p>
     * sendSuccessMsg(String msgString)写 成功 日志, 类型为SUCCESSLOG
     * </p>
     * <p>
     * sendFailedMsg(String msgString)写 失败 日志, 类型为FAILEDLOG
     * </p>
     * 
     * @param handler
     */
    public ActionCallbackImpl(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void sendResponse(int code, String msgString) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = "\t\t" + msgString + "\n";
        handler.sendMessage(msg);
    }

    @Override
    public void sendResponse(String msgString) {
        Message msg = new Message();
        msg.what = StateType.LOG;
        msg.obj = "\t\t" + msgString + "\n";
        handler.sendMessage(msg);
    }

    public void sendFailedMsg(String msg) {
        int code = StateType.LOG_FAILED;
        sendResponse(code, msg);
    }

    public void sendSuccessMsg(String msg) {
        int code = StateType.LOG_SUCCESS;
        sendResponse(code, msg);
    }

    public void sendFailedMsgInCheck(String msg) {
        int code = StateType.LOG_FAILED;
        msg = SystemProperties.getMethodName() + " " + msg;
        sendResponse(code, msg);
    }

    public void sendSuccessMsgInCheck(String msg) {
        int code = StateType.LOG_SUCCESS;
        msg = SystemProperties.getMethodName() + " " + msg;
        sendResponse(code, msg);
    }

    public void sendAlert(String items) {
        int code = StateType.ALERT;
        handler.obtainMessage(code, items).sendToTarget();
    }

    public void sendAlertMsg(String msgToDisplay) {
        Message msg = new Message();
        msg.what = StateType.LOG_ALERT;
        msg.obj = "\t\t" + msgToDisplay + "\n";
        handler.sendMessage(msg);
    }

    public void sendDisplayImg() {
        handler.obtainMessage(StateType.HANDLER_WHAT_07).sendToTarget();
    }

    public void sendNotDisplayImg() {
        handler.obtainMessage(StateType.HANDLER_WHAT_08).sendToTarget();
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
