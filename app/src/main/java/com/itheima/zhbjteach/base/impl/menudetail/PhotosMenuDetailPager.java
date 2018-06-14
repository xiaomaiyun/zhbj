package com.itheima.zhbjteach.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itheima.zhbjteach.R;
import com.itheima.zhbjteach.base.BaseMenuDetailPager;
import com.itheima.zhbjteach.domain.PhotosBean;
import com.itheima.zhbjteach.domain.PhotosBean.PhotosNews;
import com.itheima.zhbjteach.global.GlobalContants;
import com.itheima.zhbjteach.utils.CacheUtils;
import com.itheima.zhbjteach.utils.Logger;
import com.itheima.zhbjteach.utils.bitmap.MyBitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 侧边栏, 组图详情页面
 * 
 * @author Kevin
 * 
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager {

	private ArrayList<PhotosNews> mPhotosList;// 组图数据集合

	@ViewInject(R.id.lv_photo)
	private ListView lvList;

	@ViewInject(R.id.gv_photo)
	private GridView gvList;

	private ImageButton btnPhotoSwitch;// 切换展现方式的按钮

	private boolean isListShow = true; // 标记是否是listView展示方式, 默认为true

	private PhotosAdapter mAdapter;

	public PhotosMenuDetailPager(Activity activity, ImageButton btnPhotoSwitch) {
		super(activity);
		this.btnPhotoSwitch = btnPhotoSwitch;
		this.btnPhotoSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchPhotosDisplay();
			}
		});
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.photo_menu_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils
				.getCache(mActivity, GlobalContants.PHOTOS_URL);

		if (!TextUtils.isEmpty(cache)) {
			proccessData(cache);
		}

		getDataFromNet();
	}

	private void getDataFromNet() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						CacheUtils.setCache(mActivity,
								GlobalContants.PHOTOS_URL, responseInfo.result);
						proccessData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	protected void proccessData(String result) {
		Gson gson = new Gson();
		PhotosBean photos = gson.fromJson(result, PhotosBean.class);
		Logger.d("组图网络数据:" + photos);

		mPhotosList = photos.data.news;

		if (mPhotosList != null) {
			mAdapter = new PhotosAdapter();
			lvList.setAdapter(mAdapter);
		}
	}

	class PhotosAdapter extends BaseAdapter {

		// private BitmapUtils utils;
		private MyBitmapUtils utils;

		public PhotosAdapter() {
			// utils = new BitmapUtils(mActivity);
			utils = new MyBitmapUtils();
		}

		@Override
		public int getCount() {
			return mPhotosList.size();
		}

		@Override
		public PhotosNews getItem(int position) {
			return mPhotosList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.photos_list_item, null);

				holder = new ViewHolder();
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photos_list_item);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_photos_list_title);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvTitle.setText(getItem(position).title);

			if (!TextUtils.isEmpty(getItem(position).listimage)) {
				// utils.display(holder.ivPhoto, getItem(position).listimage);
				utils.display(holder.ivPhoto, getItem(position).listimage);
			} else {
				holder.ivPhoto
						.setImageResource(R.drawable.pic_item_list_default);
			}

			return convertView;
		}

	}

	public class ViewHolder {
		public ImageView ivPhoto;
		public TextView tvTitle;
	}

	/**
	 * 修改图片展现方式(ListView或者GridView)
	 */
	private void switchPhotosDisplay() {
		if (isListShow) {
			isListShow = false;
			lvList.setVisibility(View.GONE);
			gvList.setVisibility(View.VISIBLE);
			gvList.setAdapter(mAdapter);

			btnPhotoSwitch.setImageResource(R.drawable.icon_pic_list_type);
		} else {
			isListShow = true;
			lvList.setVisibility(View.VISIBLE);
			gvList.setVisibility(View.GONE);
			lvList.setAdapter(mAdapter);

			btnPhotoSwitch.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}

}
