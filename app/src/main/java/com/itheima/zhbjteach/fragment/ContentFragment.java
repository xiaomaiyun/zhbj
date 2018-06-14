package com.itheima.zhbjteach.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itheima.zhbjteach.MainActivity;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.BasePager;
import com.itheima.zhbjteach.base.impl.GovAffairPager;
import com.itheima.zhbjteach.base.impl.HomePager;
import com.itheima.zhbjteach.base.impl.NewsCenterPager;
import com.itheima.zhbjteach.base.impl.SmartServicePager;
import com.itheima.zhbjteach.base.impl.SettingPager;
import com.itheima.zhbjteach.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 主页面Fragment
 * 
 * @author Kevin
 * 
 */
public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.vp_content_pager)
	// 通过注解方式初始化View
	private NoScrollViewPager mViewPager;

	@ViewInject(R.id.rg_content_group)
	private RadioGroup mRadioGroup;

	private ArrayList<BasePager> mPagerList;// 五个子页面的集合,作为ViewPager的数据源

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view); // 把当前的View对象注入到xUtils框架中
		return view;
	}

	@Override
	public void initData() {
		mRadioGroup.check(R.id.rb_home);// 设置默认选项为首页

		// 向集合中添加5个页面
		mPagerList = new ArrayList<BasePager>();
		mPagerList.add(new HomePager(mActivity));// 添加首页
		mPagerList.add(new NewsCenterPager(mActivity));// 添加新闻中心
		mPagerList.add(new SmartServicePager(mActivity));// 添加智慧服务
		mPagerList.add(new GovAffairPager(mActivity));// 添加政务
		mPagerList.add(new SettingPager(mActivity));// 添加设置

		mViewPager.setAdapter(new ContentAdapter());// 设置ViewPager的数据源

		// 监听RadioGroup的选中事件,对页面进行切换
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					mViewPager.setCurrentItem(0, false);// 让ViewPager切换到第一个页面
					setSlidingMenuEnable(false);
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1, false);
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_service:
					mViewPager.setCurrentItem(2, false);
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_gov:
					mViewPager.setCurrentItem(3, false);
					setSlidingMenuEnable(true);
					break;
				case R.id.rb_setting:
					mViewPager.setCurrentItem(4, false);
					setSlidingMenuEnable(false);
					break;

				default:
					break;
				}
			}
		});

		// 监听ViewPager的选中事件
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mPagerList.get(position).initData();// 当该页面选中时,才开始初始化当前页面的数据
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mPagerList.get(0).initData();// 初始化第一页的数据
		setSlidingMenuEnable(false);// 不允许首页拉出侧边栏
	}

	private void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			View view = pager.mRootView;// 获取当前页面的布局对象
			container.addView(view);// 将布局对象添加到容器中
			// pager.initData();// 初始化数据, 为了节省流量,不在这里调用初始化数据的方法, 只有当标签真正被选中时才调用
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);// 从容器中销毁view对象
		}
	}

	/**
	 * 获取新闻中心页面
	 * 
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager) mPagerList.get(1);
	}
}
