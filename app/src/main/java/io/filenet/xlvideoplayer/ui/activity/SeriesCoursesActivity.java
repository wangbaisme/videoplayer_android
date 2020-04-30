package io.filenet.xlvideoplayer.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.filenet.player.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.adapter.SeriesCoursesAdapter;
import io.filenet.xlvideoplayer.adapter.SeriesDetailedAdapter;
import io.filenet.xlvideoplayer.bean.SeriesCourses;
import io.filenet.xlvideoplayer.bean.SingleVideoInfo;
import io.filenet.xlvideoplayer.utils.SystemUtil;

public class SeriesCoursesActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.series_courses_back)
    ImageView mSeriesBack;
    @BindView(R.id.series_courses_recycle)
    RecyclerView mSeriesRecycle;
    @BindView(R.id.ll_no_click)
    LinearLayout mLlNoClick;
    @BindView(R.id.series_detailed_view)
    LinearLayout mSeriesDetailedView;
    @BindView(R.id.current_series_name)
    TextView mCurrentSeriesName;
    @BindView(R.id.current_series_close)
    ImageView mCurrentSeriesClose;
    @BindView(R.id.series_detailed_recycle)
    RecyclerView mSeriesDetailedRecycle;

    private SeriesCoursesAdapter mSeriesAdapter;
    private SeriesDetailedAdapter mDetailedAdapter;
    private List<SeriesCourses> seriesCoursesList = new ArrayList<>();
    private List<SingleVideoInfo> instantiateList = new ArrayList<>();
    @Override
    public void onClick(View v) {
        stopMenuAnimation();
    }

    public void onCreate(Bundle saveInstanceStaet){
        super.onCreate(saveInstanceStaet);
        SystemUtil.hideSupportActionBar(this, true , false);
        setContentView(R.layout.activity_series_courses);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mSeriesBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mSeriesRecycle.setLayoutManager(mLayoutManager);
        seriesCoursesList.clear();
        for (int i=0; i<4; i++){
            seriesCoursesList.add(getSeriesCourses());
        }
        mSeriesAdapter = new SeriesCoursesAdapter(this, seriesCoursesList) {
            @Override
            public void seeAll(int position) {
                startMenuAnimation();
                mCurrentSeriesName.setText(seriesCoursesList.get(position).getSeriesName());
                mDetailedAdapter.updateSeriesDetailed(seriesCoursesList.get(position).getVideoInfoList());
                mDetailedAdapter.notifyDataSetChanged();
            }
        };
        mSeriesRecycle.setAdapter(mSeriesAdapter);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mSeriesDetailedRecycle.setLayoutManager(mManager);
        mDetailedAdapter = new SeriesDetailedAdapter(this, instantiateList);
        mSeriesDetailedRecycle.setAdapter(mDetailedAdapter);

        mCurrentSeriesClose.setOnClickListener(this);
    }

    private SeriesCourses getSeriesCourses(){
        int index = new Random().nextInt(6);
        if (index == 0) index = 6;
        String name = "单元"+index;
        SeriesCourses seriesCourses;
        seriesCourses = new SeriesCourses(name, index, getVideoInfoList(index));
        return seriesCourses;
    }

    private List<SingleVideoInfo> getVideoInfoList(int index){
        List<SingleVideoInfo> mVideoInfoList = new ArrayList<>();
        for (int i=0; i<index; i++){
            if (index % 3 == 0){
                if (i%3 == 0) mVideoInfoList.add(new SingleVideoInfo("有一天你会明白",R.mipmap.img_2,mUrls[2]));
                if (i%3 == 1) mVideoInfoList.add(new SingleVideoInfo("有些人不再回来",R.mipmap.img_3,mUrls[1]));
                if (i%3 == 2) mVideoInfoList.add(new SingleVideoInfo("就像你曾经追问的爱与不爱",R.mipmap.img_4,mUrls[0]));
            }else if (index % 3 == 1){
                if (i%3 == 0) mVideoInfoList.add(new SingleVideoInfo("有几人看不出来",R.mipmap.img_3,mUrls[1]));
                if (i%3 == 1) mVideoInfoList.add(new SingleVideoInfo("有几人还不能释怀",R.mipmap.img_4,mUrls[0]));
                if (i%3 == 2) mVideoInfoList.add(new SingleVideoInfo("别为难 我还不算难堪",R.mipmap.img_2,mUrls[2]));
            }else {
                if (i%3 == 0) mVideoInfoList.add(new SingleVideoInfo("自顾自地对白",R.mipmap.img_4,mUrls[0]));
                if (i%3 == 1) mVideoInfoList.add(new SingleVideoInfo("日子不算好不算坏",R.mipmap.img_2,mUrls[2]));
                if (i%3 == 2) mVideoInfoList.add(new SingleVideoInfo("念旧的灯把回忆点亮几段",R.mipmap.img_3,mUrls[1]));
            }
        }
        return mVideoInfoList;
    }

    String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
//            "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8",
//            "http://192.168.1.116/hls/guoyuenc.m3u8",
//            "http://192.168.1.116/hls/guoyu.m3u8",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };

    private void startMenuAnimation() {
        if (mLlNoClick.getVisibility() != View.VISIBLE) mLlNoClick.setVisibility(View.VISIBLE);
        if (mSeriesDetailedView.getVisibility() != View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    mSeriesDetailedView,
                    "TranslationY",
                    mSeriesDetailedView.getHeight(),
                    0);
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mSeriesDetailedView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
//                    mIsMenuAnimationStart = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mSeriesDetailedView.setVisibility(View.INVISIBLE);
                    if (mLlNoClick.getVisibility() == View.VISIBLE)
                        mLlNoClick.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }

    private void stopMenuAnimation() {
        if (mSeriesDetailedView.getVisibility() == View.VISIBLE) {
            ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                    mSeriesDetailedView,
                    "TranslationY",
                    0,
                    mSeriesDetailedView.getHeight());
            mAnimatorY.setDuration(500);
            mAnimatorY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
//                    mIsMenuAnimationStart = false;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mSeriesDetailedView.setVisibility(View.INVISIBLE);
                    if (mLlNoClick.getVisibility() == View.VISIBLE)
                        mLlNoClick.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mSeriesDetailedView.setVisibility(View.INVISIBLE);
                    if (mLlNoClick.getVisibility() == View.VISIBLE)
                        mLlNoClick.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mAnimatorY.start();
        }
    }
}

