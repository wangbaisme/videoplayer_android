package io.filenet.xlvideoplayer.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import io.filenet.xlvideoplayer.R;
import io.filenet.xlvideoplayer.bean.ModuleInfo;
import io.filenet.xlvideoplayer.reflex.IndicatorLineUtil;
import io.filenet.xlvideoplayer.utils.ToastUtil;

public class StudyFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_editText:
                ToastUtil.getInstance(getActivity()).showShortToast("跳转搜索界面");
                break;
        }
    }

    private ViewPager mItemViewPager;
    private TabLayout mItemTabLayout;
    private EditText mSearchEditText;

    private static StudyFragment mStudyFragment;
    private FragmentPagerAdapter mPagerAdatper;
    private FragmentManager mFragmentManager = getFragmentManager() ;
    private FragmentTransaction mTransaction;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mItems = {"中国舞","主持","美术"};


    private StudyFragment(){}

    public synchronized static StudyFragment newInstance(){
        if (mStudyFragment == null)
            mStudyFragment = new StudyFragment();
        return mStudyFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater,container,saveInstanceState);
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        init(view);
        initViewPager();
        return view;
    }

    private void init(View view){
        mItemViewPager = view.findViewById(R.id.item_viewpager);
        mItemTabLayout = view.findViewById(R.id.item_tablayout);
        mSearchEditText = view.findViewById(R.id.search_editText);
        mSearchEditText.setOnClickListener(this);
    }

    private void initViewPager(){

        mItemViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mFragments.clear();
        mFragments.add(instantiateFragment(mItemViewPager,0,new ItemFragment(getVideoInfoList(mItems[0]))));
        mFragments.add(instantiateFragment(mItemViewPager,1,new ItemFragment(getVideoInfoList(mItems[1]))));
        mFragments.add(instantiateFragment(mItemViewPager,2,new ItemFragment(getVideoInfoList(mItems[2]))));
        mItemViewPager.setOffscreenPageLimit(mItems.length-1);
        mPagerAdatper = new FragmentPagerAdapter(getFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mItemViewPager.setAdapter(mPagerAdatper);

        mItemTabLayout.setupWithViewPager(mItemViewPager);
        for (int i=0; i<mItems.length; i++){
            mItemTabLayout.getTabAt(i).setText(mItems[i]);
        }
        mItemTabLayout.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(mItemTabLayout,26,26);
            }
        });
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {
    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    private void hideOthersFragment(Fragment showFragment, boolean add) {
        mTransaction = mFragmentManager.beginTransaction();
        if (add)
            mTransaction.add(R.id.item_viewpager, showFragment);
        for (Fragment fragment : mFragments) {
            if (showFragment.equals(fragment)) {
                mTransaction.show(fragment);
            } else {
                mTransaction.hide(fragment);
            }
        }
        mTransaction.commit();
    }

    private boolean getFragmentExists(ViewPager viewPager, int position){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            return true;
        return false;
    }


    private ArrayList<ModuleInfo> getVideoInfoList(String type){
        ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();
        for (int i=0; i<9; i++){
            if (type.equals(mItems[0])){
                if (i%3 == 0) mModuleInfoList.add(new ModuleInfo("中国舞中国舞中国舞中国舞中国舞中国舞中国舞中国舞中国舞 ---- "+i,R.mipmap.img_2,mUrls[2],true));
                if (i%3 == 1) mModuleInfoList.add(new ModuleInfo("中国舞中国舞中国舞中国舞中国舞中国舞中国舞中国舞 ---- "+i,R.mipmap.img_3,mUrls[1],false));
                if (i%3 == 2) mModuleInfoList.add(new ModuleInfo("中国舞中国舞中国舞中国舞中国舞中国舞中国舞 ---- "+i,R.mipmap.img_4,mUrls[0],false));
            }else if (type.equals(mItems[1])){
                if (i%3 == 0) mModuleInfoList.add(new ModuleInfo("主持主持主持主持主持主持主持主持主持主持主持主持主持主持 ---- "+i,R.mipmap.img_3,mUrls[1],false));
                if (i%3 == 1) mModuleInfoList.add(new ModuleInfo("主持主持主持主持主持主持主持主持主持主持主持主持 ---- "+i,R.mipmap.img_4,mUrls[0],true));
                if (i%3 == 2) mModuleInfoList.add(new ModuleInfo("主持主持主持主持主持主持主持主持主持主持 ---- "+i,R.mipmap.img_2,mUrls[2],false));
            }else {
                if (i%3 == 0) mModuleInfoList.add(new ModuleInfo("美术美术美术美术美术美术美术美术美术美术美术美术美术美术 ---- "+i,R.mipmap.img_4,mUrls[0],false));
                if (i%3 == 1) mModuleInfoList.add(new ModuleInfo("美术美术美术美术美术美术美术美术美术美术美术美术 ---- "+i,R.mipmap.img_2,mUrls[2],false));
                if (i%3 == 2) mModuleInfoList.add(new ModuleInfo("美术美术美术美术美术美术美术美术美术美术 ---- "+i,R.mipmap.img_3,mUrls[1],true));
            }

        }
        return mModuleInfoList;
    }

    String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
//            "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8",
//            "http://192.168.1.116/hls/guoyuenc.m3u8",
//            "http://192.168.1.116/hls/guoyu.m3u8",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };
}
