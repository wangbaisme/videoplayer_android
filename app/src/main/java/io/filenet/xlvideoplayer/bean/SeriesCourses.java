package io.filenet.xlvideoplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SeriesCourses implements Parcelable {

    private String seriesName;
    private int seriesVideoNum = 0;
    private List<SingleVideoInfo> videoInfoList;

    protected SeriesCourses(Parcel in) {
        seriesName = in.readString();
        seriesVideoNum = in.readInt();
        videoInfoList = in.createTypedArrayList(SingleVideoInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seriesName);
        dest.writeInt(seriesVideoNum);
        dest.writeTypedList(videoInfoList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SeriesCourses> CREATOR = new Creator<SeriesCourses>() {
        @Override
        public SeriesCourses createFromParcel(Parcel in) {
            return new SeriesCourses(in);
        }

        @Override
        public SeriesCourses[] newArray(int size) {
            return new SeriesCourses[size];
        }
    };

    public String getSeriesName() {
        return seriesName;
    }

    public int getSeriesVideoNum() {
        return seriesVideoNum;
    }

    public List<SingleVideoInfo> getVideoInfoList() {
        return videoInfoList;
    }

    public SeriesCourses(String seriesName, int seriesVideoNum, List<SingleVideoInfo> videoInfoList){
        this.seriesName = seriesName;
        this.seriesVideoNum = seriesVideoNum;
        this.videoInfoList = videoInfoList;
    }

}
