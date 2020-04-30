package io.filenet.xlvideoplayer.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.ui.fragment.MineFragment;
import io.filenet.xlvideoplayer.ui.fragment.RankingFragment;
import io.filenet.xlvideoplayer.ui.fragment.StudyFragment;
import io.filenet.xlvideoplayer.view.NoScrollViewPager;

public class MainActivity extends BasicActivity {

    @BindView(R.id.main_viewpager)
    NoScrollViewPager mMainViewPager;
    @BindView(R.id.main_tablayout)
    TabLayout mMainTabLayout;
    @BindView(R.id.ll_fill)
    LinearLayout mLlFill;

    LinearLayout.LayoutParams params;
    LinearLayout.LayoutParams params_ll;
    private FragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentlist = new ArrayList<>();
    private String[] mItems = {"排名","学习","我的"};

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth*137/750);
        params_ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, screenWidth*92/750);
        initViewPager();

    }

    private void initViewPager(){
        mMainViewPager.setNoScroll(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragmentlist.clear();
                mFragmentlist.add(instantiateFragment(mMainViewPager,0, RankingFragment.newInstance()));
                mFragmentlist.add(instantiateFragment(mMainViewPager,1, StudyFragment.newInstance()));
                mFragmentlist.add(instantiateFragment(mMainViewPager,2, MineFragment.newInstance()));
                mMainViewPager.setOffscreenPageLimit(mFragmentlist.size() - 1);
                mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @NonNull
                    @Override
                    public Fragment getItem(int position) {
                        return mFragmentlist.get(position);
                    }

                    @Override
                    public int getCount() {
                        return mFragmentlist.size();
                    }
                };
                mMainViewPager.setAdapter(mPagerAdapter);
                mLlFill.setLayoutParams(params_ll);
                mMainTabLayout.setLayoutParams(params);
                mMainTabLayout.setupWithViewPager(mMainViewPager, false);
                for (int i=0; i<mItems.length; i++){
                    mMainTabLayout.getTabAt(i).setCustomView(makeTabLayoutView(mItems[i]));
                }
                mMainViewPager.setCurrentItem(1);
            }
        });
    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    private View makeTabLayoutView(String name){
        View view = LayoutInflater.from(mContext).inflate(R.layout.tablayout_main, null);
        view.setTag(name);
        ImageView itemImg = view.findViewById(R.id.tablayout_main_img);
        TextView itemName = view.findViewById(R.id.tablayout_main_text);
        itemName.setText(name);
        if (name.equals(mItems[0])) itemImg.setImageResource(R.drawable.tablayout_rank_sele);
        else if (name.equals(mItems[1])) itemImg.setImageResource(R.drawable.tablayout_learning_sele);
        else itemImg.setImageResource(R.drawable.tablayout_mine_sele);
        return view;
    }
}
