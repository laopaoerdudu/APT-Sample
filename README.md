
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