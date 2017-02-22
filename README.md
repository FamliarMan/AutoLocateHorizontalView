###AutoLocateHorizontalView使用方法
####一、特点
1. 使用方式基本和Recyclerview一样，熟悉Recyclerview的开发者非常易于使用本控件。
2. 支持数据的动态删除和添加，完全支持recyclerview的更新方法（局部更新方法可以使用，但最终是使用全局刷新实现的，所以局部刷新并不能在本控件中带来性能的提升）。
3. 滑动停止后可以自动选中最近的位置。

####二、使用方式

#####1. 基本使用
  和Recyclerview一模一样，唯一不同的是在继承RecyclerView.Adapter时要实现 AutoLocateHorizontalView.IAutoLocateHorizontalView 接口。如下所示：

```
	public class BarAdapter extends RecyclerView.Adapter  implements AutoLocateHorizontalView.IAutoLocateHorizontalView 
```
	
在该接口中我们要实现两个方法：
```
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
```
其中getItemView()方法是用来获取列表中的每一个item的布局，可以在onCreateViewHolder()方法中获取如：
```
@Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bar,parent,false);
        this.itemView = itemView;
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }
```
而 onViewSelected()方法中可以根据item是否被选中设置选中效果，比如选中时改变颜色，或者加粗等。
#####2. 常用方法
* setInitPos(int pos) 设置最开始时选中的位置。
* setItemCount(int itemCount) 设置每屏显示多少个item，item越多，则每个item的宽度相应的变小。
* setOnSelectedPositionChangedListener(OnSelectedPositionChangedListener listener) 设置位置改变时的监听器，当滑动或增加、删除数据时，只要选中的位置发生了改变就会回调。注意，当增加和删除时，有可能选中的位置不变，但这个时候选中的数据不再是原来的数据也会回调。

####三、参考效果
![年龄选择](http://7xpxx3.com1.z0.glb.clouddn.com/gif/blog/autolocatehorizonview_example1.gif) 
![柱状图](http://7xpxx3.com1.z0.glb.clouddn.com/gif/blog/autolocatehorizonview_example2.gif)
