package com.yc.junior.english.base.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.yc.junior.english.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wanglin  on 2019/4/25 09:46.
 */
public class PermissionManager {
    private static PermissionManager instance;

    private List<String> permissionList;

    private Activity mActivity;
    private final int REQUEST_CODE = 1024;
    private PermissionUIListener mListener;

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        synchronized (PermissionManager.class) {
            if (instance == null) {
                synchronized (PermissionManager.class) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addPermissions(Activity activity, PermissionUIListener listener, String[]... permissions) {
        this.mActivity = activity;
        this.mListener = listener;
        if (permissions.length > 0) {
            permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                permissionList.addAll(Arrays.asList(permissions[i]));
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermission(permissionList);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(List<String> permissionList) {
        if (mActivity == null) {
            throw new RuntimeException("必须在Activity中调用");
        }
        List<String> unGrantPermissons = new ArrayList<>();

        for (String permission : permissionList) {
            if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED ||
                    mActivity.shouldShowRequestPermissionRationale(permission)) {
                unGrantPermissons.add(permission);
            }
        }

        // 权限都已经有了，那么直接调用SDK
        if (unGrantPermissons.size() == 0) {
//            listener.showAdv();

            //获得权限，想干什么干什么
            mListener.onPermissionGranted();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[unGrantPermissons.size()];
            unGrantPermissons.toArray(requestPermissions);
            mActivity.requestPermissions(requestPermissions, REQUEST_CODE);
        }
    }


    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
//            listener.showAdv();
                mListener.onPermissionGranted();
                //获得权限
            } else {
                showMissingPermissionDialog();
                mListener.onPermissionDenyed();
            }
        }
    }


    private void startAppSettings() {
        // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
        Toast.makeText(mActivity, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
//        mActivity.finish();
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.tint);
        builder.setMessage("应用需要以上权限，请允许");

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.base_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }
                });

        builder.setPositiveButton(R.string.go_setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


}
