package com.dev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.dev.annotation.AptAnnotation;
import com.dev.annotation.MyBindView;
import com.dev.api.MyAptAPI;
import com.dev.api.MyButterKnife;
import com.dev.api.MyViewCreatorDelegate;

import org.jetbrains.annotations.NotNull;

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

    // 注意：一般我们会把替换 View 的逻辑放到基类里面
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull String name, @NonNull @NotNull Context context, @NonNull @NotNull AttributeSet attrs) {
        View view = MyViewCreatorDelegate.getInstance().createView(name, context, attrs);
        if (view != null) {
            return view;
        }
        return super.onCreateView(name, context, attrs);
    }
}