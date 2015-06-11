package com.shape100.gym.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.LogoUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.shape100.gym.adapter.ClubListAdapter;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.AccountLogin;
import com.shape100.gym.protocol.ClubExit;
import com.shape100.gym.protocol.ClubJoin;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.RecommendClub;
import com.shape100.gym.protocol.ThreadPool;

public class RecommendActivity extends BaseActivity implements OnClickListener,
        ClubListAdapter.JoinListener {
    private static final String PASS = "pass";
    private static final String NAME = "name";
    private ArrayList<ClubBean> clueans;
    private ListView list;
    private ClubListAdapter adapter;
    private int flag = 0;
    private TextView text;
    private String password, name;

    public static void ActionStart(Activity activity, String name,
                                   String password) {
        Intent intent = new Intent(activity, RecommendActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(PASS, password);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_recommend);
        initView();
    }

    private void initView() {
        password = getIntent().getStringExtra(PASS);
        name = getIntent().getStringExtra(NAME);
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.choice_club));
        findViewById(R.id.tv_back).setVisibility(View.GONE);
        findViewById(R.id.tv_commit).setOnClickListener(this);
        list = (ListView) findViewById(R.id.list_reconmend);
        adapter = new ClubListAdapter(this, this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // new ClubJoin(new EventProtocal(),
                // clueans.get(position)).start();
            }
        });
        ThreadPool.getInstance()
                .execute(new RecommendClub(new EventProtocal()));
        // new RecommendClub(new EventProtocal()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                ThreadPool.getInstance().execute(
                        new AccountLogin(new EventLogin(), name, password));
                // new AccountLogin(new EventLogin(), name, password).start();
                break;
            default:
                break;
        }
    }

    class EventProtocal extends ProtocolHandler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_RESULT:
                    if (msg.obj != null) {
                        clueans = (ArrayList<ClubBean>) msg.obj;
                        adapter.upDate(clueans);
                    }
                    break;
                case ConstVar.HANDLER_MSG_SUCCESS:
                    if (flag == 0) {
                        Toast.makeText(RecommendActivity.this, "加入俱乐部成功！",
                                Toast.LENGTH_LONG).show();
                        flag = 1;
                        text.setText("退出");
                    } else if (flag == 1) {
                        Toast.makeText(RecommendActivity.this, "退出俱乐部成功！",
                                Toast.LENGTH_LONG).show();
                        flag = 0;
                        text.setText("加入");
                    }

                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    if (flag == 0) {
                        Toast.makeText(RecommendActivity.this, "加入俱乐部失败，请检查网络！",
                                Toast.LENGTH_LONG).show();
                    } else if (flag == 1) {
                        Toast.makeText(RecommendActivity.this, "退出俱乐部失败，请检查网络！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    class EventLogin extends ProtocolHandler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Event.VERIFYCREDENTAIL:
                    if (MainApplication.activityActivity.size() != 0) {
                        for (Activity activity : MainApplication.activityActivity) {
                            activity.finish();
                        }
                    }
                    MainActivity.ActionStart(RecommendActivity.this, 0);
                    finish();
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    Toast.makeText(RecommendActivity.this, getResources().getString(R.string.toast_no_net),
                            Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }

    @Override
    public void onClickJoin(TextView text, int position) {
        this.text = text;
        if (flag == 0) {
            ThreadPool.getInstance().execute(
                    new ClubJoin(new EventProtocal(), clueans.get(position)));
            // new ClubJoin(new EventProtocal(), clueans.get(position)).start();
            LogoUtils.saveLogo(clueans.get(position).getLogoUrl(),
                    MainName.LOGO_JPG); // 保存logo

        } else {
            ThreadPool.getInstance().execute(
                    new ClubExit(new EventProtocal(), clueans.get(position)));
            // new ClubExit(new EventProtocal(), clueans.get(position)).start();
        }
    }

}
