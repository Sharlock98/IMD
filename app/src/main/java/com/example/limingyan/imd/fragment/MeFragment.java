package com.example.limingyan.imd.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.activity.BlueToothActivity;
import com.example.limingyan.imd.activity.ChangeMsgActivity;
import com.example.limingyan.imd.activity.DetailsActivity;
import com.example.limingyan.imd.activity.MapActivity;
import com.example.limingyan.imd.activity.ScanActivity;
import com.example.limingyan.imd.entity.t_admin;
import com.example.limingyan.imd.util.HttpRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.tag_position)
    TextView tagPosition;
    @BindView(R.id.info_change)
    TextView infoChange;
    @BindView(R.id.recognition_code)
    TextView recognitionCode;
    Unbinder unbinder;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        title.setText("个人中心");
        EventBus.getDefault().register(this);
        return view;
    }
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void setInfo(final List<t_admin> list){
        HttpRequest.getBitmap(list.get(0).getAvatar(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                head.setImageBitmap(bitmap);
                username.setText(list.get(0).getIdentity()+"："+list.get(0).getAdminName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
    private void getCode(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描ISBN"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
    @OnClick({R.id.tag_position, R.id.info_change, R.id.recognition_code,R.id.blueTooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tag_position:
                getCode();
                break;
            case R.id.info_change:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.recognition_code:
                startActivity(new Intent(getActivity(), ChangeMsgActivity.class));
                break;
            case R.id.blueTooth:
                startActivity(new Intent(getActivity(), BlueToothActivity.class));
                break;
        }
    }
}
