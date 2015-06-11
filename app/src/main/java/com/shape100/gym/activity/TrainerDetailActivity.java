package com.shape100.gym.activity;

import android.os.Bundle;
import android.view.Window;

import com.shape100.gym.R;

public class TrainerDetailActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_trainerdetail);
	}
}
