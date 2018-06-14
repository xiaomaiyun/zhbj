package com.itheima.zhbjteach.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.itheima.zhbjteach.MainActivity;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.BaseMenuDetailPager;
import com.itheima.zhbjteach.base.BasePager;
import com.itheima.zhbjteach.base.impl.menudetail.InteractMenuDetailPager;
import com.itheima.zhbjteach.base.impl.menudetail.NewsMenuDetailPager;
import com.itheima.zhbjteach.base.impl.menudetail.PhotosMenuDetailPager;
import com.itheima.zhbjteach.base.impl.menudetail.TopicMenuDetailPager;
import com.itheima.zhbjteach.domain.NewsBean;
import com.itheima.zhbjteach.domain.NewsBean.NewsMenuBean;
import com.itheima.zhbjteach.fragment.LeftMenuFragment;
import com.itheima.zhbjteach.global.GlobalContants;
import com.itheima.zhbjteach.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 新闻中心
 * 
 * @author Kevin
 * 
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 侧边栏详情页列表
	private ArrayList<NewsMenuBean> mLeftMenuList;// 侧边栏数据

	private ImageButton btnPhotoSwitch;// 切换组图展示方式的按钮

	public NewsCenterPager(Activity activity) {
		super(activity);
		btnPhotoSwitch = (ImageButton) mRootView.findViewById(R.id.btn_show);
	}

	@Override
	public void initData() {
		Log.d(TAG, "新闻中心加载数据了");
		tvTitle.setText("新闻");

		// 先从缓存中读取数据并展示
		String cache = CacheUtils.getCache(mActivity, GlobalContants.NEWS_URL);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}

		getDataFromNet();// 从网络获取数据
	}

	/**
	 * 从网络获取数据
	 */
	private void getDataFromNet() {
		HttpUtils utils = new HttpUtils();
		// RequestCallBack的泛型表示返回的数据类型, 在此我们只需要json的字符串文本, 所以传递String就可以
		utils.send(HttpMethod.GET, GlobalContants.NEWS_URL,
				new RequestCallBack<String>() {

					// 请求成功
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						Log.d(TAG, "网络Json数据: " + result);
						CacheUtils.setCache(mActivity, GlobalContants.NEWS_URL,
								result);
						processData(result);
					}

					// 请求失败
					@Override
					public void onFailure(HttpException error, String msg) {
						Log.e(TAG, "请求失败:" + msg);
						error.printStackTrace();
					}
				});
	}

	/**
	 * 解析数据
	 */
	private void processData(String result) {
		Gson gson = new Gson();
		NewsBean news = gson.fromJson(result, NewsBean.class);
		Log.d(TAG, "新闻中心数据: " + news);

		mLeftMenuList = news.data;

		MainActivity mainUI = (MainActivity) mActivity;// 获取Activity对象
		LeftMenuFragment fragment = mainUI.getLeftMenuFragment();// 获取侧边栏对象
		fragment.setNewsMenuData(mLeftMenuList);// 设置侧边栏数据

		// 初始化详情页数据
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, news.data
				.get(0)));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity, btnPhotoSwitch));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

		setCurrentDetailPager(0);// 设置新闻页面为初始页面
	}

	/**
	 * 设置当前详情页面
	 * 
	 * @param position
	 */
	public void setCurrentDetailPager(int position) {
		Log.d(TAG, "详情页面:" + position);
		BaseMenuDetailPager detailPager = mMenuDetailPagers.get(position);

		if (detailPager instanceof PhotosMenuDetailPager) {// 如果是组图页面,就展示组图切换按钮
			btnPhotoSwitch.setVisibility(View.VISIBLE);
		} else {
			btnPhotoSwitch.setVisibility(View.GONE);
		}

		flContent.removeAllViews();// 填充界面前,先把以前的界面清空
		flContent.addView(detailPager.mRootView);// 添加当前详情页的布局
		detailPager.initData();// 初始化数据

		tvTitle.setText(mLeftMenuList.get(position).title);
	}
}
