package com.shape100.gym.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shape100.gym.Event;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.adapter.CommentsAdapter;
import com.shape100.gym.adapter.WBpicAdapter;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.CommentsData;
import com.shape100.gym.protocol.AddPraise;
import com.shape100.gym.protocol.CommentsShowList;
import com.shape100.gym.protocol.CreateComments;
import com.shape100.gym.protocol.DeleteDynamic;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.widget.BottomPop;
import com.shape100.widget.BottomPop.BottomPopListener;
import com.shape100.widget.GrideforListview;
import com.shape100.widget.ListviewForScrollview;
import com.shape100.widget.MenuPop;
import com.shape100.widget.MenuPop.MenuItemListener;
import com.shape100.widget.StickyScrollView;
import com.shape100.widget.StickyScrollView.BottomScrollerListener;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event.CommendChangeEvent;

public class CommentActivity extends SlideActivity implements OnClickListener,
        BottomScrollerListener, MenuItemListener, BottomPopListener {
    public static final int RESULT_COMMEND = 11;

    private static final String DYNAMIC = "dynamic";
    private static final String FLAG = "flag";
    private static final int COUNT_PAGE = 10;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm",
            Locale.CHINA);

    private StickyScrollView scrollView;

    private CommentsData dynamic;
    private GrideforListview gridView;
    private WBpicAdapter adapter;
    private ImageView mImageContent;
    private ImageView mAvatarView;
    private TextView mTextContent;
    private TextView mTime, mName, praise, comments;
    private int flag = 0;
    private boolean flagOK;
    private int page = 1;
    private boolean isLastPage;

    private View bottomView;
    private InputMethodManager imp;
    private EditText et_write_comment;
    private TextView commentBtn;
    private ArrayList<CommentsData> commentsDatas;
    private ListviewForScrollview listview;

    private CommentsAdapter commentsAdapter;
    private int reply_pos = -1;
    private long reply_id;
    private View loadingView;
    private boolean isPraise;
    private boolean isPostEvent;
    private ArrayList<String> listText;
    private MenuPop pop;
    private int offset = 0, count_comm;
    private TextView right_head;
    private BottomPop bottomPop;
    private boolean isBottomRemove;
    private View up_show;

    public static void ActionStart(Activity activity, CommentsData dynamic,
                                   int flag) {
        Intent intent = new Intent(activity, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DYNAMIC, dynamic);
        bundle.putInt(FLAG, flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_comment);
        initView();
        initGride();
        initComments();
        ThreadPool.getInstance().execute(
                new CommentsShowList(dynamic.getId(), COUNT_PAGE, page,
                        new CommentHandler()));
    }

    private void initView() {
        loadingView = findViewById(R.id.bottom_loading_view);
        up_show = findViewById(R.id.bg_up_show);
        right_head = (TextView) findViewById(R.id.tv_title_right);
        right_head.setVisibility(View.VISIBLE);
        right_head.setBackgroundResource(R.drawable.navigation_more);
        right_head.setOnClickListener(this);
        scrollView = (StickyScrollView) findViewById(R.id.scrollview_sticky);
        scrollView.setOnBottomListener(this);
        listview = (ListviewForScrollview) findViewById(R.id.listview_comments);
        gridView = (GrideforListview) findViewById(R.id.gride_wb_pic);

        mImageContent = (ImageView) findViewById(R.id.blogitem_img);
        mTextContent = (TextView) findViewById(R.id.blogitem_text);
        mTime = (TextView) findViewById(R.id.blogitem_time);
        mName = (TextView) findViewById(R.id.tv_blogitem_name);
        mAvatarView = (ImageView) findViewById(R.id.blogitem_avatar);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.blogitem_avatar).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.comments_title));
        bottomView = findViewById(R.id.include_bottom_view);
        et_write_comment = (EditText) findViewById(R.id.et_comment_view_write);
        commentBtn = (TextView) findViewById(R.id.tv_comment_view_send);
        commentBtn.setOnClickListener(this);
        comments = (TextView) findViewById(R.id.tv_blogitem_comment);
        comments.setOnClickListener(this);
        praise = (TextView) findViewById(R.id.tv_blogitem_praise);
        praise.setOnClickListener(this);

        dynamic = (CommentsData) getIntent().getExtras().get(DYNAMIC);
        flag = getIntent().getIntExtra(FLAG, 0);
        ImageLoader.getInstance().displayImage(
                dynamic.getUser().getProfileImageUrl(), mAvatarView);
        mTextContent.setText(dynamic.getText());
        Date date = new Date(dynamic.getCreated_at());
        mTime.setText(sdf.format(date));
        mName.setText(dynamic.getUser().getName());
        reply_id = dynamic.getId();
        count_comm = dynamic.getStrincomments_count();
        setCP();
        initEditSend();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        registerForContextMenu(mTextContent);
        scrollView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoft();
                return false;
            }
        });

        bottomPop = new BottomPop(this, dynamic.getUser().getUserId(), up_show);
        clickList();
    }

    private void clickList() {
        listText = new ArrayList<String>();

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listText.clear();
                listText.add(getResources().getString(R.string.reply));
                listText.add(getResources().getString(R.string.copy));
                reply_pos = position;
                if (commentsDatas.get(position).getUser().getUserId() == AppConfig
                        .getInstance().getUserId()) {
                    listText.add(getResources().getString(R.string.delete));
                }
                pop = new MenuPop(CommentActivity.this, listText);
                pop.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (isPostEvent) {
            EventBus.getDefault().post(new CommendChangeEvent());
        }
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "复制");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                copyText(mTextContent.getText().toString());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 复制到粘贴框
     *
     * @author yupu
     * @date 2015年4月7日
     */
    private void copyText(String content) {
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy", content);
        clip.setPrimaryClip(clipData);

        Toast.makeText(CommentActivity.this, getResources().getString(R.string.copy_response), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * 隐藏软键盘
     *
     * @author yupu
     * @date 2015年3月23日
     */
    private void hideSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_write_comment.getWindowToken(), 0);
    }

    /**
     * 设置评论与赞
     *
     * @author yupu
     * @date 2015年3月13日
     */
    private void setCP() {
        String sc = dynamic.getStrincomments_count() == 0 ? getResources().getString(R.string.comments_success) : ""
                + dynamic.getStrincomments_count();
        comments.setText(sc);

        String sp = dynamic.getLikes_count() == 0 ? "" : dynamic
                .getLikes_count() + "";
        praise.setText(sp);
        isPraise = dynamic.isLiked();
        setPraise();
    }

    /**
     * 初始化评论列表
     *
     * @author yupu
     * @date 2015年3月12日
     */
    private void initComments() {
        commentsAdapter = new CommentsAdapter(this);
        listview.setAdapter(commentsAdapter);
    }

    /**
     * 是否滑到顶部
     *
     * @author yupu
     * @date 2015年3月9日
     */
    // TODO
    private void IsScrollTop() {
        if (flag != 0) {
            final View view = findViewById(R.id.comment_top_view);
            ViewTreeObserver vo = view.getViewTreeObserver();
            vo.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    scrollView.scrollTo(0, view.getTop());
                }
            });
        }
    }

    /**
     * 监听scrollview的滑动停止
     *
     * @author yupu
     * @date 2015年3月9日
     */
    // TODO
    private void scrollListener() {
        scrollView.setOnTouchListener(new OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            if (bottomView.getVisibility() != View.VISIBLE) {
                                showBottomView();
                            }
                        } else {
                            if (bottomView.getVisibility() == View.VISIBLE) {
                                stopBottomView();
                            }
                            handler.sendMessageDelayed(handler.obtainMessage(
                                    touchEventId, scroller), 5);
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                return false;
            }
        });
    }

    private void initGride() {
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SwitcherPhotoActivity.ActionStart(CommentActivity.this,
                        (ArrayList<String>) dynamic.getPic_urls(), position,
                        false);
            }
        });

        if (dynamic.getPic_urls() == null
                || TextUtils.isEmpty(dynamic.getPic_urls().get(0))) {
            gridView.setVisibility(View.GONE);
            mImageContent.setVisibility(View.GONE);
        } else if (dynamic.getPic_urls().size() == 1) {
            gridView.setVisibility(View.GONE);
            mImageContent.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(
                    dynamic.getPic_urls().get(0), mImageContent);

            mImageContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigPictureActivity.ActionStart(CommentActivity.this,
                            dynamic.getPic_urls().get(0));
                }
            });
        } else {
            gridView.setVisibility(View.VISIBLE);
            mImageContent.setVisibility(View.GONE);
            adapter = new WBpicAdapter(this);
            gridView.setAdapter(adapter);
            gridView.setNumColumns(3);
            // if (dynamic.getPic_urls().size() == 4) {
            // gridView.setNumColumns(2);
            // } else {
            // gridView.setNumColumns(3);
            // }
            adapter.upDate(dynamic.getPic_urls());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_blogitem_comment:
                et_write_comment.setHint(getResources().getString(
                        R.string.commment_hint));
                reply_id = dynamic.getId();
                break;
            case R.id.tv_comment_view_send:
                sendComment();
                isPostEvent = true;
                break;
            case R.id.tv_blogitem_praise:
                praise.setClickable(false);
                isPraise = !isPraise;
                ThreadPool.getInstance().execute(
                        new AddPraise(dynamic.getId(), new CommentHandler(),
                                isPraise));
                isPostEvent = true;
                break;
            case R.id.blogitem_avatar:
                CoachPageActivity.ActionStart(CommentActivity.this, dynamic
                        .getUser().getUserId());
                break;
            case R.id.tv_title_right:
                up_show.setVisibility(View.VISIBLE);
                bottomPop.showAtLocation(
                        getLayoutInflater().inflate(R.layout.act_comment, null),
                        Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }

    private void addPraise() {
        int co = 0;
        if (isPraise) {
            co = TextUtils.isEmpty(praise.getText()) ? 1 : Integer
                    .parseInt(praise.getText().toString()) + 1;
            praise.setText(co + "");
        } else {
            co = Integer.parseInt(praise.getText().toString()) == 1 ? 0
                    : Integer.parseInt(praise.getText().toString()) - 1;
            praise.setText(co == 0 ? "" : co + "");
        }
        dynamic.setLikes_count(co);
        setPraise();
    }

    private void setPraise() {
        if (isPraise) {
            Drawable drawable_p = getResources().getDrawable(
                    R.drawable.icon_praise_highlighted);
            drawable_p.setBounds(0, 0, drawable_p.getMinimumWidth(),
                    drawable_p.getMinimumHeight());
            praise.setCompoundDrawables(drawable_p, null, null, null);
        } else {
            Drawable drawable_n = getResources().getDrawable(
                    R.drawable.icon_praise);
            drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),
                    drawable_n.getMinimumHeight());
            praise.setCompoundDrawables(drawable_n, null, null, null);
        }
    }

    private void sendComment() {
        String comment = et_write_comment.getText().toString();
        ThreadPool.getInstance().execute(
                new CreateComments(comment, reply_id, new CommentHandler()));
    }

    /**
     * 监听输入框的输入状态
     *
     * @author yupu
     * @date 2015年3月12日
     */
    private void initEditSend() {
        changeOK();
        et_write_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    flagOK = false;
                } else {
                    flagOK = true;
                }
                changeOK();
            }
        });
    }

    /**
     * 改变发送按钮的状态
     *
     * @author yupu
     * @date 2015年3月11日
     */
    private void changeOK() {
        if (flagOK) {
            commentBtn.setBackgroundResource(R.drawable.theme_corner);
            commentBtn.setTextColor(getResources()
                    .getColor(R.color.color_white));
            commentBtn.setClickable(true);
        } else {
            commentBtn.setBackgroundResource(R.drawable.btn_gray);
            commentBtn.setTextColor(getResources().getColor(
                    R.color.font_light_more));
            commentBtn.setClickable(false);
        }
    }

    /**
     * 显示底部view
     *
     * @author yupu
     * @date 2015年3月6日
     */
    private void showBottomView() {
        Animation animation = AnimationUtils.loadAnimation(
                CommentActivity.this, R.anim.push_bottom_in);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                bottomView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        // bottomView.startAnimation(animation);
        // imp = (InputMethodManager) MainApplication.sContext
        // .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imp.showSoftInput(et_write_comment, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏底部view
     *
     * @author yupu
     * @date 2015年3月6日
     */
    private void stopBottomView() {
        Animation animation = AnimationUtils.loadAnimation(
                CommentActivity.this, R.anim.push_bottom_out);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomView.setVisibility(View.GONE);
            }
        });

        bottomView.startAnimation(animation);
        imp = (InputMethodManager) MainApplication.sContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imp.isActive()) {
            imp.hideSoftInputFromWindow(et_write_comment.getWindowToken(), 0);
        }
    }

    class CommentHandler extends ProtocolHandler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Event.CREATECOMMENTS:
                    String sc = dynamic.getStrincomments_count() == 0 ? getResources().getString(R.string.comments) : ""
                            + (++count_comm);
                    comments.setText(sc);

                    Toast.makeText(CommentActivity.this, getResources().getString(R.string.comments_success), Toast.LENGTH_LONG)
                            .show();
                    et_write_comment.setText("");
                    ThreadPool.getInstance().execute(
                            new CommentsShowList(dynamic.getId(), COUNT_PAGE, page,
                                    new CommentHandler()));
                    break;
                case Event.CREATECOMMENTS_FAILED:
                    Toast.makeText(CommentActivity.this, getResources().getString(R.string.comments_failed), Toast.LENGTH_LONG)
                            .show();
                    break;
                case Event.COMMENTSSHOW:
                    if (msg.obj != null) {
                        ArrayList<CommentsData> dat = (ArrayList<CommentsData>) msg.obj;
                        if (dat == null || dat.size() < COUNT_PAGE) {
                            loadingView.setVisibility(View.GONE);
                            isLastPage = true;
                        }

                        if (page == 1) {
                            commentsDatas = dat;
                        } else {
                            commentsDatas.addAll(dat);
                        }
                        commentsAdapter.update(commentsDatas);
                    }
                    break;
                case Event.DELETEDYNAMIC:
                    if (isBottomRemove) {
                        isBottomRemove = false;
                        EventBus.getDefault().post(new CommendChangeEvent());
                        finish();
                    } else {
                        sc = dynamic.getStrincomments_count() == 0 ? "评论" : ""
                                + (--count_comm);
                        comments.setText(sc);
                        ThreadPool.getInstance().execute(
                                new CommentsShowList(dynamic.getId(), COUNT_PAGE,
                                        page, new CommentHandler()));
                    }
                    break;
                case Event.PRAISE:
                    addPraise();
                    praise.setClickable(true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void scrollBottom() {
        if (!isLastPage) {
            loadingView.setVisibility(View.VISIBLE);
            AnimationDrawable ani = (AnimationDrawable) ((ImageView) loadingView
                    .findViewById(R.id.iv_loading)).getDrawable();
            if (!ani.isRunning()) {
                ani.start();
            }
            ThreadPool.getInstance().execute(
                    new CommentsShowList(dynamic.getId(), COUNT_PAGE, ++page,
                            new CommentHandler()));
        }
    }

    @Override
    public void onItemClick(int pos) {
        switch (pos) {
            case 0:
                et_write_comment.setHint("回复: "
                        + commentsDatas.get(reply_pos).getUser().getName());
                et_write_comment.setHintTextColor(getResources().getColor(
                        R.color.titlebar_bg));
                reply_id = commentsDatas.get(reply_pos).getId();
                pop.dismiss();
                break;

            case 1:
                copyText(commentsDatas.get(reply_pos).getText());
                pop.dismiss();
                break;

            case 2:
                pop.dismiss();
                buildDialog(commentsDatas.get(reply_pos).getId()).show();
                break;

            default:
                break;
        }
    }

    private Dialog buildDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ThreadPool.getInstance().execute(
                        new DeleteDynamic(new CommentHandler() {
                        }, id));
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    @Override
    public void clickMenu(int flag) {
        bottomPop.dismiss();
        switch (flag) {
            case BottomPop.REMOVE_FLAG:
                isBottomRemove = true;
                buildDialog(dynamic.getId()).show();
                break;
            case BottomPop.COPY_FLAG:
                copyText(dynamic.getText());
                break;
            case BottomPop.CANCEL_FLAG:
                break;
            default:
                break;
        }
    }
}
