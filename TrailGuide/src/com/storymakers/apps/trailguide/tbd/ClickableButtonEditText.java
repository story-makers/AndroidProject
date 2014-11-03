package com.storymakers.apps.trailguide.tbd;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


// http://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
public class ClickableButtonEditText extends EditText {
	public static final String LOG_TAG = "ClickableButtonEditText";

	private Drawable drawableRight;
	private Drawable drawableLeft;
	private Drawable drawableTop;
	private Drawable drawableBottom;
	private boolean consumeEvent = false;
	private int fuzz = 0;

	private DrawableClickListener clickListener;

	public ClickableButtonEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClickableButtonEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClickableButtonEditText(Context context) {
		super(context);
	}

	public void consumeEvent() {
		this.setConsumeEvent(true);
	}

	public void setConsumeEvent(boolean b) {
		this.consumeEvent = b;
	}

	public void setFuzz(int z) {
		this.fuzz = z;
	}

	public int getFuzz() {
		return fuzz;
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if (right != null) {
			drawableRight = right;
		}

		if (left != null) {
			drawableLeft = left;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x, y;
			Rect bounds;
			x = (int) event.getX();
			y = (int) event.getY();
			// this works for left since container shares 0,0 origin with bounds
			if (drawableLeft != null) {
				bounds = drawableLeft.getBounds();
				if (bounds.contains(x - fuzz, y - fuzz)) {
					clickListener.onClick(DrawableClickListener.DrawablePosition.LEFT);
					if (consumeEvent) {
						event.setAction(MotionEvent.ACTION_CANCEL);
						return false;
					}
				}
			} else if (drawableRight != null) {
				bounds = drawableRight.getBounds();
				if (x >= (this.getRight() - bounds.width() - fuzz) && x <= (this.getRight() - this.getPaddingRight() + fuzz) 
						&& y >= (this.getPaddingTop() - fuzz) && y <= (this.getHeight() - this.getPaddingBottom()) + fuzz) {

					clickListener.onClick(DrawableClickListener.DrawablePosition.RIGHT);
					if (consumeEvent) {
						event.setAction(MotionEvent.ACTION_CANCEL);
						return false;
					}
				}
			} else if (drawableTop != null) {
				// not impl reader exercise :)
			} else if (drawableBottom != null) {
				// not impl reader exercise :)
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		drawableRight = null;
		drawableBottom = null;
		drawableLeft = null;
		drawableTop = null;
		super.finalize();
	}

	public void setDrawableClickListener(DrawableClickListener listener) {
		this.clickListener = listener;
	}
}