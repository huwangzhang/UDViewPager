package com.example.huwang.udviewpager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huwang.udviewpager.R;

import java.util.List;

/**
 * Created by huwang on 2017/5/12.
 * 支持布局和动态添加子View两种方式
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;

    private Path mPath;
    // 宽度
    private int mTriangleWidth;
    // 高度
    private int mTriangleHeight;
    //底宽和Tab标签的比例
    private static final float RADIO = 1 / 6f;
    // 初始位置
    private int mInitmTranslationX;
    // 偏移步长
    private int mTranslationX;
    // 获取自定义属性
    private int mVisibleCount;
    // 默认显示数量
    private static final int COUNT_DEFAULT_TAB = 4;

    private static final int COLOR_TAB_TEXT_NORMAL = 0x77ffffff;

    private static final int COLOR_TAB_TEXT_LIGHT = 0xffffffff;

    private List<String> mTitles;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
        if (mVisibleCount < 0) {
            mVisibleCount = COUNT_DEFAULT_TAB;
        }
        typedArray.recycle();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / mVisibleCount * RADIO);
        // 初始偏移量
        mInitmTranslationX = w / mVisibleCount / 2 - mTriangleWidth / 2;
        mTriangleHeight = mTriangleWidth / 2 - 2;
        initTriangle();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        // 如果Tab栏在顶部，需要上三角
//        mPath.lineTo(mTriangleWidth / title_bar_bg_one_row, -mTriangleHeight);
        // 如果Tab栏在底部，需要下三角
        mPath.lineTo(mTriangleWidth / 2, mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mVisibleCount;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        // 上三角
//        canvas.translate(mInitmTranslationX + mTranslationX, getHeight() + title_bar_bg_one_row);
        // dy相对于当前的LinearLayout
        // 下三角
        canvas.translate(mInitmTranslationX + mTranslationX, 0);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 指示器的滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / mVisibleCount;

        mTranslationX = (int) (tabWidth * (offset + position));
        // 容器移动
        if (position >= (mVisibleCount - 2) && offset > 0 && getChildCount() > mVisibleCount && position <= getChildCount()-3) {
            if (mVisibleCount != 1) {
                this.scrollTo((int) ((position - (mVisibleCount - 2)) * tabWidth + tabWidth * offset), 0);
            } else {
                this.scrollTo((int) (position * tabWidth + tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(generateTextView(title));
            }
        }
    }

    /**
     * 设置可见的数量
     *
     * @param count
     */
    public void setVisibleTabCount(int count) {
        mVisibleCount = count;
    }


    private static final int COLOR_TEXT_NORMAL = 0xee000000;

    /**
     * 根据title创建Tab
     */
    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(lp);
        return tv;
    }

    private ViewPager mViewPager;

    /**
     * 设置关联的ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, final int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // tabWidth*positionOffset + position * tabWidth
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                highLightTabText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTabText(pos);
    }
    // 实现了系统提供的接口，所以要提供一个开放接口
    public interface PageOnChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public PageOnChangeListener mListener;

    public void setPageOnChangeListener(PageOnChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * Tab文本颜色
     */
    public void resetTabTextColor() {
        for (int i = 0; i < getChildCount(); i++) {
            setTabTextColorByPos(i, COLOR_TAB_TEXT_NORMAL);
        }
    }

    private void setTabTextColorByPos(int i, int colorTabTextNormal) {
        View view = getChildAt(i);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(colorTabTextNormal);
        }
    }

    /**
     * 高亮tab文本
     * @param pos
     */
    public void highLightTabText(int pos) {
        resetTabTextColor();
        setTabTextColorByPos(pos, COLOR_TAB_TEXT_LIGHT);
    }
}
