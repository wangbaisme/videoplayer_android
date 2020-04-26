package io.filenet.xlvideoplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class HaveFouceTextView extends TextView {
    public HaveFouceTextView(Context context) {
        super(context);
    }

    public HaveFouceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HaveFouceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HaveFouceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isFocused(){
        return true;
    }
}
