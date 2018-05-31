package com.example.limingyan.imd.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limingyan.imd.R;
import com.example.limingyan.imd.entity.t_stake;
import com.example.limingyan.imd.util.ChangeUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BlueToothActivity extends AppCompatActivity {
    private Button btn_openBlueTooth;
    private BluetoothAdapter mAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Button btn_getBlueTooth;
    private ListView lv_BlueTooth;
    private List<String> devices;
    private List<BluetoothDevice> deviceList;
    private ProgressDialog progressDialog;
    private String TAG = "1";
    private Button btn_getOwnBT;
    private final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String NAME = "Bluetooth_Socket";
    private BluetoothSocket clientSocket;
    private OutputStream os;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
        findId();
        openBlueTooth();
//        getBlueTooth();
        getMyOwnDevices();
        title.setText("蓝牙模块");
        Bmob.initialize(BlueToothActivity.this, "3cb928f0c2d0133c575f7564c0d786e0");
    }


    /**
     * 初始化绑定
     */
    private void findId() {
        btn_getOwnBT = (Button) findViewById(R.id.btn_getOwnBT);
        btn_openBlueTooth = (Button) findViewById(R.id.btn_openBlueTooth);
        btn_getBlueTooth = (Button) findViewById(R.id.btn_getBlueTooth);
        lv_BlueTooth = (ListView) findViewById(R.id.lv_BlueTooth);
    }

    /**
     * 打开蓝牙
     */
    private void openBlueTooth() {
        btn_openBlueTooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mAdapter == null) {
                    Toast.makeText(BlueToothActivity.this, "您的设备不支持蓝牙", Toast.LENGTH_LONG).show();
                } else {
                    if (mAdapter.isEnabled()) {
                        mAdapter.disable();
                    }

                    Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(i, REQUEST_ENABLE_BT);

                }
            }
        });
    }

    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(BlueToothActivity.this, "蓝牙打开成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BlueToothActivity.this, "蓝牙打开失败", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * 蓝牙广播接收
     */
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                Toast.makeText(BlueToothActivity.this, "蓝牙接收广播完毕", Toast.LENGTH_LONG).show();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    devices.add(device.getName());
                    devices.add(device.getAddress());
                    deviceList.add(device);
                    Log.e(TAG, "onReceive: 搜索完成");
                    /*        showDevices();*/
                    progressDialog.dismiss();
                }

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                progressDialog.dismiss();
                Toast.makeText(BlueToothActivity.this, "搜索完成", Toast.LENGTH_LONG).show();
            }

        }
    };


//    /**
//     * 获取周围蓝牙
//     */
//    private void getBlueTooth() {
//        btn_getBlueTooth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mAdapter.isDiscovering()) {
//                    mAdapter.cancelDiscovery();
//                }
//                mAdapter.startDiscovery();
//                if (progressDialog==null){
//                    progressDialog=new ProgressDialog(Main2Activity.this);
//                }
//                progressDialog.setMessage("搜索周围的蓝牙");
//                progressDialog.show();
//                //寻找蓝牙广播
//                IntentFilter mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//                registerReceiver(mReceiver, mFilter);
//                //找到蓝牙广播
//              IntentFilter  mFilter1=new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                registerReceiver(mReceiver,mFilter1);
//                //结束蓝牙广播
//               IntentFilter  mFilter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//                registerReceiver(mReceiver,mFilter2);
//
//            }
//        });
//    }


    /**
     * 解除广播绑定
     //     */
//    public void onDestroy() {
//
//        super.onDestroy();
//        //解除注册
//        unregisterReceiver(mReceiver);
//        Log.e("destory","解除注册");
//    }


    /**
     * 获取所有设备信息
     */

    private void getMyOwnDevices() {
        btn_getOwnBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
                if (devices.size() > 0) {
                    List<Map<String, Object>> mList = new ArrayList<>();
                    for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                        BluetoothDevice device = (BluetoothDevice) it.next();
                        //打印出远程蓝牙设备的物理地址
//                       BlueTooth blueTooth=new BlueTooth();
//                       blueTooth.setDeviceAddress(device.getAddress());
//                       blueTooth.setDeviceName(device.getName());
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", device.getName());
                        map.put("address", device.getAddress());
                        mList.add(map);
                        System.out.println(device.getName() + ":" + device.getAddress() + "这大概就是我要的位置的吧！！！！！！！！！！！！！！！！！！");
                    }
                    showDevices(mList);
                }
            }
        });


    }

    /**
     * ListView绑定与Item的点击事件
     */
    private void showDevices(final List<Map<String, Object>> mList) {
        final SimpleAdapter adapter = new SimpleAdapter(this, mList, R.layout.blue_item, new String[]{"name", "address"}, new int[]{R.id.tv_BTname, R.id.tv_BTaddress});
        lv_BlueTooth.setAdapter(adapter);
        //点击事件
        lv_BlueTooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = (Map) adapter.getItem(position);
                String address = (String) map.get("address");
                Log.d("TAG", address);
                if (mAdapter.isDiscovering()) {
                    mAdapter.cancelDiscovery();
                }
                BluetoothDevice device = mAdapter.getRemoteDevice(address);
                if (clientSocket == null) {
                    try {
                        clientSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        clientSocket.connect();
                        os = clientSocket.getOutputStream();
                        if (os != null) {
//                           os.write(ChangeUtil.changeASCII("wsad").getBytes());
                            int b = Integer.SIZE;
                            int a = ChangeUtil.changeASCII("这就很ok了").length;
//                            Toast.makeText(Main2Activity.this, ChangeUtil. changeASCII("这就很ok了"), Toast.LENGTH_SHORT).show();
                            os.write(ChangeUtil.changeASCII("这就很ok了"));

                        }

                        Toast.makeText(BlueToothActivity.this, "发送信息成功，请查收", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BlueToothActivity.this, "发送信息失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void getFromBmob(int stakeId){
        BmobQuery<t_stake> query=new BmobQuery<>();
        query.addWhereEqualTo("stakeId",stakeId);
        query.findObjects(new FindListener<t_stake>() {
            @Override
            public void done(List<t_stake> list, BmobException e) {
                list.get(0).getGoodsId();//获取到的商品id（待发出）
            }
        });
    }
    private void initView() {
        title = (TextView) findViewById(R.id.title);
    }

    private class ConnectThread extends Thread {

    }


}
