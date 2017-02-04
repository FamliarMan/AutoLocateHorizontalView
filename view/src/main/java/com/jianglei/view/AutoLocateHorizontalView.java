package com.jianglei.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jianglei on 2/1/17.
 */

public class AutoLocateHorizontalView extends RecyclerView {
    public AutoLocateHorizontalView(Context context) {
        super(context);
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {

    }

    @Override
    public void setAdapter(Adapter adapter) {
        Log.d("jianglei","width:"+getMeasuredWidth());
        super.setAdapter(adapter);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    static class WrapperAdapter extends RecyclerView.Adapter {
        private Context context;
        private RecyclerView.Adapter adapter;
        private int itemWidth;
        private int totalWidth;
        private static final int HEADER_FOOTER_TYPE = -1;

        public WrapperAdapter(Adapter adapter,Context context,int itemWidth,int totalWidth) {
            if(!(adapter instanceof IAutoLocateHorizontalView)){
                throw new IllegalStateException("The adapter should implements IAutoLocateHorizontalView !");
            }
            this.adapter = adapter;
            this.context = context;
            this.itemWidth = itemWidth;
            this.totalWidth = totalWidth;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == HEADER_FOOTER_TYPE){
                View view = new View(context);
                RecyclerView.LayoutParams params = new LayoutParams(totalWidth/2-itemWidth,1);
                view.setLayoutParams(params);
                return new HeaderFooterViewHolder(view);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(isHeaderOrFooter(position)) {
                adapter.onBindViewHolder(holder, position);
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
            return adapter.getItemViewType(position-1);
        }

        private boolean isHeaderOrFooter(int pos){
            if(pos == 0 || pos == getItemCount()-1){
                return true;
            }
            return false;
        }

        class HeaderFooterViewHolder extends RecyclerView.ViewHolder{
            public View headOrFooter;
            public HeaderFooterViewHolder(View itemView) {
                super(itemView);
                headOrFooter = itemView;
            }
        }

    }

    public interface IAutoLocateHorizontalView{
        int getItemWidth();
    }
}
