package com.example.limingyan.imd.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.entity.t_goods;
import com.example.limingyan.imd.util.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.commodity)
    ImageView commodity;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.product_area)
    TextView productArea;
    @BindView(R.id.specification)
    TextView specification;
    @BindView(R.id.currentPrice)
    TextView currentPrice;
    @BindView(R.id.type)
    TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        title.setText("商品信息");
        Bmob.initialize(DetailsActivity.this, "3cb928f0c2d0133c575f7564c0d786e0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name=getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)){
            BmobQuery<t_goods> query = new BmobQuery<>();
            query.addWhereEqualTo("goodsName", name);
            query.findObjects(new FindListener<t_goods>() {
                @Override
                public void done(final List<t_goods> list, BmobException e) {
                    if (e == null) {
                        HttpRequest.getBitmap(list.get(0).getImages(), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                setInformation(bitmap, list);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                    } else {
                        Log.e("***", String.valueOf(e));
                    }
                }
            });
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void getValue(String value) {
        Log.e("*****", value);
        BmobQuery<t_goods> query = new BmobQuery<>();
        query.addWhereEqualTo("barcode", value);
        query.findObjects(new FindListener<t_goods>() {
            @Override
            public void done(final List<t_goods> list, BmobException e) {
                if (e == null) {
                    HttpRequest.getBitmap(list.get(0).getImages(), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            setInformation(bitmap, list);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                } else {
                    Log.e("***", String.valueOf(e));
                }
            }
        });
    }

    private void setInformation(Bitmap bitmap, List<t_goods> list) {
        commodity.setImageBitmap(bitmap);
        name.setText(list.get(0).getGoodsName());
        productArea.setText(list.get(0).getOrigin());
        specification.setText("规格："+list.get(0).getSize());
        currentPrice.setText("售价："+list.get(0).getCurrentPrice()+"元");
        type.setText("类型："+list.get(0).getType());
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}
