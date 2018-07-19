package com.hhcx.frame.impl;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.hhcx.frame.common.Enums.StateType;
import com.hhcx.frame.common.LogHelper;

public class HandlerImpl extends Handler {

	private TextView txtResult;

	/**
	 * 将信息输出到显示屏上
	 */
	public HandlerImpl(TextView textView) {
		this.txtResult = textView;
	}

	@Override
	public void handleMessage(Message msg) {
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
		default:
			LogHelper.infoAppendMsg((String) msg.obj, txtResult);
			break;
		}
	}

}
