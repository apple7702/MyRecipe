
package com.example.test;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




import android.widget.LinearLayout.LayoutParams;


/**
 * Created by yuexiao on 5/5/16.
 */
public class TuTu implements ImageViewAdapter.OnItemClickListener {
    public static final String TAG = "TuTu";
    private ViewPager mViewPager;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private ImageViewAdapter myPagerAdapter;
    private int lastPoint = 0;
    private boolean isRunning = false;
    boolean stat = false;
    private Timer timer;
    private TimerTask timerTask;
    private List<ReInfo> listADbeans;
    private Context mContext;

    private int newPosition=0;
    private long delay=4000;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if(what == 0){
                if(listADbeans.size()>1){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
                }


            }
            if(what ==1){
                myPagerAdapter.notifyImages(listADbeans);
            }
        };
    };
    public TuTu(ViewPager mViewPager,LinearLayout mLinearLayout,Context mContext,List<ReInfo> listADbeans) {
        this.mContext = mContext;
        this.listADbeans = listADbeans;
        this.mViewPager = mViewPager;
        this.mLinearLayout = mLinearLayout;

        initADViewpager();

    }

    /**
     * 改变viewpager切换速度
     */
    private void changeViewpagerSpace() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initADViewpager() {
        for(int i = 0; i < listADbeans.size(); i++){

            ImageView view = new ImageView(mContext);
            //view.setBackgroundResource(listADbeans.get(i).getImgPath());
            // 把图片添加到列表
            listADbeans.get(i).setmImageView(view);
            // 实例化指示点
            ImageView point = new ImageView(mContext);
            point.setImageResource(R.drawable.point_seletor);
            LayoutParams params = new LayoutParams(dip2px(mContext,6), dip2px(mContext,6));
            params.leftMargin = dip2px(mContext,10);
            point.setLayoutParams(params);
            // 将指示点添加到线性布局里
            mLinearLayout.addView(point);

            // 设置第一个高亮显示
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }


        }
        // 设置适配器
        myPagerAdapter = new ImageViewAdapter(listADbeans);
        myPagerAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(myPagerAdapter);

        int midPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % listADbeans.size();
        if(listADbeans.size()==1){
            midPosition = 0;
        }
        mViewPager.setCurrentItem(midPosition);
        // 设置页面改变监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private float mPreviousOffset = -1;
            private float mPreviousPosition = -1;
            /**
             * 当某个页面被选中的时候回调
             */
            @Override
            public void onPageSelected(int position) {
                newPosition = position % listADbeans.size();

                // 设置对应的页面高亮
                mLinearLayout.getChildAt(newPosition).setEnabled(true);
                // 是上次的点不显示高亮
                mLinearLayout.getChildAt(lastPoint).setEnabled(false);

                lastPoint = newPosition;

            }

            /**
             * 当某个页面被滚动的时候回调
             */
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            /**
             * 当状态发生改变的时候回调, 静止-滚动-静止
             */
            @Override
            public void onPageScrollStateChanged(int state) {

                if(state ==1){
                    timer.cancel();
                    //timerTask.cancel();
                    stat = true;
                }
                if(state ==0){
                    if(stat){
                        startViewPager(delay);
                    }
                    stat = false;
                }
            }
        });

        isRunning = true;
        //getNetImages();

    }

    /**
     * 开启轮播图
     * @param delay
     */
    public void startViewPager(long delay) {
        this.delay = delay;
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, delay,delay);
    }

    public void destroyView() {
        if(timer != null){
            timer.cancel();
        }
        if(timerTask!= null){
            timerTask.cancel();
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 400;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, AccelerateInterpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * viewpager的item点击事件
     */
    @Override
    public void OnItemClick(View view, int position) {
        System.out.println(position+"");

    }

}