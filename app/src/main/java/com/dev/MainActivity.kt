package com.dev

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.annotation.AptAnnotation
import com.dev.annotation.MyBindView
import com.dev.api.MyAptAPI
import com.dev.api.MyButterKnife
import com.dev.api.MyViewCreatorDelegate

@AptAnnotation(desc = "我是 MainActivity 上面的注解")
@SuppressLint("NonConstantResourceId,SetTextI18n")
class MainActivity : AppCompatActivity() {
    @MyBindView(R.id.tvTitle)
    var mTextView: TextView? = null

    @AptAnnotation(desc = "我是 onCreate 上面的注解")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyButterKnife.bind(this)
        mTextView?.text = "erdai666"
        MyAptAPI.init()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = MyViewCreatorDelegate.getInstance().createView(name, context, attrs)
        return view ?: super.onCreateView(name, context, attrs)
    }
}