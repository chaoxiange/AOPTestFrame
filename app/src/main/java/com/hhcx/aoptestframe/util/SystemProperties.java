package com.hhcx.aoptestframe.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SystemProperties {


	public static String getModel() {
		String model = get("ro.wp.product.model").trim().toUpperCase();
		Log.e("model", model);
		return model;
	}

	public static String getMethodName() {
		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		return eles[5].getMethodName();
	}

	public static String getTimeWithFormat() {
		long time = System.currentTimeMillis();
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdr.format(time);
	}

	private static String get(String key) {
		Object bootloaderVersion = null;
		try {
			Class<?> systemProperties = Class.forName("android.os.SystemProperties");
			Log.i("systemProperties", systemProperties.toString());
			bootloaderVersion = systemProperties.getMethod("get", new Class[] { String.class, String.class }).invoke(systemProperties, new Object[] { key, "unknown" });
			Log.i("bootloaderVersion", bootloaderVersion.getClass().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootloaderVersion.toString();
	}
}
