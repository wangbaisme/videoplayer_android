package io.filenet.xlvideoplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.bean.VideoInfo;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoInfo> mVideoList = new ArrayList<>();

    public VideoAdapter(List<VideoInfo> mVideoList){
        this.mVideoList = mVideoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mVideoImg;
        TextView mVideoName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoImg = itemView.findViewById(R.id.img_video);
            mVideoName = itemView.findViewById(R.id.name_video);
        }
    }

    public void update(List<VideoInfo> mVideoList){
        this.mVideoList = mVideoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        final VideoInfo mVideoInfo = mVideoList.get(position);
        holder.mVideoName.setText(mVideoInfo.getmVideoName());
        holder.mVideoImg.setImageResource(mVideoInfo.getmVideoImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCompleteListener.resetVideoContent(mVideoInfo);
            }
        });
        if (position % 18 == 17) mOnCompleteListener.loadMore();
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    private OnCompleteListener mOnCompleteListener;

    public interface OnCompleteListener{
        void loadMore();
        void resetVideoContent(VideoInfo mVideoInfo);
    }

    public void setOnCompleteListener(OnCompleteListener listener){
        this.mOnCompleteListener = listener;
    }
}
