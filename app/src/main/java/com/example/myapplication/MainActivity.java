package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Handler handler;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        // Looper - который запускает цикл обработки сообщений
        // и стартует его в главном потоке  - это вызов getMainLooper()
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // здесь мы будем ждать сообщения из другого потока
                tv.setText("N: "+msg.getData().getInt("key"));
            }
        };

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                        doSlow();
                   }
               }).start();

            }
        });
    }

    public void doSlow() {
        for(int i=0;i<50;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("key",i);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}