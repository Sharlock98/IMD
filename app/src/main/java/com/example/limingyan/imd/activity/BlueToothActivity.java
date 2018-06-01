package com.example.limingyan.imd.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limingyan.imd.R;
import com.example.limingyan.imd.util.ChangeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueToothActivity extends Activity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button btn_send;
    private EditText et_send;
    private InputStream is;
    private TextView tv_show;
    private OutputStream os;
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_show.append("blueToothService:" + msg.arg1 + "\n");
        }
    };
    private TextView title;

    //private static String address="98:D3:32:31:74:02";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        initView();
        title.setText("发送");
        adapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences sharedPreferences = getSharedPreferences("address", 0);
        String address = sharedPreferences.getString("address", " 98:D3:32:31:74:02 ");
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(BlueToothActivity.this, "蓝牙地址为空", Toast.LENGTH_LONG).show();
        }
        BluetoothDevice device = adapter.getRemoteDevice(address);
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        findId();
        sendOS();
        getIS();
    }

    private void getIS() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (is != null) {
                        int data;
                        is = bluetoothSocket.getInputStream();
                        data = is.read();
                        tv_show.append("BlueTooth:" + data);
                        data = is.read();
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = data;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable);
    }

    private void sendOS() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_send.getText().toString();
                byte[] msgBuffer;
                msgBuffer = ChangeUtil.getHexBytes(message);
                try {
                    os = bluetoothSocket.getOutputStream();
                    os.write(msgBuffer);
                    for (int i = 0; i < msgBuffer.length; i++) {
                        byte result = msgBuffer[i];

                        tv_show.append("Android:" + String.valueOf(result) + "\n");


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findId() {
        btn_send = (Button) findViewById(R.id.btn_send);
        et_send = (EditText) findViewById(R.id.et_send);
        tv_show = (TextView) findViewById(R.id.tv_show);
    }


    private void initView() {
        title = (TextView) findViewById(R.id.title);
    }
}
