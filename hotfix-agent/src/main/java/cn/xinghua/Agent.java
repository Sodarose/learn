package cn.xinghua;

import java.lang.instrument.Instrumentation;

/**
 * @author 26016
 * @description Instrumentation的代理类
 * @date 2023-12-14 17:03
 **/
public class Agent {

    /**
     * Instrumentation实例，用于获取对Class进行监控和修改，可以在已有的类上附加（修改）字节码来实现增强的逻辑
     */
    public static Instrumentation inst;

    /**
     * Jvm启动后获取Instrumentation实例
     *
     * @param args 参数
     * @param inst 实例对象
     */
    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
        System.err.println("attach_agent");
    }

    /**
     * Jvm启动的时获取Instrumentation实例
     *
     * @param args 参数
     * @param inst 实例对象
     */
    public static void premain(String args, Instrumentation inst) {
        Agent.inst = inst;
        System.err.println("pre_agent");
    }

}
