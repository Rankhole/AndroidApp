package com.example.androidapp;

import android.app.Application;

import java.io.InputStream;

public class StoreBTSockets extends Application {
    private InputStream is=null;
    public void setIs(InputStream is){
        this.is=is;
    }
    public InputStream getIs(){
        return this.is;
    }

}
