package io.filenet.xlvideoplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleVideoInfo implements Parcelable {

    private String videoName;
    private int videoImg;
    private String videoUrl;

    protected SingleVideoInfo(Parcel in) {
        videoName = in.readString();
        videoImg = in.readInt();
        videoUrl = in.readString();
    }

    public static final Creator<SingleVideoInfo> CREATOR = new Creator<SingleVideoInfo>() {
        @Override
        public SingleVideoInfo createFromParcel(Parcel in) {
            return new SingleVideoInfo(in);
        }

        @Override
        public SingleVideoInfo[] newArray(int size) {
            return new SingleVideoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoName);
        dest.writeInt(videoImg);
        dest.writeString(videoUrl);
    }

    public String getVideoName() {
        return videoName;
    }

    public int getVideoImg() {
        return videoImg;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public SingleVideoInfo(String videoName, int videoImg, String videoUrl){
        this.videoName = videoName;
        this.videoImg = videoImg;
        this.videoUrl = videoUrl;
    }
}
