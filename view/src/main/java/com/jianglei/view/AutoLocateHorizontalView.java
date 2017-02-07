package com.jianglei.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jianglei on 2/1/17.
 */

public class AutoLocateHorizontalView extends RecyclerView {
    /**
     * 一个屏幕中显示多少个item，必须为奇数
     */
    private int itemCount;
    /**
     * 初始时选中的位置
     */
    private int initPos=2;

    private boolean isAllowedAutoScroll;
    private int deltaX;
    private WrapperAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
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
    }


    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = new WrapperAdapter(adapter,getContext(),8);
        if(linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        }
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.scrollToPositionWithOffset(0,-initPos*(this.adapter.getItemWidth()));
        Log.d("longyi","first:"+(this.adapter.getItemWidth()));
        super.setLayoutManager(linearLayoutManager);
        super.setAdapter(this.adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if(!(layout instanceof LinearLayoutManager)){
            throw new IllegalStateException("Here must be LinearLayoutManager!");
        }
        this.linearLayoutManager = (LinearLayoutManager) layout;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
/*
        if(state == SCROLL_STATE_IDLE){
            Log.d("jianglei","scroll:"+deltaX+" itemWidth:"+adapter.getItemWidth());
            if(adapter == null){
                return;
            }
            int itemWidth = adapter.getItemWidth();
            int headerFooterWidth = adapter.getHeaderFooterWidth();
            if(itemWidth == 0 || headerFooterWidth == 0){
                //此时adapter还没有准备好，忽略此次调用
                return;
            }
            int lastPos = (deltaX-initPos*itemWidth)/itemWidth;
            //超出上个item的位置
            int overLastPosOffset = deltaX - (lastPos*itemWidth);
            Log.d("jianglei","lastPos:"+lastPos+"  overLastPosOffset:"+overLastPosOffset);
            if(overLastPosOffset == 0){
                //刚好处于一个item选中位置，无需滑动偏移纠正
            }else if(overLastPosOffset <= itemWidth/2){
                //所处位置超过了上一个item的选中位置，但更靠近上一个item，向左纠正
                scrollBy(-overLastPosOffset,0);
            }else{
                scrollBy((itemWidth-overLastPosOffset),0);
            }
        }

*/
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

        public int getItemWidth(){
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
