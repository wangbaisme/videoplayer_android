package io.filenet.xlvideoplayer.bean;

public class VideoInfo {
    private String mVideoName;
    private int mVideoImg;
    private String mVideoUrl;

    public String getmVideoName() {
        return mVideoName;
    }
    public int getmVideoImg() {
        return mVideoImg;
    }
    public String getmVideoUrl() {return mVideoUrl;}

    public VideoInfo(String mVideoName, int mVideoImg, String mVideoUrl){
        this.mVideoName = mVideoName;
        this.mVideoImg = mVideoImg;
        this.mVideoUrl = mVideoUrl;
    }

}
