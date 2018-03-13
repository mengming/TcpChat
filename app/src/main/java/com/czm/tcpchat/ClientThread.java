package com.czm.tcpchat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Phelps on 2018/3/13.
 */

public class ClientThread implements Runnable {

    private Handler mHandler;
    public Handler sendHandler;
    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private OutputStream mOutputStream;
    public ClientThread(Handler handler){
        mHandler = handler;
    }
    @Override
    public void run() {
        try {
            mSocket = new Socket("123.207.124.140", 8989);
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOutputStream = mSocket.getOutputStream();
            new Thread(){
                @Override
                public void run() {
                    String mString = null;
                    try {
                        while ((mString = mBufferedReader.readLine()) != null){
                            System.out.println("receive");
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = mString;
                            mHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            sendHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x345)
                    {
                        try
                        {
                            mOutputStream.write((msg.obj.toString() + "\r\n")
                                    .getBytes("utf-8"));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
