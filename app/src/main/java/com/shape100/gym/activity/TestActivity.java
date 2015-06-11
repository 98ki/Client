package com.shape100.gym.activity;

import java.io.File;
import java.io.IOException;

import com._98ki.util.FileUtils;
import com.shape100.gym.R;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends SlideActivity {
	TextView textView;

	public static void Start(Activity activity) {
		Intent intent = new Intent(activity, TestActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_test);
		textView = (TextView) findViewById(R.id.tv_test);
		try {
			byte[] by = FileUtils.readFile(new File(FileUtils.getSDPAHT()
					+ "log.txt"));
			textView.setText(new String(by));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerForContextMenu(textView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 0, 0, "复制一下吧");
		menu.add(0, 1, 0, "删除本地log日志");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clipData = ClipData.newPlainText("copy", textView
					.getText().toString());
			clip.setPrimaryClip(clipData);

			Toast.makeText(TestActivity.this, "已复制到粘贴板", Toast.LENGTH_LONG)
					.show();
			break;
		case 1:
			FileUtils.deleteFile(new File(FileUtils.getSDPAHT() + "log.txt"));
			break;
		default:
			break;
		}
		return true;
	}
}
