package io.filenet.xlvideoplayer.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BasicActivity extends Activity {

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE
    };
    private final int permissionCode = 1001;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            registerPermission();
    }

    private void registerPermission(){
        for (int i=0; i<permissions.length; i++){
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, permissionCode);
                return;
            }
        }
    }
}
