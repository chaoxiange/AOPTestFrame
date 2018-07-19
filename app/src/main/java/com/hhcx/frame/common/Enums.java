package com.hhcx.frame.common;

public class Enums {
	
	//Log类型
	/**
	 * 日志类型
	 *
	 */
	public class StateType{
		public static final int LOG = 1;
		public static final int LOG_SUCCESS = 2;
		public static final int LOG_FAILED = 3;
		/**
		 * 发生返回事件
		 */
		public static final int KEY_BACK = 4;
		public static final int ALERT = 5;
		/**
		 * 提示语句
		 */
		public static final int LOG_ALERT = 6;
		public static final int HANDLER_WHAT_07 = 7;
		public static final int HANDLER_WHAT_08 = 8;
	}
	
	/**
	 * 正在使用的网络类型
	 */
	public class NetType{
		public static final int WIFI = 1;
		public static final int ETH0 = 2;
		public static final int G3 = 3;
		public static final int ALL = 0;
	}

}
