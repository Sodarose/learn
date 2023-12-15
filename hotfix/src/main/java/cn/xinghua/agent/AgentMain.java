package cn.xinghua.agent;

import java.lang.management.ManagementFactory;

/**
 * @author 26016
 * @description
 * @date 2023-12-15 11:54
 **/
public class AgentMain {

    public static void main(String[] args) throws InterruptedException {
        System.err.println("开始");
        Thread.sleep(100_000);
        System.err.println(ManagementFactory.getRuntimeMXBean().getName());
        System.err.println("结束");
    }

}
