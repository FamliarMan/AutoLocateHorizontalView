package com.jianglei.autolocatehorizontalview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianglei.view.AutoLocateHorizontalView;

import java.util.List;

/**
 * Created by 龙衣 on 17-2-20.
 */

public class BarAdapter extends RecyclerView.Adapter implements AutoLocateHorizontalView.IAutoLocateHorizontalView {
    List<Integer> ages;
    private Context context;
    private int maxValue = -1;
    private View itemView;
    private int maxHeight = 200;
    public BarAdapter(Context context,List<Integer>ages){
        this.ages = ages;
        this.context = context;
        for(Integer age:ages){
            if(age > maxValue){
                maxValue = age;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bar,parent,false);
        this.itemView = itemView;
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).tvAge.setText(String.valueOf(ages.get(position)));
    }

    @Override
    public int getItemCount() {
        return ages.size();
    }

    @Override
    public View getItemView() {
        return itemView;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder,int itemWidth) {
        ViewHolder holder1 = (ViewHolder) holder;
        ViewGroup.LayoutParams params = holder1.bar.getLayoutParams();
        params.height = (int) (ages.get(pos)*1f / maxValue * maxHeight);
        params.width = itemWidth/3;
        holder1.bar.setLayoutParams(params);
        if(isSelected){
            holder1.bar.setBackgroundColor(Color.RED);
        }else{
            holder1.bar.setBackgroundColor(Color.BLUE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public View bar;
        public TextView tvAge;

        public ViewHolder(View itemView) {
            super(itemView);
            bar = (View)itemView.findViewById(R.id.view_bar);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
        }
    }
}
