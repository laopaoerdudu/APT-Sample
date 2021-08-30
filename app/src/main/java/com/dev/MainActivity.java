package com.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.dev.annotation.AptAnnotation;
import com.dev.annotation.MyBindView;
import com.dev.api.MyAptAPI;
import com.dev.api.MyButterKnife;

@AptAnnotation(desc = "我是 MainActivity 上面的注解")
@SuppressLint("NonConstantResourceId,SetTextI18n")
public class MainActivity extends AppCompatActivity {
    @MyBindView(R.id.tvTitle)
    TextView mTextView;

    @AptAnnotation(desc = "我是 onCreate 上面的注解")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyButterKnife.bind(this);
        mTextView.setText("david 666");
        MyAptAPI.init();
    }
}