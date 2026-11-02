package org.nyanya.android.traditionalt9;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CandidateView extends View {

	private List<String> mSuggestions;
	protected int mSelectedIndex;

	private Drawable mSelectionHighlight;

	private Rect mBgPadding;

	private static final int MAX_SUGGESTIONS = 32;
	private static final int SCROLL_PIXELS = 20;

	private int[] mWordWidth = new int[MAX_SUGGESTIONS];
	private int[] mWordX = new int[MAX_SUGGESTIONS];

	private static final int X_GAP = 10;

	private static final List<String> EMPTY_LIST = new ArrayList<String>();

	private int mColorNormal;
	private int mColorRecommended;
	private int mColorOther;
	private int mColorAdd;
	private int mColorBackground;
	private int mColorSelectedText;
	private int mColorHighlight;
	private boolean mCustomHighlight;
	private float mBaseTextSize;
	private boolean mResetScroll;
	private int mVerticalPadding;
	private Paint mPaint;
	private int mTargetScrollX;
	private String mAddWordLabel;
	private int mTotalWidth;
	private boolean addWordOption;
	Rect mPadding;

	/**
	 * Construct a CandidateView for showing suggested words for completion.
	 *
	 * @param context
	 */
	public CandidateView(Context context) {
		super(context);
		mSelectionHighlight = context.getResources().getDrawable(
			android.R.drawable.list_selector_background);
		mSelectionHighlight.setState(new int[] {
			android.R.attr.state_enabled, android.R.attr.state_focused,
			android.R.attr.state_window_focused, android.R.attr.state_pressed });

		Resources r = context.getResources();

		mAddWordLabel=r.getString(R.string.candidate_view_add_word);

		setBackgroundColor(r.getColor(R.color.candidate_background));

		mColorNormal = r.getColor(R.color.candidate_normal);
		mColorRecommended = r.getColor(R.color.candidate_recommended);
		mColorOther = r.getColor(R.color.candidate_other);
		mColorAdd=r.getColor(R.color.candidate_background_add);
		mColorBackground = r.getColor(R.color.candidate_background);
		mColorSelectedText = mColorRecommended;
		mCustomHighlight = false;

		mVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);

		mPaint = new Paint();
		mPaint.setColor(mColorNormal);
		mPaint.setAntiAlias(true);
		mBaseTextSize = r.getDimensionPixelSize(R.dimen.candidate_font_height);
		mPaint.setTextSize(mBaseTextSize);
		mPaint.setStrokeWidth(0);

		mPadding = new Rect();

		applyTheme(context);

		setHorizontalFadingEdgeEnabled(true);
		setWillNotDraw(false);
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
	}

	/**
	 * Applies the colour theme chosen in the settings (see CandidateTheme).
	 */
	public void applyTheme(Context context) {
		Resources r = context.getResources();
		switch (CandidateTheme.get(context)) {
			case CandidateTheme.GRAY_BLUE:
				mColorBackground = Color.parseColor("#5A595A");
				mColorNormal = Color.parseColor("#FFFFFF");
				mColorOther = Color.parseColor("#CECBCE");
				mColorSelectedText = Color.parseColor("#000000");
				mColorHighlight = Color.parseColor("#00BAD6");
				mColorAdd = Color.parseColor("#4FD8EA");
				mCustomHighlight = true;
				break;
			case CandidateTheme.GOLD_BLACK:
				mColorBackground = Color.parseColor("#181C21");
				mColorNormal = Color.parseColor("#FFFFFF");
				mColorOther = Color.parseColor("#C6C7C6");
				mColorSelectedText = Color.parseColor("#000000");
				mColorHighlight = Color.parseColor("#FFD700");
				mColorAdd = Color.parseColor("#FFD700");
				mCustomHighlight = true;
				break;
			default:
				mColorBackground = r.getColor(R.color.candidate_background);
				mColorNormal = r.getColor(R.color.candidate_normal);
				mColorOther = r.getColor(R.color.candidate_other);
				mColorSelectedText = r.getColor(R.color.candidate_recommended);
				mColorAdd = r.getColor(R.color.candidate_background_add);
				mCustomHighlight = false;
				break;
		}
		setBackgroundColor(mColorBackground);
		mBgPadding = null;
		float newSize = mBaseTextSize * CandidateTheme.getFontScale(context);
		if (newSize != mPaint.getTextSize()) {
			mPaint.setTextSize(newSize);
			requestLayout();
		}
		invalidate();
	}

	@Override
	public int computeHorizontalScrollRange() {
		return mTotalWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = resolveSize(50, widthMeasureSpec);

		// Get the desired height of the icon menu view (last row of items does
		// not have a divider below)

		mSelectionHighlight.getPadding(mPadding);
		final int desiredHeight = ((int) mPaint.getTextSize()) + mVerticalPadding + mPadding.top
			+ mPadding.bottom;

		// Maximum possible width and desired height
		setMeasuredDimension(measuredWidth, resolveSize(desiredHeight, heightMeasureSpec));
	}

	/**
	 * If the canvas is null, then only touch calculations are performed to pick
	 * the target candidate.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mTotalWidth = 0;
		if (mSuggestions == null)
			return;

		if (mBgPadding == null) {
			mBgPadding = new Rect(0, 0, 0, 0);
			if (getBackground() != null) {
				getBackground().getPadding(mBgPadding);
			}
		}
		final int count = mSuggestions.size();
		final int height = getHeight();
		final Rect bgPadding = mBgPadding;
		final Paint paint = mPaint;
		final int y = (int) (((height - mPaint.getTextSize()) / 2) - mPaint.ascent());

		// first pass: measure (logical order, first suggestion = x 0)
		int x = 0;
		for (int i = 0; i < count; i++) {
			final int wordWidth = (int) paint.measureText(mSuggestions.get(i)) + X_GAP * 2;
			mWordX[i] = x;
			mWordWidth[i] = wordWidth;
			x += wordWidth;
		}
		mTotalWidth = x;
		// right-to-left layout: the first suggestion is pinned to the right edge
		// and the following ones go to the left
		final int contentWidth = Math.max(mTotalWidth, getWidth());
		if (mResetScroll) {
			mResetScroll = false;
			mTargetScrollX = scrollTargetFor(mSelectedIndex < 0 ? 0 : mSelectedIndex, contentWidth);
			scrollTo(mTargetScrollX, getScrollY());
		}

		for (int i = 0; i < count; i++) {
			String suggestion = mSuggestions.get(i);
			final int wordWidth = mWordWidth[i];
			final int left = contentWidth - mWordX[i] - wordWidth;
			paint.setColor(mColorNormal);
			if (i == mSelectedIndex) {
				if (mCustomHighlight) {
					paint.setColor(mColorHighlight);
					canvas.drawRect(left, bgPadding.top, left + wordWidth, height, paint);
				} else {
					canvas.translate(left, 0);
					mSelectionHighlight.setBounds(0, bgPadding.top, wordWidth, height);
					mSelectionHighlight.draw(canvas);
					canvas.translate(-left, 0);
				}
				paint.setFakeBoldText(true);
				paint.setColor(mColorSelectedText);
			} else {
				paint.setColor(mCustomHighlight ? mColorNormal : mColorOther);
			}
			if (i==count-1 && !(mCustomHighlight && i == mSelectedIndex))
				paint.setColor(mColorAdd);

			canvas.drawText(suggestion, left + X_GAP, y, paint);
			paint.setColor(mColorOther);
			canvas.drawLine(left + 0.5f, bgPadding.top, left + 0.5f, height + 1, paint);
			paint.setFakeBoldText(false);
		}

		if (mTargetScrollX != getScrollX()) {
			scrollToTarget();
		}
	}

	private void scrollToTarget() {
		int sx = getScrollX();
		if (mTargetScrollX > sx) {
			sx += SCROLL_PIXELS;
			if (sx >= mTargetScrollX) {
				sx = mTargetScrollX;
				requestLayout();
			}
		} else {
			sx -= SCROLL_PIXELS;
			if (sx <= mTargetScrollX) {
				sx = mTargetScrollX;
				requestLayout();
			}
		}
		scrollTo(sx, getScrollY());
		invalidate();
	}

	/** Scroll position that centres the given word in the right-to-left layout. */
	private int scrollTargetFor(int index, int contentWidth) {
		final int fullsize = getWidth();
		final int left = contentWidth - mWordX[index] - mWordWidth[index];
		int target = left + (mWordWidth[index] / 2) - fullsize / 2;
		final int max = Math.max(0, contentWidth - fullsize);
		if (target < 0) {
			target = 0;
		} else if (target > max) {
			target = max;
		}
		return target;
	}

	protected void setSuggestions(List<String> suggestions, int initialSel) {
		clear();
		if (suggestions != null) {
			mSuggestions = suggestions;
			if (addWordOption)
				mSuggestions.add(mAddWordLabel);
			mSelectedIndex = initialSel;
		}
		scrollTo(0, 0);
		mTargetScrollX = 0;
		mResetScroll = true;
		// Compute the total width
		// onDraw(null);
		invalidate();
		requestLayout();
	}

	protected void clear() {
		mSuggestions = EMPTY_LIST;
		mSelectedIndex = -1;
		invalidate();
	}

	protected void scrollSuggestion(int increment) {
		if (mSuggestions != null && mSuggestions.size() > 1) {
			mSelectedIndex = mSelectedIndex + increment;
			if (mSelectedIndex == mSuggestions.size()) {
				mSelectedIndex = 0;
			} else if (mSelectedIndex < 0) {
				mSelectedIndex = mSuggestions.size() - 1;
			}

			mTargetScrollX = scrollTargetFor(mSelectedIndex, Math.max(mTotalWidth, getWidth()));

			invalidate();
		}
	}

	public void setAddWordOption(boolean addWordOption) {
		this.addWordOption = addWordOption;
	}

}
