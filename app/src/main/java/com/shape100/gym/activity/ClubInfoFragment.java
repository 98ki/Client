/**
 * file name：ClubInfoFragment.java    
 *
 * @author zpy zpy@98ki.com   
 * @date：2014-11-18
 * Copyright shape100.com Corporation 2014         
 *
 */
package com.shape100.gym.activity;

import java.net.URISyntaxException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com._98ki.util.LogoUtils;
import com._98ki.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.adapter.ImagePagerAdapter;
import com.shape100.gym.adapter.TimeAdapter;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.ClubExit;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.ClubUtil;
import com.shape100.widget.ListviewForScrollview;
import com.viewpagerindicator.CirclePageIndicator;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

/**
 * project：shape100 class：ClubInfoFragment desc： author：zpy zpy@98ki.com create
 * date：2014-11-18 下午12:26:02 modify author: zpy update date：2014-11-18
 * 下午12:26:02 update remark：
 */
public class ClubInfoFragment extends BaseFragment implements OnClickListener {
    /* Data */
    private static final Logger log = Logger.getLogger("clubInfoFragment");
    private String[] picArr = {"drawable://" + R.drawable.hometab_bg};
    ;
    private ClubBean mClubBean;
    private Bitmap mThumb;

    /* View */
    private AutoScrollViewPager viewPager;
    private CirclePageIndicator indicator;
    private TextView tvClubName;
    private TextView tvClubAddress;
    private TextView mClubPhoneView;
    private TextView mClubHomePageUrlView;
    private TextView tvClubBusinessHours;
    private TextView tvClubDescription;
    private ImageView mClubShareView;
    private Button mExitBtn;
    private ProgressBar mProgressBar;
    private int shitFlag;
    private ListviewForScrollview listview;
    private TimeAdapter adapter;
    private RotateAnimation rightAnimation;
    private ImageView rightView;
    private ImageView club_logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_clubinfo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 切换系统已安装地图导航，仅支持百度地图，高德地图
     *
     * @author yupu
     * @date 2015年1月27日
     */
    private void switchMap() {
        String curr_lat = AppConfig.getInstance().getCurrLat();
        String curr_lon = AppConfig.getInstance().getCurrLon();
        String club_lat = mClubBean.getLat();
        String club_lon = mClubBean.getLon();

        // String st = "cur_lat:" + curr_lat + "---curr_lon:" + curr_lon
        // + "--club_lat:" + club_lat + "--club_lon" + club_lon;
        // Toast.makeText(getActivity(), st, Toast.LENGTH_LONG).show();
        if (Utils.isInstallByread(Utils.BDpackage)) {
            // ----39.905787,116.558903 116.341449,39.770388

            StringBuilder sb = new StringBuilder();
            sb.append("intent://map/direction?origin=latlng:")
                    .append(curr_lat + ",")
                    .append(curr_lon + "|name:当前位置")
                    .append("&destination=" + club_lat + "," + club_lon)
                    .append("&mode=driving")
                    .append("&src=shape100|shape100")
                    .append("&referer=Autohome|GasStation")
                    .append("#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

            try {
                Intent intent = Intent.getIntent(sb.toString());
                startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (Utils.isInstallByread(Utils.GDpackage)) {
            // androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc
            // &dlat=36.3&dlon=116.2&dname=def&dev=0&m=0&t=1&showType=1
            // pkg=com.autonavi.minimap
            // androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=36.547901&lon=104.258354&dev=1&style=2
            StringBuilder sb = new StringBuilder();
            sb.append("androidamap://navi?sourceApplication=shape100")
                    .append("&lat=" + club_lat + "&lon=" + club_lon)
                    .append("&dev=0") // 起终点是否偏移(0:lat
                            // 和
                            // lon
                            // 是已经加密后的,不需要国测加密;
                            // 1:需要国测加密)
                    .append("&style=2"); // 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4
            // 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7
            // 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
            try {
                Intent intent = Intent.getIntent(sb.toString());
                startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_address:
                // TODO
                // 调用地图
                switchMap();
                break;
            case R.id.btn_clubinfo_join_exit:
                exitClub();
                break;
            case R.id.iv_clubinfo_share:
                // mProgressBar.setVisibility(View.VISIBLE);
                intent = new Intent(getActivity(), SharePopupWindow.class);
                intent.putExtra("clubbean", mClubBean);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_clubinfo_detail:
                intent = new Intent(getActivity(), ClubInfoDetailActivity.class);
                intent.putExtra("desc", mClubBean.getDescription());
                if (picArr != null) {
                    intent.putExtra("url", picArr[0]);
                }
                startActivity(intent);
                break;
            case R.id.layout_tel:
                callClub();
                break;
            case R.id.web_clubInfo:
                String homePageUrl = mClubHomePageUrlView.getText() + "";
                Uri uri = Uri.parse(homePageUrl);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.layout_time:
                showList();
                break;
        }
    }

    public void callClub() {
        final String number = mClubPhoneView.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!TextUtils.isEmpty(number)) {
            builder.setMessage(number);
            builder.setPositiveButton(getResources().getString(R.string.club_call),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                    .parse("tel:" + number));
                            startActivity(intent);
                        }
                    });
        } else {
            builder.setMessage(getResources().getString(R.string.club_notime_value));
        }

        builder.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.show();
    }

    public void exitClub() {
        new ClubExit(new EventProtocolHandler(), mClubBean).start();
    }

