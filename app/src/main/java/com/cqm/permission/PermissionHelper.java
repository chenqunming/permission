package com.cqm.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by chenqunming on 2017/4/12.
 */

public class PermissionHelper {

    /**
     * 权限申请事件监听
     */
    public interface OnPermissionListener {

        /**
         * 申请所有权限之后的逻辑
         */
        void onAfterApplyAllPermission(PermissionModel model, boolean allow);
    }

    public static class PermissionModel {
        public String pemission;//申请的权限
        public String explain;//权限说明
        public String name;//权限名称
    }

    private final static String TAG = "PermissionHelper";

    public final static int PERMISSIONS_REQUEST = 98;//权限返回码
    public final static int SETTING_RESULT_CODE = 99;//应用设置返回码

    private Activity mActivity;//上下文

    private boolean mNeedClose = false;//是否需要关闭当前Activity

    private PermissionModel mPmodel;
    private OnPermissionListener mOnPermissionListener;//权限申请事件监听


    public PermissionHelper(Activity activity) {
        mActivity = activity;
    }

    public void setOnApplyPermissionListener(OnPermissionListener onPermissionListener) {
        mOnPermissionListener = onPermissionListener;
    }

    /**
     * 请求权限
     *
     * @param model
     */
    public void checkPermission(PermissionModel model) {
        checkPermission(model, false);
    }

    /**
     * 请求权限
     *
     * @param model
     * @param needclose
     */
    public void checkPermission(PermissionModel model, boolean needclose) {
        mPmodel = model;
        mNeedClose = needclose;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(mActivity,
                    mPmodel.pemission);
            //判断是否有权限
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

//                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
//                        mPermission)) {
                //前一次已被拒绝，再次调起向用户额外解释权限的情况
//                showExplainDialog();
//                    Toast.makeText(mActivity,"shouldShowRequestPermissionRationale",Toast.LENGTH_LONG).show();
//                } else {
                Log.v(TAG, "没有" + mPmodel.name + "权限，请求权限");
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{mPmodel.pemission},
                        PERMISSIONS_REQUEST);
//                }
            } else {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onAfterApplyAllPermission(mPmodel, true);
                }
                Log.v(TAG, mPmodel.name + " 已有权限");
            }
        } else {
            Log.v(TAG, "不是6.0系统");
             if (mOnPermissionListener != null) {
                mOnPermissionListener.onAfterApplyAllPermission(mPmodel, true);
            }
        }
    }


    /**
     * 申请权限返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mOnPermissionListener != null) {
                        mOnPermissionListener.onAfterApplyAllPermission(mPmodel, true);
                    }
                    Log.v(TAG, "允许" + mPmodel.name + "权限");

                } else {
                    showAppSettingsDialog();
                }
                return;
            }
        }
    }


    /**
     * 显示权限说明信息
     */
    private void showExplainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("权限提示");
        builder.setMessage(mPmodel.explain);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkPermission(mPmodel, mNeedClose);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示权限提示信息
     */
    private void showAppSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("权限提示");
        builder.setMessage("当前应用缺少" + mPmodel.name + "权限。请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        String cancelStr = "取消";
        if (mNeedClose) {
            cancelStr = "退出";
        }
        builder.setNegativeButton(cancelStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mOnPermissionListener != null) {
                            mOnPermissionListener.onAfterApplyAllPermission(mPmodel, false);
                        }
                        if (mNeedClose) {
                            mActivity.finish();
                        }
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        try {
            Intent intent =
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mActivity.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivityForResult(intent, SETTING_RESULT_CODE);
        } catch (Throwable e) {
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 应用权限设置返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTING_RESULT_CODE:
                int permissionCheck = ContextCompat.checkSelfPermission(mActivity,
                        mPmodel.pemission);
                //判断是否有权限
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (mOnPermissionListener != null) {
                        mOnPermissionListener.onAfterApplyAllPermission(mPmodel, false);
                    }
                    if (mNeedClose) {
                        mActivity.finish();
                    }

                } else {
                    if (mOnPermissionListener != null) {
                        mOnPermissionListener.onAfterApplyAllPermission(mPmodel, true);
                    }
                    Log.v(TAG, "允许" + mPmodel.name + "权限");
                }
                break;
        }

    }


}
