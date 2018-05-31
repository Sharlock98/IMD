package com.example.limingyan.imd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limingyan.imd.R;
import com.example.limingyan.imd.fragment.InfoFragment;
import com.example.limingyan.imd.fragment.ManageFragment;
import com.example.limingyan.imd.fragment.MeFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {
    long firstTime;
    private FragmentManager manager;
    private InfoFragment infoFragment;
    private ManageFragment manageFragment;
    private MeFragment meFragment;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String result = scanResult.getContents();
        if (result != null){
            EventBus.getDefault().postSticky(result);
            startActivity(new Intent(MainActivity.this, DetailsActivity.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=getSupportFragmentManager();
        infoFragment=new InfoFragment();
        manager.beginTransaction().add(R.id.mainPage,infoFragment).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                hideFragment();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (infoFragment == null){
                            infoFragment=new InfoFragment();
                            manager.beginTransaction().add(R.id.mainPage,infoFragment).commit();
                        }else {
                            manager.beginTransaction().show(infoFragment).commit();
                        }
                        return true;
                    case R.id.navigation_dashboard:
                        if (manageFragment == null){
                            manageFragment=new ManageFragment();
                            manager.beginTransaction().add(R.id.mainPage,manageFragment).commit();
                        }else {
                            manager.beginTransaction().show(manageFragment).commit();
                        }
                        return true;
                    case R.id.navigation_notifications:
                        if (meFragment == null){
                            meFragment=new MeFragment();
                            manager.beginTransaction().add(R.id.mainPage,meFragment).commit();
                        }else {
                            manager.beginTransaction().show(meFragment).commit();
                        }
                        return true;
                }
                return false;
            }
        });
        navigation.setSelectedItemId(0);
    }

    private void hideFragment() {
        if(infoFragment != null)
            manager.beginTransaction().hide(infoFragment).commit();
        if(manageFragment != null)
            manager.beginTransaction().hide(manageFragment).commit();
        if(meFragment != null)
            manager.beginTransaction().hide(meFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
