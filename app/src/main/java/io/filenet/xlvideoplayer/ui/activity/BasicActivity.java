package io.filenet.xlvideoplayer.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.filenet.xlvideoplayer.utils.StatusBarUtil;
import io.filenet.xlvideoplayer.utils.SystemUtil;

public class BasicActivity extends AppCompatActivity {

    protected Context mContext;
    protected int screenWidth;
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        screenWidth = SystemUtil.getScreenWidth(mContext);
        SystemUtil.hideSupportActionBar(this, true, false);
        setStatusBar();
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

    protected void setStatusBar() {
        //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近
        StatusBarUtil.setTransparent(this);
    }
}
