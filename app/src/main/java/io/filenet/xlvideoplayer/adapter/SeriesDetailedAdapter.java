package io.filenet.xlvideoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.bean.SingleVideoInfo;
import io.filenet.xlvideoplayer.ui.activity.VideoPlayActivity;
import io.filenet.xlvideoplayer.utils.SystemUtil;

public class SeriesDetailedAdapter extends RecyclerView.Adapter<SeriesDetailedAdapter.ViewHolder> {

    private Context mContext;
    private List<SingleVideoInfo> singleVideoInfoList;
    LinearLayout.LayoutParams params;
    private int screenWidth;

    public SeriesDetailedAdapter(Context context, List<SingleVideoInfo> singleVideoInfoList){
        mContext = context;
        this.singleVideoInfoList = singleVideoInfoList;
        screenWidth = (SystemUtil.getScreenWidth(mContext) - SystemUtil.dip2px(mContext,50))/2
                - SystemUtil.dip2px(mContext,10);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,screenWidth*9/16
                + SystemUtil.dip2px(mContext,30));
    }

    public void updateSeriesDetailed(List<SingleVideoInfo> singleVideoInfoList){
        this.singleVideoInfoList = singleVideoInfoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mVideoImg;
        TextView mVideoName;
        CardView mSeriesDetailedCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoImg = itemView.findViewById(R.id.detailed_video_img);
            mVideoName = itemView.findViewById(R.id.detailed_video_name);
            mSeriesDetailedCardView = itemView.findViewById(R.id.series_detailed_card_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_series_detailed, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mSeriesDetailedCardView.setLayoutParams(params);
        holder.mVideoName.setText(singleVideoInfoList.get(position).getVideoName());
        holder.mVideoImg.setImageResource(singleVideoInfoList.get(position).getVideoImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra("videoUrl", singleVideoInfoList.get(position).getVideoUrl());
                intent.putExtra("videoName", singleVideoInfoList.get(position).getVideoName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleVideoInfoList.size();
    }
}
