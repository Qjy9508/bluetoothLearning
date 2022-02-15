package com.example.bluetoothlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BT = 1;
    private Button btn1;
    private TextView tv1;
    private BluetoothAdapter mbluetoothAdapter = null;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private ListView lv1;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;

    ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1
    );

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 当 Discovery 发现了一个设备
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                // 从 Intent 中获取发现的 BluetoothDevice
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 将名字和地址放入要显示的适配器中
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn_1);
        tv1 = findViewById(R.id.tv_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
        lv1 = findViewById(R.id.lv_1);

        setBtn_1_ClickListener_getAdapter();
        setBtn_2_ClickListener_checkEnabled();
        setBtn_3_ClickListener_openBlueToothByEnable();
        setBtn_4_ClickListener_closeBlueTooth();
        setBtn_5_ClickListener_openBlueToothByIntent();
        setBtn_6_ClickListener_getBondedDevices();
        setBtn_7_ClickListener_startDiscover();

    }

    private void setBtn_7_ClickListener_startDiscover() {
        btn7.setOnClickListener(view -> {

//            发现设备使用 startDiscovery()该进程为异步进程。该方法会立刻返回一个布尔值，指示是否已成功启动发现操作。
//            发现进程通常包含约 12 秒的查询扫描，之后对发现的设备进行扫描，以检索其蓝牙设备的名字。
//            通过广播ACTION_FOUNT intent 接收到发现的设备
            mbluetoothAdapter.startDiscovery();



        });
    }

    private void setBtn_6_ClickListener_getBondedDevices() {
        btn6.setOnClickListener(view -> {
            if(mbluetoothAdapter != null) {
                Set<BluetoothDevice> pairedDevices = mbluetoothAdapter.getBondedDevices();

                lv1.setAdapter(mArrayAdapter);
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        // 把名字和地址取出来添加到适配器中
                        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            } else {
                tv1.setText("adapter空的！！！！");
            }
        });
    }

    private void setBtn_5_ClickListener_openBlueToothByIntent() {
        btn5.setOnClickListener(view -> {
            if (mbluetoothAdapter.isEnabled()) {
                tv1.setText("已经开启了蓝牙，无需再次打开");
            } else {
                tv1.setText("正在打开蓝牙....");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_BT);
                tv1.setText("蓝牙开启完成....");
            }
        });
    }

    private void setBtn_4_ClickListener_closeBlueTooth() {
        btn4.setOnClickListener(view -> {
            if (mbluetoothAdapter.isEnabled()) {
                tv1.setText("正在关闭蓝牙....");
                mbluetoothAdapter.disable();
                tv1.setText("蓝牙关闭完成....");
            } else {
                tv1.setText("蓝牙已经关闭了，无需再次关闭....");
            }
        });
    }

    private void setBtn_3_ClickListener_openBlueToothByEnable() {
        btn3.setOnClickListener(view -> {
            if (mbluetoothAdapter.isEnabled()) {
                tv1.setText("已经开启了蓝牙，无需再次打开");
            } else {
                tv1.setText("正在打开蓝牙....");
                mbluetoothAdapter.enable();
                tv1.setText("蓝牙开启完成....");
            }
        });
    }

    private void setBtn_2_ClickListener_checkEnabled() {
        btn2.setOnClickListener(view -> {
            if (mbluetoothAdapter != null) {
                if (mbluetoothAdapter.isEnabled()) {
                    tv1.setText("当前蓝牙：enabled");
                    Toast.makeText(getApplicationContext(), "当前蓝牙是开启的", Toast.LENGTH_LONG).show();
                } else {
                    tv1.setText("当前蓝牙： not enabled");
                    Toast.makeText(getApplicationContext(), "当前蓝牙是关闭的", Toast.LENGTH_LONG).show();
                }
            } else {
                tv1.setText("adapter == null, 先获取");
            }
        });
    }

    private void setBtn_1_ClickListener_getAdapter() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mbluetoothAdapter == null) {
                    mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mbluetoothAdapter == null) {
                        // Device doesn't support Bluetooth
                        tv1.setText("并不支持蓝牙");
                        return;
                    }
                    else {
                        tv1.setText("支持蓝牙");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "当前已经获取到了蓝牙Adapter", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}