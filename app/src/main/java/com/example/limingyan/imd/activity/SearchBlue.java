package com.example.limingyan.imd.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limingyan.imd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchBlue extends AppCompatActivity {
    private Button btn_searchBt;
    private Button btn_searchBond;
    private TextView tv_home;
    private ListView lv_BlueTooth;
    private SimpleAdapter adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private List<Map<String, Object>> mList = new ArrayList<>();
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blue);
        initView();
        title.setText("蓝牙搜索");
        findId();
        bondadpter();
        onClick();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        openBlueTooth();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);


    }

    /**
     * 反馈信息
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_NONE) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", device.getName());
                    map.put("address", device.getAddress());
                    mList.add(map);
                    bondadpter();
                    Toast.makeText(SearchBlue.this, "未配对完成", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     * 打开蓝牙
     */
    private void openBlueTooth() {
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
            mBluetoothAdapter.enable();
        }
    }

    /**
     * 点击事件
     */
    private void onClick() {
        btn_searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();
            }
        });
        btn_searchBond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_home.setText("NAME:" + mBluetoothAdapter.getName() + "ADDRESS:" + mBluetoothAdapter.getAddress());
                Set<BluetoothDevice> bts = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bluetoothDevice : bts) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", bluetoothDevice.getName());
                    map.put("address", bluetoothDevice.getAddress());
                    mList.add(map);
                    bondadpter();
                }

            }
        });
        lv_BlueTooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map m = (Map) adapter.getItem(position);
                String address = (String) m.get("address");
                SharedPreferences userSettings = getSharedPreferences("address", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.putString("address", address);
                editor.commit();
                Intent i = new Intent(SearchBlue.this, BlueToothActivity.class);
                startActivity(i);

            }
        });
    }

    /**
     * lv绑定适配器
     */
    private void bondadpter() {
        adapter = new SimpleAdapter(SearchBlue.this, mList, R.layout.lv_item, new String[]{"name", "address"}, new int[]{R.id.tv_name, R.id.tv_address});
        lv_BlueTooth.setAdapter(adapter);
    }

    /**
     * 初始化变量
     */
    private void findId() {
        btn_searchBond = (Button) findViewById(R.id.btn_searchBond);
        btn_searchBt = (Button) findViewById(R.id.btn_searchBlueTooth);
        tv_home = (TextView) findViewById(R.id.tv_hint);
        lv_BlueTooth = (ListView) findViewById(R.id.lv_blueTooth);
    }

    /**
     * 重写销毁方法
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
    }
}
