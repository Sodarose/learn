package cn.xinghua;

import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author 26016
 * @description
 * @date 2023-12-14 16:56
 **/
public class InstrumentationUtil {

    /**
     * agent.jar 路径
     */
    private String agentJarPath;

    /**
     * Instrumentation实例对象
     */
    private Instrumentation instrumentation;

    @SuppressWarnings("resource")
    public InstrumentationUtil(String libPathName) throws IOException {
        Path libPath = Paths.get(libPathName);
        if (!Files.exists(libPath)) {
            System.err.println("libPath 不存在");
            return;
        }
        Optional<Path> jarPath = Files.find(libPath, 1, (path, basicFileAttributes) -> {
            String name = path.getFileName().toString();
            return name.endsWith(".jar");
        }).findAny();
        if (jarPath.isEmpty()) {
            System.err.println("无法找到agent.jar");
            return;
        }
        Path agentPath = jarPath.get();
        agentJarPath = agentPath.toAbsolutePath().toString();
        System.err.printf("路径:[%s],寻找到agent.jar%n", agentJarPath);
    }

    /**
     * 获取Instrumentation实例类
     */
    public synchronized Instrumentation getInstrumentation() {
        if (instrumentation == null && agentJarPath != null) {
            VirtualMachine vm = null;
            try {
                // 找到当前程序的进程PID，并加载agent.jar
                String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
                vm = VirtualMachine.attach(pid);
                vm.loadAgent(agentJarPath, null);
                // 返回Agent中的Instrumentation
                Class<?> agentClazz = Class.forName("cn.xinghua.Agent");
                Field instField = agentClazz.getDeclaredField("inst");
                instrumentation = (Instrumentation) instField.get(null);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (vm != null) {
                    try {
                        vm.detach();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instrumentation;
    }

}
