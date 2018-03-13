package com.czm.tcpchat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView mTextView;
    Handler handler;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.textView);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123)
                {
                    // 将读取的内容追加显示在文本框中
                    mTextView.append("\n" + msg.obj.toString());
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 0x345;
                msg.obj = editText.getText().toString();
                clientThread.sendHandler.sendMessage(msg);
                editText.setText("");
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    clientThread = new ClientThread(handler);
                    // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
                    new Thread(clientThread).start();
                }
            }
        });
    }
}
