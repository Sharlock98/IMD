package com.example.limingyan.imd.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.entity.t_Info;
import com.example.limingyan.imd.util.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.news)
    TextView news;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.readTime)
    TextView readTime;
    @BindView(R.id.content)
    TextView content;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("m分s秒");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        title.setText("正文");
        Bmob.initialize(NewsActivity.this, "3cb928f0c2d0133c575f7564c0d786e0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        news.setText(getIntent().getStringExtra("title"));
        setImg();
        BmobQuery<t_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("title", getIntent().getStringExtra("title"));
        query.findObjects(new FindListener<t_Info>() {
            @Override
            public void done(List<t_Info> list, BmobException e) {
                if (e == null){
                    readTime.setText("阅读数："+list.get(0).getReadTime());
                    content.setText(list.get(0).getContent());
                }else {
                    Log.e("***", String.valueOf(e));
                }
            }
        });

    }

    private void setImg() {
        HttpRequest.getBitmap(getIntent().getStringExtra("address"), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
