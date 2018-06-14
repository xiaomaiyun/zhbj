package com.itheima.zhbjteach.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.zhbjteach.MainActivity;
import com.itheima.zhbjteach.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主页的各个子页面的父类
 * 
 * @author Kevin
 * 
 */
public class BasePager {

	public static final String TAG = BasePager.class.getSimpleName();
	
	public Activity mActivity;
	public TextView tvTitle;// 标题
	public ImageButton btnMenu;// 菜单按钮
	public FrameLayout flContent;// 正文

	public View mRootView; // 根布局的view对象

	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	/**
	 * 初始化View
	 */
	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		
		//点击按键, 打开/关闭侧边栏
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity mainUI = (MainActivity) mActivity;
				SlidingMenu slidingMenu = mainUI.getSlidingMenu();
				slidingMenu.toggle();// 如果侧边栏打开,则关闭;如果关闭,则打开
			}
		});
		
		return view;
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
