package com.example.androidapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Handler;
import android.os.Message;

public class BtServer extends Thread {


    private BluetoothServerSocket serverSocket;
    private String appName;
    private UUID uuid;
    private Handler handler;
    private BluetoothSocket socket;

    public BtServer(BluetoothAdapter bluetoothAdapter, String appName, UUID uuid, Handler handler){
        this.appName=appName;
        this.uuid=uuid;
        this.handler=handler;
        this.socket=null;
        try {
            serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(this.appName, this.uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){


        while(socket==null){
            try {
                Message message=Message.obtain();
                message.what=ConnectActivity.STATE_CONNECTING;
                handler.sendMessage(message);

                socket=serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();

                Message message=Message.obtain();
                message.what=ConnectActivity.STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }

            if(socket!=null){
                Message message=Message.obtain();
                message.what=ConnectActivity.STATE_CONNECTED;
                handler.sendMessage(message);
                //write some Code for send/receive
                break;
            }
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
