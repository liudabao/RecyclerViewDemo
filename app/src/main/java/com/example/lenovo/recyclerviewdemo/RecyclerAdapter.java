package com.example.lenovo.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by lenovo on 2016/6/1.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHodler> {

    Context context;
    List<Content> list;

    public RecyclerAdapter(Context context, List<Content> list){

        this.context=context;
        this.list=list;
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.view_item, parent, false);
        MyHodler hodler=new MyHodler(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyHodler holder, int position) {

        Content content=list.get(position);
        //holder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        Glide.with(context).load(content.getUrl()).placeholder(R.mipmap.ic_launcher).centerCrop().crossFade().into(holder.imageView);
        holder.textView.setText(content.getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(int position, Content content){
        list.add(position, content);
        notifyItemInserted(position);
    }

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }


    class MyHodler extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public MyHodler(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view){
            imageView=(ImageView) view.findViewById(R.id.imageView);
            textView=(TextView)view.findViewById(R.id.textView);
        }

    }

}
