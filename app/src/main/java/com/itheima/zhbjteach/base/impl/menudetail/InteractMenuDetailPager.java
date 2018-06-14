package com.itheima.zhbjteach.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbjteach.base.BaseMenuDetailPager;

/**
 * 侧边栏, 互动详情页面
 * 
 * @author Kevin
 * 
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {

	public InteractMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		TextView tvContent = new TextView(mActivity);
		tvContent.setText("详情页: 互动");
		tvContent.setTextColor(Color.RED);
		tvContent.setTextSize(25);
		tvContent.setGravity(Gravity.CENTER);

		return tvContent;
	}

}
