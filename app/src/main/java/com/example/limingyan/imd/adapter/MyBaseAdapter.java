package com.example.limingyan.imd.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MyBaseAdapter extends BaseAdapter {
    private List list;
    private OnList onList;

    public MyBaseAdapter(List list) {
        this.list = list;
    }

    public interface OnList{
        View getList(int position,View convertView);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return onList.getList(position,convertView);
    }
    public void setAdapter(OnList onList){
        this.onList=onList;
    }
}
