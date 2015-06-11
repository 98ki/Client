/**
 * file name：OwnerClassFavoriteActivity.java    
 *
 * @author zpy zpy@98ki.com   
 * @date：2014-12-1
 * Copyright shape100.com Corporation 2014         
 *
 */
package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.model.ClassBean;
import com.shape100.gym.protocol.ClassFavorites;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.provider.ClassUtil;

/**
 * project：shape100 class：OwnerClassFavoriteActivity desc： author：zpy
 * zpy@98ki.com create date：2014-12-1 上午10:57:56 modify author: zpy update
 * date：2014-12-4 上午17:32:00 update remark：
 *
 * @version 1.1
 */
public class FavoriteClassActivity extends SlideActivity implements
        OnClickListener, OnItemClickListener {
    /* Data */
    private static final Logger log = Logger.getLogger("clubInfoActivity");
    private static final String USERID = "userid";
    private ContentAdapter adapter;
    private DisplayImageOptions options;
    private ArrayList<ClassBean> mClassList = new ArrayList<ClassBean>();
    private List<ItemNode> mItemNodes = new ArrayList<ItemNode>();

    /* View */
    private ListView mFavoriteView;

    /**
     * @author yupu
     * @date 2015年1月9日
     */
    public static void ActionStart(Activity activity, long userID) {
        Intent intent = new Intent(activity, FavoriteClassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(USERID, userID);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owner_favorite_class);
        ((TextView) findViewById(R.id.tv_title)).setText(getResources()
                .getString(R.string.favorite_class));
        findViewById(R.id.tv_back).setOnClickListener(this);
        mFavoriteView = (ListView) findViewById(R.id.lv_owner_favorite_class);
        // init cache image options
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_club_default_logo)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        // set listview adapter
        adapter = new ContentAdapter(this, mItemNodes);
        mFavoriteView.setAdapter(adapter);
        mFavoriteView.setOnItemClickListener(this);

        // get Bundle
        Bundle bundle = getIntent().getExtras();
        long userId = bundle.getLong(USERID);

        ThreadPool.getInstance().execute(
                new ClassFavorites(new EventProtocolHandler(), mClassList,
                        userId));
        // new ClassFavorites(new EventProtocolHandler(), mClassList, userId)
        // .start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CourseDetailActivity.ActionStart(FavoriteClassActivity.this,
                ClassUtil.getClassById(mClassList.get(position).getClassId()));
    }

    private class ContentAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<ItemNode> mItemNodes;

        public ContentAdapter(Context context, List<ItemNode> itemNodes) {
            mItemNodes = itemNodes;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemNodes.size();
        }

        @Override
        public Object getItem(int position) {
            return mItemNodes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder holder;

            ItemNode node = mItemNodes.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem_course, null);
                holder = new ItemHolder();
                holder.mNameView = (TextView) convertView
                        .findViewById(R.id.courseitem_name);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolder) convertView.getTag();
            }

            holder.mNameView.setText(node.mName);

            return convertView;
        }

    }

    public class ItemNode {
        public String mName;
        public String mAddress;
        public String mPhone;
        public String logo;
    }

    ;

    private class ItemHolder {
        public ImageView mAvatarView;
        public TextView mNameView;
        public TextView mAddressView;
        public TextView mPhoneView;
    }

    ;

    /**
     * project：shape100 class：EventProtocolHandler desc： author：zpy zpy@98ki.com
     * create date：2014-12-1 上午11:06:04 modify author: zpy update date：2014-12-1
     * 上午11:06:04 update remark：
     */
    private class EventProtocolHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_SUCCESS:
                    for (int i = 0; i < mClassList.size(); i++) {
                        ItemNode node = new ItemNode();
                        node.mName = mClassList.get(i).getClassName();
                        mItemNodes.add(node);
                    }

                    adapter.notifyDataSetChanged();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    Toast.makeText(MainApplication.sContext, "加入失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ConstVar.HANDLER_MSG_ERROR:
                    Toast.makeText(MainApplication.sContext, getResources().getString(R.string.toast_no_net),
                            Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

}
