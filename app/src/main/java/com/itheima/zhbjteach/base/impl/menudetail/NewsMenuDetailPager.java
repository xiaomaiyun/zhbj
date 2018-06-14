package com.itheima.zhbjteach.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.zhbjteach.MainActivity;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.BaseMenuDetailPager;
import com.itheima.zhbjteach.domain.NewsBean.NewsMenuBean;
import com.itheima.zhbjteach.domain.NewsBean.NewsMenuTab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 侧边栏, 新闻详情页面
 * 
 * @author Kevin
 * 
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	private static final String TAG = NewsMenuDetailPager.class.getSimpleName();

	@ViewInject(R.id.tpi_news_menu)
	private TabPageIndicator mIndicator;

	@ViewInject(R.id.vp_news_menu)
	private ViewPager mViewPager;

	private ArrayList<NewsMenuTab> mNewsTabDataList;// 页签对应的数据
	private ArrayList<TabDetailPager> mNewsTabPagerList;// 页签对应的页面列表

	private NewsMenuAdapter mAdapter;

	public NewsMenuDetailPager(Activity activity, NewsMenuBean newsMenuBean) {
		super(activity);
		mNewsTabDataList = newsMenuBean.children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		ViewUtils.inject(this, view); // 把当前的View对象注入到xUtils框架中
		return view;
	}

	@Override
	public void initData() {
		mNewsTabPagerList = new ArrayList<TabDetailPager>();

		// 初始化页签页面列表
		for (NewsMenuTab newsTab : mNewsTabDataList) {
			TabDetailPager pager = new TabDetailPager(mActivity, newsTab);
			mNewsTabPagerList.add(pager);
		}

		mAdapter = new NewsMenuAdapter();
		mViewPager.setAdapter(mAdapter);

		// 注意: 如果ViewPagerIndicator和ViewPager搭配使用时,
		// 设置ViewPager滑动监听的方法必须在ViewPagerIndicator中执行!!!!
		// mViewPager.setOnPageChangeListener(this);

		mIndicator.setViewPager(mViewPager);// 将指针和ViewPager关联起来
		mIndicator.setOnPageChangeListener(this);// 设置ViewPager的滑动监听
	}

	/**
	 * ViewPager的数据适配器
	 * 
	 * @author Kevin
	 * 
	 */
	class NewsMenuAdapter extends PagerAdapter {

		/**
		 * 返回页面标题, 用于在指针中显示
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTabDataList.get(position).title;
		}

		@Override
		public int getCount() {
			return mNewsTabDataList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager tabDetailPager = mNewsTabPagerList.get(position);
			container.addView(tabDetailPager.mRootView);
			tabDetailPager.initData();// 初始化数据
			return tabDetailPager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@OnClick(R.id.iv_news_tab_next)
	public void nextTab(View view) {
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d(TAG, "onPageSelected=" + arg0);
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (arg0 == 0) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 设置侧边栏不可用
		}
	}
}
