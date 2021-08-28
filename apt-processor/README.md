
读取 `apt-annotation` 的注解，自动生成代码，它主要做三个事：

- 注解处理器声明

- 注解处理器注册

```
// 这叫动态注册
implementation 'com.google.auto.service:auto-service:1.0-rc6'
annotationProcessor 'com.google.auto.service:auto-service:1.0-rc6'

// 2，在注解处理器上加上 @AutoService(Processor.class) 即可完成注册
```

- 注解处理器生成类文件

>通过 javapoet 框架来编写

