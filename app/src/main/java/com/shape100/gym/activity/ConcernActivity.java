package com.shape100.gym.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.adapter.PeopleListAdapter;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.FollowsClass;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

/**
 * 共同最关注列表
 *
 * @author yupu
 * @date 2015年3月20日
 */
public class ConcernActivity extends SlideActivity implements
        OnItemClickListener, OnClickListener {
    private Logger log = Logger.getLogger("ConcernActivity");
    private static final int COUNT = 50;
    private int page = 1;
    private static final String KEY_ID = "class_id";
    private PullToRefreshListView listview;
    private ArrayList<UserInfo> userInfos;
    private PeopleListAdapter adapter;
    private long classId;

    public static void StartAction(Activity activity, long class_id) {
        Intent intent = new Intent(activity, ConcernActivity.class);
        intent.putExtra(KEY_ID, class_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.act_concern_list);
        classId = getIntent().getLongExtra(KEY_ID, 0);
        initView();
        ThreadPool.getInstance().execute(
                new FollowsClass(classId, COUNT, page, new ResultHandler()));
    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.concern_list));
        listview = (PullToRefreshListView) findViewById(R.id.list_concern_common);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(ConcernActivity.this,
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                ThreadPool.getInstance().execute(
                        new FollowsClass(classId, COUNT, page,
                                new ResultHandler()));
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.load_more));
                refreshView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.up_pull_lab));
                refreshView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.up_release_lab));
                ThreadPool.getInstance().execute(
                        new FollowsClass(classId, COUNT, ++page,
                                new ResultHandler()));
            }
        });
        adapter = new PeopleListAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        log.e("__________"+ userInfos.get(position - 1).getUser().getUserId());
        CoachPageActivity.ActionStart(ConcernActivity.this,
                userInfos.get(position - 1).getUser().getUserId());
    }

    class ResultHandler extends ProtocolHandler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Event.CONCERN_LIST:
                    if (listview.isRefreshing()) {
                        listview.onRefreshComplete();
                    }
                    ArrayList<UserInfo> data = (ArrayList<UserInfo>) msg.obj;
                    if (data != null && data.size() != 0) {
                        if (page == 1) {
                            userInfos = data;
                        } else {
                            userInfos.addAll(data);
                        }
                    }
                    adapter.upDate(userInfos);
                    break;
                case Event.CONCERN_LIST_FAILED:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;

            default:
                break;
        }

    }
}
