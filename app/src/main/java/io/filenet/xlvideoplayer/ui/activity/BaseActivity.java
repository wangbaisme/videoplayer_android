package io.filenet.xlvideoplayer.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.filenet.xlvideoplayer.R;

public class BaseActivity extends Activity implements View.OnClickListener {

    private TextView mBtnGetCode;
    private TextView mBtnConfrim;
    private EditText mEtPhone;
    private EditText mEtCode;

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE
    };
    private final int permissionCode = 1001;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        MobSDK.submitPolicyGrantResult(true, null);
        sms_verification();
        setContentView(R.layout.activity_base);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            registerPermission();
    }

    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void init(){
        mBtnGetCode = findViewById(R.id.btn_get_code);
        mBtnConfrim = findViewById(R.id.btn_confrim);
        mBtnGetCode.setOnClickListener(this);
        mBtnConfrim.setOnClickListener(this);
        mEtCode = findViewById(R.id.et_code);
        mEtPhone = findViewById(R.id.et_phone);
        mEtPhone.setError("请输入正确号码");
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 11 || Integer.valueOf((""+s.charAt(0))) != 1)
                    mEtPhone.setError("请输入正确号码");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void registerPermission(){
       for (int i=0; i<permissions.length; i++){
           if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, permissionCode);
                return;
           }
       }
    }

    private String country = "86";
    private String phone = "";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_code:
                if (mEtPhone.getText().toString().isEmpty())
                    showMsgToast("号码不能为空");
                else if (mEtPhone.getText().toString().length() != 11
                        || Integer.valueOf(mEtPhone.getText().toString().substring(0,1)) != 1)
                    showMsgToast("请正确输入手机号码");
                else{
                    Log.d("maintest","发送验证码");
                    SMSSDK.getVerificationCode(country,mEtPhone.getText().toString());
                    phone = mEtPhone.getText().toString();
                    Message msg=new Message();
                    msg.arg1 = 10001; //事件
                    msg.arg2 = 60; //时间
                    msg.obj = null;
                    handler.sendMessage(msg);
                }
                break;
            case R.id.btn_confrim:
                if (phone.isEmpty() || mEtCode.getText().toString().isEmpty()){
                    showMsgToast("验证码不能为空");
                    return;
                }
                SMSSDK.submitVerificationCode(country,phone,mEtCode.getText().toString());
                break;
            default:
                break;
        }
    }

    public void sms_verification(){
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();//创建了一个对象
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == 10001){ //倒计时
                if (result >= 1){
                    final int time = result - 1;
                    mBtnGetCode.setText(result + "s后获取");
                    mBtnGetCode.setClickable(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msgTime=new Message();
                            msgTime.arg1 = 10001; //事件
                            msgTime.arg2 = time; //时间
                            msgTime.obj = null;
                            handler.sendMessage(msgTime);
                        }
                    },1000);
                }else {
                    mBtnGetCode.setClickable(true);
                    mBtnGetCode.setText("获取验证码");
                }
                return;
            }
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        showMsgToast("该手机号已经注册过，请重新输入");
                        mEtPhone.requestFocus();
                        return;
                    }
                }
            }
            if (result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    showMsgToast("验证码输入正确");
                }
            }
            else {//其他出错情况

            }
        }

    };

    private void showMsgToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
