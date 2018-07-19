package com.hhcx.frame.base;

import android.content.Context;

import java.util.Map;

/**
 * Action的父类
 */
public abstract class AbstractAction {
	protected Context mContext;
	
	public void setContext(Context context) {
		this.mContext = context;
	}

	/**
	 * 这个方法在每个方法被调用之前都会被调用一次.
	 */
	protected void doBefore(Map<String, Object> param, ActionCallback callback) {
	}
	
	/**
	 * 这个方法在每个方法被调用之前都会被调用一次.
	 */
	protected void doAfter(Map<String, Object> param, ActionCallback callback) {
	}
}
