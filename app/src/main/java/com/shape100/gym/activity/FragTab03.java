package com.shape100.gym.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.shape100.gym.Logger;
import com.shape100.gym.R;

public class FragTab03 extends BaseFragment implements OnClickListener {

	private static final Logger log = Logger.getLogger("FragTab03");
	/* UI */
	private Fragment mCourseFragment;
	private TabTrainerFragment mTrainerFragment;
	private Button btn_course;
	private Button btn_trainer;
	/* Data */
	private final int TABID_COURSE = 0;
	private final int TABID_TRAINER = 1;
	/*
	 * sTabRes[][0] -> layout.id && sTabRes[][1] -> button.id && sTabRes[][2] ->
	 * button.background(normal) && sTabRes[][3] -> button.background(focus)
	 */
	private final int sTabRes[][] = {
			{ R.id.club_tab_course, R.id.club_btn_course,
					R.drawable.btn_club_map_normal,
					R.drawable.btn_club_map_focus },
			{ R.id.club_tab_trainer, R.id.club_btn_trainer,
					R.drawable.btn_club_list_normal,
					R.drawable.btn_club_list_focus } };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreateView");
		}
		return inflater.inflate(R.layout.hometab03, container, false);
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

		View v = this.getView();

		btn_course = (Button) v.findViewById(R.id.club_btn_course);
		btn_trainer = (Button) v.findViewById(R.id.club_btn_trainer);
		btn_course.setOnClickListener(this);
		btn_trainer.setOnClickListener(this);

		switchFragment(TABID_COURSE);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.club_btn_course:
			switchFragment(TABID_COURSE);
			break;
		case R.id.club_btn_trainer:
			switchFragment(TABID_TRAINER);
			break;
		}
	}

	private void switchFragment(int tabId) {
		// change btn's color
		btn_course.setBackgroundResource(sTabRes[0][2 + (1 ^ tabId)]);
		btn_trainer.setBackgroundResource(sTabRes[1][2 + tabId]);
		// change the Fragment
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		if (mCourseFragment != null) {
			ft.hide(mCourseFragment);
		}

		if (mTrainerFragment != null) {
			ft.hide(mTrainerFragment);
		}

		switch (tabId) {
		case TABID_COURSE:
			if (mCourseFragment == null) {
				mCourseFragment = new TabCourseFragment();
				ft.add(R.id.tabcontent, mCourseFragment);
			} else {
				ft.show(mCourseFragment);
			}
			break;
		case TABID_TRAINER:
			if (mTrainerFragment == null) {
				mTrainerFragment = new TabTrainerFragment();
				ft.add(R.id.tabcontent, mTrainerFragment);
			} else {
				ft.show(mTrainerFragment);
			}
			break;
		}
		ft.commit();
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

	@Override
	public void onDestroy() {
		if (Logger.DBG) {
			log.d("onDestroy");
		}
		super.onDestroy();
	}

}
