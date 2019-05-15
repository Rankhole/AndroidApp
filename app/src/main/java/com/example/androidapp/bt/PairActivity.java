package com.example.androidapp.bt;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.androidapp.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PairActivity extends AppCompatActivity {
    private BluetoothAdapter myBluetoothAdapter;
    ArrayList<String> stringArrayList;
    ArrayAdapter<String> arrayAdapterScan;
    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private Button scanButton, discoverable;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        stringArrayList = new ArrayList<>();
        arrayAdapterScan = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);

        listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapterScan);
        discoverable = findViewById(R.id.buttonDiscoverability);
        scanButton = findViewById(R.id.buttonScan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toast.makeText(getApplicationContext(), "sende Kopplungsanfrage...", Toast.LENGTH_LONG).show();
                    if (createBond(devices.get(position))) {
                        Toast.makeText(getApplicationContext(), "erfolgreich gekoppelt", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, intentFilter);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
        discoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);
                startActivity(discoverableIntent);
            }
        });
    }

    void buttonClicked() {
        Toast.makeText(getApplicationContext(), "scan for devices...", Toast.LENGTH_LONG).show();
        checkBTPermissions();
        myBluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
                String deviceName = "";
                if (null == device.getName()) {
                    deviceName = device.getAddress();
                } else {
                    deviceName = device.getName();
                }
                stringArrayList.add(deviceName);
                arrayAdapterScan.notifyDataSetChanged();
            }
        }
    };

    public boolean createBond(BluetoothDevice btDevice)
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");


            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {

        }
    }
}
