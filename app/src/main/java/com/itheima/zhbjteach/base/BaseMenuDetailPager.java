package com.itheima.zhbjteach.base;

import android.app.Activity;
import android.view.View;

/**
 * 新闻中心下, 几个菜单子页面的基类
 * 
 * @author Kevin
 * 
 */
public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;

	public BaseMenuDetailPager(Activity activity) {
		this.mActivity = activity;
		mRootView = initView();
	}

	/**
	 * 初始化界面
	 * @return
	 */
	public abstract View initView();

	/**
	 * 初始化数据
	 */
	public void initData() {

	};
}
