package io.filenet.xlvideoplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.utils.ToastUtil;

public class ExchangeDialog extends Dialog implements View.OnClickListener {

    private EditText mExchangeEdit;
    private TextView mBtnConfirm;
    private TextView mBtnCancel;
    private Context mContext;

    public ExchangeDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ExchangeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exchange, null);
        setContentView(view);

        mExchangeEdit = view.findViewById(R.id.exchange_editText);
        mBtnConfirm = view.findViewById(R.id.exchange_confirm);
        mBtnCancel = view.findViewById(R.id.exchange_cancel);

        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exchange_confirm:
                if (mExchangeEdit.getText().toString().isEmpty()){
                    ToastUtil.getInstance(mContext).showShortToast("兑换码不能为空");
                    return;
                }
                //处理兑换逻辑
                break;

            case R.id.exchange_cancel:
                dismiss();
                break;
        }
    }


}
