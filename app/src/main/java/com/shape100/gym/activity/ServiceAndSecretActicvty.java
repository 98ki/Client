package com.shape100.gym.activity;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com._98ki.util.FileUtils;
import com.shape100.gym.R;

public class ServiceAndSecretActicvty extends SlideActivity implements
		OnClickListener {
	private TextView textContent;

	public static void ActionStart(Activity activity) {
		Intent intent = new Intent(activity, ServiceAndSecretActicvty.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_service);
		((TextView) findViewById(R.id.tv_title)).setText(getResources()
				.getString(R.string.serviceandarticle));
		findViewById(R.id.tv_back).setOnClickListener(this);
		textContent = (TextView) findViewById(R.id.tv_text_content);
		setTextContent();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_back) {
			finish();
		}
	}

	private void setTextContent() {
		InputStream input = null;
		byte[] as = new byte[1024];
		try {
			input = getResources().getAssets().open("service.txt");
			as = FileUtils.inputStreamToByte(input);
			String ad = new String(as);
			textContent.setText(ad);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
