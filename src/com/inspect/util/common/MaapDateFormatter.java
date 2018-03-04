package com.inspect.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MaapDateFormatter {

	private static SimpleDateFormat commonStoreFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat storeFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	private static SimpleDateFormat displayFormat = new SimpleDateFormat(
			"yyyy年MM月dd日HH时mm分");

	private static SimpleDateFormat displayGraphFormat = new SimpleDateFormat(
			"HHmmss");

	private MaapDateFormatter() {
	};

	public static SimpleDateFormat getCommonStoreFormat() {
		return commonStoreFormat;
	}

	public static SimpleDateFormat getStoreFormatter() {
		return storeFormat;
	}

	public static SimpleDateFormat getDisplayFormatter() {
		return displayFormat;
	}

	public static SimpleDateFormat getDisplayGraphFormatter() {
		return displayGraphFormat;
	}

	public static SimpleDateFormat getCustomDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	public static String convertStoreToDisplay(String timestamp) {
		try {
			return MaapDateFormatter.getDisplayFormatter().format(
					MaapDateFormatter.getStoreFormatter().parse(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从前台得到的跟数据库的时间戳匹配
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String convertStoreToCal(String timestamp) {
		try {
			return MaapDateFormatter.getStoreFormatter().format(
					MaapDateFormatter.getCommonStoreFormat().parse(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertStoreToDisplayGraph(String timestamp) {
		try {
			return MaapDateFormatter.getDisplayGraphFormatter().format(
					MaapDateFormatter.getStoreFormatter().parse(timestamp));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatDateTime(String pattern) {
		java.util.Date d = new java.util.Date();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				pattern);
		return format.format(d);
	}

	public static String getCurrentDate() {
		return formatDateTime("yyyy-MM-dd");
	}

	public static void main(String[] args) {
		MaapDateFormatter a = new MaapDateFormatter();
		//System.out.println("---" + a.getCurrentDate());
	}
}
