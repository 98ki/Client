package com.example.bluetooth.le.h5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.bluetooth.le.R;

import java.io.File;

public class H5Activity extends Activity {
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        mWebView = (WebView) findViewById(R.id.webView);

        int name = getIntent().getIntExtra("name",0);
        if (name==1) {
            //活动推送ki
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl("http://mp.weixin.qq.com/s?__biz=MjM5ODI3NTIwMA==&mid=207301338&idx=1&sn=f2444ce4eced2e35a60990beea659b35#wechat_redirect");

        } else if (name==2) {
            //器材教程
            mWebView.setVisibility(View.INVISIBLE);

            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/test.mp4");
//调用系统自带的播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            startActivity(intent);
           }
    }


}
