package com.shape100.gym.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Event;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.adapter.WBpicAdapter;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.model.Dynamic;
import com.shape100.gym.model.User;
import com.shape100.gym.protocol.AddPraise;
import com.shape100.gym.protocol.DeleteDynamic;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.UserTimeLine;
import com.shape100.widget.GrideforListview;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.CommendChangeEvent;

public class DynamicActivity extends SlideActivity implements OnClickListener,
        OnScrollListener {
    private static final String USERID = "userid";
    private static final int COUNT = 50;
    private static final Logger log = Logger.getLogger("DynamicActivity");
    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm",
            Locale.CHINA);

    private PullToRefreshListView mPullRefreshListView;
    private ContentAdapter adapter;

    private ArrayList<CommentsData> dynamics = new ArrayList<CommentsData>();

    private int page = 1;
    private boolean isPraise; // 是否点赞了

    private TextView praiseView;
    private CommentsData nodeFlag;
    private long userID;

    public static void ActionStart(Context context, long user_id) {
        Intent intent = new Intent(context, DynamicActivity.class);
        intent.putExtra(USERID, user_id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.hometab02);
        userID = getIntent().getLongExtra(USERID, 0);
        if (userID == 0) {
            log.e("传进来的userid出错");
        }
        initView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * eventbus post
     *
     * @author yupu
     * @date 2015年3月25日
     */
    public void onEventMainThread(CommendChangeEvent event) {
        if (event != null) {
            page = 1;
            ThreadPool.getInstance().execute(
                    new UserTimeLine(new EventResult(), userID, COUNT, page));
        }
    }

    /**
     * 初始化
     *
     * @author yupu
     * @date 2015年3月27日
     */
    private void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setOnScrollListener(this);
        findViewById(R.id.layout_title_view).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.dynamic_title));
        findViewById(R.id.tv_back).setOnClickListener(this);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(
                                DynamicActivity.this,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        // Do work to refresh the list here.
                        page = 1;
                        ThreadPool.getInstance().execute(
                                new UserTimeLine(new EventResult(), userID,
                                        COUNT, page));
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.load_more));
                        refreshView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.up_pull_lab));
                        refreshView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.up_release_lab));
                        // Do work to refresh the list here.
                        ThreadPool.getInstance().execute(
                                new UserTimeLine(new EventResult(), userID,
                                        COUNT, ++page));
                    }
                });

        // Add an end-of-list listener
        mPullRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {

                    }
                });

        mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CommentActivity.ActionStart(DynamicActivity.this,
                        dynamics.get(position - 1), 0);
            }
        });

        adapter = new ContentAdapter(this);
        mPullRefreshListView.setAdapter(adapter);
        ThreadPool.getInstance().execute(
                new UserTimeLine(new EventResult(), userID, COUNT, page));
    }

    private class ViewHolder {
        public CircleImageView mAvatarView;
        public TextView mTextContent;
        public ImageView mImageContent;
        public TextView mTime;
        public TextView mName;
        public TextView comment;
        public TextView praise;
        public GrideforListview gridView;
    }

    ;

    private class ContentAdapter extends BaseAdapter implements OnClickListener {

        private LayoutInflater mInflater;
        private WBpicAdapter adapter;
        private Context context;

        public ContentAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return dynamics == null ? 0 : dynamics.size();
        }

        @Override
        public Object getItem(int position) {
            return dynamics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            CommentsData node = dynamics.get(position);
            User user = node.getUser();
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.blogitem, parent,
                        false);
                holder = new ViewHolder();
                holder.mAvatarView = (CircleImageView) convertView
                        .findViewById(R.id.blogitem_avatar);
                holder.mTextContent = (TextView) convertView
                        .findViewById(R.id.blogitem_text);
                holder.mImageContent = (ImageView) convertView
                        .findViewById(R.id.blogitem_img);
                holder.mTime = (TextView) convertView
                        .findViewById(R.id.blogitem_time);
                holder.gridView = (GrideforListview) convertView
                        .findViewById(R.id.gride_wb_pic);
                holder.mName = (TextView) convertView
                        .findViewById(R.id.tv_blogitem_name);
                holder.comment = (TextView) convertView
                        .findViewById(R.id.tv_blogitem_comment);
                holder.praise = (TextView) convertView
                        .findViewById(R.id.tv_blogitem_praise);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(
                    user.getProfileImageUrl(),
                    holder.mAvatarView,
                    MainApplication.getInstance().getDisplayImageOptions(
                            R.drawable.ic_unknown));
            holder.mTextContent.setText(Utils.ToDBC(node.getText()));
            Date date = new Date(node.getCreated_at());
            holder.mTime.setText(sdf.format(date));
            holder.mName.setText(node.getUser().getName());
            holder.comment.setTag(position);
            if (node.getStrincomments_count() == 0) {
                holder.comment.setText(getResources().getString(R.string.comments));
            } else {
                holder.comment.setText(node.getStrincomments_count() + "");
            }

            if (node.getLikes_count() != 0) {
                holder.praise.setText(node.getLikes_count() + "");
            } else {
                holder.praise.setText("");
            }

            isPraise = node.isLiked();
            setPraise(holder.praise);

            holder.praise.setTag(node);
            holder.mAvatarView.setTag(node.getUser().getUserId());
            holder.mAvatarView.setOnClickListener(this);
            holder.comment.setOnClickListener(this);
            holder.praise.setOnClickListener(this);

            initGride(holder, node);
            return convertView;
        }

        private void initGride(ViewHolder holder, final Dynamic node) {
            holder.gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    SwitcherPhotoActivity.ActionStart(DynamicActivity.this,
                            (ArrayList<String>) node.getPic_urls(), position,
                            false);
                }
            });

            if (node.getPic_urls() == null
                    || TextUtils.isEmpty(node.getPic_urls().get(0))) {
                holder.gridView.setVisibility(View.GONE);
                holder.mImageContent.setVisibility(View.GONE);
            } else if (node.getPic_urls().size() == 1) {
                holder.gridView.setVisibility(View.GONE);
                holder.mImageContent.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(
                        node.getPic_urls().get(0), holder.mImageContent);
                holder.mImageContent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BigPictureActivity.ActionStart(DynamicActivity.this,
                                node.getPic_urls().get(0));
                    }
                });
            } else {
                holder.gridView.setVisibility(View.VISIBLE);
                holder.mImageContent.setVisibility(View.GONE);
                adapter = new WBpicAdapter(context);
                holder.gridView.setAdapter(adapter);
                holder.gridView.setNumColumns(3);
                adapter.upDate(node.getPic_urls());
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_blogitem_comment:
                    int pos = (int) v.getTag();
                    int flag = 0;

                    CommentActivity.ActionStart(DynamicActivity.this,
                            dynamics.get(pos), flag);
                    break;
                case R.id.tv_blogitem_praise:
                    nodeFlag = (CommentsData) v.getTag();
                    praiseView = (TextView) v;
                    praiseView.setClickable(false);
                    long id = nodeFlag.getId();
                    isPraise = !nodeFlag.isLiked();
                    ThreadPool.getInstance().execute(
                            new AddPraise(id, new EventResult(), isPraise));
                    break;
                case R.id.blogitem_avatar:
                    long userid = (long) v.getTag();
                    CoachPageActivity.ActionStart(DynamicActivity.this, userid);
                    break;
                default:
                    break;
            }
        }
    }

    private Dialog buildDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                DynamicActivity.this);
        builder.setTitle(getResources().getString(R.string.dialog_title));
        builder.setMessage(getResources().getString(R.string.dialog_delete_prompt));
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ThreadPool.getInstance().execute(
                        new DeleteDynamic(new EventResult(), id));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancle), null);
        return builder.create();
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

    /**
     * 设置点赞
     *
     * @author yupu
     * @date 2015年3月13日
     */
    private void addPraise(TextView view) {
        int co = 0;
        if (isPraise) {
            co = TextUtils.isEmpty(view.getText()) ? 1 : Integer.parseInt(view
                    .getText().toString()) + 1;
            view.setText(co + "");
        } else {
            co = Integer.parseInt(view.getText().toString()) == 1 ? 0 : Integer
                    .parseInt(view.getText().toString()) - 1;
            view.setText(co == 0 ? "" : co + "");
        }

        nodeFlag.setLikes_count(co);
        setPraise(view);
    }

    private void setPraise(TextView view) {
        if (isPraise) {
            Drawable drawable_p = getResources().getDrawable(
                    R.drawable.icon_praise_highlighted);
            drawable_p.setBounds(0, 0, drawable_p.getMinimumWidth(),
                    drawable_p.getMinimumHeight());
            view.setCompoundDrawables(drawable_p, null, null, null);
        } else {
            Drawable drawable_n = getResources().getDrawable(
                    R.drawable.icon_praise);
            drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),
                    drawable_n.getMinimumHeight());
            view.setCompoundDrawables(drawable_n, null, null, null);
        }
    }

    class EventResult extends ProtocolHandler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstVar.HANDLER_MSG_SUCCESS:
                    if (msg.obj != null) {
                        if (mPullRefreshListView.isRefreshing()) {
                            mPullRefreshListView.onRefreshComplete();
                        }

                        if (page == 1) {
                            dynamics = (ArrayList<CommentsData>) msg.obj;
                        } else {
                            dynamics.addAll((ArrayList<CommentsData>) msg.obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case ConstVar.HANDLER_MSG_FAILURE:
                    if (mPullRefreshListView.isRefreshing()) {
                        mPullRefreshListView.onRefreshComplete();
                    }
                    Toast.makeText(DynamicActivity.this, getResources().getString(R.string.dynamic_get_failed),
                            Toast.LENGTH_LONG).show();
                    break;
                case Event.PRAISE:
                    addPraise(praiseView);
                    nodeFlag.setLiked(isPraise);
                    praiseView.setClickable(true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }
}
