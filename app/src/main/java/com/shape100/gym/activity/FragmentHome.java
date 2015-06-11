package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shape100.gym.Logger;
import com.shape100.gym.R;

public class FragmentHome extends BaseFragment implements OnPageChangeListener,
        View.OnClickListener {

    private static final Logger log = Logger.getLogger("FragmentHome");

    private ViewPager mViewPage;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<ImageView> mDotViews = new ArrayList<ImageView>();
    private List<String> mTabTitles = new ArrayList<String>();
    private TextView mTabTitleView;
    private ImageView mWriteBlogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Logger.DBG) {
            log.d("onCreateView");
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Logger.DBG) {
            log.d("onCreate");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (Logger.DBG) {
            log.d("onActivityCreated");
        }
        initData();
        initView();
        View v = getView();
        mViewPage = (ViewPager) v.findViewById(R.id.home_viewpager);

        // TODO
        mFragments.add(new FragTab01());
        mFragments.add(new FragTab02());
        //	mFragments.add(new FragTab03());

        mAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager());
        mViewPage.setAdapter(mAdapter);
        mViewPage.setOnPageChangeListener(this);

        mWriteBlogView = (ImageView) v.findViewById(R.id.hometab_writeblog);
        mWriteBlogView.setOnClickListener(this);

        mTabTitleView = (TextView) v.findViewById(R.id.hometab_title);

        // TODO version update
        ImageView iv = (ImageView) v.findViewById(R.id.hometab_dot1);
        mDotViews.add(iv);

        iv = (ImageView) v.findViewById(R.id.hometab_dot2);
        mDotViews.add(iv);

//		iv = (ImageView) v.findViewById(R.id.hometab_dot3);
//		mDotViews.add(iv);

        mTabTitles.add(getString(R.string.hometab_title01));
        mTabTitles.add(getString(R.string.hometab_title02));
        mTabTitles.add(getString(R.string.hometab_title03));

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        if (Logger.DBG) {
            log.d("onStart");
        }
        super.onStart();
    }

    @Override
    public void onResume() {

        log.d("onResume");

        super.onResume();
    }

    @Override
    public void onPause() {
        if (Logger.DBG) {
            log.d("onPause");
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Logger.DBG) {
            log.d("onStop");
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        log.d("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        if (Logger.DBG) {
            log.d("onAttach");
        }
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        if (Logger.DBG) {
            log.d("onDetach");
        }
        super.onDetach();
    }

    public void initData() {

    }

    public void initView() {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int index) {
        int pageSize = mDotViews.size();

        if (index >= 0 && index < pageSize) {
            for (int i = 0; i < pageSize; i++) {
                ImageView iv = mDotViews.get(i);
                iv.setBackgroundResource(R.drawable.dot_white);
            }

            ImageView iv = mDotViews.get(index);
            iv.setBackgroundResource(R.drawable.dot_black);
        }

        String title = mTabTitles.get(index);
        mTabTitleView.setText(title);

        if (index == 1) {
            mWriteBlogView.setVisibility(View.VISIBLE);
        } else {
            mWriteBlogView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.hometab_writeblog) {
            Intent intent = new Intent(this.getActivity(),
                    WriteBlogActivity.class);
            this.startActivity(intent);
            //new FriendsTimeline(null).start();
        }
    }

    private class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

        public HomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

    ;
    /*
	 * = new FragmentPagerAdapter(){
	 * 
	 * @Override public Fragment getItem(int arg0) { return
	 * mFragments.get(arg0); }
	 * 
	 * @Override public int getCount() { return mFragments.size(); }
	 * 
	 * };
	 */

}
