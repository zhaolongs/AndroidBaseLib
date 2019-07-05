package com.base.commonlibrary.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class PermissionChecker {
	
	
	public interface PermissionCallBack {
		void permissFaile();
		
		void permissSuccess();
		
	}
	
	private PermissionCallBack mPermissionCallBack;
	private Context mContext;
	private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
	
	private Activity mActivity;
	
	public PermissionChecker(Activity activity) {
		mActivity = activity;
	}
	
	/**
	 * Check that all given permissions have been granted by verifying that each entry in the
	 * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
	 *
	 * @see Activity#onRequestPermissionsResult(int, String[], int[])
	 */
	private boolean verifyPermissions(int[] grantResults) {
		// At least one result must be checked.
		if (grantResults.length < 1) {
			return false;
		}
		
		// Verify that each required permission has been granted, otherwise return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	public boolean checkPermission() {
		boolean ret = true;
		
		List<String> permissionsNeeded = new ArrayList<String>();
		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
			permissionsNeeded.add("CAMERA");
		}
		if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
			permissionsNeeded.add("MICROPHONE");
		}
		if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			permissionsNeeded.add("Write external storage");
		}
		
		if (permissionsNeeded.size() > 0) {
			// Need Rationale
			String message = "You need to grant access to " + permissionsNeeded.get(0);
			for (int i = 1; i < permissionsNeeded.size(); i++) {
				message = message + ", " + permissionsNeeded.get(i);
			}
			// Check for Rationale Option
			if (!mActivity.shouldShowRequestPermissionRationale(permissionsList.get(0))) {
				showMessageOKCancel(message,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
										REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
							}
						});
			} else {
				mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
						REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			}
			ret = false;
		}
		
		return ret;
	}
	
	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(mActivity)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private boolean addPermission(List<String> permissionsList, String permission) {
		boolean ret = true;
		if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			ret = false;
		}
		return ret;
	}
	
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
			if (verifyPermissions(grantResults)) {
				// all permissions granted
			} else {
				// some permissions denied
				displayFrameworkBugMessageAndExit("");
			}
		}
	}
	
	private void displayFrameworkBugMessageAndExit(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showInstalledAppDetails(mContext.getApplicationContext(), mContext.getPackageName());
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if (mPermissionCallBack != null) {
					mPermissionCallBack.permissFaile();
				}
			}
		});
		builder.show();
	}
	
	private static final String SCHEME = "package";
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
	 */
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
	 */
	private static final String APP_PKG_NAME_22 = "pkg";
	/**
	 * InstalledAppDetails所在包名
	 */
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	/**
	 * InstalledAppDetails类名
	 */
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。
	 * 对于Android 2.3（Api Level9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
	 *
	 * @param context
	 * @param packageName 应用程序的包名
	 */
	
	public static void showInstalledAppDetails(Context context, String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
}
