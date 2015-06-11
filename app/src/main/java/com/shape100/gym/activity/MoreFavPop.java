package com.shape100.gym.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;

import com.shape100.gym.R;
import com.shape100.gym.activity.CurrTabSelf.ItemNode;
import com.shape100.gym.adapter.FavoriteAdapter;

public class MoreFavPop extends BaseActivity {
	private ArrayList<ItemNode> itemNodes;
	private GridView mGridView;
	private FavoriteAdapter adapter;
	private LayoutParams params;
	
	public static void ActionStart(Activity activity,
			ArrayList<ItemNode> itemNodes, LayoutParams params) {
		
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.more_favorite_course);
	}
}
