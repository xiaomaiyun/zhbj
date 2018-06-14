package com.itheima.zhbjteach;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.itheima.zhbjteach.utils.PrefUtils;
/**
 * 闪屏页面
 * @author Kevin
 *
 */
public class SplashActivity extends Activity {

	protected static final String TAG = SplashActivity.class.getSimpleName();

	public static final String PREF_IS_USER_GUIDE_SHOWED = "is_user_guide_showed";//标记新手引导是否已经展示过

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		initViews();
	}

	// 初始化欢迎页面的动画
	private void initViews() {
		RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

		// 旋转动画
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(1000);// 动画执行时间
		rotate.setFillAfter(true);// 动画结束后保持最终状态

		// 缩放动画
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);
		scale.setFillAfter(true);

		// 渐变动画
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);// 初始化动画集合
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画结束的回调
			@Override
			public void onAnimationEnd(Animation animation) {
				// 判断新手引导是否展示过
				boolean showed = PrefUtils.getBoolean(
						SplashActivity.this, PREF_IS_USER_GUIDE_SHOWED, false);

				if (showed) {
					// 已经展示过, 进入主页面
					Log.d(TAG, "进入主页面");
					startActivity(new Intent(SplashActivity.this,
							MainActivity.class));
				} else {
					// 没展示, 进入新手引导页面
					Log.d(TAG, "进入新手引导页面");
					startActivity(new Intent(SplashActivity.this,
							GuideActivity.class));
				}

				finish();
			}
		});

		rlRoot.startAnimation(set);
	}
}