    public void initData() {
        mClubBean = ClubUtil.getClubById(AccountDetailUtil.getUserClub());
        log.e("user's club:" + mClubBean.getId());

        // set scroll imgs
        try {
            log.e("clubPic:" + mClubBean.getPicUrls());
            picArr = mClubBean.getPicUrls().split(",");
        } catch (Exception e) {
            log.e("CoursePageActivity:course picUrls is null!");
        }

        LogoUtils.saveLogo(mClubBean.getLogoUrl(), MainName.LOGO_JPG);
    }

    public void initView() {
        club_logo = (ImageView) getActivity().findViewById(R.id.iv_club_logo);
        listview = (ListviewForScrollview) getActivity().findViewById(
                R.id.list_expand);
        rightView = (ImageView) getActivity().findViewById(R.id.iv_right);

        viewPager = (AutoScrollViewPager) getActivity().findViewById(
                R.id.vp_clubinfo);
        indicator = (CirclePageIndicator) getActivity().findViewById(
                R.id.cpi_clubinfo);

        tvClubName = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_name);
        tvClubAddress = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_address);
        mClubPhoneView = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_phone);
        mClubHomePageUrlView = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_homepageurl);
        tvClubBusinessHours = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_businesshours);
        tvClubDescription = (TextView) getActivity().findViewById(
                R.id.tv_clubinfo_desc);
        getActivity().findViewById(R.id.tv_clubinfo_detail).setOnClickListener(
                this);
        mClubShareView = (ImageView) getActivity().findViewById(
                R.id.iv_clubinfo_share);
        mProgressBar = (ProgressBar) getActivity().findViewById(
                R.id.pb_clubinfo);
        mExitBtn = (Button) getActivity().findViewById(
                R.id.btn_clubinfo_join_exit);
        getActivity().findViewById(R.id.web_clubInfo).setOnClickListener(this);
        getActivity().findViewById(R.id.layout_tel).setOnClickListener(this);
        getActivity().findViewById(R.id.layout_time).setOnClickListener(this);
        getActivity().findViewById(R.id.layout_address)
                .setOnClickListener(this);

        mExitBtn.setText(R.string.exit_club);
        mExitBtn.setOnClickListener(this);
        mClubShareView.setOnClickListener(this);

        tvClubName.setText(mClubBean.getName());
        tvClubAddress.setText(mClubBean.getAddress());
        mClubPhoneView.setText(mClubBean.getPhone());

        if (!TextUtils.isEmpty(mClubBean.getLogoUrl())) {
            ImageLoader.getInstance().displayImage(mClubBean.getLogoUrl(),
                    club_logo);
        }

        if (!TextUtils.isEmpty(mClubBean.getHomepageUrl())) {
            mClubHomePageUrlView.setText(mClubBean.getHomepageUrl());
        } else {
            getActivity().findViewById(R.id.web_clubInfo).setVisibility(
                    View.GONE);
        }

        log.e(mClubBean.getBusinessHours());
        String businessHours = "";
        if (mClubBean.getBusinessHours() != null) {
            /*
             * businessHours = mClubBean.getBusinessHours() .replaceAll("\\|",
			 * "\n");
			 */
            setList(mClubBean.getBusinessHours().split("\\|"));
        }
        String description = "";
        if (mClubBean.getDescription() != null) {
            description = mClubBean.getDescription();
            if (description.length() > 100) {
                description = description.substring(0, 100) + "……";
            }
        }

        tvClubDescription.setText(description);

        // set Adapter
        log.d("__PicArr.length:" + picArr.length);
        viewPager.setAdapter(new ImagePagerAdapter(getActivity(), picArr));
        viewPager.setInterval(3000);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % picArr.length);
        indicator.setViewPager(viewPager);

    }

    private void setList(String[] content) {
        if (content != null) {
            tvClubBusinessHours.setText(content[0]);
            adapter = new TimeAdapter(getActivity(), content, false);
            listview.setAdapter(adapter);
        } else {
            tvClubBusinessHours.setText(getResources().getString(R.string.club_notime_value));
        }
    }

    /**
     * 切换营业时间动画
     *
     * @author yupu
     * @date 2015年1月13日
     */
    private void showList() {
        rightView.clearAnimation();
        if (listview.getVisibility() == View.VISIBLE) {
            listview.setVisibility(View.GONE);
            rightAnimation = new RotateAnimation(90f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rightAnimation.setFillAfter(true);
            rightAnimation.setDuration(500);
            rightView.startAnimation(rightAnimation);
        } else {
            adapter.notifyDataSetChanged();
            listview.setVisibility(View.VISIBLE);
            rightAnimation = new RotateAnimation(0f, 90f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rightAnimation.setFillAfter(true);
            rightAnimation.setDuration(500);
            rightView.startAnimation(rightAnimation);
        }
    }

    /**
     * EventProtocolHandler
     *
     * @author zpy zpy@98ki.com
     * @date 2014-11-12 下午8:14:20
     * @version: V1p.0
     */
    private class EventProtocolHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_SUCCESS:
                    // post EventBus to FragmentCurr, refresh club and my schedule.
                    EventBus.getDefault().post(
                            new Event.RefreshScheduleEvent("exitClub"));
                    // change FragmentClub
                    EventBus.getDefault().post(
                            new Event.ChangeFragmentEvent("FragmentClub"));
                    // hint
                    Toast.makeText(MainApplication.sContext,
                            "成功退出" + mClubBean.getName() + "!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    Toast.makeText(MainApplication.sContext,
                            "退出" + mClubBean.getName() + "失败!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case ConstVar.HANDLER_MSG_ERROR:
                    Toast.makeText(MainApplication.sContext, "请检查网络是否正确！",
                            Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

}
