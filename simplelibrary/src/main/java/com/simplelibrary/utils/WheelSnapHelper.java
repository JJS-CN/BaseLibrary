package com.simplelibrary.utils;

import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * 说明：用于RecyclerView滚动辅助类，实现类似wheelview的滚动效果
 * Created by jjs on 2018/11/23
 */

public class WheelSnapHelper extends LinearSnapHelper {
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private int itemHalf;
    private int orientation;
    private int mItemCount;
    private OnSnapListener mOnSnapListener;

    /**
     * @param orientation     设置滑动方向，linearlayoutmanager
     * @param WidthOrHeightDP itemView对应方向尺寸，单位dp；用于初始化
     */
    public WheelSnapHelper(@WheelSnapHelper.Orientation int orientation, int WidthOrHeightDP) {
        this.orientation = orientation;
        this.itemHalf = ConvertUtils.dp2px(WidthOrHeightDP) / 2;

    }

    /**
     * 设置滚动监听，重复position不会重复回调，初始会加载0;
     *
     * @param onSnapListener
     */
    public void setOnSnapListener(OnSnapListener onSnapListener) {
        mOnSnapListener = onSnapListener;
    }

    @Override
    public void attachToRecyclerView(@Nullable final RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                //这里处理，可用返回有效信息
                if (mItemCount == 0) {
                    mItemCount = state.getItemCount();
                }
                if (parent.getChildAdapterPosition(view) == 0) {
                    if (orientation == VERTICAL) {
                        outRect.top = parent.getHeight() / 2 - itemHalf;
                    } else {
                        outRect.left = parent.getWidth() / 2 - itemHalf;
                    }


                }
                if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                    if (orientation == VERTICAL) {
                        outRect.bottom = parent.getHeight() / 2 - itemHalf;
                    } else {
                        outRect.right = parent.getWidth() / 2 - itemHalf;
                    }
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    findPosition(recyclerView);
                }
            }
        });
        if (mOnSnapListener != null) {
            if (recyclerView.getAdapter() == null) {
                throw new NullPointerException("The RecyclerView of adapter is null!!");
            }
            mOnSnapListener.onSnaped(0);
        }
    }

    private void findPosition(RecyclerView recyclerView) {
        if (mOnSnapListener != null) {
            LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (linearManager == null) {
                throw new ClassCastException("The RecyclerView LayoutManager is null!!");
            }
            View view = findSnapView(linearManager);
            int position = linearManager.getPosition(view);
            if (position != lastPosition) {
                lastPosition = position;
                mOnSnapListener.onSnaped(lastPosition);
            }
        }
    }

    private int lastPosition = -1;

    public interface OnSnapListener {
        void onSnaped(int position);
    }
}
