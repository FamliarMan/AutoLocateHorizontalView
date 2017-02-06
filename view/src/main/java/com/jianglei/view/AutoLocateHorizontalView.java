package com.jianglei.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by jianglei on 2/1/17.
 */

public class AutoLocateHorizontalView extends RecyclerView {
    private WrapperAdapter wrapAdapter;
    private RecyclerView.Adapter realAdapter;
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
                Log.d("jianglei","back :"+getMeasuredWidth());
            }
        });
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(new WrapperAdapter(adapter,getContext(),8));
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    static class WrapperAdapter extends RecyclerView.Adapter {
        private Context context;
        private RecyclerView.Adapter adapter;
        private int itemCount;
        private static final int HEADER_FOOTER_TYPE = -1;
        private View itemView;

        public WrapperAdapter(Adapter adapter,Context context,int itemCount) {
            this.adapter = adapter;
            this.context = context;
            this.itemCount = itemCount;
            if(adapter instanceof IAutoLocateHorizontalView){
                itemView = ((IAutoLocateHorizontalView) adapter).getItemView();
            }else{
                throw new RuntimeException(adapter.getClass().getSimpleName() +" should implements com.jianglei.view.AutoLocateHorizontalView.IAutoLocateHorizontalView !");
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == HEADER_FOOTER_TYPE){
                View view = new View(context);

                RecyclerView.LayoutParams params = new LayoutParams(parent.getMeasuredWidth()/2-(parent.getMeasuredWidth()/itemCount)/2, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setBackgroundColor(Color.BLACK);
                view.setLayoutParams(params);
                return new HeaderFooterViewHolder(view);
            }

            ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
            Log.d("jianglei","fuck");
            itemView = ((IAutoLocateHorizontalView)adapter).getItemView();
            int width = parent.getMeasuredWidth()/itemCount;
            ViewGroup.LayoutParams params =  itemView.getLayoutParams();
            params.width = width;
            itemView.setLayoutParams(params);
            return  holder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(!isHeaderOrFooter(position)) {
                adapter.onBindViewHolder(holder, position-1);
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

    /**
     * 获取item的根布局
     */
    public interface IAutoLocateHorizontalView{

        View getItemView();
    }
}
