package com.example.limingyan.imd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.entity.t_admin;
import com.example.limingyan.imd.util.HttpRequest;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView head;
    private EditText edt_workId;
    private EditText edt_password;
    private Button login;
    private String workId;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Bmob.initialize(LoginActivity.this, "3cb928f0c2d0133c575f7564c0d786e0");

    }

    @Override
    protected void onResume() {
        super.onResume();
        edt_workId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String workId = edt_workId.getText().toString().trim();
                if (!TextUtils.isEmpty(workId)){
                    BmobQuery<t_admin> query = new BmobQuery<>();
                    query.addWhereEqualTo("jobNumber", Integer.parseInt(workId));
                    query.findObjects(new FindListener<t_admin>() {
                        @Override
                        public void done(List<t_admin> list, BmobException e) {
                            if (list.size() != 0){
                                HttpRequest.getBitmap(list.get(0).getAvatar(), new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        head.setImageBitmap(bitmap);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        head = (CircleImageView) findViewById(R.id.head);
        edt_workId = (EditText) findViewById(R.id.edt_workId);
        edt_password = (EditText) findViewById(R.id.edt_password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (submit()) {
                    BmobQuery<t_admin> query = new BmobQuery<>();
                    query.addWhereEqualTo("jobNumber", Integer.parseInt(workId));
                    query.findObjects(new FindListener<t_admin>() {
                        @Override
                        public void done(List<t_admin> list, BmobException e) {
                            if (list.size() != 0) {
                                if (password.equals(list.get(0).getPassword())) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                    EventBus.getDefault().postSticky(list);
                                }else {
                                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "工号不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    private boolean submit() {
        // validate
        workId = edt_workId.getText().toString().trim();
        if (TextUtils.isEmpty(workId)) {
            Toast.makeText(this, "工号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        password = edt_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
        // TODO validate success, do something
    }
}
