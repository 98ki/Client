package com.shape100.gym.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shape100.gym.Event;
import com.shape100.gym.R;
import com.shape100.gym.adapter.PeopleListAdapter;
import com.shape100.gym.model.UserInfo;
import com.shape100.gym.protocol.FollowsOrFollowings;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

public class FunsOrConcernActivity extends SlideActivity implements
        OnClickListener, OnItemClickListener {
    public static final int FOLLOWS = FollowsOrFollowings.FOLLOWS;
    public static final int FOLLOWINGS = FollowsOrFollowings.FOLLOWING;

    private static final String KEY_ID = "user_id";
    private static final String KEY_FLAG = "flag";
    private static final int COUNT = 50;
    private int page = 1;
    private PullToRefreshListView listview;
    private ArrayList<UserInfo> userInfos;
    private PeopleListAdapter adapter;
    private long userid;
    private int FLAG;

    public static void StartAction(Activity activity, long user_id, int flag) {
        Intent intent = new Intent(activity, FunsOrConcernActivity.class);
        intent.putExtra(KEY_ID, user_id);
        intent.putExtra(KEY_FLAG, flag);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_concern_list);
        userid = getIntent().getLongExtra(KEY_ID, 0);
        FLAG = getIntent().getIntExtra(KEY_FLAG, 0);
        initView();
        ThreadPool.getInstance().execute(
                new FollowsOrFollowings(userid, COUNT, page,
                        new ResultHandler(), FLAG));
    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        String titlename = null;
        if (FLAG == FOLLOWINGS) {
            titlename = getResources().getString(R.string.concern_list);
        } else {
            titlename = getResources().getString(R.string.funs_list);
        }
        ((TextView) findViewById(R.id.tv_title)).setText(titlename);
        listview = (PullToRefreshListView) findViewById(R.id.list_concern_common);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        FunsOrConcernActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                // TODO
                ThreadPool.getInstance().execute(
                        new FollowsOrFollowings(userid, COUNT, page,
                                new ResultHandler(), FLAG));
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.load_more));
                refreshView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.up_pull_lab));
                refreshView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.up_release_lab));
                ThreadPool.getInstance().execute(
                        new FollowsOrFollowings(userid, COUNT, ++page,
                                new ResultHandler(), FLAG));
            }
        });
        adapter = new PeopleListAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CoachPageActivity.ActionStart(FunsOrConcernActivity.this, userInfos
                .get(position - 1).getUser().getUserId());
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
