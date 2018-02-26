package com.newland.wstdd.common.tools;

import android.content.Context;
import android.content.Intent;

import com.newland.wstdd.login.login.LoginActivity;

public class UiToUiHelper {

	/**
	 * 跳转到
	 * @param contetx
	 * @param activity
	 */
	public static void showLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
}
