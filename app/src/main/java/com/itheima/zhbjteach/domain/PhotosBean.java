package com.itheima.zhbjteach.domain;

import java.util.ArrayList;

/**
 * 组图对象
 * 
 * @author Kevin
 * 
 */
public class PhotosBean {

	public int retcode;
	public PhotosData data;

	public class PhotosData {
		public String countcommenturl;
		public String more;
		public ArrayList<PhotosNews> news;

		@Override
		public String toString() {
			return "PhotosData [news=" + news + "]";
		}
	}

	public class PhotosNews {
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String largeimage;
		public String listimage;
		public String pubdate;
		public String smallimage;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "PhotosNews [listimage=" + listimage + ", title=" + title
					+ "]";
		}
	}

	@Override
	public String toString() {
		return "PhotosBean [data=" + data + "]";
	}
	
}
