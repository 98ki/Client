package com.shape100.gym.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetooth.le.DeviceScanActivity;
import com.example.bluetooth.le.h5.H5Activity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.KenBurnsView.TransitionListener;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.gym.provider.SysSettings;

public class FragTab01 extends Fragment implements View.OnClickListener {

    private static final Logger log = Logger.getLogger("FragTab01");
    private AccountDetailBean mAccountDetailBean;

    TextView mBMIView;
    TextView mNameView;
    TextView mMorningView;
    KenBurnsView kenview;
    ImageView mBeaconView;
    int bmi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Logger.DBG) {
            log.d("onCreateView");
        }
        return inflater.inflate(R.layout.hometab01, container, false);
    }

    public void createNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
// Creates an explicit intent for an Activity
        Intent resultIntent = new Intent(getActivity().getApplicationContext(), H5Activity.class);
        resultIntent.putExtra("name", 1);
// Creating a artifical activity stack for the notification activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext());
        stackBuilder.addParentStack(TestActivity.class);
        stackBuilder.addNextIntent(resultIntent);
// Pending intent to the notification manager
        PendingIntent resultPending = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
// Building the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher) // notification icon
                .setContentTitle("浩沙16周年庆活动") // main title of the notification
                .setContentText("日复一日，年复一年，转眼浩沙健身迎来第16周年庆。浩沙健身与您彼此相知相伴16载，在此特别的日子，特为您奉上我们独有的温情与惊喜！\n" +
                        "\n" +
                        "\n") // notification text
                .setContentIntent(resultPending); // notification intent
// mId allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        createNotification();
        if (Logger.DBG) {
            log.d("onCreate");
        }
        super.onCreate(savedInstanceState);
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
        log.d("onResume");
        if (AppConfig.getInstance().getUserId() != 0) {
            mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();
            mNameView.setText(mAccountDetailBean.getName()
                    + getResources().getString(R.string.hello));
            setBMI();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (Logger.DBG) {
            log.d("onActivityCreated");
        }
        mBMIView = (TextView) getActivity().findViewById(R.id.tv_hometab01_bmi);
        mBMIView.setOnClickListener(this);
        mNameView = (TextView) getActivity().findViewById(
                R.id.tv_hometab01_name);
        mMorningView = (TextView) getActivity().findViewById(
                R.id.tv_hometab01_morning);
        mBeaconView = (ImageView) getActivity().findViewById(R.id.iv_hometab01_beacon);
        mBeaconView.setOnClickListener(this);
        getActivity().findViewById(R.id.layout_BMI).setOnClickListener(this);

        mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();

        if (AppConfig.getInstance().getUserId() != 0) {
            log.d("bean.toString():" + mAccountDetailBean.toString());
            mNameView.setText(mAccountDetailBean.getName()
                    + getResources().getString(R.string.hello));
            setBMI();
        }
        mMorningView.setText(SysSettings.doMorning());
        super.onActivityCreated(savedInstanceState);
    }

    private void setBMI() {
        bmi = SysSettings.countBMI(mAccountDetailBean.getHeight(),
                mAccountDetailBean.getWeight());
        mBMIView.setText(bmi + "");
        if (bmi < 18.5) {
            mBMIView.setTextColor(Color.parseColor("#CCFFCC"));
        } else if (bmi >= 18.5 && bmi < 25) {
            mBMIView.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (bmi >= 25 && bmi < 30) {
            mBMIView.setTextColor(Color.parseColor("#FF6600"));
        } else if (bmi >= 30) {
            mBMIView.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    /**
     * 控制活动图片
     *
     * @author yupu
     * @date 2015年2月12日
     */
    private void controlMoveView() {
        kenview = (KenBurnsView) getActivity().findViewById(R.id.iv_bg);
        // kenview.pause(); 暂停动画
        // 监听活动图片
        kenview.setTransitionListener(new TransitionListener() {

            @Override
            public void onTransitionStart(Transition arg0) {
            }

            @Override
            public void onTransitionEnd(Transition arg0) {
                // TODO Auto-generated method stub

            }
        });
        // 设置时间，变化率
        // AccelerateDecelerateInterpolator 先加速再减速
        // AccelerateInterpolator 加速
        // AnticipateInterpolator 先回退一小步，然后再迅速前进
        // AnticipateOvershootInterpolator 先回退一小步，然后再迅速前进，在超过右边界一小步
        // BounceInterpolator 实现弹球效果
        // CycleInterpolator 周期运动
        // DecelerateInterpolator 减速
        // LinearInterpolator 匀速
        // OvershootInterpolator 快速前进到右边界上，再往外突出一小步
        kenview.setTransitionGenerator(new RandomTransitionGenerator(2000,
                new AnticipateInterpolator()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_BMI:
                if (AppConfig.getInstance().getUserId() != 0) {
                    OwnerSettingActivity.ActionStart(getActivity());
                } else {
                    LoginActivity.ActionStart(getActivity());
                    getActivity().finish();
                }
                break;
            case R.id.iv_hometab01_beacon:
                Intent intent = new Intent(getActivity(), DeviceScanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
