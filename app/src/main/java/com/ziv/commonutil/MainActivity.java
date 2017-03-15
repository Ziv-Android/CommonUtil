package com.ziv.commonutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ziv.util.CrashHandler;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencv_java");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
    }
}
