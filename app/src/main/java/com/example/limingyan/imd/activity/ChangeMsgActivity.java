package com.example.limingyan.imd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.limingyan.imd.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeMsgActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.scan_code1)
    Button scanCode1;
    @BindView(R.id.code1)
    TextView code1;
    @BindView(R.id.scan_code2)
    Button scanCode2;
    @BindView(R.id.code2)
    TextView code2;
    @BindView(R.id.bound)
    Button bound;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_msg);
        ButterKnife.bind(this);
    }
    private void getCode(){
        IntentIntegrator integrator = new IntentIntegrator(ChangeMsgActivity.this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描ISBN"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (flag == 1){
            if (scanResult != null) {
                String result = scanResult.getContents();
                code1.setText(result);
            }
        }else {
            if (scanResult != null) {
                String result = scanResult.getContents();
                code2.setText(result);
            }
        }

    }

    @OnClick({R.id.scan_code1, R.id.scan_code2, R.id.bound})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_code1:
                getCode();
                flag = 1;
                break;
            case R.id.scan_code2:
                getCode();
                flag = 2;
                break;
            case R.id.bound:
                break;
        }
    }
}
