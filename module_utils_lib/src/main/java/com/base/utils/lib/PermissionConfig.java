package com.base.utils.lib;

public class PermissionConfig {
    //申请权限提示用户弹框标题
    public static String sPermissAlertTitle="需要权限";

    //显示用户拒绝使用权限后的提示
    public static boolean sIsShowRefuseToast = true;
    public static String sShowRefuseText="已拒绝使用";

    //读取内存卡提示语
    public static String sShowApplyReadPic="该功能需要读取您的内存卡";
    public static String sShowApplyRefauseReadPic="该功能需要读取您的内存卡,请手动开启权限";
    //相机使用提示语
    public static String sShowApplyUseCamera ="该功能需要您授予拍照权限,否则将无法使用";
    public static String sShowApplyRefauseCamera ="该功能需要您授予拍照权限,请手动开启权限";
}
