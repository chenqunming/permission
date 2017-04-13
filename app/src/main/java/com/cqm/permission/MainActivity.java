package com.cqm.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    protected PermissionHelper mHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new PermissionHelper(this);

        Button contacts = (Button) findViewById(R.id.contacts);
        Button msm = (Button) findViewById(R.id.sms);
        Button camera = (Button) findViewById(R.id.camera);
        Button phone = (Button) findViewById(R.id.phone);
        Button storage = (Button) findViewById(R.id.storage);

        mHelper.setOnApplyPermissionListener(new PermissionHelper.OnPermissionListener() {
            @Override
            public void onAfterApplyAllPermission(PermissionHelper.PermissionModel model, boolean allow) {
                if (allow) {
                    Toast.makeText(MainActivity.this, model.name + "权限已获取！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, model.name + "权限已被拒绝！", Toast.LENGTH_LONG).show();
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.PermissionModel model = new PermissionHelper.PermissionModel();
                model.pemission = Manifest.permission.READ_CONTACTS;
                model.name = "联系人";
                mHelper.checkPermission(model);
            }
        });

        msm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.PermissionModel model = new PermissionHelper.PermissionModel();
                model.pemission = Manifest.permission.SEND_SMS;
                model.name = "短信";
                mHelper.checkPermission(model);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.PermissionModel model = new PermissionHelper.PermissionModel();
                model.pemission = Manifest.permission.CAMERA;
                model.name = "相机";
                mHelper.checkPermission(model);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.PermissionModel model = new PermissionHelper.PermissionModel();
                model.pemission = Manifest.permission.CALL_PHONE;
                model.name = "电话";
                mHelper.checkPermission(model);
            }
        });

        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.PermissionModel model = new PermissionHelper.PermissionModel();
                model.pemission = Manifest.permission.CALL_PHONE;
                model.name = "存儲";
                mHelper.checkPermission(model, true);
            }
        });


    }

//    private void checkPermission(String permission) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
//                    permission);
//            //判断是否有权限
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//
////                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
////                        permission)) {
////                    //前一次一倍拒绝，再次调起向用户额外解释权限的情况
////                    Toast.makeText(this,"shouldShowRequestPermissionRationale",Toast.LENGTH_LONG).show();
////                } else {
//                    Toast.makeText(this,"没有"+permission+"权限，请求权限",Toast.LENGTH_LONG).show();
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{permission},
//                            PERMISSIONS_REQUEST);
////                }
//            }else {
//                Toast.makeText(this,permission+" 已有权限",Toast.LENGTH_LONG).show();
//            }
//        }else{
//            Toast.makeText(this,"不是6.0系统",Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this,"允许"+permissions.toString()+"权限",Toast.LENGTH_LONG).show();
//
//                } else {
//                    showAppSettingsDialog(permissions.toString());
//                }
//                return;
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 0:
//                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.CAMERA);
//                //判断是否有权限
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this,"允许"+Manifest.permission.CAMERA+"权限",Toast.LENGTH_LONG).show();
//
//                } else {
//                    finish();
//                }
//                break;
//        }

    }

    /**
     * 显示提示信息
     *
     */
//    private void showAppSettingsDialog(String permissions) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("权限提示");
//        builder.setMessage("当前应用缺少"+permissions+"权限。请点击\"设置\"-\"权限\"-打开所需权限。");
//
//        // 拒绝, 退出应用
//        builder.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        builder.setPositiveButton("设置",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        startAppSettings();
//                    }
//                });
//
//        builder.setCancelable(false);
//
//        builder.show();
//    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
//    private void startAppSettings() {
////        Intent intent = new Intent(
////                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////        intent.setData(Uri.parse("package:" + getPackageName()));
////        startActivity(intent);
//
//        try {
//            Intent intent =
//                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//
//            // Android L 之后Activity的启动模式发生了一些变化
//            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
//            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
//            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivityForResult(intent, 0);
//        } catch (Throwable e) {
//            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
//        }
//    }


}
