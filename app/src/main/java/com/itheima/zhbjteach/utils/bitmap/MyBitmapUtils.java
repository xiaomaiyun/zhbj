package com.itheima.zhbjteach.utils.bitmap;

import com.itheima.zhbjteach.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 自定义图片加载工具类
 * 
 * @author Kevin
 * 
 */
public class MyBitmapUtils {

	private NetCacheUtils mNetCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;

	public MyBitmapUtils() {
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
	}

	/**
	 * 加载图片的核心api
	 * 
	 * @param ivPic
	 *            ImageView对象
	 * @param url
	 *            图片链接
	 */
	public void display(ImageView ivPic, String url) {
		ivPic.setImageResource(R.drawable.pic_item_list_default);

		// 从内存缓存读
		Bitmap bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
		if (bitmap != null) {// 如果内存存在，就直接设置并返回
			ivPic.setImageBitmap(bitmap);
			System.out.println("从内存读取图片");
			return;
		}

		// 从本地缓存读
		bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
		if (bitmap != null) {// 如果本地文件存在，就直接设置并返回
			ivPic.setImageBitmap(bitmap);
			System.out.println("从本地读取图片");
			mMemoryCacheUtils.putBitmapToMemory(url, bitmap);// 设置内存图片
			return;
		}

		// 从网络缓存下载
		mNetCacheUtils.getBitmapFromNet(ivPic, url);
	}

}
