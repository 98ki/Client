package com.shape100.gym.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com._98ki.util.LocalImgUtils;
import com.shape100.gym.R;
import com.shape100.gym.adapter.MoreImgGridAdapter;
import com.shape100.gym.adapter.MoreImgGridAdapter.CheckItemListener;
import com.shape100.gym.adapter.MorePicListAdapter;
import com.shape100.gym.model.FileFolder;
import com.shape100.gym.protocol.ThreadPool;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

public class MorePicActivity extends SlideActivity implements OnClickListener,
		CheckItemListener {
	private static final int what = 101;
	public static final int CODE = 1;
	public static final String key_code = "selected";
	public static final String COUNT = "count";
	private PopupWindow popwindow;
	private TextView selectTileView;
	private GridView gridView;
	private View layout;
	private ListView listView;
	private View popView;
	private MorePicListAdapter listAdapter;
	private MoreImgGridAdapter gridAdapter;
	private LocalImgUtils locals;
	private ArrayList<String> selectedPic;
	private List<String> inPic; // 点进去的list
	private int countSelect;
	private String cur_name;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			initGrid();
			initList();
		}

		;
	};

	public static void ActionStart(Activity activity, int count) {
		Intent intent = new Intent(activity, MorePicActivity.class);
		intent.putExtra(COUNT, count);
		activity.startActivityForResult(intent, CODE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_more_pic);
		initView();
	}

	private void initView() {
		countSelect = getIntent().getIntExtra(COUNT, 0);
		selectedPic = new ArrayList<String>();
		layout = findViewById(R.id.layout_out_grid);
		selectTileView = (TextView) findViewById(R.id.tv_select_list);
		selectTileView.setOnClickListener(this);
		findViewById(R.id.tv_checked_complete).setOnClickListener(this);
		findViewById(R.id.tv_checked_cancle).setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.gride_img_more);
		popView = LayoutInflater.from(this)
				.inflate(R.layout.pop_pic_list, null);
		popwindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		popwindow.setBackgroundDrawable(new BitmapDrawable());
		popwindow.setFocusable(true);
		popwindow.setOutsideTouchable(true);
		popwindow.setAnimationStyle(R.style.AnimTop);
		locals = LocalImgUtils.getInstence();
		inPic = new ArrayList<String>();
		initGrid();
		initList();
	}

	/**
	 * 图片文件夹
	 *
	 * @author yupu
	 * @date 2015年2月2日
	 */
	private void initList() {
		listView = (ListView) popView.findViewById(R.id.list_pic_id);
		listAdapter = new MorePicListAdapter(this);
		listView.setAdapter(listAdapter);
		List<FileFolder> fileFolders = new ArrayList<FileFolder>();
		for (int i = 0; i < locals.getAllFolder().size() + 1; i++) {
			FileFolder fileFolder = new FileFolder();
			if (i == 0) {
				fileFolder.setName("相机胶卷");
				fileFolder.setContent(locals.getAllImgs());
			} else {
				fileFolder = locals.getAllFolder().get(i - 1);
			}
			fileFolders.add(fileFolder);
		}

		listAdapter.upDate(fileFolders);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FileFolder folder = (FileFolder) listAdapter.getItem(position);
				selectTileView.setText(folder.getName());
				inPic.clear();
				inPic.add(null);
				inPic.addAll(folder.getContent());
				gridAdapter.upDate(inPic, countSelect);
				popwindow.dismiss();
			}
		});
	}

	/**
	 * 图片显示
	 *
	 * @author yupu
	 * @date 2015年2月2日
	 */
	private void initGrid() {
		gridAdapter = new MoreImgGridAdapter(this);
		gridAdapter.setOnCheckListener(this);
		gridView.setAdapter(gridAdapter);
		selectTileView.setText("相机胶卷");
		inPic.clear();
		inPic.add(null);
		inPic.addAll(locals.getAllImgs());
		gridAdapter.upDate(inPic, countSelect);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_select_list:
			popwindow.showAsDropDown(layout);
			break;
		case R.id.tv_checked_complete:

			ThreadPool.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					Intent data = new Intent();
					data.putStringArrayListExtra(key_code, selectedPic);
					setResult(CODE, data);
					finish();
				}
			});
			break;
		case R.id.tv_checked_cancle:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClickItem(int position, boolean flag) {
		if (flag) {
			selectedPic.add(inPic.get(position));
		} else {
			selectedPic.remove(inPic.get(position));
		}
	}

	@Override
	public void onClickPhoto() {
		try {
			if (FileUtils.isSDcardExist()) {
				cur_name = MD5Tools.toMD5(System.currentTimeMillis() + "")
						+ ".jpg";
				String path = Environment.getExternalStorageDirectory()
						+ File.separator + Environment.DIRECTORY_DCIM + "/"
						+ cur_name;
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(path);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 2);
			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(MorePicActivity.this, "调用相机失败", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		String path = Environment.getExternalStorageDirectory()
				+ File.separator + Environment.DIRECTORY_DCIM + "/";
		Bitmap bitmap = ImageUtils.getBitmapFromLocal(path + cur_name);

		try {
			MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
					cur_name, "photo");
			// 通知更新
			getApplicationContext().sendBroadcast(
					new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
							.parse("file://" + path + cur_name)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bitmap.recycle();
		}

		ThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				LocalImgUtils.getInstence().initImg();
				locals = LocalImgUtils.getInstence();
				handler.sendEmptyMessage(what);
			}
		});
	}

	/**
	 * 建立图片选择信息
	 *
	 * @author yupu
	 * @date 2015年2月2日
	 */
	public class SelectPic {
		private String path;
		private boolean selected;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
}
