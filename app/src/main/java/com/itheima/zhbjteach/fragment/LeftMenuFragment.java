package com.itheima.zhbjteach.fragment;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.zhbjteach.MainActivity;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.impl.NewsCenterPager;
import com.itheima.zhbjteach.domain.NewsBean.NewsMenuBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 左侧菜单Fragment
 * 
 * @author Kevin
 * 
 */
public class LeftMenuFragment extends BaseFragment {

	private static final String TAG = LeftMenuFragment.class.getSimpleName();
	ArrayList<NewsMenuBean> mMenuList;// 菜单列表
	
	@ViewInject(R.id.lv_left_menu)
	private ListView lvList;
	
	private LeftMenuAdapter mAdapter;

	private int mCurrentPosition;// 当前选中第几个菜单

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPosition = position;
				Log.d(TAG, "当前菜单:" + mCurrentPosition);
				mAdapter.notifyDataSetChanged();// 刷新listview

				MainActivity mainUI = (MainActivity) mActivity;
				SlidingMenu slidingMenu = mainUI.getSlidingMenu();
				slidingMenu.toggle();// 如果侧边栏打开,则关闭;如果关闭,则打开
				
				setCurrentDetailPager(position);//设置当前要显示的详情页面
			}
		});

		return view;
	}

	/**
	 * 设置当前要显示的详情页面
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment fragment = mainUI.getContentFragment();//获取主页Fragment
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();//获取新闻中心页面
		newsCenterPager.setCurrentDetailPager(position);//设置新闻中心的详情页面
	}

	/**
	 * 设置侧边栏数据
	 * 
	 * @param data
	 */
	public void setNewsMenuData(ArrayList<NewsMenuBean> data) {
		this.mMenuList = data;
		mCurrentPosition = 0;//初始化位置信息
		Log.d(TAG, "侧边栏数据: " + data);
		mAdapter = new LeftMenuAdapter();
		lvList.setAdapter(mAdapter);//设置listview的数据源
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMenuList.size();
		}

		@Override
		public NewsMenuBean getItem(int position) {
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.left_menu_list_item,
					null);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvTitle.setText(getItem(position).title);

			// 如果是当前选中的item, 置为可用,显示为红色,如果不是,置为不可用,显示为白色
			tvTitle.setEnabled(position == mCurrentPosition);
			return view;
		}

	}
}
