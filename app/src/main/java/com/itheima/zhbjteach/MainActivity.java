package com.itheima.zhbjteach;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.itheima.zhbjteach.fragment.ContentFragment;
import com.itheima.zhbjteach.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 * 
 * @author Kevin
 * 
 */
public class MainActivity extends SlidingFragmentActivity {

	private static final String FRAG_CONTENT = "frag_content";// 主页面Fragment的标记
	private static final String FRAG_MENU_LEFT = "frag_menu_left";// 侧边栏Fragment的标记

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题
		setContentView(R.layout.activity_main);// 配置主界面

		// 配置左侧菜单
		setBehindContentView(R.layout.left_menu);
		// 设置菜单模式
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置触摸模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置主界面显示宽度
		int width = getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*3/5);

		initFragment();
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction ft = fm.beginTransaction();
		// 替换帧布局
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAG_MENU_LEFT);
		ft.replace(R.id.fl_main, new ContentFragment(), FRAG_CONTENT);
		// 提交事务
		ft.commit();
		// fm.findFragmentByTag(arg0); 根据tag获取Fragment对象
	}

	/**
	 * 获取侧边栏对象
	 * @return
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm
				.findFragmentByTag(FRAG_MENU_LEFT);//根据tag获取Fragment对象
		return fragment;
	}
	
	/**
	 * 获取主页对象
	 * @return
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(FRAG_CONTENT);//根据tag获取Fragment对象
		return fragment;
	}
	
}
