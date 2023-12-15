package cn.xinghua;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

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

    }


}
