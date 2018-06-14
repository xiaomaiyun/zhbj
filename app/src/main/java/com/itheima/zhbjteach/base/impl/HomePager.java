package com.itheima.zhbjteach.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbjteach.base.BasePager;

/**
 * 首页
 * 
 * @author Kevin
 * 
 */
public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		Log.d(TAG, "首页加载数据了");
		
		tvTitle.setText("智慧北京");
		btnMenu.setVisibility(View.GONE);

		TextView tvContent = new TextView(mActivity);
		tvContent.setText("首页");
		tvContent.setTextColor(Color.RED);
		tvContent.setTextSize(25);
		tvContent.setGravity(Gravity.CENTER);

		flContent.addView(tvContent);
	}

}
