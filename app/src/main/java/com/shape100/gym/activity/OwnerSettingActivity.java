/**
 * file name：SettingOwnerActivity.java    
 *
 * @author zpy zpy@98ki.com   
 * @date：2014-11-28
 * Copyright shape100.com Corporation 2014         
 *
 */
package com.shape100.gym.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.model.AccountDetailBean;
import com.shape100.gym.protocol.MediaUploadPic;
import com.shape100.gym.protocol.MediaUploadPicConfirm;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.UpdateProfile;
import com.shape100.gym.provider.AccountDetailUtil;
import com.shape100.widget.BirthdayWheel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

/**
 * project：shape100 class：SettingOwnerActivity desc： author：zpy zpy@98ki.com
 * create date：2014-11-28 下午2:44:15 modify author: zpy update date：2014-11-28
 * 下午2:44:15 update remark：
 */
public class OwnerSettingActivity extends SlideActivity implements
        OnClickListener {
    private EditText mNameEdit;
    private EditText mHeightEdit;
    private EditText mWeightEdit;
    private TextView mBirthView;
    private RadioGroup mSexGroup;
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private ImageView mHeadView;
    private BirthdayWheel mBirthWheel;
    private Button mSaveBtn;

    private static final Logger log = Logger.getLogger("OwnerSettingActivity");
    private AccountDetailBean mAccountDetailBean;
    private Bitmap mImage;
    private Bitmap mHeadIcon;
    private boolean isUpdateHead;

    public static void ActionStart(Activity activity) {
        Intent intent = new Intent(activity, OwnerSettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owner_setting);
        ((TextView) findViewById(R.id.tv_title))
                .setText(R.string.owner_setting);
        initDate();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == SelectPicPopupWindow.RESULTPIC && data != null) {
                // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                Uri mImageCaptureUri = data.getData();
                // is update photo tag
                // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                if (mImageCaptureUri != null) {
                    try {
                        // 这个方法是根据Uri获取Bitmap图片的静态方法
                        mImage = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(), mImageCaptureUri);
                        ImageUtils.bitmapToFile(mImage, MainApplication
                                .getInstance().getImageCacheDir()
                                + MainName.HEAD_LARGE_JPG);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            covertThenShowBitmap();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_owner_save:
                saveOwnerSetting();
                break;
            case R.id.iv_setting_owner_head:
                // 使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
                startActivityForResult(
                        new Intent(this, SelectPicPopupWindow.class), 1);
                break;
            case R.id.layout_birth_day:
                hideSoft(mNameEdit);
                hideSoft(mHeightEdit);
                hideSoft(mWeightEdit);
                choiceBirthday();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    /**
     * covertThenShowBitmap covert Bitmap's size and quality, then show and save
     * to /sdcard/shape100/mHeadIcon.jpg
     *
     * @param
     */
    public void covertThenShowBitmap() {
        mImage = ImageUtils.compressImageFromFile(MainApplication.getInstance()
                .getImageCacheDir() + MainName.HEAD_LARGE_JPG);

        mHeadIcon = ImageUtils.scaleImageByWidth(mImage, 100);
        mHeadView.setImageBitmap(mHeadIcon);

        isUpdateHead = true;

        mAccountDetailBean.setProfileImageUrl(MainApplication.getInstance()
                .getImageCacheDir() + MainName.HEAD_LARGE_JPG);

        ThreadPool.getInstance().execute(
                new MediaUploadPic(new EventProtocolHandler(),
                        mAccountDetailBean));
    }

    public void saveOwnerSetting() {

        if (!mNameEdit.getText().toString().equals("")) {
            mAccountDetailBean.setName(mNameEdit.getText() + "");
        }
        if (!mWeightEdit.getText().toString().equals("")) {
            mAccountDetailBean.setWeight(Float.parseFloat(mWeightEdit.getText()
                    + ""));
        }
        if (!mHeightEdit.getText().toString().equals("")) {
            mAccountDetailBean.setHeight(Integer.parseInt(mHeightEdit.getText()
                    + ""));
        }
        if (mSexGroup.getCheckedRadioButtonId() == mFemaleRadio.getId()) {
            mAccountDetailBean.setGender(0);
        } else if (mSexGroup.getCheckedRadioButtonId() == mMaleRadio.getId()) {
            mAccountDetailBean.setGender(1);
        }

        if (!(mBirthView.getText().toString()).equals("1980-01-01")) {
            mAccountDetailBean.setBirthday(mBirthView.getText() + "");
        }

        ThreadPool.getInstance().execute(
                new UpdateProfile(new EventProtocolHandler(),
                        mAccountDetailBean, isUpdateHead));
    }

    public void choiceBirthday() {
        mBirthWheel = new BirthdayWheel(OwnerSettingActivity.this);
        mBirthWheel.showAtLocation(
                OwnerSettingActivity.this.findViewById(R.id.setting_owner),
                Gravity.BOTTOM, 0, 0);
    }

    public void initDate() {
        mAccountDetailBean = AccountDetailUtil.getAccountDetailBean();
    }

    public void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        mNameEdit = (EditText) findViewById(R.id.et_setting_owner_name);
        mWeightEdit = (EditText) findViewById(R.id.et_setting_owner_weight);
        mHeightEdit = (EditText) findViewById(R.id.et_setting_owner_height);
        mSexGroup = (RadioGroup) findViewById(R.id.rg_setting_owner_sex);
        mMaleRadio = (RadioButton) findViewById(R.id.rb_setting_owner_male);
        mFemaleRadio = (RadioButton) findViewById(R.id.rb_setting_owner_female);
        mBirthView = (TextView) findViewById(R.id.tv_birth);
        mSaveBtn = (Button) findViewById(R.id.btn_setting_owner_save);
        mHeadView = (ImageView) findViewById(R.id.iv_setting_owner_head);
        findViewById(R.id.layout_birth_day).setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mHeadView.setOnClickListener(this);
        mNameEdit.setHint(mAccountDetailBean.getName());
        mWeightEdit.setHint(mAccountDetailBean.getWeight() + "");
        mHeightEdit.setHint(mAccountDetailBean.getHeight() + "");

        // load user head
        loadImage();

        if (mAccountDetailBean.getGender() == 0) {
            mFemaleRadio.setChecked(true);
        } else {
            mMaleRadio.setChecked(true);
        }

        mBirthView.setText(mAccountDetailBean.getBirthday());

    }

    public void loadImage() {
        ImageLoader.getInstance().displayImage(
                mAccountDetailBean.getProfileImageUrl(),
                mHeadView,
                MainApplication.getInstance().getDisplayImageOptions(
                        R.drawable.ic_unknown));
    }

    /**
     * 判断输入法是否显示着
     *
     * @author yupu
     * @date 2015年2月3日
     */
    private void hideSoft(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }
    }

    @SuppressLint("HandlerLeak")
    private class EventProtocolHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_SUCCESS:
                    Toast.makeText(OwnerSettingActivity.this, "个人信息更新成功！",
                            Toast.LENGTH_SHORT).show();
                    if (isUpdateHead) {
                        EventBus.getDefault().post(new Event.CommendChangeEvent());
                    }
                    finish();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    if (msg.arg1 == 500) {
                        Toast.makeText(OwnerSettingActivity.this, "个人信息没有任何修改！",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstVar.HANDLER_MSG_ERROR:
                    Toast.makeText(OwnerSettingActivity.this, "请检查网络是否正确！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ConstVar.HANDLER_MSG_CONTINUE1:
                    log.e("Media pic upload put Succeed!"); // put image Succeed
                    new MediaUploadPicConfirm(new EventProtocolHandler(),
                            mAccountDetailBean).start();
                    break;
                case ConstVar.HANDLER_MSG_CONTINUE2:
                    log.e("Media pic upload confirm Succeed!");
                    break;
            }
        }
    }

}
