package com.newland.wstdd.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.annotation.SuppressLint;
import android.net.ParseException;
import android.widget.Toast;

public class DateUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getIncreaseDateStr(String day, int dayAddNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			try {
				nowDate = df.parse(day);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDecreaseDateStr(String day, int dayDecNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			try {
				nowDate = df.parse(day);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() - dayDecNum * 24 * 60 * 60 * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

	// 获取当前的时间 格式2015-10-10
	public static String getDateNowString() {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(today);
	}

	// 获取当前的时间 格式2013年9月3日 14:44
	public static String getDateNowString1() {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return formatter.format(today);
	}

	// 获取当前的时间 格式2013年9月3日 14:44
	public static String getDateNowString2() {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatter.format(today);
	}

	// 获取当前的时间 格式2015-10-10
	public static String getDateNowSecondString() {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(today);
	}

	// 月份 日 添加0的封装函数
	public static String getDayFormatString(int date) {

		String tempDay;
		if (date < 10)
			tempDay = "0" + (date);
		else {
			tempDay = date + "";
		}
		return tempDay;
	}

	// 判断两个2013-9-3 14:44格式的时间的大小  endTime大的话就返回true   否是返回false
	public static boolean judgeTimeLarge(String startTime, String endTime) {
		int sYear, sMonth, sDay, sHour, sMinute;
		int eYear, eMonth, eDay, eHour, eMinute;
		sYear = Integer.valueOf(startTime.substring(0, startTime.indexOf("-")));
		sMonth = Integer.valueOf(startTime.substring(startTime.indexOf("-") + 1, startTime.lastIndexOf("-")));
		sDay = Integer.valueOf(startTime.substring(startTime.lastIndexOf("-") + 1, startTime.indexOf(" ")));
		sHour = Integer.valueOf(startTime.substring(startTime.indexOf(" ") + 1, startTime.indexOf(":")));
		sMinute = Integer.valueOf(startTime.substring(startTime.indexOf(":") + 1, startTime.length()));

		eYear = Integer.valueOf(endTime.substring(0, endTime.indexOf("-")));
		eMonth = Integer.valueOf(endTime.substring(endTime.indexOf("-") + 1, endTime.lastIndexOf("-")));
		eDay = Integer.valueOf(endTime.substring(endTime.lastIndexOf("-") + 1, endTime.indexOf(" ")));
		eHour = Integer.valueOf(endTime.substring(endTime.indexOf(" ") + 1, endTime.indexOf(":")));
		eMinute = Integer.valueOf(endTime.substring(endTime.indexOf(":") + 1, endTime.length()));
		String start = String.format("%d-%d-%d-%d-%d", sYear, sMonth, sDay, sHour, sMinute);
		String end = String.format("%d-%d-%d-%d-%d", eYear, eMonth, eDay, eHour, eMinute);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm"); // 这里的格式需要对应上面的%d-%d-%d-%d-%d
		Date startDate;
		try {
			startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			if (startDate.compareTo(endDate) < 0) {
				return true;
				// Toast.makeText(activity, "结束日期不能早于开始日期", 1).show();
			}
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
