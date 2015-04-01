package com.intrepid.travel.ui.control;


import java.util.List;

import com.intrepid.travel.R;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class TabNaviBar extends FrameLayout {

	/**
	 * Define the cursor height  , unit is dp.
	 */
	private final static int HEIGHT_CURSOR = 3;
	
	private ImageView mImgViewCursor;

	private FrameLayout mViewGrpTrack;

	private List<View> mTabViewList;

	private int currIndex = 0;

	private OnTabChangedListener mOnTabChangListener;

	private TabAdapter mTabAdapter;

	public TabNaviBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TabNaviBar(Context context) {
		super(context);
		init();
	}

	void init() {
		mImgViewCursor = new ImageView(getContext());
		mImgViewCursor.setBackgroundColor(getResources().getColor(R.color.pink));
	}

	public void setAdapter(TabAdapter adapter) {
		mTabAdapter = adapter;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		LinearLayout container = new LinearLayout(getContext());
		container.setOrientation(LinearLayout.HORIZONTAL);

		mTabViewList = adapter.getTabView();
		int index = 0;
		if (mTabViewList != null && !mTabViewList.isEmpty()) {
			for (View view : mTabViewList) {
				container.addView(view, lp);
				view.setTag(index++);
				view.setOnClickListener(mTabOnclickListener);
			}
			refreshState();
		}

		addView(container, lp);
		mViewGrpTrack = new FrameLayout(getContext());
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		flp.gravity = Gravity.BOTTOM;
		addView(mViewGrpTrack, flp);
		
		float density = getResources().getDisplayMetrics().density;

		ImageView bottomDivider = new ImageView(getContext());
		bottomDivider.setBackgroundColor(getResources().getColor(R.color.blue));
		flp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		flp.gravity = Gravity.BOTTOM;
		mViewGrpTrack.addView(bottomDivider, flp);

		flp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) density * HEIGHT_CURSOR);
		flp.gravity = Gravity.BOTTOM;
		mViewGrpTrack.addView(mImgViewCursor, flp);

	}

	public List<View> getTabViews() {
		return mTabViewList;
	}

	private OnClickListener mTabOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			move((Integer) v.getTag());
		}
	};

	private void refreshState() {
		int index = 0;
		List<View> viewList = mTabViewList;
		for (View view : viewList) {
			mTabAdapter.viewStateChange(view, index == currIndex);
			index++;
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getTabCount();
		int sizeWidth = getMeasuredWidth();
		if (count > 0) {
			sizeWidth = sizeWidth / count;
		}

		int sizeHeight = mImgViewCursor.getMeasuredHeight();
		int widthSpec = MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.EXACTLY);
		int heightSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.EXACTLY);
		mImgViewCursor.measure(widthSpec, heightSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (getTabCount() > 0) {
			int step = getWidth() / getTabCount();
			mImgViewCursor.setTranslationX(currIndex * step);
		}		
	}

	/**
	 * Move the focus tab into special index.
	 * 
	 * @param index
	 */
	public void move(int index) {
		if (currIndex == index || getTabCount() == 0) {
			return;
		}

		if (mOnTabChangListener != null) {
			mOnTabChangListener.onChange(index);
		}

		int step = getWidth() / getTabCount();

		ObjectAnimator objAnima = ObjectAnimator.ofObject(mImgViewCursor, "translationX", new FloatEvaluator(),
				currIndex * step, index * step);
		objAnima.setInterpolator(new DecelerateInterpolator(1.0f));
		objAnima.setDuration(0);
		objAnima.start();

		currIndex = index;
		refreshState();
	}

	/**
	 * Get current activate tab index.
	 * 
	 * @return
	 */
	public int getCurrtentIndex() {
		return currIndex;
	}

	/**
	 * Get total tab children.
	 * 
	 * @return
	 */
	public int getTabCount() {
		return mTabViewList == null ? 0 : mTabViewList.size();
	}

	/**
	 * Set a callback listener when focus tab changed,this can be invoked.
	 * 
	 * @param listener
	 */
	public void setOnTabChangedListener(OnTabChangedListener listener) {
		mOnTabChangListener = listener;
	}

	public interface OnTabChangedListener {
		void onChange(int index);
	}

	/**
	 * A tab content source supplier.
	 * 
	 */
	public interface TabAdapter {

		/**
		 * Create all of tab views.
		 * 
		 * @return
		 */
		public List<View> getTabView();

		/**
		 * The tab view state changed.
		 * 
		 * @param view
		 * @param isSelect
		 */
		public void viewStateChange(View view, boolean isSelect);

	}

}
