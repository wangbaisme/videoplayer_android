package io.filenet.xlvideoplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoInfo implements Parcelable {
    private String videoName;
    private int videoImg;
    private String videoUrl;

    private boolean exchangeState;

    protected VideoInfo(Parcel in) {
        videoName = in.readString();
        videoImg = in.readInt();
        videoUrl = in.readString();
        exchangeState = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoName);
        dest.writeInt(videoImg);
        dest.writeString(videoUrl);
        dest.writeByte((byte) (exchangeState ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    public String getmVideoName() {
        return videoName;
    }
    public int getmVideoImg() {
        return videoImg;
    }
    public String getmVideoUrl() {return videoUrl;}
    public boolean isExchangeState() {
        return exchangeState;
    }

    public VideoInfo(String mVideoName, int mVideoImg, String mVideoUrl, boolean mExchangeState){
        this.videoName = mVideoName;
        this.videoImg = mVideoImg;
        this.videoUrl = mVideoUrl;
        this.exchangeState = mExchangeState;
    }
}
