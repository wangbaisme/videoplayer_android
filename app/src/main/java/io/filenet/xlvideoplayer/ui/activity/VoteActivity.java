package io.filenet.xlvideoplayer.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import io.filenet.xlvideoplayer.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class VoteActivity extends AppCompatActivity {

    private long expired;
    private TextView tv;
    private TimePickerView pvTime;
    private Button update;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        selectData();
    }

    public static long getExpiredTime(String dateStr){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateTmp = null;
        try
        {
            dateTmp = dateFormat.parse(dateStr);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        calendar.setTime(dateTmp);
        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar2.setTime(dateTmp);
        calendar2.add(Calendar.MONTH, 1);
        long expiredTime1 = calendar2.getTimeInMillis() - calendar.getTimeInMillis();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH)-2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        long expiredTime2 = calendar2.getTimeInMillis() - calendar.getTimeInMillis();
        return expiredTime1 < expiredTime2 ? expiredTime1 : expiredTime2;
    }

    public static boolean isOneDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        long a = date.getTime() - 28800000;
        if (format.format(new Date(a)).equals("20191231")){
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static boolean isMayOneDayUtilSixDay(String ymd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try{
            date = format.parse(ymd);
            DateFormat df1 = DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.CHINA);
            df1.setTimeZone(TimeZone.getTimeZone("UTC"));
            String m = df1.format(date).split("年")[1].split("月")[0];
            String d = df1.format(date).split("年")[1].split("月")[1].split("日")[0];
            if (Integer.valueOf(m) == 5 &&
                Integer.valueOf(d) >= 1 && Integer.valueOf(d) <= 6)
                return true;
        }catch (Exception e){
        }
        return false;
    }

    public void updateExpired(String date){
        if(isOneDay()) {
            expired = 241920;
        }else if (isMayOneDayUtilSixDay(date)){
            expired = 190080;
        } else{
            expired = getExpiredTime(date) / 10000;
        }
        tv.setText("区块链高度 ： "+expired);
    }

    private void selectData(){
        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = et.getText().toString();
                if (date!=null && !date.isEmpty()){
//                    main(date);
                    updateExpired(date);
                }

            }
        });
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019, 4, 13);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2059, 11, 28);

        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    public static void main(String args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        long time = dateFormat.parse(args, new ParsePosition(0)).getTime();
        Log.e("maintest","获取指定时间的时间戳:" + time);
    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult){
        String tag = "android:switcher:"+viewPager.getId()+":"+position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? fragment : defaultResult;
    }
}
