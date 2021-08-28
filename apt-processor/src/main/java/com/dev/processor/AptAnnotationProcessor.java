package com.dev.processor;

import com.dev.annotation.AptAnnotation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.dev.processor.ProcessorConstant.APT_GENERATION_PKG;

// 注解处理器声明

@AutoService(Processor.class)
@SupportedOptions("MODULE_NAME")
@SupportedAnnotationTypes("com.dev.annotation.AptAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AptAnnotationProcessor extends AbstractProcessor {
    Filer filer; // 文件生成器
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        moduleName = processingEnv.getOptions().get("MODULE_NAME");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(AptAnnotation.class);

        // build a function
        MethodSpec.Builder builder = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(String.class, "param");
        builder.addStatement("$T.out.println($S)", System.class, "module: " + moduleName);

        if (rootElements != null && !rootElements.isEmpty()) {
            for (Element element : rootElements) {
//                if (element.getKind() == ElementKind.CLASS) {
//                    // 如果元素是类
//
//                } else if (element.getKind() == ElementKind.INTERFACE) {
//                    // 如果元素是接口
//                }

                String elementName = element.getSimpleName().toString();
                String desc = element.getAnnotation(AptAnnotation.class).desc();
                builder.addStatement("$T.out.println($S)", System.class, "element: " + elementName + ", " + "desc: " + desc);
            }
        }

        MethodSpec main = builder.build();

        // build class
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder(APT_GENERATION_PKG, helloWorld).build();
        try {
            // create file
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 接收外来传入的参数，最常用的形式就是在 Gradle 里 javaCompileOptions 配置的属性
     * @return 属性的 Set 集合
     */
//    @Override
//    public Set<String> getSupportedOptions() {
//        return Collections.singleton("MODULE_NAME");
//    }

    /**
     * 当前注解处理器支持的注解集合，如果支持，就会调用 process 方法
     * @return 支持的注解集合
     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        //只支持 AptAnnotation 注解
//        return Collections.singleton(AptAnnotation.class.getCanonicalName());
//    }

    /**
     * 编译当前注解处理器的 JDK 版本
     * @return JDK 版本
     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.RELEASE_8;
//    }
}
