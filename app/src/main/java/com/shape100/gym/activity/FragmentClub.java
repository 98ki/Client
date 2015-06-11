package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.model.ClubBean;

/**
 * TODO Club fragment,show all of the club infomation, include map fragment and
 * list fragment.
 * 
 * @author zpy@98ki.com
 * @data 2014-10-20 下午6:00:12
 * @version: V1.0
 */
public class FragmentClub extends BaseFragment implements OnClickListener {

	private static final Logger log = Logger.getLogger("FragmentClub");
	/* UI */
	private ClubMapFragment mMapFragment;
	private ClubListFragment mListFragment;
	private Button mMapBtn;
	private Button mListBtn;
	private ImageView mFindImageView;
	private ImageView mPopFindImageView;
	private EditText mFindView;
	private RelativeLayout mTitleLayout;
	private RelativeLayout mFindLayout;
	/* Data */
	private final int TABID_MAP = 0;
	private final int TABID_LIST = 1;
	private List<ClubBean> mClubItems = new ArrayList<ClubBean>();
	private FragmentManager fm;
	private FragmentTransaction ft;
	private final int sTabRes[][] = {
			{ R.id.tab_map, R.id.club_button_map,
					R.drawable.btn_club_map_normal,
					R.drawable.btn_club_map_focus },
			{ R.id.tab_list, R.id.club_button_list,
					R.drawable.btn_club_list_normal,
					R.drawable.btn_club_list_focus } };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("onCreateView");
		return inflater.inflate(R.layout.fragment_club, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.d("onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		log.d("onDestroy");
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}

		// init view
		initView();
		// init map,list fragment
		fm = this.getChildFragmentManager();
		ft = fm.beginTransaction();
		mMapFragment = new ClubMapFragment();
		ft.add(R.id.clubcontent, mMapFragment);
		mListFragment = new ClubListFragment();
		ft.add(R.id.clubcontent, mListFragment);
		ft.commit();
		super.onActivityCreated(savedInstanceState);
	}

	public void initView() {
		View v = this.getView();
		mMapBtn = (Button) v.findViewById(R.id.club_button_map);
		mListBtn = (Button) v.findViewById(R.id.club_button_list);
		mPopFindImageView = (ImageView) v
				.findViewById(R.id.iv_frag_club_popfind);
		mFindImageView = (ImageView) v.findViewById(R.id.iv_frag_club_find);
		mFindView = (EditText) v.findViewById(R.id.et_frag_club_find);
		mTitleLayout = (RelativeLayout) v
				.findViewById(R.id.lyt_frag_club_title);
		mFindLayout = (RelativeLayout) v.findViewById(R.id.lyt_frag_club_find);

		mMapBtn.setOnClickListener(this);
		mListBtn.setOnClickListener(this);
		mPopFindImageView.setOnClickListener(this);
		mFindImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.club_button_map:
			switchFragment(TABID_MAP);
			break;
		case R.id.club_button_list:
			switchFragment(TABID_LIST);
			break;
		case R.id.iv_frag_club_popfind:
			popFindClub();
			break;
		case R.id.iv_frag_club_find:
			findClub();

			// 关闭软键盘
			View view = getActivity().getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			break;
		}

	}

	public void popFindClub() {
		mTitleLayout.setVisibility(View.INVISIBLE);
		mFindLayout.setVisibility(View.VISIBLE);
	}

	public void findClub() {
		mTitleLayout.setVisibility(View.VISIBLE);
		mFindLayout.setVisibility(View.INVISIBLE);
		// Find club
		mListFragment.findClubByKeyWord(mFindView.getText() + "");
		mMapFragment.findClubByKeyWord(mFindView.getText() + "");
	}

	private void switchFragment(int tabId) {
		// change btn's color if (map) tabNumber=0
		mMapBtn.setBackgroundResource(sTabRes[0][2 + (1 ^ tabId)]);
		mListBtn.setBackgroundResource(sTabRes[1][2 + tabId]);
		// change the Fragment
		fm = this.getChildFragmentManager();
		ft = fm.beginTransaction();

		if (mMapFragment != null) {
			ft.hide(mMapFragment);
		}

		if (mListFragment != null) {
			ft.hide(mListFragment);
		}

		switch (tabId) {
		case TABID_MAP:
			ft.show(mMapFragment);
			break;
		case TABID_LIST:
			ft.show(mListFragment);
			break;
		}
		ft.commit();
	}

	// @Override
	// public void updateLocation(Double geoLat, Double geoLng) {
	// mGeoLat = geoLat;
	// mGeoLng = geoLng;
	// }

	public double getGeoLat() {
		if (mMapFragment != null) {
			return mMapFragment.getGeoLat();
		}
		return 0.0;
	}

	public double getGetLng() {
		if (mMapFragment != null) {
			return mMapFragment.getGeoLng();
		}
		return 0.0;
	}

	@Override
	public void onStart() {
		if (Logger.DBG) {
			log.d("onCreate");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (Logger.DBG) {
			log.d("onResume");
		}
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

}
