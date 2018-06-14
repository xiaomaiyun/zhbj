package com.itheima.zhbjteach.utils;

import android.content.Context;
import android.graphics.Bitmap.Config;

import com.lidroid.xutils.BitmapUtils;

/**
 * 获取BitmapUtils对象, 保证多个模块共用一个BitmapUtils对象,避免内存溢出
 * 
 * @author Kevin
 * 
 */
public class BitmapHelper {

	private static BitmapUtils mBitmapUtils = null;

	public static BitmapUtils getBitmapUtils(Context ctx) {
		if (mBitmapUtils == null) {
			mBitmapUtils = new BitmapUtils(ctx);
			mBitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);
		}
		
		return mBitmapUtils;
	}
}
