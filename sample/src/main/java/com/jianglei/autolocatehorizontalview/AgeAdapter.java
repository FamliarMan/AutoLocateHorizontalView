package com.jianglei.autolocatehorizontalview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jianglei on 2/4/17.
 */

public class AgeAdapter extends RecyclerView.Adapter<AgeAdapter.AgeViewHolder> {
    private Context context;
    String[]ages = new String[]{"12","13","14","15","16","17","18","19","20","21","22","23","24"};
    public AgeAdapter(Context context){
        this.context = context;
    }

    @Override
    public AgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("jianglei","parednt:"+parent.getWidth());
        View view = LayoutInflater.from(context).inflate(R.layout.item_age,parent,false);
        return new AgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgeViewHolder holder, int position) {
        holder.tvAge.setText(ages[position]);
    }

    @Override
    public int getItemCount() {
        return  ages.length;
    }

    class AgeViewHolder extends RecyclerView.ViewHolder{
        public TextView tvAge;
        public AgeViewHolder(View itemView) {
            super(itemView);
            tvAge = (TextView)itemView.findViewById(R.id.tv_age);
        }
    }
}
