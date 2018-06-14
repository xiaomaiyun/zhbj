package com.itheima.zhbjteach.domain;

import java.util.ArrayList;

/**
 * 页签数据封装
 * 
 * @author Kevin
 * 
 */
public class TabDetailBean {

	public int retcode;
	public TabDatailData data;

	public class TabDatailData {
		public String countcommenturl;
		public String more;
		public ArrayList<News> news;
		public String title;
		public ArrayList<Topic> topic;
		public ArrayList<TopNews> topnews;

		@Override
		public String toString() {
			return "TabDatailData [news=" + news + ", title=" + title
					+ ", topnews=" + topnews + "]";
		}
	}

	/**
	 * 新闻列表
	 * 
	 * @author Kevin
	 * 
	 */
	public class News {
		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "News [title=" + title + "]";
		}
	}

	public class Topic {
		public String description;
		public String id;
		public String listimage;
		public String sort;
		public String title;
		public String url;
	}

	/**
	 * 顶部新闻条
	 * 
	 * @author Kevin
	 * 
	 */
	public class TopNews {
		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "TopNews [topimage=" + topimage + ", title=" + title + "]";
		}
	}

	@Override
	public String toString() {
		return "TabDetailBean [data=" + data + "]";
	}
	
}
