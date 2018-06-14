package com.itheima.zhbjteach.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.itheima.zhbjteach.base.BasePager;
/**
 * 政务
 * @author Kevin
 *
 */
public class GovAffairPager extends BasePager {

	public GovAffairPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		Log.d(TAG, "政务加载数据了");
		tvTitle.setText("人口管理");

		TextView tvContent = new TextView(mActivity);
		tvContent.setText("政务");
		tvContent.setTextColor(Color.RED);
		tvContent.setTextSize(25);
		tvContent.setGravity(Gravity.CENTER);

		flContent.addView(tvContent);
	}

}
