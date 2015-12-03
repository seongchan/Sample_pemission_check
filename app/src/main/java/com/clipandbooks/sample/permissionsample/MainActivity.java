package com.clipandbooks.sample.permissionsample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mRequiredPermissionTxt;
    private TextView mDeniedPermissionTxt;

    private Button mRequestBtn;
    private final int REQUEST_CODE = 100;
    private boolean mAppDetail;
    private Context context;
    private List<String> mDeniedPermissionList;

    private StringBuilder mRequirePermissions = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mDeniedPermissionList = new ArrayList<>();

        mRequiredPermissionTxt = (TextView) findViewById(R.id.requierd_permission);
        mDeniedPermissionTxt = (TextView) findViewById(R.id.denied_permission);

        mRequestBtn = (Button) findViewById(R.id.requet_btn);
        mRequestBtn.setOnClickListener(this);

        mRequirePermissions.append(getString(R.string.calendar_permission_title)).append(", ")
                .append(getString(R.string.camera_permission_title)).append(", ")
                .append(getString(R.string.location_permission_title)).append(", ")
                .append(getString(R.string.contact_permission_title)).append(", ")
                .append(getString(R.string.microphone_permission_title)).append(", ")
                .append(getString(R.string.phone_permission_title)).append(", ")
                .append(getString(R.string.sms_permission_title)).append(", ")
                .append(getString(R.string.ex_storage_permission_title));

        mRequiredPermissionTxt.setText(mRequirePermissions.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionCheck();
    }

    private void permissionCheck() {

        StringBuilder deniedPermission = new StringBuilder();

        boolean commaFlag = false;

        // android.permission.READ_CALENDAR
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            deniedPermission.append(getString(R.string.calendar_permission_title));
            mDeniedPermissionList.add(Manifest.permission.READ_CALENDAR);
            commaFlag = true;
        }

        // android.permission.CAMERA
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.camera_permission_title));
            mDeniedPermissionList.add(Manifest.permission.CAMERA);
        }

        // android.permission.ACCESS_FINE_LOCATION
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.location_permission_title));
            mDeniedPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // android.permission.READ_CONTACTS
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.contact_permission_title));
            mDeniedPermissionList.add(Manifest.permission.READ_CONTACTS);
        }

        // android.permission.RECORD_AUDIO
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.microphone_permission_title));
            mDeniedPermissionList.add(Manifest.permission.RECORD_AUDIO);
        }

        // android.permission.READ_PHONE_STATE
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.phone_permission_title));
            mDeniedPermissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        // android.permission.RECEIVE_SMS
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.sms_permission_title));
            mDeniedPermissionList.add(Manifest.permission.RECEIVE_SMS);
        }

        // android.permission.READ_EXTERNAL_STORAGE
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (commaFlag) {
                deniedPermission.append(", ");
            }
            commaFlag = true;
            deniedPermission.append(getString(R.string.ex_storage_permission_title));
            mDeniedPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (commaFlag) {
            mDeniedPermissionTxt.setText(deniedPermission.toString());
            mRequestBtn.setText(getString(R.string.request_permission));
            mAppDetail = false;
        } else {
            mDeniedPermissionTxt.setText(getString(R.string.granted_all_permissions));
            mRequestBtn.setText(getString(R.string.go_app_detail_setting));
            mAppDetail = true;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.requet_btn:
                if (mAppDetail) {
                    Intent goAppDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
                    startActivity(goAppDetail);
                } else {
                    String[] deniedPermissionStringArray = mDeniedPermissionList.toArray(new String[mDeniedPermissionList.size()]);
                    requestPermissions(deniedPermissionStringArray, REQUEST_CODE);
                }
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                int i = grantResults.length;
                ArrayList deniedIndex = new ArrayList();
                for (int j = 0 ; j < i; j++) {
                    if (grantResults[j] != PackageManager.PERMISSION_GRANTED) {
                        deniedIndex.add(j);
                        Log.d("TAG", permissions[j] + " : " + grantResults[j]);
                    } else {
                        Log.d("TAG", permissions[j] + " : " + grantResults[j]);
                    }
                }
                if (deniedIndex.size() > 0 ) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent goAppDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
                            startActivity(goAppDetail);
                        }
                    });
                    alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    alert.setIcon(R.mipmap.ic_launcher);
                    alert.setTitle(R.string.setting_permission_title);
                    alert.setMessage(getString(R.string.setting_permission));
                    alert.show();
                }
                break;
        }
    }
}
