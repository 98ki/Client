package com.shape100.gym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Shake.ShakeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.UserShow;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.SysSettings;
import com.shape100.widget.XScrollview;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.CommendChangeEvent;

public class FragmentOwner extends Fragment implements OnClickListener {
	private AccountDetailBean mAccountDetailBean;
	private static final Logger log = Logger.getLogger("FragmentOwner");
	private TextView mNameView;
	private TextView mBMIView;
	private RelativeLayout mFavoriteCoachLyt;
	private RelativeLayout mFavoriteClassLyt;
	private RelativeLayout mShakeLyt;
	private RelativeLayout mSettingLyt;
	private ImageView mHeadView, headBgView;
	private XScrollview XScrollview;
	private TextView tv_funs, tv_concern, tv_dynamic;
	private UserInfo userinfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onCreateView");
		}
		return inflater.inflate(R.layout.fragment_owner, container, false);
	}


    @Override
	public void onCreate(Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		if (Logger.DBG) {
			log.d("onCreate");
		}
		mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}


    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (Logger.DBG) {
			log.d("onActivityCreated");
		}

		super.onActivityCreated(savedInstanceState);
		// 设置阻尼效果
		headBgView = (ImageView) getActivity().findViewById(R.id.img_head_bg);
		XScrollview = (XScrollview) getActivity()
				.findViewById(R.id.xscrollview);
		XScrollview.setImageView(headBgView);

		mSettingLyt = (RelativeLayout) getActivity().findViewById(
				R.id.lyt_frag_owner_setting);
		mNameView = (TextView) getActivity().findViewById(
				R.id.tv_frag_owner_name);
		mBMIView = (TextView) getActivity()
				.findViewById(R.id.tv_frag_owner_bmi);
		mHeadView = (ImageView) getActivity().findViewById(
				R.id.iv_frag_owner_head);
		mFavoriteCoachLyt = (RelativeLayout) getActivity().findViewById(
				R.id.lyt_frag_owner_favorite_coach);
		mFavoriteClassLyt = (RelativeLayout) getActivity().findViewById(
				R.id.lyt_frag_owner_favorite_class);
		mShakeLyt = (RelativeLayout) getActivity().findViewById(R.id.lyt_frag_owner_shake);

		getActivity().findViewById(R.id.tv_frag_owner_mod).setOnClickListener(
				this);
		getActivity().findViewById(R.id.lyt_frag_owner_dynamic)
				.setOnClickListener(this);

		getActivity().findViewById(R.id.view_concern_count).setOnClickListener(
				this);
		getActivity().findViewById(R.id.view_dynamic).setOnClickListener(this);
		getActivity().findViewById(R.id.view_funs_count).setOnClickListener(
				this);

		tv_funs = (TextView) getActivity().findViewById(R.id.tv_funs);
		tv_dynamic = (TextView) getActivity().findViewById(R.id.tv_dynamic);
		tv_concern = (TextView) getActivity().findViewById(R.id.tv_concern);

		mFavoriteCoachLyt.setOnClickListener(this);
		mFavoriteClassLyt.setOnClickListener(this);
		mSettingLyt.setOnClickListener(this);
		mShakeLyt.setOnClickListener(this);
		mNameView.setText(mAccountDetailBean.getName());

		mBMIView.setText("BMI:"
				+ SysSettings.countBMI(mAccountDetailBean.getHeight(),
						mAccountDetailBean.getWeight()) + "");

		mNameView.setOnClickListener(this);
		mBMIView.setOnClickListener(this);
		mHeadView.setOnClickListener(this);

		loadImage(AccountDetailUtil.getAccountDetailBean().getProfileImageUrl());

		if (AppConfig.getInstance().getUserId() != 0) {
			ThreadPool.getInstance().execute(
					new UserShow(AppConfig.getInstance().getUserId(),
							new ResultHandler(), 0));
		}

	}


	public void onEventMainThread(CommendChangeEvent event) {
		if (AppConfig.getInstance().getUserId() != 0 && event != null) {
			log.e("----------------------------更新执行了");
			mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();
			mAccountDetailBean.setProfileImageUrl(mAccountDetailBean
					.getProfileImageUrl().replace("thumbnail", "square"));
			mNameView.setText(mAccountDetailBean.getName());
			mBMIView.setText("BMI:"
					+ SysSettings.countBMI(mAccountDetailBean.getHeight(),
							mAccountDetailBean.getWeight()) + "");
			loadImage(mAccountDetailBean.getProfileImageUrl());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.lyt_frag_owner_setting) {
			SettingActivity.ActionStart(getActivity());

		} else if (i == R.id.iv_frag_owner_head) {
			BigPictureActivity.ActionStart(getActivity(),
					mAccountDetailBean.getProfileImageUrl());

		} else if (i == R.id.tv_frag_owner_mod) {
			OwnerSettingActivity.ActionStart(getActivity());

		} else if (i == R.id.lyt_frag_owner_favorite_coach) {
			FavoriteCoachActivity.ActionStart(getActivity(),
					mAccountDetailBean.getUserId());

		} else if (i == R.id.lyt_frag_owner_shake) {
			Intent intent = new Intent(getActivity(), ShakeActivity.class);
			startActivity(intent);

		} else if (i == R.id.lyt_frag_owner_favorite_class) {
			FavoriteClassActivity.ActionStart(getActivity(),
					mAccountDetailBean.getUserId());

		} else if (i == R.id.lyt_frag_owner_dynamic) {
			DynamicActivity.ActionStart(getActivity(), AppConfig.getInstance()
					.getUserId());

		} else if (i == R.id.view_funs_count) {
			FunsOrConcernActivity.StartAction(getActivity(), AppConfig
					.getInstance().getUserId(), FunsOrConcernActivity.FOLLOWS);

		} else if (i == R.id.view_dynamic) {
			DynamicActivity.ActionStart(getActivity(), AppConfig.getInstance()
					.getUserId());

		} else if (i == R.id.view_concern_count) {
			FunsOrConcernActivity.StartAction(getActivity(), AppConfig
							.getInstance().getUserId(),
					FunsOrConcernActivity.FOLLOWINGS);

		}
 	}

	public void loadImage(String uri) {
		ImageLoader.getInstance().displayImage(
				uri,
				mHeadView,
				MainApplication.getInstance().getDisplayImageOptions(
						R.drawable.ic_unknown));
	}

	class ResultHandler extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Event.USERINFO:
				if (msg != null) {
					userinfo = (UserInfo) msg.obj;
					tv_funs.setText(userinfo.getUser().getFollowersCount() + "");
					tv_concern.setText(userinfo.getUser().getFollwingsCount()
							+ "");
					tv_dynamic.setText(userinfo.getUser().getStatusesCount()
							+ "");
				}

				break;
			default:
				break;
			}
		}
	}
}
