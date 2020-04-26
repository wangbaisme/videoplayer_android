package io.filenet.xlvideoplayer.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.filenet.player.player.VideoPlayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.adapter.VideoAdapter;
import io.filenet.xlvideoplayer.bean.VideoInfo;
import io.filenet.xlvideoplayer.utils.AudioMngHelper;
import io.filenet.xlvideoplayer.utils.NetUtil;
import io.filenet.xlvideoplayer.utils.SystemUtil;
import io.filenet.xlvideoplayer.utils.TimeUtil;
import io.filenet.xlvideoplayer.utils.ToastUtil;
import io.filenet.xlvideoplayer.view.VerticalProgress;
import io.filenet.xlvideoplayer.view.ViewControlFrameLayout;

public class PointControlActivity extends BasicActivity implements View.OnClickListener, VideoPlayer.OnVideoPlayerStateListener
        , ViewControlFrameLayout.OnTwoPointEventListener {
    private final String TAG = PointControlActivity.class.getSimpleName();

    @BindView(R.id.video_view_control)
    ViewControlFrameLayout mVideoControl;
    @BindView(R.id.ll_change)
    LinearLayout mLlChange;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.ll_full_list_control)
    LinearLayout mVideoListViewFull;
    @BindView(R.id.loading_video_list)
    LinearLayout mLoadingTip;
    @BindView(R.id.btn_full)
    TextView mBtnFull;
    @BindView(R.id.btn_full_show_list)
    TextView mBtnShowViewList;
    @BindView(R.id.btn_full_hide_list)
    TextView mBtnHideVeiwList;
    @BindView(R.id.view_video_player)
    VideoPlayer mVideoPlayer;
    @BindView(R.id.video_list_view)
    RecyclerView mVideoListView;
    @BindView(R.id.speed_bar)
    SeekBar mSpeedBar;
    @BindView(R.id.tv_current_time)
    TextView mCurrentTimeText;
    @BindView(R.id.tv_count_time)
    TextView mMaxTimeText;
    @BindView(R.id.video_control_tail)
    LinearLayout mVideoControlTail;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.brightness_bar)
    VerticalProgress mBrightnessBar;
    @BindView(R.id.volume_bar)
    VerticalProgress mVolumeBar;
    @BindView(R.id.speed_view)
    LinearLayout mSpeedView;
    @BindView(R.id.time_text)
    TextView mTimeText;
    @BindView(R.id.time_text_bar)
    ProgressBar mTimeTextBar;
    @BindView(R.id.buffer_loading)
    RelativeLayout mBufferLoadingView;
    @BindView(R.id.buffer_speed)
    TextView mBufferSpeed;

    private Context mContext;
    private List<VideoInfo> mVideoInfoList = new ArrayList<>();
    private AudioMngHelper mAudioManager;
    private VideoAdapter mAdapter;
    private boolean isWindowFull = false;
    private boolean isShowVideoList_Full = false;
    private boolean isVideoControlHide = false;
    private boolean isVideoPause = false;
    private boolean isVideoPrepared = false;
    private boolean isloading = false;
    private long lastHideVolumeBarTime = 0;
    private long lastHideControlTime = 0;
    private long lastHideBrightnessBarTime = 0;
    private long lastShowBufferloadingTime = 0;
    private long mVideoCurrentTime = -1;
    private float mVideoContentRatioXY = 0;
    private int mVideoViewWidth, mVideoViewHeight;
    private int mAppCurrentBrightness = -1;
    private int mAudioStep = -1;
    private int mCurrentChangeVolume = 0;
    private int mCurrentVolume = -1;
    private int onGlobalIndex = 0;

    String [] mUrls = {
//            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
//            "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8",
            "http://192.168.1.107/hls/guoyu.m3u8",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };

    @Override
    public void videoStart() {
        if (mVideoPlayer == null) return;
        mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
    }

    @Override
    public void videoReset() {
        isVideoPrepared = false;
    }

    @Override
    public void videoState(int stateCode) {
        switch (stateCode){
            case 701: //开始缓冲
                mHandle.sendEmptyMessage(BUFFERSTART);
                break;
            case 702: //缓存结束
                mHandle.sendEmptyMessage(BUFFEREND);
                break;
            case 703: //网络带宽，网速方面
                break;
            case -10000: ///数据连接中断
                mHandle.sendEmptyMessage(DATAINTERRUPT);
                break;
        }
    }

    @Override
    public void videoCompletion() {
        mHandle.sendEmptyMessage(VIDEOCOMPLETION);
    }

    @Override
    public void videoPrepared() {
        isVideoPrepared = true;
        mHandle.sendEmptyMessage(UPDATEVIDEOCOUNTTIME);
        mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
    }

    @Override
    public void videoChange(float width, float height) {
        mVideoContentRatioXY = width / height;
        getVideoPlayerNewSize();
        changeViewTree();
    }

    @Override
    public void twoPointEvent(boolean isShow) {
        if (!isWindowFull) return;
        if (isShow)
            mHandle.sendEmptyMessage(FULLSHOWLIST);
        else
            mHandle.sendEmptyMessage(FULLHIDELIST);
    }

    @Override
    public void onTwoClick() {
        if (mVideoPlayer == null) return;
        if (mVideoPlayer.isPlaying())
            mVideoPlayer.pause();
        else
            mVideoPlayer.start();
    }

    @Override
    public void onOneClick() {
        mHandle.sendEmptyMessage(CHANGEVIDEOCONTROLSTATE);
    }

    @Override
    public void volumeEventFinish() {
        mCurrentVolume = -1; //此处设为-1是为了下次音量调节动画更精准
        lastHideVolumeBarTime = System.currentTimeMillis();
        mHandle.postDelayed(autoTaskHideVolumeBar,1000);
    }

    @Override
    public void brightnessEventFinish() {
        lastHideBrightnessBarTime = System.currentTimeMillis();
        mHandle.postDelayed(autoTaskHideBrightnessBar,1000);
    }

    @Override
    public void volumeEvent(boolean add, int percentage) {
        if (percentage < 3) return;
        if (mCurrentVolume == -1) mCurrentVolume = mAudioManager.get100CurrentVolume();
        percentage = (int)Math.ceil((float)(percentage-1)/2.55);
        if (add) mCurrentVolume = mCurrentVolume + percentage > 100 ? 100 : mCurrentVolume + percentage;
        else mCurrentVolume = mCurrentVolume - percentage < 0 ? 0 : mCurrentVolume - percentage;
        if (System.currentTimeMillis() - lastHideVolumeBarTime < 1000) mHandle.removeCallbacks(autoTaskHideVolumeBar);
        if (mVolumeBar.getVisibility() == View.GONE) mVolumeBar.setVisibility(View.VISIBLE);
        mVolumeBar.setProgress(mCurrentVolume);
        if (mAudioStep == -1)
            mAudioStep = (int) Math.ceil(100/mAudioManager.getSystemMaxVolume());
        mCurrentChangeVolume += percentage;
        if (mCurrentChangeVolume < mAudioStep) return;
        int mVolume = 0;
        if (add)
            mVolume = mAudioManager.get100CurrentVolume() + mCurrentChangeVolume > 100 ? 100 :
                    mAudioManager.get100CurrentVolume() + mCurrentChangeVolume;
        else
            mVolume = mAudioManager.get100CurrentVolume() - mCurrentChangeVolume < 0 ? 0 :
                    mAudioManager.get100CurrentVolume() - mCurrentChangeVolume;
        mAudioManager.setVoice100(mVolume);
        mCurrentChangeVolume = 0;
    }

    @Override
    public void brightnessEvent(boolean add, int percentage) {
        if (percentage < 3) return;
        if (mAppCurrentBrightness == -1)
            mAppCurrentBrightness = SystemUtil.getSystemBrightness(mContext);
        if (add)
            mAppCurrentBrightness = mAppCurrentBrightness + (percentage-1) > 255 ? 255 : mAppCurrentBrightness + (percentage-1);
        else
            mAppCurrentBrightness = mAppCurrentBrightness - (percentage-1) < 0 ? 0 : mAppCurrentBrightness - (percentage-1);
        if (System.currentTimeMillis() - lastHideBrightnessBarTime < 1000) mHandle.removeCallbacks(autoTaskHideBrightnessBar);
        if (mBrightnessBar.getVisibility() == View.GONE) mBrightnessBar.setVisibility(View.VISIBLE);
        mBrightnessBar.setProgress((int)Math.ceil(mAppCurrentBrightness/2.55));
        SystemUtil.changeAPPBrightness(mContext, mAppCurrentBrightness);
    }

    @Override
    public void progressTextEvent(boolean add, int percentage) {
        if (percentage < 2 && !isVideoPrepared) return;
        if (mSpeedView.getVisibility() == View.GONE) mSpeedView.setVisibility(View.VISIBLE);
        if (mVideoCurrentTime == -1) mVideoCurrentTime = mVideoPlayer.getCurrentPosition();
        if (add) mVideoCurrentTime = mVideoCurrentTime + (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500) > mVideoPlayer.getDuration() ?
                mVideoPlayer.getDuration() : mVideoCurrentTime + (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500);
        else mVideoCurrentTime = mVideoCurrentTime - (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500) < 0 ?
                0 : mVideoCurrentTime - (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500);
        mTimeText.setText(TimeUtil.getTime(mVideoCurrentTime));
        mTimeTextBar.setProgress((int)(mVideoCurrentTime*500/mVideoPlayer.getDuration()));
    }

    @Override
    public void progressSeekEvent() {
        if (mTimeTextBar == null) return;
        mVideoPlayer.seekTo((long) Math.ceil(mVideoPlayer.getDuration()*mTimeTextBar.getProgress()/500));
        mSpeedView.setVisibility(View.GONE);
    }

    private final int LAODMORE = 1001;
    private final int SHOWLOAD = 1002;
    private final int HIDELOAD = 1003;
    private final int SHOWLAND = 1004;
    private final int SHOWPORT = 1005;
    private final int FULLHIDELIST = 1006;
    private final int FULLSHOWLIST = 1007;
    private final int UPDATEVIDEOCOUNTTIME = 1008;
    private final int UPDATEVIDEOCURRENTTIME = 1009;
    private final int VIDEOCOMPLETION = 1010;
    private final int CHANGEVIDEOCONTROLSTATE = 1011;
    private final int BUFFERSTART = 1012;
    private final int BUFFEREND = 1013;
    private final int DATAINTERRUPT = 1014;
    @SuppressLint("HandlerLeak")
    Handler mHandle = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LAODMORE:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isloading) return;
                            mAdapter.update(getVideoInfoList(mVideoInfoList.size()/18));
                            mAdapter.notifyDataSetChanged();
                            mHandle.sendEmptyMessage(HIDELOAD);
                        }
                    });
                    break;
                case SHOWLOAD:
                    isloading = true;
                    mLoadingTip.setVisibility(View.VISIBLE);
                    break;
                case HIDELOAD:
                    isloading = false;
                    mLoadingTip.setVisibility(View.GONE);
                    break;
                case SHOWLAND:
                    isWindowFull = true;
                    getVideoPlayerNewSize();
                    mBtnFull.setText("port");
                    mLlContent.setVisibility(View.GONE);
                    mBtnShowViewList.setVisibility(View.VISIBLE);
                    SystemUtil.hideFulltStatusBar(mContext);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case SHOWPORT:
                    isWindowFull = false;
                    isShowVideoList_Full = false;
                    getVideoPlayerNewSize();
                    mBtnFull.setText("land");
                    mLlContent.setVisibility(View.VISIBLE);
                    mVideoListViewFull.setVisibility(View.GONE);
                    mBtnShowViewList.setVisibility(View.GONE);
                    SystemUtil.showNotFulltStatusBar(mContext);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case FULLHIDELIST:
                    isShowVideoList_Full = false;
                    getVideoPlayerNewSize();
                    mBtnShowViewList.setVisibility(View.VISIBLE);
                    mVideoListViewFull.setVisibility(View.GONE);
                    break;
                case FULLSHOWLIST:
                    isShowVideoList_Full = true;
                    getVideoPlayerNewSize();
                    mBtnShowViewList.setVisibility(View.GONE);
                    mVideoListViewFull.setVisibility(View.VISIBLE);
                    break;
                case UPDATEVIDEOCOUNTTIME:
                    mMaxTimeText.setText(TimeUtil.getTime(mVideoPlayer.getDuration()));
                    break;
                case UPDATEVIDEOCURRENTTIME:
                    mCurrentTimeText.setText(TimeUtil.getTime(mVideoPlayer.getCurrentPosition()));
                    if (mVideoPlayer.getDuration() == 0)
                        mSpeedBar.setProgress(0);
                    else
                        mSpeedBar.setProgress((int)(mVideoPlayer.getCurrentPosition()*100/mVideoPlayer.getDuration()));
                    mHandle.postDelayed(autoUpdateTime,1000);
                    break;
                case VIDEOCOMPLETION:
                    mCurrentTimeText.setText(TimeUtil.getTime(mVideoPlayer.getDuration()));
                    mSpeedBar.setProgress(100);
                    break;
                case CHANGEVIDEOCONTROLSTATE:
                    if (!isVideoControlHide){
                        isVideoControlHide = true;
                        mVideoControlTail.setVisibility(View.GONE);
                        if (System.currentTimeMillis() - lastHideControlTime < 5000){
                            lastHideControlTime = 0;
                            mHandle.removeCallbacks(autoTaskHideControl);
                        }
                        break;
                    }
                    isVideoControlHide = false;
                    mVideoControlTail.setVisibility(View.VISIBLE);
                    lastHideControlTime = System.currentTimeMillis();
                    mHandle.postDelayed(autoTaskHideControl,5000);
                    break;
                case BUFFERSTART:
                    if (mBufferLoadingView.getVisibility() == View.GONE) mBufferLoadingView.setVisibility(View.VISIBLE);
                    lastShowBufferloadingTime = System.currentTimeMillis();
                    mBufferSpeed.setText(NetUtil.getNetSpeed(getApplicationInfo().uid));
                    mHandle.postDelayed(autoUpdateBufferSpeed,2000);
                    break;
                case BUFFEREND:
                    if (mBufferLoadingView.getVisibility() == View.VISIBLE) mBufferLoadingView.setVisibility(View.GONE);
                    if (System.currentTimeMillis() - lastShowBufferloadingTime < 2000) mHandle.removeCallbacks(autoUpdateBufferSpeed);
                    break;
                case DATAINTERRUPT:
                    ToastUtil.getInstance(mContext).showShortToast(getString(R.string.data_interrupt));
                    break;
            }
        }
    };

    Runnable autoUpdateTime = new Runnable() {
        @Override
        public void run() {
            if (mVideoPlayer == null) return;
            if (mVideoPlayer.isPlaying())
                mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
        }
    };
    Runnable autoTaskHideControl = new Runnable() {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(CHANGEVIDEOCONTROLSTATE);
        }
    };
    Runnable autoTaskHideVolumeBar = new Runnable() {
        @Override
        public void run() {
            mVolumeBar.setVisibility(View.GONE);
        }
    };
    Runnable autoTaskHideBrightnessBar = new Runnable() {
        @Override
        public void run() {
            mBrightnessBar.setVisibility(View.GONE);
        }
    };
    Runnable autoUpdateBufferSpeed = new Runnable() {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(BUFFERSTART);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mContext = this;
        SystemUtil.hideSupportActionBar(mContext,true,true);
        SystemUtil.keepScreenOn(mContext);
        setContentView(R.layout.activity_point_control);
        ButterKnife.bind(this);
        init();
        setListener();
        loadVideo(mUrls[0]);
    }

    public void onResume(){
        super.onResume();
        if (mVideoPlayer == null) return;
        if (!mVideoPlayer.isPlaying() && isVideoPause){
            isVideoPause = false;
            mVideoPlayer.start();
        }
    }

    public void onPause(){
        super.onPause();
        if (mVideoPlayer == null) return;
        if (mVideoPlayer.isPlaying()){
            isVideoPause = true;
            mVideoPlayer.pause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer == null) return;
        mVideoPlayer.stop();
        mVideoPlayer.release();
    }

    private void init(){
        mAudioManager = new AudioMngHelper(mContext);
        lastHideControlTime = System.currentTimeMillis();
        mHandle.postDelayed(autoTaskHideControl,5000);
    }

    @SuppressLint("SetTextI18n")
    private void setListener(){
        mBtnFull.setOnClickListener(this);
        mBtnShowViewList.setOnClickListener(this);
        mBtnHideVeiwList.setOnClickListener(this);
        mVideoPlayer.setOnVideoPlayerStateListener(this);
        mVideoControl.setTwoPointEventListener(this);
        mBtnBack.setOnClickListener(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mVideoListView.setLayoutManager(mLinearLayoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new VideoAdapter(getVideoInfoList(0));
                mAdapter.setOnCompleteListener(new VideoAdapter.OnCompleteListener() {
                    @Override
                    public void loadMore() {
                        mHandle.sendEmptyMessage(LAODMORE);
                    }

                    @Override
                    public void resetVideoContent(VideoInfo mVideoInfo) {
                        mVideoPlayer.reset();
                        mVideoPlayer.setPath(mVideoInfo.getmVideoUrl());
                        try {
                            mVideoPlayer.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mVideoListView.setAdapter(mAdapter);
                mHandle.sendEmptyMessage(HIDELOAD);
            }
        }).start();

        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentTimeText.setText(TimeUtil.getTime(progress*mVideoPlayer.getDuration()/100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (System.currentTimeMillis() - lastHideControlTime < 5000)
                    mHandle.removeCallbacks(autoTaskHideControl);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoPlayer.seekTo(seekBar.getProgress()*mVideoPlayer.getDuration()/100);
                mHandle.postDelayed(autoTaskHideControl,5000);
            }
        });
    }

    private void loadVideo(String mUrl){
        try {
            mVideoPlayer.setPath(mUrl);
            mVideoPlayer.load();
        } catch (IOException e) {
            ToastUtil.getInstance(mContext).showShortToast(getString(R.string.playback_failed));
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_full:
                if (!isWindowFull)
                    mHandle.sendEmptyMessage(SHOWLAND);
                else
                    mHandle.sendEmptyMessage(SHOWPORT);
                break;
            case R.id.btn_full_show_list:
                mHandle.sendEmptyMessage(FULLSHOWLIST);
                break;
            case R.id.btn_full_hide_list:
                mHandle.sendEmptyMessage(FULLHIDELIST);
                break;
            case R.id.btn_back:
                if (isWindowFull) {
                    mHandle.sendEmptyMessage(SHOWPORT);
                    break;
                }
                finish();
                break;
            default:
                break;
        }
    }

    private synchronized List<VideoInfo> getVideoInfoList(int index){
        mHandle.sendEmptyMessage(SHOWLOAD);
        for (int i=0+index*18; i<18+index*18; i++){
            if (i%3 == 0) mVideoInfoList.add(new VideoInfo("这是一个很长很长很长的标题 ---- "+i,R.mipmap.img_2,mUrls[2]));
            if (i%3 == 1) mVideoInfoList.add(new VideoInfo("这是一个很长很长很长的标题 ---- "+i,R.mipmap.img_3,mUrls[1]));
            if (i%3 == 2) mVideoInfoList.add(new VideoInfo("这是一个很长很长很长的标题 ---- "+i,R.mipmap.img_4,mUrls[0]));
        }
        return mVideoInfoList;
    }

    private void getVideoPlayerNewSize(){
        ViewTreeObserver vto = mVideoPlayer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onGlobalIndex++;
                if (onGlobalIndex == 3){
                    onGlobalIndex = 0;
                    mVideoPlayer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mVideoPlayer.setLayoutParams(getVideoPlayerParams());
            }
        });
    }

    private void getRootViewDisplay(){
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        this.mVideoViewWidth = outMetrics.widthPixels;
        this.mVideoViewHeight = outMetrics.heightPixels;
    }

    private LinearLayout.LayoutParams getVideoPlayerParams(){
        LinearLayout.LayoutParams params;
        if (mVideoContentRatioXY == 0) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            return params;
        }
        getRootViewDisplay();
        if (!isWindowFull){
            mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
            params = compareRatio(this.mVideoViewWidth*3, this.mVideoViewHeight);
        }else {
            if (!isShowVideoList_Full){
                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
                params = compareRatio(this.mVideoViewWidth, this.mVideoViewHeight);
            }else {
                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth*3/4);
                params = compareRatio(this.mVideoViewWidth*3, this.mVideoViewHeight*4);
            }
        }
        return params;
    }

    private LinearLayout.LayoutParams compareRatio(float width, float height){
        LinearLayout.LayoutParams params;
        if ((width/height) <= mVideoContentRatioXY)
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    (int)(mVideoPlayer.getWidth()/mVideoContentRatioXY));
        else
            params = new LinearLayout.LayoutParams((int)(mVideoPlayer.getHeight()*mVideoContentRatioXY),
                    LinearLayout.LayoutParams.MATCH_PARENT);
        return params;
    }

    private void changeViewTree(){
        if (mLlChange.getVisibility() == View.VISIBLE)
            mLlChange.setVisibility(View.GONE);
        else
            mLlChange.setVisibility(View.VISIBLE);
    }

}
