package io.filenet.xlvideoplayer.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.adapter.ItemAdapter;
import io.filenet.xlvideoplayer.bean.ModuleInfo;
import io.filenet.xlvideoplayer.ui.activity.SeriesCoursesActivity;

public class ItemFragment extends BaseFragment {
    private final String VIDEOINFO = "videoInfo";

    private RecyclerView mVideoItem;
    private ItemAdapter mItemAdapter;
    private SwipeRefreshLayout mItemSwipeRefresh;

    private ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();

    public ItemFragment(ArrayList<ModuleInfo> moduleInfolist){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VIDEOINFO, moduleInfolist);
        setArguments(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        init(view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void init(View view){
        mVideoItem = view.findViewById(R.id.item_video);
        mItemAdapter = new ItemAdapter(getActivity(), mModuleInfoList) {
            @Override
            public void changeFragment(String videoUrl) {
                Intent intent = new Intent(getActivity(), SeriesCoursesActivity.class);
//                intent.putExtra("videoUrl",videoUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mVideoItem.setLayoutManager(mLayoutManager);
        mVideoItem.setAdapter(mItemAdapter);

       mItemSwipeRefresh = view.findViewById(R.id.item_swipe_refresh);
       mItemSwipeRefresh.setProgressBackgroundColorSchemeColor(R.color.color_82000000);
       mItemSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.color_4EA6FD);
       mItemSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       mItemSwipeRefresh.setRefreshing(false);
                   }
               },3000);
           }
       });
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {
        mItemAdapter.updateItem(mModuleInfoList);
    }

    @Override
    protected void onFragmentFirstVisible() {
        mModuleInfoList = getArguments().getParcelableArrayList(VIDEOINFO);
    }

}
