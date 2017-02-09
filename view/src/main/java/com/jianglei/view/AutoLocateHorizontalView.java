package com.jianglei.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by jianglei on 2/1/17.
 */

public class AutoLocateHorizontalView extends RecyclerView {
    /**
     * 一个屏幕中显示多少个item，必须为奇数
     */
    private int itemCount = 8;
    /**
     * 初始时选中的位置
     */
    private int initPos = 0;

    private int deltaX;
    private WrapperAdapter wrapAdapter;
    private Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isInit;
    /**
     * 当前被选中的位置
     */
    private int selectPos = initPos;

    public AutoLocateHorizontalView(Context context) {
        super(context);
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isInit) {
                    if(initPos >= adapter.getItemCount()){
                        initPos = adapter.getItemCount()-1;
                    }
                    linearLayoutManager.scrollToPositionWithOffset(0, -initPos * (wrapAdapter.getItemWidth()));
                    isInit = false;
                }
            }
        });
    }

    /**
     * 设置初始化时选中的位置
     * @param initPos 初始位置，如果位置超过了item的数量则默认选中最后一项item
     */
    private void setInitPos(int initPos){
        this.initPos = initPos;
    }

    /**
     * 设置每次显示多少个item
     * @param itemCount 必须为奇数，否则默认会设置成小于它的最大奇数
     */
    private void setItemCount(int itemCount){
        if(itemCount % 2 ==0){
           this.itemCount = itemCount -1;
        }
        this.itemCount = itemCount;
    }

    /**
     * 删除item后偏移距离可能需要重新计算，从而保证selectPos的正确
     * @param adapter
     */
    private void correctDeltax(Adapter adapter) {
        if (adapter.getItemCount() <= selectPos) {
            deltaX -= wrapAdapter.getItemWidth() * (selectPos - adapter.getItemCount() + 1);
        }
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        this.adapter = adapter;
        this.wrapAdapter = new WrapperAdapter(adapter, getContext(), itemCount);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                correctDeltax(adapter);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                wrapAdapter.notifyItemRangeInserted(positionStart + 1, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                correctDeltax(adapter);
                wrapAdapter.notifyItemRangeRemoved(positionStart + 1, itemCount);
            }
        });
        deltaX = 0;
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        }
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        super.setLayoutManager(linearLayoutManager);
        super.setAdapter(this.wrapAdapter);
        isInit = true;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof LinearLayoutManager)) {
            throw new IllegalStateException("Here must be LinearLayoutManager!");
        }
        this.linearLayoutManager = (LinearLayoutManager) layout;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == SCROLL_STATE_IDLE) {
            if (wrapAdapter == null) {
                return;
            }
            int itemWidth = wrapAdapter.getItemWidth();
            int headerFooterWidth = wrapAdapter.getHeaderFooterWidth();
            if (itemWidth == 0 || headerFooterWidth == 0) {
                //此时adapter还没有准备好，忽略此次调用
                return;
            }
            //超出上个item的位置
            int overLastPosOffset = deltaX % itemWidth;
            if (overLastPosOffset == 0) {
                //刚好处于一个item选中位置，无需滑动偏移纠正
            } else if (Math.abs(overLastPosOffset) <= itemWidth / 2) {
                scrollBy(-overLastPosOffset, 0);
            } else if (overLastPosOffset > 0) {
                scrollBy((itemWidth - overLastPosOffset), 0);
            } else {
                scrollBy(-(itemWidth + overLastPosOffset), 0);
            }
            if (deltaX > 0) {
                selectPos = (deltaX) / itemWidth + initPos;
            } else {
                selectPos = initPos + (deltaX) / itemWidth;
            }
            Log.d("jianglei", "deltax:" + deltaX + " curPos:" + selectPos + "  overLastPosOffset:" + overLastPosOffset + "  itemWidth:" + itemWidth);
        }


    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltaX += dx;
    }

    static class WrapperAdapter extends RecyclerView.Adapter {
        private Context context;
        private RecyclerView.Adapter adapter;
        private int itemCount;
        private static final int HEADER_FOOTER_TYPE = -1;
        private View itemView;
        /**
         * 头部或尾部的宽度
         */
        private int headerFooterWidth;

        /**
         * 每个item的宽度
         */
        private int itemWidth;

        public WrapperAdapter(Adapter adapter, Context context, int itemCount) {
            this.adapter = adapter;
            this.context = context;
            this.itemCount = itemCount;
            if (adapter instanceof IAutoLocateHorizontalView) {
                itemView = ((IAutoLocateHorizontalView) adapter).getItemView();
            } else {
                throw new RuntimeException(adapter.getClass().getSimpleName() + " should implements com.jianglei.view.AutoLocateHorizontalView.IAutoLocateHorizontalView !");
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER_FOOTER_TYPE) {
                View view = new View(context);
                headerFooterWidth = parent.getMeasuredWidth() / 2 - (parent.getMeasuredWidth() / itemCount) / 2;
                RecyclerView.LayoutParams params = new LayoutParams(headerFooterWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                return new HeaderFooterViewHolder(view);
            }

            ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
            itemView = ((IAutoLocateHorizontalView) adapter).getItemView();
            int width = parent.getMeasuredWidth() / itemCount;
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params != null) {
                params.width = width;
                itemWidth = width;
                itemView.setLayoutParams(params);
            }
            return holder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!isHeaderOrFooter(position)) {
                adapter.onBindViewHolder(holder, position - 1);
            }
        }


        @Override
        public int getItemCount() {
            return adapter.getItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == getItemCount() - 1) {
                return HEADER_FOOTER_TYPE;
            }
            return adapter.getItemViewType(position - 1);
        }

        private boolean isHeaderOrFooter(int pos) {
            if (pos == 0 || pos == getItemCount() - 1) {
                return true;
            }
            return false;
        }

        public int getHeaderFooterWidth() {
            return headerFooterWidth;
        }

        public int getItemWidth() {
            return itemWidth;
        }

        class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
            public View headOrFooter;

            public HeaderFooterViewHolder(View itemView) {
                super(itemView);
                headOrFooter = itemView;
            }
        }

    }


    public interface IAutoLocateHorizontalView {
        /**
         * 获取item的根布局
         */
        View getItemView();
    }
}
