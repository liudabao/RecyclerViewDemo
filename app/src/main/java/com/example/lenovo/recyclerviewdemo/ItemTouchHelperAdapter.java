package com.example.lenovo.recyclerviewdemo;

/**
 * Created by lenovo on 2016/6/20.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
