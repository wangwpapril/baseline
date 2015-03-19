package com.intrepid.travel.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

public class Common {

	public static ProgressDialog dialogLoading;

	// 显示提示信息对话框
	public static void showHintDialog(String title, String content) {

		if (context != null) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
			dialogBuilder
					.setTitle(title)
					.setMessage(content)
					.setCancelable(true)
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();
		}
	}

//	public static void showHintDialog(Context con, String title,
//			String content, final HandleDialogData handle) {
//		if (con != null) {
//			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con);
//			dialogBuilder
//					.setTitle(title)
//					.setMessage(content)
//					.setCancelable(true)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									dialog.dismiss();
//									handle.handle();
//								}
//							}).show();
//		}
//	}

	// 显示Loanding界面
	public static void showLoading() {
		if (dialogLoading != null)
			return;
		if (context == null)
			return;
		// 创建ProgressDialog对象
		dialogLoading = new ProgressDialog(context);

		// 设置进度条风格，风格为圆形，旋转的
		dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog 标题
		dialogLoading.setTitle("请稍候");

		// 设置ProgressDialog 提示信息
		dialogLoading.setMessage("读取中...");

		// 设置ProgressDialog 的进度条是否不明确
		dialogLoading.setIndeterminate(false);

		// 设置ProgressDialog 是否可以按退回按键取消
		dialogLoading.setCancelable(true);

		dialogLoading.setOnDismissListener(listenerDismissLoading);

		// 让ProgressDialog显示
		dialogLoading.show();
	}

	// 显示Loanding界面
	public static void showLoading(String str) {
		if (dialogLoading != null)
			return;
		if (context == null)
			return;
		// 创建ProgressDialog对象
		dialogLoading = new ProgressDialog(context);

		// 设置进度条风格，风格为圆形，旋转的
		dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog 标题
//		dialogLoading.setTitle("请稍候");

		// 设置ProgressDialog 提示信息
		dialogLoading.setMessage(str);

		// 设置ProgressDialog 的进度条是否不明确
		dialogLoading.setIndeterminate(false);

		// 设置ProgressDialog 是否可以按退回按键取消
		dialogLoading.setCancelable(true);

		dialogLoading.setOnDismissListener(listenerDismissLoading);

		// 让ProgressDialog显示
		dialogLoading.show();
	}

	static OnDismissListener listenerDismissLoading = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
//			NetManager.isConnection = false;
//			TimeOutManager.getInstance().stopTimer();
//			Logger.log("Stop  When  DissmissLoding");
		}
	};
	public static Context context;

	// 取消Loanding界面
	public static void cancelLoading() {
		if (dialogLoading != null) {
			if (dialogLoading.isShowing())
				dialogLoading.cancel();
			dialogLoading = null;
		}
	}
}