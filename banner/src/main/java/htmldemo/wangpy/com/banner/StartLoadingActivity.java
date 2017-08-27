package htmldemo.wangpy.com.banner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import htmldemo.wangpy.com.banner.adapter.MyAdapter;
import htmldemo.wangpy.com.banner.bean.Ad;
import htmldemo.wangpy.com.banner.db.AdvertisementDao;
import htmldemo.wangpy.com.banner.utils.AdvertiseUtil;

/**
 * Created by dell on 2017/8/27.
 */

public class StartLoadingActivity extends AppCompatActivity {
    private final int MSG_LOGIN_SUCCESS = 100;
    private final int MSG_LOGIN_FAIL = 101;
    private final int MSG_VERSION_CHECK_TIMEOUT = 102;
    private final int UPDATE_TEAY_TIME = 103;

    private int delayTime = 9;// 广告4秒倒计时
    private List<Ad> alist;
    private ImageView welComeImg, adImg;
    private RelativeLayout rl;
    private TextView dTime;
    private Button skipBtn;
    // 是否首次登陆
    private Boolean myIsfirst = null;
    SharedPreferences.Editor edited = null;
    SharedPreferences share = null;
    private Context context;
    private boolean adIsFinish = false;
    private Ad mAdver = null;
    private ViewPager viewpager;
    private int[] arr = {R.drawable.liuyifei, R.drawable.liuyifei, R.drawable.liuyifei};
    private List<View> list = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_loading_activity);
        welComeImg = (ImageView) findViewById(R.id.iv_welcome_img);
        skipBtn = (Button) findViewById(R.id.ll_ad_skip_btn);
        dTime = (TextView) findViewById(R.id.tv_time);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        alist = AdvertisementDao.getAdDataList();// 查询广告信息
        delayTime = alist.size() * 3;
        if (alist.size() == 0) {
            //发起下载请求
            // 获取广告
            List<Ad> list = new ArrayList<Ad>();
            Ad ad = new Ad();
            ad.setAndroidimg("http://cdn.duitang.com/uploads/item/201610/20/20161020070310_c5xWi.thumb.700_0.jpeg");
            ad.setUrl("http:www.baidu.com");
            ad.setEnd("123456465767567");
            list.add(ad);

            Ad ad1 = new Ad();
            ad1.setAndroidimg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4288482290,2505498678&fm=117&gp=0.jpg");
            ad1.setUrl("http:www.baidu.com");
            ad1.setEnd("12345647567");
            list.add(ad1);

            Ad ad2 = new Ad();
            ad2.setAndroidimg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3454305406,3449992120&fm=117&gp=0.jpg");
            ad2.setUrl("http:www.baidu.com");
            ad2.setEnd("12345646567");
            list.add(ad2);

            AdvertiseUtil.getLoadingAd(context, list);
            startActivity(new Intent(StartLoadingActivity.this, MainActivity.class));
        } else {
            //显示轮播
            initList(alist);
            viewpager.setAdapter(new MyAdapter(this, list));
            viewpager.setCurrentItem(0);
            //监听
            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == list.size() - 1 && delayTime == 0) {
                        jumpToMain();
                        finish();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_VERSION_CHECK_TIMEOUT, 1 * 1000);
    }

    private void jumpToMain() {
        startActivity(new Intent(StartLoadingActivity.this, MainActivity.class));
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGIN_SUCCESS:
                    Intent intent = new Intent(StartLoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MSG_LOGIN_FAIL:
                    break;
                case MSG_VERSION_CHECK_TIMEOUT:
                    // 启动页面版本检查接口调用：3秒中没有返回，跳转页面
                    skipToLoginOrMain();
                    break;
                case UPDATE_TEAY_TIME:
                    if (adIsFinish) {
                        if (delayTime > 0) {
                            if (delayTime % 3 == 0 && delayTime != list.size() * 3) {
                                int currentItem = viewpager.getCurrentItem();
                                ++currentItem;
                                viewpager.setCurrentItem(currentItem);
                            }
                            welComeImg.setVisibility(View.GONE);
                            viewpager.setVisibility(View.VISIBLE);
                            dTime.setVisibility(View.VISIBLE);
                            dTime.setText(delayTime + "");
                            handler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                            delayTime--;
                        } else {
                            jumpToMain();
                            finish();
                        }
                    } else {
                        jumpToMain();
                        finish();
                    }
                    break;
            }
        }
    };

    private void skipToLoginOrMain() {
        handler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
    }

    //初始化数据源
    private void initList(List<Ad> adList) {
        for (int i = 0; i < adList.size(); i++) {
            View v = View.inflate(StartLoadingActivity.this, R.layout.activity_loginview, null);
            ImageView mlog_Img = (ImageView) v.findViewById(R.id.iv_ad_img);
            Button mlog_btn = (Button) v.findViewById(R.id.ll_ad_skip_btn);
            File f = new File(adList.get(i).getAndroidimg());// 获取最新的一条广告信息
            if (f.exists()) {//// 判断文件存在，并且没有过期
                Bitmap b = AdvertiseUtil.scaleImgSize(f);
                mlog_Img.setImageBitmap(b);
                adIsFinish = true;
                b = null;
            }
            list.add(v);
            mlog_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StartLoadingActivity.this, MarginLayoutParamsCompat.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.removeMessages(MSG_VERSION_CHECK_TIMEOUT);
            handler.removeMessages(MSG_LOGIN_FAIL);
            handler = null;
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在欢迎界面屏蔽BACK键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back
            return true;
        }
        return false;
    }

    //广告有更新  删除以前文件
    private void deleteBanner() {
        for (Ad ad : alist) {
            AdvertiseUtil.deleteCachedAdverImg(context, ad);
            AdvertisementDao.deleteAd(ad);
        }
    }
}
