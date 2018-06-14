package com.itheima.zhbjteach.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.itheima.zhbjteach.base.BasePager;
/**
 * 智慧服务
 * @author Kevin
 *
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		Log.d(TAG, "智慧服务加载数据了");
		tvTitle.setText("生活");

		TextView tvContent = new TextView(mActivity);
		tvContent.setText("智慧服务");
		tvContent.setTextColor(Color.RED);
		tvContent.setTextSize(25);
		tvContent.setGravity(Gravity.CENTER);

		flContent.addView(tvContent);
	}

}
