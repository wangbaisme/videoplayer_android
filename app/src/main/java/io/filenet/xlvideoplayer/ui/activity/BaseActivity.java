package io.filenet.xlvideoplayer.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mob.MobSDK;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.utils.ToastUtil;

public class BaseActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.btn_get_code)
    TextView mBtnGetCode;
    @BindView(R.id.btn_confrim)
    TextView mBtnConfrim;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_phone)
    EditText mEtPhone;

    private String country = "86";
    private String phone = "";
    private int timed = 0;

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
                    timed = result - 1;
                    mBtnGetCode.setText(result + getString(R.string.s_over_get));
                    mBtnGetCode.setClickable(false);
                    handler.postDelayed(autoTimedTask,1000);
                }else {
                    mBtnGetCode.setClickable(true);
                    mBtnGetCode.setText(getString(R.string.get_verification_code));
                }
                return;
            }
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        showMsgToast(getString(R.string.the_mobile_number_has_been_registered_please_re_enter));
                        mEtPhone.requestFocus();
                        return;
                    }
                }
            }
            if (result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    showMsgToast(getString(R.string.verification_passed));
                }
            }
            else {//其他出错情况

            }
        }

    };

    Runnable autoTimedTask = new Runnable() {
        @Override
        public void run() {
            Message msgTime=new Message();
            msgTime.arg1 = 10001; //事件
            msgTime.arg2 = timed; //时间
            msgTime.obj = null;
            handler.sendMessage(msgTime);
        }
    };

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        MobSDK.submitPolicyGrantResult(true, null);
        sms_verification();
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        init();
    }

    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
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

    private void init(){
        mBtnGetCode.setOnClickListener(this);
        mBtnConfrim.setOnClickListener(this);
        mEtPhone.setError(getString(R.string.please_enter_the_correct_number));
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 11 || Integer.valueOf((""+s.charAt(0))) != 1)
                    mEtPhone.setError(getString(R.string.please_enter_the_correct_number));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_code:
                if (mEtPhone.getText().toString().isEmpty())
                    showMsgToast(getString(R.string.number_is_null));
                else if (mEtPhone.getText().toString().length() != 11
                        || Integer.valueOf(mEtPhone.getText().toString().substring(0,1)) != 1)
                    showMsgToast(getString(R.string.please_enter_the_correct_number));
                else{
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
                    showMsgToast(getString(R.string.verification_code_is_null));
                    return;
                }
                SMSSDK.submitVerificationCode(country,phone,mEtCode.getText().toString());
                break;
            default:
                break;
        }
    }

    private void showMsgToast(String content){
        ToastUtil.getInstance(getApplicationContext()).showShortToast(content);
    }
}
