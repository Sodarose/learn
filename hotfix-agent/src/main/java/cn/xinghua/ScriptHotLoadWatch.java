package cn.xinghua;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author 26016
 * @description 热更代码
 * @date 2023-12-14 16:18
 **/
public class ScriptHotLoadWatch {

    /**
     * Instrumentation实例，用于获取对Class进行监控和修改
     */
    private final Instrumentation instrumentation;

    public ScriptHotLoadWatch(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    /**
     * 启动
     *
     * @param libPathName agent.jar的路径
     * @param scriptPath  文件热更路径
     */
    public static boolean start(String libPathName, String scriptPath) throws Exception {
        // 获取Instrumentation实例
        Instrumentation instrumentation = new InstrumentationUtil(libPathName).getInstrumentation();
        if (instrumentation == null) {
            return false;
        }
        // 启动监控
        new ScriptHotLoadWatch(instrumentation).startWatch(scriptPath);
        return true;
    }

    /**
     * 监控热更脚本路径
     *
     * @param scriptPath 文件热更路径
     */
    private void startWatch(String scriptPath) {
        Path path = Paths.get(scriptPath);
        if (!Files.exists(path)) {
            throw new RuntimeException("path is not exists!");
        }
        new ScriptFileMonitor(path, this::redefineClasses).start();
        System.err.println("hot fix starting");
    }

    /**
     * 重新定义class
     *
     * @param files 文件列表
     */
    private void redefineClasses(Collection<File> files) {
        try {
            // className -> class content
            Map<String, byte[]> classContentMap = new HashMap<>();
            for (File file : files) {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                byte[] content = Files.readAllBytes(file.toPath());
                ClassReader classReader = new ClassReader(content);
                String className = classReader.getClassName().replace("/", ".");
                classContentMap.put(className, content);
            }
            List<ClassDefinition> classDefinitions = new ArrayList<>();
            classContentMap.forEach((className, bytes) -> {
                ClassDefinition classDefinition = newClassDefinition(className, bytes);
                classDefinitions.add(classDefinition);
            });
            instrumentation.redefineClasses(classDefinitions.toArray(new ClassDefinition[0]));
            classDefinitions.forEach(classDefinition -> System.err.printf("hot fix class:%s\n", classDefinition.getClass()));
            System.err.println("hot fix success");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建一个class定义
     *
     * @param className class名称
     * @param bytes     class内容
     */
    private ClassDefinition newClassDefinition(String className, byte[] bytes) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            clazz = defineClass(className, bytes);
        }
        return new ClassDefinition(clazz, bytes);
    }

    /**
     * 定义一个Class
     *
     * @param className class名称
     * @param bytes     class内容
     */
    private Class<?> defineClass(String className, byte[] bytes) {
        try {
            // ClassLoader使用defineClass方法定义一个Class类,使用反射获取该方法
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass.setAccessible(true);
            defineClass.invoke(classLoader, bytes, 0, bytes.length);
            // 确保Class.forName能够正确的找到Class
            return Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
