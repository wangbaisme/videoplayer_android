package io.filenet.xlvideoplayer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.filenet.xlvideoplayer.R;

public class RankingFragment extends BaseFragment{

    private static RankingFragment mRankingFragment;

    private RankingFragment(){}

    public synchronized static RankingFragment newInstance(){
        if (mRankingFragment == null)
            mRankingFragment = new RankingFragment();
        return mRankingFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        return view;
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }
}
