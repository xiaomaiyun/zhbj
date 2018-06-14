package com.itheima.zhbjteach.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.zhbjteach.NewsDetailActivity;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.BaseMenuDetailPager;
import com.itheima.zhbjteach.domain.NewsBean.NewsMenuTab;
import com.itheima.zhbjteach.domain.TabDetailBean;
import com.itheima.zhbjteach.domain.TabDetailBean.News;
import com.itheima.zhbjteach.domain.TabDetailBean.TopNews;
import com.itheima.zhbjteach.global.GlobalContants;
import com.itheima.zhbjteach.utils.BitmapHelper;
import com.itheima.zhbjteach.utils.CacheUtils;
import com.itheima.zhbjteach.utils.PrefUtils;
import com.itheima.zhbjteach.view.HorizontalScrollViewPager;
import com.itheima.zhbjteach.view.RefreshListView;
import com.itheima.zhbjteach.view.RefreshListView.RefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 页签对应的详情页面
 * 
 * @author Kevin
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener, OnItemClickListener {

	protected static final String TAG = TabDetailPager.class.getSimpleName();

	private static final String PREF_NEWS_READ = "news_read_id";

	NewsMenuTab mNewsTab;// 页签数据

	@ViewInject(R.id.cpi_tab_detail)
	CirclePageIndicator mIndicator;

	@ViewInject(R.id.vp_tab_detail)
	HorizontalScrollViewPager mViewPager;

	@ViewInject(R.id.tv_title)
	TextView tvTitle;// 顶部新闻的标题

	@ViewInject(R.id.lv_list)
	RefreshListView lvList;// 列表新闻

	private TopNewsAdapter mTopNewsAdapter;

	private ArrayList<TopNews> mTopNews;

	private String mUrl;// 页签网路数据的url

	private ArrayList<News> mNewsList;// 列表新闻数据

	private String mMoreUrl;

	private NewsAdapter mNewsAdapter;

	private Handler mHandler;

	private static final int TOP_NEWS_CHANGE_TIME = 4000;// 顶部新闻切换事件

	public TabDetailPager(Activity activity, NewsMenuTab newsTab) {
		super(activity);
		mNewsTab = newsTab;
		mUrl = GlobalContants.SERVER_URL + mNewsTab.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		ViewUtils.inject(this, view);

		View headerView = View.inflate(mActivity,
				R.layout.top_news_header_view, null);
		ViewUtils.inject(this, headerView);

		lvList.addHeaderView(headerView);// 增加顶部新闻为HeaderView

		lvList.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {
				Log.d(TAG, "onRefresh...");
				getDataFromNet();
			}

			@Override
			public void onLoadMore() {
				Log.d(TAG, "onLoadMore...");
				getMoreDataFromNet();
			}
		});

		/**
		 * 设置Item点击监听
		 */
		lvList.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mActivity, mUrl);
		// lvList.setRefreshing();// 显示下拉刷新控件
		if (!TextUtils.isEmpty(cache)) {
			processData(cache, false);
		}
		getDataFromNet();
	}

	// 请求网络数据
	private void getDataFromNet() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// Log.d(TAG, "页签详情页网络数据:" + responseInfo.result);
				Log.d(TAG, "页签详情页url:" + mUrl);
				CacheUtils.setCache(mActivity, mUrl, responseInfo.result);
				processData(responseInfo.result, false);

				lvList.onRefreshComplete(true);// 隐藏下拉刷新控件
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e(TAG, "请求失败:" + msg);
				lvList.onRefreshComplete(false);// 隐藏下拉刷新控件
				Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 加载更多数据
	 */
	private void getMoreDataFromNet() {
		if (mMoreUrl != null) {
			HttpUtils utils = new HttpUtils();
			utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					processData(responseInfo.result, true);
					lvList.onRefreshComplete(false);// 隐藏下拉刷新控件
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Log.e(TAG, "请求失败:" + msg);
					lvList.onRefreshComplete(false);// 隐藏下拉刷新控件
					Toast.makeText(mActivity, "加载更多失败", Toast.LENGTH_SHORT)
							.show();
				}
			});
		} else {
			lvList.onRefreshComplete(false);
			Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 解析网络数据
	 * 
	 * @param result
	 *            Json数据
	 * @param more
	 *            是否加载更多
	 */
	protected void processData(String result, boolean more) {
		Gson gson = new Gson();
		TabDetailBean tabInfo = gson.fromJson(result, TabDetailBean.class);

		if (!TextUtils.isEmpty(tabInfo.data.more)) {
			mMoreUrl = GlobalContants.SERVER_URL + tabInfo.data.more;
		} else {
			mMoreUrl = null;
		}

		if (!more) {// 不加载更多
			mTopNews = tabInfo.data.topnews;

			if (mTopNews != null) {
				mTopNewsAdapter = new TopNewsAdapter();
				mViewPager.setAdapter(mTopNewsAdapter);

				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);// 设置圆点的切换方式, 快照方式
				mIndicator.setOnPageChangeListener(this);
				mIndicator.onPageSelected(0);// 设置当前选中页面为第一个, 保证圆点标记位置正确

				tvTitle.setText(mTopNews.get(0).title);// 设置顶部新闻的标题
			}

			mNewsList = tabInfo.data.news;
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);// 为列表新闻填充数据
			}
		} else {// 加载更多
			ArrayList<News> news = tabInfo.data.news;
			if (mNewsList != null) {
				mNewsList.addAll(news);
				mNewsAdapter.notifyDataSetChanged();
			}
		}

		if (mHandler == null) {
			// 创建轮播条的Handler
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					int item = mViewPager.getCurrentItem();

					if (item < mTopNews.size() - 1) {
						item++;
					} else {// 判断是否到达最后一个
						item = 0;
					}
					// Log.d(TAG, "轮播条:" + item);

					mViewPager.setCurrentItem(item);
					mHandler.sendMessageDelayed(Message.obtain(),
							TOP_NEWS_CHANGE_TIME);
				};
			};

			mHandler.sendMessageDelayed(Message.obtain(), TOP_NEWS_CHANGE_TIME);// 延时4s发送消息
		}
	}

	/**
	 * 列表新闻适配器
	 * 
	 * @author Kevin
	 * 
	 */
	class NewsAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public NewsAdapter() {
			// utils = new MyBitmapUtils();
			utils = BitmapHelper.getBitmapUtils(mActivity);
			//utils.configDefaultLoadingImage(R.drawable.news_pic_default);// 设置默认图片
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public News getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.news_list_item,
						null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_news_icon);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_news_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvTitle.setText(getItem(position).title);
			holder.tvTime.setText(getItem(position).pubdate);

			utils.display(holder.ivIcon, getItem(position).listimage);// 加载图片

			String ids = PrefUtils.getString(mActivity,
					PREF_NEWS_READ, "");

			if (ids.contains(getItem(position).id)) {// 如果再已读列表中
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

			return convertView;
		}

		/**
		 * 局部刷新TextView
		 * 
		 * @param view
		 *            被点击的Item对象
		 */
		public void changeTextColor(View view) {
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_news_title);
			tvTitle.setTextColor(Color.GRAY);
		}
	}

	class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvTime;
	}

	/**
	 * 顶部新闻条适配器
	 * 
	 * @author Kevin
	 * 
	 */
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopNewsAdapter() {
			utils = BitmapHelper.getBitmapUtils(mActivity);
			utils.configDefaultBitmapConfig(Config.ARGB_4444);
			//utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);// 设置图片展现样式为:
													// 宽高填充ImageView(图片可能被拉伸或者缩放)
			image.setImageResource(R.drawable.topnews_item_default);
			container.addView(image);

			utils.display(image, mTopNews.get(position).topimage);// 参1表示ImageView对象,
																	// 参2表示图片url
			image.setOnTouchListener(new TopNewsTouchListener());
			// image.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent(mActivity,
			// NewsDetailActivity.class);
			// intent.putExtra("news_url", mTopNews.get(position).url);
			// mActivity.startActivity(intent);
			// }
			// });
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		tvTitle.setText(mTopNews.get(arg0).title);// 设置顶部新闻的标题
	}

	/**
	 * Item点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "Item点击:" + position);
		if (mNewsList != null) {
			News news = mNewsList.get(position);
			Log.d(TAG, "新闻id=" + news.id + "; 新闻标题=" + news.title);

			// 35314,35315,35316
			String ids = PrefUtils.getString(mActivity,
					PREF_NEWS_READ, "");

			if (!ids.contains(news.id)) {// 只有在不包含该id时才添加
				ids = ids + news.id + ",";
				PrefUtils.putString(mActivity, PREF_NEWS_READ, ids);// 更新已读id列表
			}

			// mNewsAdapter.notifyDataSetChanged();// 刷新ListView
			mNewsAdapter.changeTextColor(view);// 局部刷新ListView

			Intent intent = new Intent(mActivity, NewsDetailActivity.class);
			intent.putExtra("news_url", news.url);
			mActivity.startActivity(intent);
		}
	}

	class TopNewsTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d(TAG, "手指按下");
				mHandler.removeCallbacksAndMessages(null);// 移除消息队列中的所有元素
				break;
			case MotionEvent.ACTION_CANCEL:// 事件取消(比如按下后开始移动,
											// 那么就不会响应ACTION_UP动作了)
				Log.d(TAG, "事件取消");
				mHandler.sendMessageDelayed(Message.obtain(),
						TOP_NEWS_CHANGE_TIME);
				break;
			case MotionEvent.ACTION_UP:
				Log.d(TAG, "手指抬起");
				mHandler.sendMessageDelayed(Message.obtain(),
						TOP_NEWS_CHANGE_TIME);
				break;

			default:
				break;
			}

			return true;
		}
	}

}
