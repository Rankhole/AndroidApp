package com.example.androidapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.os.Handler;
import android.os.Message;

public class BtClient extends Thread {

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private String appName;
    private UUID uuid;
    private android.os.Handler handler;
    private BluetoothDevice bluetoothDevice;

    public BtClient(BluetoothDevice device, UUID uuid, Handler handler){
        this.appName=appName;
        this.uuid=uuid;
        this.handler=handler;
        this.bluetoothDevice=device;

        try {
            socket=device.createRfcommSocketToServiceRecord(this.uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        // cancel discovery before connecting device
        try {
            socket.connect();
            Message message=Message.obtain();
            message.what=ConnectActivity.STATE_CONNECTED;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            Message message=Message.obtain();
            message.what=ConnectActivity.STATE_CONNECTION_FAILED;
            handler.sendMessage(message);
        }
    }

    public InputStream getInputStream(){
        InputStream is =null;
        try {
            is= socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }


    public OutputStream getOutputStream(){
        OutputStream os =null;
        try {
            os= socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }


}
