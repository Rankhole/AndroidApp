package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;

public class ConnectActivity extends AppCompatActivity {

    private TextView status;
    private Button listen, listDevices, goToSetup;
    private ListView listView;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice[] btArray;
    private BtServer server;
    private BtClient client;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;

    private int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String APP_NAME = "AndroidApp";
    private static final UUID MY_UUID = UUID.fromString("c6149cf5-c208-40a2-8def-5d1e3ec4ca02");
    private boolean isServer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        findViewByIds();
        implementListeners();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    private void findViewByIds() {
        this.status = findViewById(R.id.textView);
        this.listen = findViewById(R.id.buttonListen);
        this.listDevices = findViewById(R.id.buttonShowPaired);
        this.listView = findViewById(R.id.listView);
        this.goToSetup = findViewById(R.id.buttonGoToSetup);
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;

                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;

                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;

                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;

            }
            return true;
        }
    });

    private void implementListeners() {
        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                btArray = new BluetoothDevice[bt.size()];
                int index = 0;

                if (bt.size() > 0) {
                    for (BluetoothDevice device : bt) {
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server = new BtServer(bluetoothAdapter, APP_NAME, MY_UUID, handler);
                server.start();
                isServer = true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                client = new BtClient(btArray[position], MY_UUID, handler);
                client.start();
                status.setText("Connecting");

            }
        });

        goToSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connected = false;
                if (isServer) {
                    if (server.getInputStream() != null) {
                        connected = true;
                    }
                }
                if (!isServer) {
                    if (client.getInputStream() != null) {
                        connected = true;
                    }
                } if(connected){
                    Intent intent = new Intent(getApplicationContext(), GamePreparation.class);
                    startActivity(intent);
                }else{
                    status.setText("you are not connected");
                }
            }
        });

    }
}
