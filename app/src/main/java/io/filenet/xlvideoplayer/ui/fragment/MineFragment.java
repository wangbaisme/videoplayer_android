package io.filenet.xlvideoplayer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.filenet.xlvideoplayer.R;

public class MineFragment extends BaseFragment {

    private static MineFragment mMineFragment;

    private MineFragment(){}

    public static MineFragment newInstance(){
        if (mMineFragment == null)
            mMineFragment = new MineFragment();
        return mMineFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }
}
