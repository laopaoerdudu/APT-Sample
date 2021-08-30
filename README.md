
#### 这个 Sample 存在的意义？

Android 中少量的系统控件是通过 new 的方式创建出来的，而大部分控件如 androidx.appcompat.widget 下的控件，自定义控件，第三方控件等等，都是通过反射创建的。
大量的反射创建多多少少会带来一些性能问题，因此我们需要去解决反射创建的问题，我的解决思路是：

- 通过编写 Android 插件获取 Xml 布局中的所有控件
  
- 拿到控件后，通过 APT 用 new 的方式创建 View 的类
  
- 最后通过反射获取当前类并在基类里面完成替换

>一些不带 . 的系统控件，如 TextView，ImageView 。系统会默认给我们通过 new 的方式去创建。
带 . 的控件。可能为 androidx.appcompat.widget 下的控件，自定义控件，第三方控件等等，系统会通过反射去创建。

**在实际工作中，我们一般会这么做：**

1、将需要生成的类文件实现某个定义好的接口，通过接口代理来使用

>关于接口，我们一般会放到 apt-api 这个 Module 中，apt-processor 自动生成代码

2、规定生成的 Java 类模版，根据模版去进行生成代码逻辑的编写

```
package com.dev.apt_generation;

public class MyViewCreatorImpl implements IMyViewCreator {
  @Override
  public View createView(String name, Context context, AttributeSet attr) {
    View view = null;
    switch(name) {
      case "androidx.core.widget.NestedScrollView":
      	view = new NestedScrollView(context,attr);
      	break;
      case "androidx.constraintlayout.widget.ConstraintLayout":
      	view = new ConstraintLayout(context,attr);
      	break;
      case "androidx.appcompat.widget.ButtonBarLayout":
      	view = new ButtonBarLayout(context,attr);
      	break;
        //...
      default:
      	break;
    }
    return view;
}
```

### 原理

>通过APT（Annotation Processing Tool）技术，即注解处理器，在编译时扫描并处理注解，注解处理器最终生成处理注解逻辑的.java文件。


 
#### Notes

Java 源文件实际上是一种结构体语言，源代码的每一个部分都对应了一个特定类型的 Element

注意：`getSupportedAnnotationTypes()`、`getSupportedSourceVersion()` 和 `getSupportedOptions()` 这三个方法，
我们还可以采用注解的方式进行提供：

```
@SupportedOptions("MODULE_NAME")
@SupportedAnnotationTypes("com.dev.annotation.AptAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AptAnnotationProcessor extends AbstractProcessor {
    //...
}
```

[Javapoet](https://github.com/square/javapoet)

[参考：](https://juejin.cn/post/6978500975770206239)