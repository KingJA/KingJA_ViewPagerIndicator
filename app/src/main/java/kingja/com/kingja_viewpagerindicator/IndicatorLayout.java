package kingja.com.kingja_viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2016/3/13.
 */
public class IndicatorLayout extends LinearLayout {
    private static final float RATIO = 1 / 6f;
    private static final int COUNT_DEFAULT_VISIBLE = 4;
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTranslation;
    private int mInitTranslation;
    private int mTriangleHeight;
    private int mTabWidth;
    private int mVisibleCount;
    private int mTextColor;
    private int screenWidth;
    private List<String> mTitleList=new ArrayList<>();

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorLayout);
        mVisibleCount = typedArray.getInt(R.styleable.IndicatorLayout_visibleCount, COUNT_DEFAULT_VISIBLE);
        mTextColor = typedArray.getColor(R.styleable.IndicatorLayout_tabTextColor, 0xffff0000);
        typedArray.recycle();
    }

    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化画笔，颜色，抗锯齿，样式，圆角setPathEffect
        initPaint();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = getWidth() / mVisibleCount;
        mTriangleWidth = (int) (w / mVisibleCount * RATIO);
        mTriangleHeight = mTriangleWidth / 2;
        mInitTranslation = w / mVisibleCount / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslation + mTranslation, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 关联ViewPager的滑动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        mTranslation = (int) (mTabWidth * (offset + position));
        if (position >= (mVisibleCount - 2) && offset > 0 && getChildCount() > mVisibleCount) {
            if (mVisibleCount != 1) {
                this.scrollTo((position - (mVisibleCount -2)) * mTabWidth +(int)(mTabWidth * offset), 0);
            } else {
                this.scrollTo(position  * mTabWidth +(int)(mTabWidth * offset), 0);
            }
        }
        invalidate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) childView.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mVisibleCount;
            childView.setLayoutParams(lp);
        }
    }

    /**
     * 获取屏幕宽度
     * @return
     */

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 动态设置标题
     * @param list
     */
    public void setTitles(List<String> list) {
        if (list != null && list.size() > 0) {
            this.removeAllViews();
            this.mTitleList=list;
            for (String title : mTitleList) {
                addView(getTitleView(title));
            }
        }
    }

    /**
     * 设置标题样式
     * @param title
     * @return
     */
    private View getTitleView(String title) {
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(mTextColor);
        LinearLayout.LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.width=getScreenWidth()/mVisibleCount;
        tv.setLayoutParams(lp);
        return tv;
    }

    public void setUpWithViewPager(ViewPager viewPager, int position) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                IndicatorLayout.this.scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
