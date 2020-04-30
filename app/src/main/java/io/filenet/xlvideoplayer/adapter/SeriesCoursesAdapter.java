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
import io.filenet.xlvideoplayer.bean.SeriesCourses;
import io.filenet.xlvideoplayer.ui.activity.VideoPlayActivity;
import io.filenet.xlvideoplayer.utils.SystemUtil;

public abstract class SeriesCoursesAdapter extends RecyclerView.Adapter<SeriesCoursesAdapter.ViewHolder> {
    private Context mContext;
    private int srceenWidth;
    private LinearLayout.LayoutParams params;
    private List<SeriesCourses> seriesInfoList;

    public SeriesCoursesAdapter(Context context, List<SeriesCourses> seriesInfoList){
        mContext = context;
        this.seriesInfoList = seriesInfoList;
        srceenWidth = SystemUtil.getScreenWidth(mContext)/2 - SystemUtil.dip2px(mContext,40);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (srceenWidth*9/16+SystemUtil.dip2px(mContext,45)));
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mSeriesName;
        TextView mSeriesMore;
        LinearLayout mLlView01;
        LinearLayout mLlView02;
        CardView mSeriesView01;
        CardView mSeriesView02;
        CardView mSeriesView03;
        CardView mSeriesView04;
        ImageView mSeriesImg01;
        ImageView mSeriesImg02;
        ImageView mSeriesImg03;
        ImageView mSeriesImg04;
        TextView mSeriesText01;
        TextView mSeriesText02;
        TextView mSeriesText03;
        TextView mSeriesText04;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSeriesName = itemView.findViewById(R.id.series_name);
            mSeriesMore = itemView.findViewById(R.id.series_more);
            mLlView01 = itemView.findViewById(R.id.ll_view_01);
            mLlView02 = itemView.findViewById(R.id.ll_view_02);
            mSeriesView01 = itemView.findViewById(R.id.series_view_01);
            mSeriesView02 = itemView.findViewById(R.id.series_view_02);
            mSeriesView03 = itemView.findViewById(R.id.series_view_03);
            mSeriesView04 = itemView.findViewById(R.id.series_view_04);
            mSeriesImg01 = itemView.findViewById(R.id.series_image_view_01);
            mSeriesImg02 = itemView.findViewById(R.id.series_image_view_02);
            mSeriesImg03 = itemView.findViewById(R.id.series_image_view_03);
            mSeriesImg04 = itemView.findViewById(R.id.series_image_view_04);
            mSeriesText01 = itemView.findViewById(R.id.series_text_view_01);
            mSeriesText02 = itemView.findViewById(R.id.series_text_view_02);
            mSeriesText03 = itemView.findViewById(R.id.series_text_view_03);
            mSeriesText04 = itemView.findViewById(R.id.series_text_view_04);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_series_courses, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mLlView01.setLayoutParams(params);
        if (seriesInfoList.get(position).getSeriesVideoNum() > 2) {
            holder.mLlView02.setVisibility(View.VISIBLE);
            holder.mLlView02.setLayoutParams(params);
        }else
            holder.mLlView02.setVisibility(View.GONE);
        holder.mSeriesName.setText(seriesInfoList.get(position).getSeriesName());
        holder.mSeriesMore.setVisibility(View.GONE);
        switch (seriesInfoList.get(position).getSeriesVideoNum()){
            case 1:
                holder.mSeriesView02.setVisibility(View.GONE);
                holder.mSeriesView03.setVisibility(View.GONE);
                holder.mSeriesView04.setVisibility(View.GONE);
                holder.mSeriesImg01.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoImg());
                holder.mSeriesText01.setText(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                holder.mSeriesView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                    }
                });
                break;
            case 2:
                holder.mSeriesView03.setVisibility(View.GONE);
                holder.mSeriesView04.setVisibility(View.GONE);
                holder.mSeriesImg01.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoImg());
                holder.mSeriesText01.setText(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                holder.mSeriesView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                    }
                });
                holder.mSeriesImg02.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoImg());
                holder.mSeriesText02.setText(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                holder.mSeriesView02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                    }
                });
                break;
            case 3:
                holder.mSeriesView04.setVisibility(View.GONE);
                holder.mSeriesImg01.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoImg());
                holder.mSeriesText01.setText(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                holder.mSeriesView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                    }
                });
                holder.mSeriesImg02.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoImg());
                holder.mSeriesText02.setText(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                holder.mSeriesView02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                    }
                });
                holder.mSeriesImg03.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoImg());
                holder.mSeriesText03.setText(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoName());
                holder.mSeriesView03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(2).getVideoName());
                    }
                });
                break;
            default:
                if (seriesInfoList.get(position).getSeriesVideoNum() > 4){
                    holder.mSeriesMore.setText("查看更多 >");
                    holder.mSeriesMore.setVisibility(View.VISIBLE);
                    holder.mSeriesMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seeAll(position);
                        }
                    });
                }
                holder.mSeriesImg01.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoImg());
                holder.mSeriesText01.setText(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                holder.mSeriesView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(0).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(0).getVideoName());
                    }
                });
                holder.mSeriesImg02.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoImg());
                holder.mSeriesText02.setText(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                holder.mSeriesView02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(1).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(1).getVideoName());
                    }
                });
                holder.mSeriesImg03.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoImg());
                holder.mSeriesText03.setText(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoName());
                holder.mSeriesView03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(2).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(2).getVideoName());
                    }
                });
                holder.mSeriesImg04.setImageResource(seriesInfoList.get(position).getVideoInfoList().get(3).getVideoImg());
                holder.mSeriesText04.setText(seriesInfoList.get(position).getVideoInfoList().get(3).getVideoName());
                holder.mSeriesView04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(seriesInfoList.get(position).getVideoInfoList().get(3).getVideoUrl(),
                                seriesInfoList.get(position).getVideoInfoList().get(3).getVideoName());
                    }
                });
                break;
        }
    }


    private void playVideo(String url, String videoName){
        Intent intent = new Intent(mContext, VideoPlayActivity.class);
        intent.putExtra("videoUrl", url);
        intent.putExtra("videoName", videoName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return seriesInfoList.size();
    }

    public abstract void seeAll(int position);

}
