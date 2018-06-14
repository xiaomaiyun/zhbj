package com.itheima.zhbjteach.utils.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.itheima.zhbjteach.utils.MD5Encoder;

/**
 * 本地缓存工具类
 * 
 * @author Kevin
 * 
 */
public class LocalCacheUtils {

	private static final String LOCAL_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache_48";

	/**
	 * 从本地读取图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String url) {
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(LOCAL_PATH, fileName);

			if (file.exists()) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
						file));
				return bitmap;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向本地存图片
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void putBitmapToLocal(String url, Bitmap bitmap) {
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(LOCAL_PATH, fileName);
			File parent = file.getParentFile();

			// 创建父文件夹
			if (!parent.exists()) {
				parent.mkdirs();
			}

			bitmap.compress(CompressFormat.JPEG, 100,
					new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
