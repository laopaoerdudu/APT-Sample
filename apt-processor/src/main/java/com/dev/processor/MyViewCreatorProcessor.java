package com.dev.processor;

import com.dev.annotation.ViewCreator;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.dev.processor.ProcessorConstant.APT_GENERATION_PKG;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.dev.annotation.ViewCreator")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyViewCreatorProcessor extends AbstractProcessor {
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<String> mViewNameSet = readViewNameFromLocalFile();

        if (mViewNameSet == null || mViewNameSet.isEmpty()) {
            return false;
        }

        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ViewCreator.class);
        for (Element element : elementsAnnotatedWith) {
            System.out.println("Hello " + element.getSimpleName() + ", 欢迎使用 APT");
            startGenerateCode(mViewNameSet);
            //如果有多个地方标注了注解，我们只读取第一次的就行了
            break;
        }
        return true;
    }

    /**
     * 开始执行生成代码的逻辑
     * @param mViewNameSet 控件名称集合
     */
    private void startGenerateCode(Set<String> mViewNameSet) {
        ClassName viewType = ClassName.get("android.view", "View");
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder("createView")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(viewType)
                .addParameter(String.class, "name")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(ClassName.get("android.util", "AttributeSet"), "attr");

        methodBuilder.addStatement("$T view = null", viewType);
        methodBuilder.beginControlFlow("switch(name)");

        for (String viewName : mViewNameSet) {
            if (viewName.contains(".")) {
                //针对包含 . 的控件名称进行处理
                //分离包名和控件名，如：androidx.constraintlayout.widget.ConstraintLayout
                //packageName：androidx.constraintlayout.widget
                //simpleViewName：ConstraintLayout
                String packageName = viewName.substring(0, viewName.lastIndexOf("."));
                String simpleViewName = viewName.substring(viewName.lastIndexOf(".") + 1);
                ClassName returnType = ClassName.get(packageName, simpleViewName);

                methodBuilder.addCode("case $S:\n", viewName);
                methodBuilder.addStatement("\tview = new $T(context,attr)", returnType);
                methodBuilder.addStatement("\tbreak");
            }
        }
        methodBuilder.addCode("default:\n");
        methodBuilder.addStatement("\tbreak");
        methodBuilder.endControlFlow();
        methodBuilder.addStatement("return view");
        MethodSpec createView = methodBuilder.build();

        TypeSpec myViewCreatorImpl = TypeSpec.classBuilder("MyViewCreatorImpl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get("com.dev.api", "IMyViewCreator"))
                .addMethod(createView)
                .build();

        JavaFile javaFile = JavaFile.builder(APT_GENERATION_PKG, myViewCreatorImpl).build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> readViewNameFromLocalFile() {
        try {
            File file = new File("/Users/david/study-code/APT-Sample/viewList.txt");
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties.stringPropertyNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
