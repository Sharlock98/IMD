package com.example.limingyan.imd.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.activity.NewsActivity;
import com.example.limingyan.imd.adapter.MyBaseAdapter;
import com.example.limingyan.imd.entity.t_Info;
import com.example.limingyan.imd.util.HttpRequest;
import com.itheima.loopviewpager.LoopViewPager;
import com.itheima.loopviewpager.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    //    @BindView(R.id.page1)
//    ImageView page1;
//    @BindView(R.id.page2)
//    ImageView page2;
//    @BindView(R.id.page3)
//    ImageView page3;
    @BindView(R.id.MsgList)
    ListView msgList;
    Unbinder unbinder;
    @BindView(R.id.viewPager)
    LoopViewPager viewPager;
    private ImageView[] pages;
    private Timer timer;
    int flag;
    String[] imgAddress = {"http://www.linkshop.com.cn/upload/article/2018/20180528090817_8750.jpg", "http://img2.utuku.china.com/649x0/news/20180423/30214979-b40e-4512-9b85-280a47a6ed15.jpg"
            , "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2440470337,382073285&fm=27&gp=0.jpg"};
    String[] imgTitle = {"零售店为什么打造以顾客价值为中心的新营销模式？", "体验“无人值守智慧零售超市”", "超市经营管理服务明显改善 百姓消费满意度提升"};
    private BmobQuery<t_Info> bmobQuery;
    private List<t_Info> t_infoList;
    private MyBaseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        title.setText("资讯");
        Bmob.initialize(getActivity(), "3cb928f0c2d0133c575f7564c0d786e0");
        return view;
    }

    private List<String> imgListString() {
        List<String> imageData = new ArrayList<>();
        imageData.add(imgAddress[0]);
        imageData.add(imgAddress[1]);
        imageData.add(imgAddress[2]);
        return imageData;
    }

    private List<String> titleListString() {
        List<String> titleData = new ArrayList<>();
        titleData.add(imgTitle[0]);
        titleData.add(imgTitle[1]);
        titleData.add(imgTitle[2]);
        return titleData;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setImgData(imgListString());
        viewPager.setTitleData(titleListString());
        viewPager.start();
        viewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                switch (i) {
                    case 0:
                        intent.putExtra("title", imgTitle[0]);
                        intent.putExtra("address", imgAddress[0]);
                        break;
                    case 1:
                        intent.putExtra("title", imgTitle[1]);
                        intent.putExtra("address", imgAddress[1]);
                        break;
                    case 2:
                        intent.putExtra("title", imgTitle[2]);
                        intent.putExtra("address", imgAddress[2]);
                        break;
                }
                startActivity(intent);
            }
        });
        t_infoList = new ArrayList<>();
        bmobQuery = new BmobQuery<>();
        setAdapter();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<t_Info>() {
            @Override
            public void done(List<t_Info> list, BmobException e) {
                getNews(list, flag);
            }
        });
        msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=t_infoList.get(position).getTitle();
                String imgAddress=t_infoList.get(position).getImages();
                Intent intent=new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("address", imgAddress);
                startActivity(intent);
            }
        });
    }

    private void getNews(final List<t_Info> list, int i) {
        if (flag != list.size()){
            bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(list.get(i).getObjectId(), new QueryListener<t_Info>() {
                @Override
                public void done(t_Info t_info, BmobException e) {
                    t_infoList.add(t_info);
                    adapter.notifyDataSetChanged();
                    flag ++;
                    getNews(list,flag);
                }
            });

        }else {
            flag = 0;
        }
    }

    private void setAdapter() {
        adapter = new MyBaseAdapter(t_infoList);
        msgList.setAdapter(adapter);
        adapter.setAdapter(new MyBaseAdapter.OnList() {
            class ViewHolder {
                View rootView;
                ImageView newsImg;
                TextView newsTitle;
                TextView origin;

                ViewHolder(View rootView) {
                    this.rootView = rootView;
                    this.newsImg = (ImageView) rootView.findViewById(R.id.newsImg);
                    this.newsTitle = (TextView) rootView.findViewById(R.id.newsTitle);
                    this.origin = (TextView) rootView.findViewById(R.id.origin);
                }

            }

            @Override
            public View getList(final int position, View convertView) {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                HttpRequest.getBitmap(t_infoList.get(position).getImages(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.newsImg.setImageBitmap(bitmap);
                        holder.newsTitle.setText(t_infoList.get(position).getTitle());
                        holder.origin.setText(t_infoList.get(position).getOrigin());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                return convertView;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
