package com.paulz.audiotrack;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AudioTrackTabLayout extends HorizontalScrollView {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    // @formatter:off

    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};
    // @formatter:on
    private LinearLayout.LayoutParams matchParentTabLayoutParams;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private PageListener pageListener;
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private IPagerInTabLayoutProxy pager;

    private int tabCount;

    public int getCurrentPosition() {
        return currentPosition;
    }

    private int currentPosition = 0;
    private int selectedPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    //该属性表示里面的TAB是否均分整个PagerSlidingTabStrip控件的宽,
    // true是,false不均分,从左到右排列,默认false
    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 0;
    private int dividerPaddingTopBottom = 12;
    private int tabPadding = 0;
    private int backgroundPaddingBottom = 0;
    private int backgroundPaddingTop = 0;
    private int backgroundPaddingLeft = 0;
    private int backgroundPaddingRight = 0;
    private int dividerWidth = 1;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF45c01a;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;
    /**
     * 导航条圆角长度，默认为0；
     */
    private int pstsIndicatorRoundDegree = 0;

    private int tabBackgroundResId;
    private Locale locale;
    private Context context;
    private boolean smoothScrollWhenClickTab = true;             //设置当点击TAB时,内容区域的ViewPager是否需要动画切换
    private List<Map<String, View>> tabViews = new ArrayList<>();
    private boolean mFadeEnabled = true;
    private float zoomMax = 0.3f;
    private int tabTextGravity = Gravity.CENTER;
    private State mState;

    private enum State {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    private int oldPage;

    private int indicatorMode = INDICATOR_MODE_ALIGN_TAB;
    private int indicatorLength = 18;
    public final static int INDICATOR_MODE_ALIGN_TAB = 0;
    public final static int INDICATOR_MODE_ALIGN_TEXT = 1;
    public final static int INDICATOR_MODE_SPECIFY_LENGTH = 2;

    private int waveWidth=2;
    AudioTrack audioTrack;


    public AudioTrackTabLayout(Context context) {
        this(context, null);
    }

    public AudioTrackTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioTrackTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setFillViewport(true);
        setWillNotDraw(false);// 防止onDraw方法不执行

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        //设置默认值
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset,
                dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                indicatorHeight, dm);
        indicatorLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                indicatorLength, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                underlineHeight, dm);
        dividerPaddingTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dividerPaddingTopBottom, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth,
                dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        // get system attrs (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
                indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
                underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        indicatorLength = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorLength, indicatorLength);
        indicatorMode = a.getInt(R.styleable.PagerSlidingTabStrip_pstsIndicatorMode, indicatorMode);
        underlineHeight = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        pstsIndicatorRoundDegree = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorRoundDegree, pstsIndicatorRoundDegree);
        dividerPaddingTopBottom = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsDividerPaddingTopBottom, dividerPaddingTopBottom);
        tabPadding = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground,
                tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
                shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
                scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        selectedTabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTextSelectedColor,
                selectedTabTextColor);
        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTextColor,
                tabTextColor);

        tabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTextSize,
                tabTextSize);
        zoomMax = a.getFloat(R.styleable.PagerSlidingTabStrip_pstsScaleZoomMax, zoomMax);
        tabTextGravity = a.getInt(R.styleable.PagerSlidingTabStrip_pstsTabTextGravity, Gravity.CENTER);
        smoothScrollWhenClickTab = a.getBoolean(
                R.styleable.PagerSlidingTabStrip_pstsSmoothScrollWhenClickTab,
                smoothScrollWhenClickTab);

        backgroundPaddingTop = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_backgroundPaddingTop, 0);
        backgroundPaddingBottom = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_backgroundPaddingBottom, 0);
        backgroundPaddingLeft = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_backgroundPaddingLeft, 0);
        backgroundPaddingRight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_backgroundPaddingRight, 0);
        waveWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_waveWidth, waveWidth);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        matchParentTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
        pageListener = new PageListener();

        audioTrack=new AudioTrack();
        audioTrack.setWaveWidth(waveWidth);
    }

    /****
     * 关联ViewPager
     *
     * @param pager pager
     */
    public void setViewPager(IPagerInTabLayoutProxy pager) {
        this.pager = pager;
        if (this.pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        this.pager.addOnPageChangeListener(pageListener);
        this.pager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }
        });
        this.notifyDataSetChanged();
    }

    /****
     * 设置状态监听
     *
     * @param listener listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    private void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
                updateTabStyles();
            }
        });
    }

    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(tabTextGravity);
        tab.setSingleLine();
        tab.setIncludeFontPadding(false);

        TextView tab2 = new TextView(getContext());
        tab2.setText(title);
        tab2.setGravity(tabTextGravity);
        tab2.setSingleLine();
        tab2.setIncludeFontPadding(false);

        addTab(position, tab, tab2);
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab, null);
    }

    private void addTab(final int position, View tab, View tab2) {

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tab2.setPadding(tabPadding, 0, tabPadding, 0);

        FrameLayout framelayout = new FrameLayout(context);
        framelayout.addView(tab, 0, matchParentTabLayoutParams);
        framelayout.addView(tab2, 1, matchParentTabLayoutParams);
        defaultTabLayoutParams.bottomMargin=indicatorHeight;
        expandedTabLayoutParams.bottomMargin=indicatorHeight;
        tabsContainer.addView(framelayout, position,
                shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);

        framelayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFadeEnabled = false;//点击时没有文字颜色渐变效果
                pager.setCurrentItem(position, smoothScrollWhenClickTab);
//                currentPosition = position;
                scrollToChild(position, 0);//滚动HorizontalScrollView
            }
        });

        Map<String, View> map = new HashMap<>();

        ViewHelper.setAlpha(tab, 1);
        map.put("normal", tab);

        ViewHelper.setAlpha(tab2, 0);
        map.put("selected", tab2);

        tabViews.add(position, map);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            FrameLayout frameLayout = (FrameLayout) tabsContainer.getChildAt(i);
            frameLayout.setBackgroundResource(tabBackgroundResId);

            for (int j = 0; j < frameLayout.getChildCount(); j++) {
                View v = frameLayout.getChildAt(j);
                if (v instanceof TextView) {
                    TextView tab = (TextView) v;
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
                    tab.setPadding(tabPadding, 0, tabPadding, 0);
                    if (j == 0) {
                        tab.setTextColor(tabTextColor);
                        tab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    } else {
                        tab.setTextColor(selectedTabTextColor);
                        tab .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    ViewHelper.setAlpha(tabViews.get(i).get("normal"), 1);
                    ViewHelper.setAlpha(tabViews.get(i).get("selected"), 0);

                    setTabScale(frameLayout,1);

                    if (textAllCaps) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            tab.setAllCaps(true);
                        } else {
                            tab.setText(tab.getText().toString().toUpperCase(locale));
                        }
                    }
                    if (i == selectedPosition) {
                        ViewHelper.setAlpha(tabViews.get(i).get("normal"), 0);
                        ViewHelper.setAlpha(tabViews.get(i).get("selected"), 1);

                        setTabScale(frameLayout,1+zoomMax);
                    }
                }
            }
        }
        ajustIndicatorState();

    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            //不居中的
            // smoothScrollTo(newScrollX, 0);
            //以下是当tab很多时，点击屏幕右边的，点击的那个居中!!!
            int k = tabsContainer.getChildAt(position).getMeasuredWidth();
            int l = tabsContainer.getChildAt(position).getLeft() + offset;
            int i2 = l + k / 2 - this.getMeasuredWidth() / 2;
            smoothScrollTo(i2, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getBackground().setBounds(backgroundPaddingLeft,backgroundPaddingTop,getWidth()-backgroundPaddingRight,getHeight()-backgroundPaddingBottom);
        super.onDraw(canvas);
        Log.d("paulz","onDraw   currentPosition="+currentPosition);

        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int bottom = getHeight() - getPaddingBottom();

        // draw underline 当为INDICATOR_MODE_SPECIFY_LENGTH的时候不画下划线
        if (indicatorMode != INDICATOR_MODE_SPECIFY_LENGTH) {
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0, bottom - underlineHeight, tabsContainer.getWidth(), bottom, rectPaint);
        }
        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = computeLeft(currentTab);
        float lineRight = computeRight(currentTab);
        float curScale=1+zoomMax*(1-currentPositionOffset);
        float nextScale=1;
        curScale=1+zoomMax*(1-currentPositionOffset);
        nextScale=1+zoomMax*currentPositionOffset;

        // if there is an offset, start interpolating left and right coordinates between current and next tab

        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);

            final float nextTabLeft = computeLeft(nextTab);
            final float nextTabRight = computeRight(nextTab);

            lineLeft = (currentPositionOffset * nextTabLeft
                    + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight
                    + (1f - currentPositionOffset) * lineRight);

            setTabScale(nextTab,nextScale);
            ViewHelper.setTranslationX(nextTab,(nextTab.getWidth()-2*tabPadding)*zoomMax*(1-currentPositionOffset));

        }

        setTabScale(currentTab,curScale);


        ArrayList<RectF> rects=new ArrayList<>();
        RectF indicatorRect=new RectF(lineLeft,bottom-indicatorHeight,lineRight, bottom);
        Matrix matrix=new Matrix();
        matrix.postScale(curScale,curScale,indicatorRect.left,indicatorRect.centerY());
        matrix.mapRect(indicatorRect);
        for(int i=0;i<tabsContainer.getChildCount();i++){
//            if(i==currentPosition){
//                rects.add(indicatorRect);
//                continue;
//            }
            View tab = tabsContainer.getChildAt(i);
            lineLeft = computeLeft(tab);
            lineRight = computeRight(tab);
            RectF rect=new RectF(lineLeft,bottom-indicatorHeight,lineRight, bottom);
//            if(i>selectedPosition){
//                matrix.reset();
//                matrix.postTranslate(tab.getMeasuredWidth()*zoomMax,0);
//                matrix.mapRect(rect);
//            }
            if(i==currentPosition){
                matrix.reset();
                matrix.postScale(curScale,curScale,rect.left,rect.centerY());
//                matrix.postTranslate(tab.getMeasuredWidth()*zoomMax,0);
                matrix.mapRect(rect);
            }else if(i==currentPosition+1){
                matrix.reset();
                matrix.postScale(nextScale,nextScale,rect.left,rect.centerY());
                matrix.postTranslate((tab.getWidth()-2*tabPadding)*zoomMax*(1-currentPositionOffset),0);
                matrix.mapRect(rect);
            }else if(i>currentPosition+1){
                matrix.reset();
                matrix.postTranslate((tab.getWidth()-2*tabPadding)*zoomMax,0);
                matrix.mapRect(rect);
            }
            rects.add(rect);
        }
        audioTrack.drawSelf(this,canvas,indicatorColor,rects,currentPosition);


    }

    private void setTabScale(View tab,float scale){
        ViewHelper.setPivotX(tab, tab.getMeasuredWidth() * 0.0f+tabPadding);
        ViewHelper.setPivotY(tab, tab.getMeasuredHeight());
        ViewHelper.setScaleX(tab,scale);
        ViewHelper.setScaleY(tab,scale);
    }

    private int computeLeft(View tab) {
        if (indicatorMode == INDICATOR_MODE_SPECIFY_LENGTH) {
            return tab.getLeft() + (tab.getWidth() - indicatorLength) / 2+getPaddingLeft();
        } else if (indicatorMode == INDICATOR_MODE_ALIGN_TEXT) {
            return tab.getLeft()+getPaddingLeft();
        } else {
            return tab.getLeft()+getPaddingLeft();
        }
    }

    private int computeRight(View tab) {
        if (indicatorMode == INDICATOR_MODE_SPECIFY_LENGTH) {
            return tab.getLeft() + (tab.getWidth() + indicatorLength) / 2+getPaddingLeft();
        } else if (indicatorMode == INDICATOR_MODE_ALIGN_TEXT) {
            return tab.getRight()+getPaddingLeft();
        } else {
            return tab.getRight()+getPaddingLeft();
        }
    }

    public void setPageSelectListener(FragmentPageSelectListener listener) {
        this.mListener = listener;
    }

    private FragmentPageSelectListener mListener;

    private class PageListener implements OnPageChangeListener {
        private int oldPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("paulz","position="+position+"----positionOffset="+positionOffset);
            currentPosition = position;
            currentPositionOffset = positionOffset;
            if (mListener != null) {
                mListener.onSelected(position, positionOffset, positionOffsetPixels);
                return;
            }

            if (tabsContainer != null && tabsContainer.getChildAt(position) != null) {
                scrollToChild(position,
                        (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            }

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (mState == State.IDLE && positionOffset > 0) {
                oldPage = pager.getCurrentItem();
                mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
            }
            boolean goingRight = position == oldPage;
            if (mState == State.GOING_RIGHT && !goingRight)
                mState = State.GOING_LEFT;
            else if (mState == State.GOING_LEFT && goingRight)
                mState = State.GOING_RIGHT;

            float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

            View mLeft = tabsContainer.getChildAt(position);
            View mRight = tabsContainer.getChildAt(position + 1);

            if (effectOffset == 0) {
                mState = State.IDLE;
            }

            //            if (mFadeEnabled)
            //                animateFadeScale(mLeft, mRight, effectOffset, position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
                mFadeEnabled = true;
                ajustIndicatorState();
                audioTrack.doWave(AudioTrackTabLayout.this);
            }else {
                audioTrack.stopWave();
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
            Log.d("paulz","onPageScrollStateChanged   state="+state);

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("paulz","onPageSelected   position="+position);
            selectedPosition = position;

            //set old view statue
            ViewHelper.setAlpha(tabViews.get(oldPosition).get("normal"), 1);
            ViewHelper.setAlpha(tabViews.get(oldPosition).get("selected"), 0);

            //set new view statue
            ViewHelper.setAlpha(tabViews.get(position).get("normal"), 0);
            ViewHelper.setAlpha(tabViews.get(position).get("selected"), 1);

            if (delegatePageListener != null) {

                delegatePageListener.onPageSelected(position);
            }
            oldPosition = selectedPosition;
        }
    }

    private void ajustIndicatorState(){
        for(int i=0;i<tabsContainer.getChildCount();i++){
            View v=tabsContainer.getChildAt(i);
            float scale=1;
            if(i==selectedPosition){
                scale= 1 + zoomMax;
            }
            if(i>selectedPosition){
                ViewHelper.setTranslationX(v,(v.getWidth()-2*tabPadding)*zoomMax);
            }else {
                ViewHelper.setTranslationX(v,0);
            }
            setTabScale(v,scale);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightDp) {
        this.indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                indicatorLineHeightDp, getResources().getDisplayMetrics());
        defaultTabLayoutParams.bottomMargin=indicatorHeight;
        expandedTabLayoutParams.bottomMargin=indicatorHeight;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightDp) {
        this.underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                underlineHeightDp, getResources().getDisplayMetrics());
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPaddingTopBottom(int dividerPaddingDp) {
        this.dividerPaddingTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dividerPaddingDp, getResources().getDisplayMetrics());
        invalidate();
    }

    public int getDividerPaddingTopBottom() {
        return dividerPaddingTopBottom;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        notifyDataSetChanged();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizeSp) {
        this.tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeSp,
                getResources().getDisplayMetrics());
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setSelectedTextColor(int textColor) {
        this.selectedTabTextColor = textColor;
        updateTabStyles();
    }

    public void setSelectedTextColorResource(int resId) {
        this.selectedTabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getSelectedTextColor() {
        return selectedTabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
        updateTabStyles();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingDp) {
        this.tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingDp,
                getResources().getDisplayMetrics());
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public boolean isSmoothScrollWhenClickTab() {
        return smoothScrollWhenClickTab;
    }

    public void setSmoothScrollWhenClickTab(boolean smoothScrollWhenClickTab) {
        this.smoothScrollWhenClickTab = smoothScrollWhenClickTab;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setFadeEnabled(boolean enabled) {
        mFadeEnabled = enabled;
    }

    public boolean getFadeEnabled() {
        return mFadeEnabled;
    }

    public float getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }

    public void setIndicatorMode(int indicatorMode) {
        this.indicatorMode = indicatorMode;
    }

    public void setIndicatorLength(int indicatorLength) {
        this.indicatorLength = indicatorLength;
    }

    public void setWaveWidth(int waveWidth) {
        this.waveWidth = waveWidth;
        audioTrack.setWaveWidth(waveWidth);
    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

    protected void animateFadeScale(View left, View right, float positionOffset, int position) {
        if (mState != State.IDLE) {
            if (left != null) {
                ViewHelper.setAlpha(tabViews.get(position).get("normal"), positionOffset);
                ViewHelper.setAlpha(tabViews.get(position).get("selected"), 1 - positionOffset);
                float mScale = 1 + zoomMax - zoomMax * positionOffset;
                ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
                ViewHelper.setScaleX(left, mScale);
                ViewHelper.setScaleY(left, mScale);
            }
            if (right != null) {
                ViewHelper.setAlpha(tabViews.get(position + 1).get("normal"), 1 - positionOffset);
                ViewHelper.setAlpha(tabViews.get(position + 1).get("selected"), positionOffset);
                float mScale = 1 + zoomMax * positionOffset;
                ViewHelper.setPivotX(right, right.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
                ViewHelper.setScaleX(right, mScale);
                ViewHelper.setScaleY(right, mScale);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != pageListener && this.pager != null)
            this.pager.removeOnPageChangeListener(pageListener);
    }
}
